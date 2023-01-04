/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodigoFuncional;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author diego
 */
public class Socket {
      private static MulticastSocket socket;
      private static InetAddress group;

    public Socket() {
    }
      
    public static void listener() {
        DatagramPacket entrada;
        byte[] buffer = new byte[1000];
        entrada = new DatagramPacket(buffer, buffer.length);
        while(true) {
            try {
                Socket.limpiarArraysDBytes(buffer);
                socket.receive(entrada);
                Main.getGestorVentanas().getVentanaPrincipal().
                        imprimirNuevoMensaje(new String(entrada.getData()));
            } catch (IOException e) {
                System.out.println("Socket: " + e.getMessage());
            }  
        } 
    }

    public static MulticastSocket getSocket() {
        return socket;
    }

    public static InetAddress getGroup() {
        return group;
    }
    
    public static void limpiarArraysDBytes(byte[] buffer) {
        for (int i = 0; i < 1000; i++) {
            buffer[i] = 0;
        }
    }
    
    public static void inicializarSocket(){
        try{
         group = InetAddress.getByName("224.0.0.100");
         socket = new MulticastSocket(6703);
         socket.joinGroup(group);   
        } catch (IOException e) {
            System.out.println("Socket: " + e.getMessage());
        }
         
    }
}
