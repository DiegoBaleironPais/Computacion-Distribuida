/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Paquete;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

/**
 *
 * @author diego
 */
public class Servidor extends TimerTask {
    public static ArrayList<String> nombreEmpresas = new ArrayList<String>();
    public static ArrayList<Float> cotizacionAcciones = new ArrayList<Float>();
    private static CallbackServerImpl exportedObj;

    public static void main( String[] args ) throws IOException {
        try {
            exportedObj = new CallbackServerImpl();
            //Obtener los nombres de las empresas
            Servidor.registrarEmpresas();
            //Obtener los valores de las cotizaciones de las empresas
            TimerTask tarea = new Servidor();
            Timer refrescarCotizaciones = new Timer();
            refrescarCotizaciones.schedule(tarea,new java.util.Date(), 60000);
            //Publicación del objeto remoto
            InputStreamReader is =
                    new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(is);
            String portNum, registryURL;
            System.out.println("Enter the RMIregistry port number:");
            portNum = (br.readLine()).trim();
            int RMIPortNum = Integer.parseInt(portNum);
            //Creación del registro RMI
            startRegistry(RMIPortNum);
            //Creación del objeto servidor

            registryURL = "rmi://localhost:" + portNum + "/callback";
            Naming.rebind(registryURL, exportedObj);
            System.out.println("ServidorBolsa listo");
        }// end try
        catch (Exception re) {
            System.out.println("Exception: " + re.getMessage());
        }
    }

    private static void startRegistry(int RMIPortNum) throws RemoteException {
        try {
            Registry registry =
                    LocateRegistry.getRegistry(RMIPortNum);
            registry.list( );
            // This call will throw an exception
            // if the registry does not already exist
        }
        catch (RemoteException e) {
            // No valid registry at that port.
            Registry registry =
                    LocateRegistry.createRegistry(RMIPortNum);
        }
    }

    public void run()  {
        ArrayList<Float> aux2 = new ArrayList<Float>();
        int contador = 0;
        String[] aux;
        try {
            Document doc = Jsoup.connect("https://www.bolsamadrid.es/esp/aspx/Mercados/Precios.aspx?indice=ESI100000000&punto=indice").get();
            Element idTabla = doc.getElementById("ctl00_Contenido_tblAcciones");
            Elements tabla = idTabla.getElementsByTag("td");
            for (Element fila : tabla) {
                if (contador == 1) {
                    aux = fila.text().split(",");
                    aux2.add(Float.parseFloat(aux[0] + "." + aux[1]));
                }
                contador++;
                if (contador == 9) {
                    contador = 0;
                }
            }
            cotizacionAcciones = aux2;
            Servidor.exportedObj.doCallbacks();
        } catch (java.io.IOException e) {
            System.err.println("Exception "+e.getMessage());
        }
    }

    private static void registrarEmpresas()  {
        int contador = 0;
        try {
            Document doc = Jsoup.connect("https://www.bolsamadrid.es/esp/aspx/Mercados/Precios.aspx?indice=ESI100000000&punto=indice").get();
            Element idTabla = doc.getElementById("ctl00_Contenido_tblAcciones");
            Elements tabla = idTabla.getElementsByTag("td");
            for (Element fila : tabla) {
                if (contador == 0) {
                    Servidor.nombreEmpresas.add(fila.text());
                }
                contador++;
                if (contador == 9) {
                    contador = 0;
                }
            }
        } catch (java.io.IOException e) {
            System.err.println("Exception "+e.getMessage());
        }

    }

    public static String verEmpresas() {
        StringBuilder toReturn = new StringBuilder();
        Iterator<String> itS = nombreEmpresas.iterator();
        Iterator<Float> itF = cotizacionAcciones.iterator();

        while(itS.hasNext()) {
            toReturn.append("Empresa: ").append(itS.next()).append(" Cotización: ").append(itF.next()).append("\n");
        }

        return toReturn.toString();
    }
}
