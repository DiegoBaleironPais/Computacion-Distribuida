/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p5_compdis;



/**
 *
 * @author david
 */
public interface InterfazCliente extends java.rmi.Remote {
    public void notifyMe(String message, String usuario)
        throws java.rmi.RemoteException;
    public String getNombre() throws java.rmi.RemoteException;
    public void actualizarSolicitudes() throws java.rmi.RemoteException;
    public void actualizarSolicitudesEnviadas() throws java.rmi.RemoteException;
    public void actualizarAmigosConectados() throws java.rmi.RemoteException;
    public void recibirNotificacion(String notificacion) throws java.rmi.RemoteException;
}