/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VentanasGráficas;

/**
 *
 * @author diego
 */
public class GestionVentanas {
    private VentanaInicio ventanaInicio;
    private VentanaPrincipal ventanaPrincipal;

    public GestionVentanas() {
        ventanaPrincipal = new VentanaPrincipal();
        ventanaInicio = new VentanaInicio(ventanaPrincipal,true);
        ventanaPrincipal.setVisible(true);
        ventanaInicio.setVisible(true);
    }

    public VentanaPrincipal getVentanaPrincipal() {
        return ventanaPrincipal;
    }
    
    public static boolean esCadenaVacia(String cadena) {
        cadena = cadena.replace(" ", "");
        if (cadena.equals("")) {
            return true;
        } else {
            return false;
        }
    }
    
}
