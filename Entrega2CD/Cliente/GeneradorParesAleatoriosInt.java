/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.Remote;

/**
 *
 * @author diego
 */
public interface GeneradorParesAleatoriosInt extends Remote {
    public long ParesEnCirculo (long numeroDePares) 
            throws java.rmi.RemoteException;
}
