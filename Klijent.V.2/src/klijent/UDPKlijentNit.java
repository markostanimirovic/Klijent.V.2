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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @version V.2 - omogucena UDP komunikacija izmedju klijenta i servera za slanje liste korisnika
 * @author Marko Stanimirovic
 */
public class UDPKlijentNit extends Thread {

    static DatagramSocket datagramSoket = null;
    static DatagramPacket paketOdServera = null;
    static byte[] podaciOdServera = null;
    static final String PORT = "C:\\Users\\PC Servis\\Documents\\NetBeansProjects\\fajlovi\\port.txt";
    static int udpPort;

    public static int ucitajIzFajlaPort() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(PORT));
            String s = in.readLine();
            int port = Integer.parseInt(s);
            in.close();
            return port;
        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {

        }
        return -1;
    }

    public static void udp() {
        try {
            udpPort = ucitajIzFajlaPort();
            datagramSoket = new DatagramSocket(udpPort);
            podaciOdServera = new byte[1024];
        } catch (SocketException ex) {

        }
    }

    @Override
    public void run() {
        while (Klijent.kraj == false) {
            try {
                podaciOdServera = new byte[1024];
                paketOdServera = new DatagramPacket(podaciOdServera, podaciOdServera.length);
                datagramSoket.receive(paketOdServera);
                String s = new String(paketOdServera.getData());
                System.out.print(s);
                podaciOdServera = null;
            } catch (IOException ex) {
                continue;
            }
        }
    }

}
