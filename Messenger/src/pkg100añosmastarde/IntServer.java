/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg100añosmastarde;
import java.rmi.*;
import java.util.ArrayList;
import p5_compdis.InterfazCliente;

/**
 *
 * @author david
 */
public interface IntServer extends Remote {
    public boolean registerForCallback(InterfazCliente callbackClientObject,
            String id, String password) throws java.rmi.RemoteException;
    public void unregisterForCallback(String id, InterfazCliente callbackClientObject)
            throws java.rmi.RemoteException;
    public boolean registrarUsuario(String id, String password)
            throws java.rmi.RemoteException;
    public void añadirSolicitud (String id, String amigo, InterfazCliente callbackClientObject) 
            throws java.rmi.RemoteException;
    public void aceptarAmigo(String id, String amigo, InterfazCliente callbackClientObject)
            throws java.rmi.RemoteException;
    public void rechazarAmigo(String id, String amigo, InterfazCliente callbackClientObject)
            throws java.rmi.RemoteException;
    public ArrayList<String> obtenerListaUsuarios(String id)
            throws java.rmi.RemoteException;
    public ArrayList<String> obtenerListaAmigos(String id)
            throws java.rmi.RemoteException;
    public ArrayList<String> obtenerListaSugerencias(String id, String texto) throws 
            java.rmi.RemoteException;
    public ArrayList<String> refrescarListaSolicitudesPendientes
        (String id, InterfazCliente interfaz) throws RemoteException;
    public ArrayList<String> refrescarListaAmigosPendientes
        (String id, InterfazCliente interfaz) throws RemoteException;
    public ArrayList<String> refrescarListaAmigosConectados
            (String id, InterfazCliente interfaz) throws RemoteException;
    public InterfazCliente obtenerReferenciaAmigo(String amigo) throws RemoteException;
    public void actualizarContraseña (String id, InterfazCliente interfaz, String nuevaContraseña) throws RemoteException;
}
