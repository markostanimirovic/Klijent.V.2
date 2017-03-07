/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klijent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @version V.2 - omogucena UDP komunikacija izmedju klijenta i servera za slanje liste korisnika
 * @author Marko Stanimirovic
 */
public class Klijent implements Runnable {

    static Socket soketZaKomunikaciju = null;
    static PrintStream izlazniTokKaServeru = null;
    static BufferedReader ulazniTokOdServera = null;
    static BufferedReader ulazniTokSaKonzole = null;
    static boolean kraj = false;
    
    public static void main(String[] args) {
        try {
            int port = 10101;
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }
            
            soketZaKomunikaciju = new Socket("localhost", port);           
            ulazniTokSaKonzole = new BufferedReader(new InputStreamReader(System.in));
            izlazniTokKaServeru = new PrintStream(soketZaKomunikaciju.getOutputStream());
            ulazniTokOdServera = new BufferedReader(new InputStreamReader(soketZaKomunikaciju.getInputStream()));
            UDPKlijentNit.udp();
            
            new Thread(new Klijent()).start();
            new UDPKlijentNit().start();
            
            while(!kraj) {
                izlazniTokKaServeru.println(ulazniTokSaKonzole.readLine());
            }
            
            soketZaKomunikaciju.close();
            UDPKlijentNit.datagramSoket.close();
        } catch (UnknownHostException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Override
    public void run() {
        String tekst;
        try {
            while((tekst = ulazniTokOdServera.readLine()) != null) {
                System.out.println(tekst);
                if(tekst.endsWith("dovidjenja! ***")) {
                    kraj = true;
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
