/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodigoFuncional;

import VentanasGráficas.GestionVentanas;


/**
 *
 * @author diego
 */
public class Main {
    private static GestionVentanas gestorVentanas;

    public static GestionVentanas getGestorVentanas() {
        return gestorVentanas;
    }
   
    public static void main (String[] args)  {
        gestorVentanas = new GestionVentanas();
        Socket.inicializarSocket();
        Socket.listener();
    }
        
}
