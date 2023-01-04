/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.Naming;
import java.util.Scanner;

/**
 *
 * @author diego
 */
public class MonteCarloCliente {

    public static void main(String args[]) {
        Scanner entradaDatos = new Scanner(System.in);
        int numeroPuerto;
        String nombreHost;
        Thread myThreads[] = new Thread[4];
        Hilos[] hilos = new Hilos[4];
        long totalParesCorrectos = 0;
        double pi;

        int numeroServidores = 4;
        long numeroPruebas;
        System.out.println("Con cuantos pares quieres probar el método de "
                + "Monte Carlo?");
        numeroPruebas = entradaDatos.nextLong();
        while (numeroServidores-- > 0) {
            System.out.println("Enter the RMIRegistry host name for server "
                    + (4 - numeroServidores));
            entradaDatos.nextLine();
            nombreHost = entradaDatos.nextLine();
            System.out.println("Enter the RMIregistry port number for server "
                    + (4 - numeroServidores));
            numeroPuerto = entradaDatos.nextInt();
            hilos[3 - numeroServidores] = new Hilos(nombreHost,
                    String.valueOf(numeroPuerto), numeroPruebas / 4);
            if (numeroServidores == 1) {
                hilos[3 - numeroServidores].setNumberOfTests(numeroPruebas / 4
                        + numeroPruebas % 4);
            }
            myThreads[3 - numeroServidores] = new Thread(hilos[3 - numeroServidores]);
            myThreads[3 - numeroServidores].start();
        }
        
        while (numeroServidores++ != 3) {
            try {
                myThreads[numeroServidores].join();
                totalParesCorrectos += hilos[numeroServidores].getCorrectPairs();
            } catch (InterruptedException e) {
                System.err.println("InterruptedException: " + e.getMessage());
            }
        }
        
        pi = 4 * (double) totalParesCorrectos/numeroPruebas;
        System.out.println("Resultado: " + pi);
        
    }
}
