package Paquete;

import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class implements the remote interface 
 * CallbackServerInterface.
 * @author M. L. Liu
 */

public class CallbackServerImpl extends UnicastRemoteObject implements CallbackServerInterface {

    private ArrayList<Alerta> alertas;


    public CallbackServerImpl() throws RemoteException {
        super( );
        alertas = new ArrayList<Alerta>();
    }

    public synchronized void registerForCallback(CallbackClientInterface callbackClientObject, String nombreEmpresa,
        float valorUmbral, boolean Venta) throws java.rmi.RemoteException {
        // store the callback object into the vector
        Alerta nuevaAlerta = new Alerta(callbackClientObject, nombreEmpresa, valorUmbral, Venta);
        if (Servidor.nombreEmpresas.contains(nombreEmpresa) && !alertas.contains(nuevaAlerta)) {
            alertas.add(nuevaAlerta);
            System.out.println("Alerta registrada: " + nombreEmpresa + " " + valorUmbral + " " + Venta);
        }
    }

    public synchronized void unregisterForCallback(CallbackClientInterface callbackClientObject) throws java.rmi.RemoteException {
        Alerta aux;
        Iterator<Alerta> itA = alertas.iterator();
        ArrayList<Alerta> eliminarAlertas = new ArrayList<Alerta>();
        while(itA.hasNext()) {
            aux = itA.next();
            if (aux.getReferenciaCliente().equals(callbackClientObject)) {
                eliminarAlertas.add(aux);
            }
        }
        for (int i = 0; i < eliminarAlertas.size(); i++) {
            alertas.remove(eliminarAlertas.get(i));
        }
    }

    public synchronized void doCallbacks() throws java.rmi.RemoteException{
        Iterator<Alerta> itA = alertas.iterator();
        Alerta aux;
        ArrayList<Alerta> eliminarAlertas = new ArrayList<Alerta>();
        while(itA.hasNext()) {
            aux = itA.next();
            if (aux.isVenta() == true) {
                if (Servidor.cotizacionAcciones.get(Servidor.nombreEmpresas.indexOf(aux.getNombreEmpresa())) >
                        aux.getCotizaciónUmbral()) {
                    aux.getReferenciaCliente().notifyMe("Alerta de venta en la empresa: " + aux.getNombreEmpresa() +
                            "  umbral: " + aux.getCotizaciónUmbral() + "\nSe ha eliminado la alerta.");
                    eliminarAlertas.add(aux);
                    System.out.println("Alerta de venta notificada");
                }
            } else {
                if (Servidor.cotizacionAcciones.get(Servidor.nombreEmpresas.indexOf(aux.getNombreEmpresa())) <
                        aux.getCotizaciónUmbral()) {
                    aux.getReferenciaCliente().notifyMe("Alerta de compra en la empresa: " + aux.getNombreEmpresa() +
                            "  umbral: " + aux.getCotizaciónUmbral() + "\nSe ha eliminado la alerta.");
                    eliminarAlertas.add(aux);
                    System.out.println("Alerta de compra notificada");
                }
            }
        }

        for (int i = 0; i < eliminarAlertas.size(); i++) {
            alertas.remove(eliminarAlertas.get(i));
        }
    }

}