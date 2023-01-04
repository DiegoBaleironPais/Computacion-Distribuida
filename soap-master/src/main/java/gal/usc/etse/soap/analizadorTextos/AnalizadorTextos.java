/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gal.usc.etse.soap.analizadorTextos;

import javax.jws.WebService;

/**
 *
 * @author diego
 */
@WebService
public interface AnalizadorTextos {
    int contadorPalabras(String texto);
    int contadorDCaracteres(String texto);
    int contadorDFrases(String texto);
    int numeroOcurrencias(String texto, String palabra);
    String palabraMasUsada(String texto);
    String palabraMenosUsada(String texto);
    String reemplazarPalabra(String texto,String palabraASustituir, 
            String nuevaPalabra);
}
