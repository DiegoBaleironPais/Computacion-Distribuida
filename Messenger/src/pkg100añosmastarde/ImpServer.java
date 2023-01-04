/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg100añosmastarde;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import p5_compdis.InterfazCliente;

public class ImpServer extends UnicastRemoteObject
        implements IntServer {

    HashMap<String, InterfazCliente> clientesConectados;
    ConexionUsuarios conexion;

    public ImpServer() throws RemoteException {
        super();
        clientesConectados = new HashMap<String, InterfazCliente>();
        conexion = new ConexionUsuarios();
    }

    @Override
    public void unregisterForCallback(String id, InterfazCliente callbackClientObject) throws RemoteException {
        if (clientesConectados.containsKey(id) && clientesConectados.get(id).equals(callbackClientObject)) {
            clientesConectados.remove(id);
            System.out.println(id + " se ha desconectado");
            actualizarListaAmigos();

            for (String usuario : obtenerListaAmigos(id)) {
                if (clientesConectados.containsKey(usuario)) {
                    clientesConectados.get(usuario).recibirNotificacion(id + " se ha desconectado");
                }
            }
        }
    }

    @Override
    public boolean registrarUsuario(String id, String password) {
        return conexion.registrarUsuario(id, password);
    }

    @Override
    public void añadirSolicitud(String id, String amigo, InterfazCliente callbackClientObject) throws RemoteException {
        if (conexion.añadirSolicitud(id, amigo) && clientesConectados.containsKey(amigo) && clientesConectados.get(id).equals(callbackClientObject)) {
            callbackClientObject.actualizarSolicitudes();
        }
    }

    @Override
    public void aceptarAmigo(String id, String amigo, InterfazCliente callbackClientObject) throws RemoteException {
        if (clientesConectados.get(id).equals(callbackClientObject)) {
            conexion.aceptarAmigo(id, amigo);
            System.out.println("id= " + id + " amigo= " + amigo);
            callbackClientObject.actualizarSolicitudes();
        }
    }

    @Override
    public void rechazarAmigo(String id, String amigo, InterfazCliente callbackClientObject) throws RemoteException {
        if (clientesConectados.get(id).equals(callbackClientObject)) {
            conexion.rechazarSolicitud(id, amigo);
            callbackClientObject.actualizarSolicitudes();
        }
    }

    @Override
    public ArrayList<String> obtenerListaUsuarios(String id) {
        return conexion.obtenerListaUsuarios(id);
    }

    @Override
    public ArrayList<String> obtenerListaSugerencias(String id, String texto) throws RemoteException {
        return conexion.obtenerListaSugerencias(id, texto);
    }

    @Override
    public boolean registerForCallback(InterfazCliente callbackClientObject, String id, String password) throws RemoteException {
        //devuelve true si el usuario se registra correctamente o false en caso contrario.
        if (!clientesConectados.containsValue(callbackClientObject)
                && conexion.inicioSesion(id, password) && !clientesConectados.containsKey(id)) {
            clientesConectados.put(id, callbackClientObject);
            System.out.println(id + " se ha conectado");
            callbackClientObject.actualizarSolicitudes();
            System.out.println("hola");
            callbackClientObject.actualizarSolicitudesEnviadas();
            actualizarListaAmigos();

            for (String usuario : obtenerListaAmigos(id)) {
                if (clientesConectados.containsKey(usuario)) {
                    clientesConectados.get(usuario).recibirNotificacion(id + " se ha conectado");
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public ArrayList<String> obtenerListaAmigos(String id) {
        return conexion.obtenerListaAmigos(id);
    }

    @Override
    public ArrayList<String> refrescarListaAmigosPendientes(String id, InterfazCliente interfaz) throws RemoteException {
        ArrayList<String> solicitudesPendientes = new ArrayList<String>();
        if (clientesConectados.get(id).equals(interfaz)) {
            solicitudesPendientes = conexion.obtenerListaSolicitudesRecibidas(id);
        }
        return solicitudesPendientes;
    }

    @Override
    public ArrayList<String> refrescarListaSolicitudesPendientes(String id, InterfazCliente interfaz) throws RemoteException {
        ArrayList<String> solicitudesPendientes = new ArrayList<String>();
        if (clientesConectados.get(id).equals(interfaz)) {
            solicitudesPendientes = conexion.obtenerListaSolicitudesEnviadas(id);
        }
        return solicitudesPendientes;
    }

    @Override
    public ArrayList<String> refrescarListaAmigosConectados(String id, InterfazCliente interfaz) throws RemoteException {
      ArrayList<String> amigosPendientes = new ArrayList<String>();
      ArrayList<String> listaConectados = new ArrayList<String>();
      if (clientesConectados.get(id).equals(interfaz)) {
        amigosPendientes = conexion.obtenerListaAmigos(id);
        for(String amigo: amigosPendientes) {
            if(clientesConectados.containsKey(amigo)) 
                listaConectados.add(amigo);
        }
        }
      return listaConectados;
    }

    @Override
    public InterfazCliente obtenerReferenciaAmigo(String amigo) {
        return clientesConectados.get(amigo);
    }

    private void actualizarListaAmigos() throws RemoteException {
        for (String llave : clientesConectados.keySet()) {
            clientesConectados.get(llave).actualizarAmigosConectados();
        }
    }

    @Override
    public void actualizarContraseña(String id, InterfazCliente interfaz, String nuevaContraseña) throws RemoteException {
        if (clientesConectados.get(id).equals(interfaz)) {
            conexion.actualizarContraseña(id, nuevaContraseña);
        }
    }
}
