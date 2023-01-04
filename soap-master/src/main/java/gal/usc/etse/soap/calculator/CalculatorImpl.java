package gal.usc.etse.soap.calculator;

import static java.lang.Math.sqrt;
import java.util.ArrayList;
import javax.jws.WebService;
import java.util.Collections;
import java.util.Iterator;

@WebService(
        endpointInterface = "gal.usc.etse.soap.calculator.Calculator",
        serviceName = "Calculator"
)
public class CalculatorImpl implements Calculator {
    @Override
    public int suma(int a, int b) {
        return a + b;
    }

    @Override
    public int resta(int a, int b) {
        return a - b;
    }

    @Override
    public int multiplicacion(int a, int b) {
        return a * b;
    }

    @Override
    public double division(int a, int b) {
        return (double) a / (double) b;
    }

    @Override
    public int potencia(int a, int b) {
        int resultado = 1;
        while (b-- > 0) {
            resultado *= a;
        }
        return resultado;
    }

    @Override
    public double raizCuadrada(int a) {
        return Math.sqrt(a);
    }

    @Override
    public double ln(int a) {
        return Math.log(a);
    }
    

    @Override
    public int maximo(ArrayList<Integer> nums) {
        Collections.sort(nums);
        return nums.get(nums.size()-1);
    }

    
    @Override
    public int min(ArrayList<Integer> nums) {
        Collections.sort(nums);
        return nums.get(0);
    }
    
    @Override
    public double media (ArrayList<Integer> nums) {
        Iterator<Integer> numsIt = nums.iterator();
        double media = 0;
        while(numsIt.hasNext()) {
            media += numsIt.next();
        }
        return media/nums.size();
    }

    @Override
    public double mediana(ArrayList<Integer> nums) {
        Collections.sort(nums);
        int size = nums.size();
        if (size % 2 != 0) {
            return nums.get((nums.size()-1)/2);
        } else {
            double media = ((double) nums.get(nums.size()/2) + (double) nums.get((nums.size()/2)-1)) / 2;
            return media;
        }
    }

    @Override
    public int moda(ArrayList<Integer> nums) {
        Collections.sort(nums);
        Iterator<Integer> numsIt = nums.iterator();
        int[] masRepetido = {0,0};
        int[] auxiliar = {0,0};
        while(numsIt.hasNext()) {
            int valorIterator = numsIt.next();
            if (auxiliar[0] != valorIterator) {
                auxiliar[0] = valorIterator;
                auxiliar[1] = 1;
            } else {
                auxiliar[1]++;
            }
            if (auxiliar[1] >= masRepetido[1]) {
                masRepetido[0] = auxiliar[0];
                masRepetido[1] = auxiliar[1];
            }
        }
        return masRepetido[0];
    }
            

    @Override
    public double desviacionTipica(ArrayList<Integer> nums) {
        double media = 0;
        Iterator<Integer> numsIt = nums.iterator();
        while(numsIt.hasNext()) {
           media += numsIt.next();
        }
        media = media / nums.size();
        numsIt = nums.iterator();
        double sumatorio = 0;
        double aux;
        while(numsIt.hasNext()) {
           aux = (numsIt.next() - media);
           aux = aux * aux;
           sumatorio += aux;
        }
        sumatorio = sumatorio / nums.size();
        return sqrt(sumatorio);
    }

}
