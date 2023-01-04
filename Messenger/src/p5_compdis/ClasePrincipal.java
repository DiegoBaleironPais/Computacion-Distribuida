/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p5_compdis;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import pkg100añosmastarde.IntServer;

/**
 *
 * @author david
 */
public class ClasePrincipal {

    //ventanas
    VentanaInicioSesion v_inicio;
    VentanaPrincipal v_principal;
    VentanaRegistro v_registro;
    VentanaConexion v_conexion;
    //varibles para conexión con el servidor
    IntServer h;
    InterfazCliente callbackObject;
    String hostName;
    int RMIPort;
    private String nombre;
    HashMap<String, DefaultListModel> conversaciones;
    HashMap<String, InterfazCliente> interfacesAmigos;

    public ClasePrincipal() throws RemoteException {
        v_inicio = new VentanaInicioSesion(this);
        v_conexion = new VentanaConexion(this);
        v_principal = new VentanaPrincipal(this);
        v_registro = new VentanaRegistro(this);
        v_conexion.setVisible(true);
        callbackObject = new ImplementacionCliente(this);

        conversaciones = new HashMap<String, DefaultListModel>();
        interfacesAmigos = new HashMap<String, InterfazCliente>();
    }

    public void conectar(String host, String puerto) throws Exception {
        hostName = host;
        String portNum = puerto;
        RMIPort = Integer.parseInt(portNum);
        String registryURL = "rmi://" + hostName + ":" + portNum + "/servidorMensajes";

        h = (IntServer) Naming.lookup(registryURL);
        System.out.println("Lookup completed");
    }

    //abrir la ventana principal del usuario
    public void abrirVentanaPrincipal() {
        v_inicio.setVisible(false);
        v_principal.setVisible(true);
    }

    //abrir ventana para iniciar sesión
    public void abrirVentanaInicio() {
        v_inicio.setVisible(true);
    }

    //abrir la ventana para registrar un usuario nuevo
    public void abrirVentanaRegistro() {
        v_registro.setVisible(true);
    }

    public IntServer getH() {
        return h;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;

    }

    public void enviarMensaje(DefaultListModel modelo, String usuario) throws RemoteException {
        String mensaje = nombre + ": " + (String) modelo.get(modelo.getSize() - 1);
        modelo.setElementAt(mensaje, modelo.getSize() - 1);

        //introducir la conversación en el array de conversaciones
        conversaciones.put(usuario, modelo);
        v_principal.actualizarConversacion(usuario, modelo);

        //enviar mensaje a otro usuario
        interfacesAmigos.get(usuario).notifyMe(mensaje, nombre);
    }

    public void obtenerConversacion(String nombre) {
        DefaultListModel modelo;
        if (conversaciones.containsKey(nombre)) {
            modelo = conversaciones.get(nombre);
        } else {
            modelo = new DefaultListModel();
        }
        v_principal.actualizarConversacion(nombre, modelo);

    }

//    public void actualizarNotificaciones(String notificacion) {
//        v_principal.actualizarNotificaciones(notificacion);
//    }

    public boolean iniciarCallback(String id, String password) throws RemoteException {
        return h.registerForCallback(callbackObject, id, password);
    }

    //registrar a un usuario en la base de datos
    public boolean registrar_usuario(String id, String password) throws RemoteException {
        return h.registrarUsuario(id, password);
    }

    public void addMensaje(String usuario, String mensaje) {
        DefaultListModel modelo;

        if (conversaciones.containsKey(usuario)) {
            modelo = conversaciones.get(usuario);
        } else {
            modelo = new DefaultListModel();
        }

        modelo.add(modelo.getSize(), mensaje);
        conversaciones.put(usuario, modelo);

        v_principal.actualizarConversacion(usuario, modelo);

    }

    //añadir solicitud de amistad
    public void anadirSolicitud(String amigo) throws RemoteException {
        h.añadirSolicitud(this.nombre, amigo, callbackObject);
    }

    public InterfazCliente getCallbackObject() {
        return callbackObject;
    }

    public ArrayList<String> obtenerSugerencias(String texto) throws RemoteException {
        //obtener sugerencias de la base de datos
        return h.obtenerListaSugerencias(this.nombre, texto);
    }

    public void aceptarSolicitud(String amigo) throws RemoteException {
        h.aceptarAmigo(this.nombre, amigo, callbackObject);
    }

    public void rechazarSolicitud(String amigo) throws RemoteException {
        h.rechazarAmigo(this.nombre, amigo, callbackObject);
    }

    public VentanaPrincipal getV_principal() {
        return v_principal;
    }

    public String getNombre() {
        return nombre;
    }

    public void obtenerReferenciasAmigos(String id) {
        try {
            interfacesAmigos.clear();

            for (String i : h.obtenerListaAmigos(nombre)) {
                interfacesAmigos.put(i, h.obtenerReferenciaAmigo(i));
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

}
