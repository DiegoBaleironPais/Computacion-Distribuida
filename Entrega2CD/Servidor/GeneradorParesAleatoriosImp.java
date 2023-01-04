/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author diego
 */
public class GeneradorParesAleatoriosImp extends UnicastRemoteObject implements
        GeneradorParesAleatoriosInt{

    public GeneradorParesAleatoriosImp() throws RemoteException{
        super ();
    }
    
    public long ParesEnCirculo (long numeroDePares) 
            throws java.rmi.RemoteException {
        long numeroParesResultado = 0;
        double x = 0.0f;
        double y = 0.0f;
        Date fechaActual = new Date();
        Random generadorNumerosAleatorios = new Random(fechaActual.getTime());
        while(numeroDePares-- > 0) {
            x = generadorNumerosAleatorios.nextDouble();
            y = generadorNumerosAleatorios.nextDouble();
            if ((x*x+y*y) <= 1) {
                numeroParesResultado++;
            }
        }
        return numeroParesResultado;
    }

    
    
}
