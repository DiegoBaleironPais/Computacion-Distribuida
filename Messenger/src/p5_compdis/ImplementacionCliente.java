/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p5_compdis;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ImplementacionCliente extends UnicastRemoteObject
    implements InterfazCliente{
    ClasePrincipal cp;
    String nombre;

    
    public ImplementacionCliente(ClasePrincipal cp) throws RemoteException{
        super();
        this.cp = cp; 
    }
    
    @Override
    public void notifyMe(String message, String usuario) throws RemoteException {
        cp.addMensaje(usuario, message); //añadir a la tabla de conversaciones un mensaje de cierto cliente
    }

    @Override
    public String getNombre() throws RemoteException {
        return nombre;
    }

    @Override
    public void actualizarSolicitudes() throws RemoteException {
        ArrayList<String> id = 
            cp.getH().refrescarListaAmigosPendientes(cp.getNombre(), cp.callbackObject);
        cp.getV_principal().obtenerSolicitudes(id, 0);
    }
    
     @Override
    public void actualizarSolicitudesEnviadas () throws RemoteException {
        ArrayList<String> id = 
            cp.getH().refrescarListaSolicitudesPendientes(cp.getNombre(), cp.callbackObject);
        cp.getV_principal().obtenerSolicitudes(id, 1);
    }
    
    @Override
    public void actualizarAmigosConectados() throws java.rmi.RemoteException {
        ArrayList<String> id = 
            cp.getH().refrescarListaAmigosConectados(cp.getNombre(), cp.callbackObject);
        cp.getV_principal().obtenerSolicitudes(id, 2);
        cp.obtenerReferenciasAmigos(cp.getNombre());
    }
    
    
    @Override //mandar
    public void recibirNotificacion(String notificacion){
        cp.getV_principal().recibirNotificacion(notificacion);
    }
}
