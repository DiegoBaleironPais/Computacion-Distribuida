package Paquete;

import java.io.*;
import java.rmi.*;

public class Cliente {

    public static void main(String args[]) {
        try {
            int nuevaAlerta = 1;
            int RMIPort;
            String hostName;
            String nombreEmpresa;
            float cotizacionBolsa;
            char venta;
            InputStreamReader is = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(is);
            System.out.println("Enter how many seconds to stay registered:");
            String timeDuration = br.readLine();
            int time = Integer.parseInt(timeDuration.trim());
            System.out.println("Nombre de la maquina en la que se ubica el registro rmi:");
            hostName = br.readLine();
            System.out.println("Número de puerto en el que esta situado el registro rmi");
            String portNum = br.readLine();
            RMIPort = Integer.parseInt(portNum);
            String registryURL = "rmi://" + hostName  + ":" + portNum + "/callback";
            // find the remote object and cast it to interface object
            CallbackServerInterface h = (CallbackServerInterface) Naming.lookup(registryURL);
            CallbackClientInterface callbackObj =
                    new CallbackClientImpl();
            // register for callback
            while(nuevaAlerta == 1) {
                System.out.println("Nombre de la empresa para la que quieres generar la alerta:");
                nombreEmpresa = br.readLine();
                System.out.println("Valor umbral:");
                cotizacionBolsa = Float.parseFloat(br.readLine().trim());
                System.out.println("Pulsa 'v' para definir una alerta de venta, de lo contrario se definirá una alerta de compra:");
                venta = (char) br.read();
                h.registerForCallback(callbackObj,nombreEmpresa,cotizacionBolsa,venta=='v');
                System.out.println("Pulsa '1' para definir una nueva alerta de venta, para no definir otra alerta pulsa" +
                        " cualquier otro número");
                br.readLine();
                nuevaAlerta = Integer.parseInt(br.readLine().trim());
            }
            try {
                Thread.sleep(time * 1000);
            }
            catch (InterruptedException ex){ // sleep over
            }
            h.unregisterForCallback(callbackObj);
            System.out.println("Unregistered for callback.");
            System.exit(0);
        } // end try
        catch (Exception e) {
            System.out.println("Exception in CallbackClient: " + e);
        } // end catch
    }
}
