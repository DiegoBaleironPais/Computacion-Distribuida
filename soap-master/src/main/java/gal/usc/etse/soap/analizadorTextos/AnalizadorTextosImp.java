package gal.usc.etse.soap.analizadorTextos;

import javax.jws.WebService;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


@WebService(
        endpointInterface = "gal.usc.etse.soap.analizadorTextos.AnalizadorTextos",
        serviceName = "AnalizadorTextos"
)
public class AnalizadorTextosImp implements AnalizadorTextos {

    @Override
    public int contadorPalabras(String texto) {
        String[] palabras = texto.split(" ");
        return palabras.length;
    }

    @Override
    public int contadorDCaracteres(String texto) {
        return texto.length();
    }

    @Override
    public int contadorDFrases(String texto) {
        String[] palabras = texto.split("\\.");
        System.out.println(palabras[0]);
        return palabras.length;
    }

    @Override
    public int numeroOcurrencias(String texto, String palabra) {
        int ocurrencias = 0;
        String[] palabras = texto.split(" ");
        for (int i = 0; i < palabras.length; i++) {
            if (palabras[i].equals(palabra)) {
                ocurrencias++;
            }
        }
        return ocurrencias;
    }

    @Override
    public String palabraMasUsada(String texto) {
        String[] words = texto.split("\\s+");
        int finalCount = 0;
        int tempCount = 0;
        String mostlyUsedWord = null;
        for (String word: words) {
            tempCount = 0;
            for (String w: words) {
                if (word.equals(w)) {
                    tempCount++;
                }
            }
            if (tempCount >= finalCount) {
                finalCount = tempCount;
                mostlyUsedWord = word;
            }
        }
        
        return mostlyUsedWord;
    }

    @Override
    public String palabraMenosUsada(String texto) {
        String[] words = texto.split("\\s+");
        int finalCount = Integer.MAX_VALUE;
        int tempCount = 0;
        String leslyUsedWord = null;
        for (String word: words) {
            tempCount = 0;
            for (String w: words) {
                if (word.equals(w)) {
                    tempCount++;
                }
            }
            if (tempCount < finalCount) {
                finalCount = tempCount;
                leslyUsedWord = word;
            }
        }
        return leslyUsedWord;
    }

    @Override
    public String reemplazarPalabra(String texto, String palabraASustituir, 
            String nuevaPalabra) {
        StringBuilder retorno = new StringBuilder();
        String[] palabras = texto.split(" ");
        for (int i = 0; i < palabras.length; i++) {
            if (palabras[i].equals(palabraASustituir)) {
                palabras[i] = nuevaPalabra;
            }
            retorno.append(palabras[i]).append(" ");
        }
        retorno.deleteCharAt(retorno.length()-1);
        return retorno.toString();
    }

}
