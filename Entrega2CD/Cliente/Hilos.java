import jdk.javadoc.internal.doclets.formats.html.SourceToHTMLConverter;

import jdk.javadoc.internal.doclets.formats.html.SourceToHTMLConverter;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author diego
 */
public class Hilos implements Runnable {

    long numberOfTests;
    String nombreHost;
    String numeroPuerto;
    long correctPairs;

    public Hilos(String nombreHost, String numeroPuerto, long numberOfTests) {
        this.numberOfTests = numberOfTests;
        this.nombreHost = nombreHost;
        this.numeroPuerto = numeroPuerto;
    }

    public long getCorrectPairs() {
        return correctPairs;
    }

    public void setNumberOfTests(long numberOfTests) {
        this.numberOfTests = numberOfTests;
    }

    public void run() {
        // invoke the remote method
        String URL = "rmi://" + nombreHost + ":" + numeroPuerto + "/GeneradorPar"
                + "esAleatorios";
        GeneradorParesAleatoriosInt h;
        try {
            h = (GeneradorParesAleatoriosInt) Naming.lookup(URL);
            correctPairs = h.ParesEnCirculo(numberOfTests);
        } catch (MalformedURLException e) {
            System.err.println("MalformedURLException: " + e.getMessage());
        } catch (NotBoundException n) {
            System.err.println("NotBoundException: " + n.getMessage());
        } catch (RemoteException r) {
            System.err.println("RemoteException: " + r.getMessage());
        }
    }

}