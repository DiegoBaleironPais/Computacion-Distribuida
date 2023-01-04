package gal.usc.etse.soap.calculator;

import java.util.ArrayList;
import javax.jws.WebService;

@WebService
public interface Calculator {
    int suma(int a, int b);
    int resta(int a, int b);
    int multiplicacion(int a, int b);
    double division(int a, int b);
    int potencia(int a, int b);
    double raizCuadrada(int a);
    double ln(int a);
    int maximo (ArrayList<Integer> nums);
    int min (ArrayList<Integer> nums);
    double media (ArrayList<Integer> listaDeMuestras);
    double mediana (ArrayList<Integer> listaDeMuestras);
    int moda (ArrayList<Integer> nums);
    double desviacionTipica (ArrayList<Integer> nums);
}
