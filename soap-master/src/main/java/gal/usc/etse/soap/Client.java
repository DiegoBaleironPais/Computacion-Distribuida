package gal.usc.etse.soap;

import gal.usc.etse.soap.calculator.Calculator;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.log4j.BasicConfigurator;
import gal.usc.etse.soap.analizadorTextos.AnalizadorTextos;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws MalformedURLException {
        BasicConfigurator.configure();
        String resultadoString;
        int resultadoInt;
        Double resultadoDouble;
        int opcion = 0;
        int[] valores;
        String aux, aux2, aux3;
        ArrayList<Integer> arrlist;
        Scanner lector = new Scanner(System.in);
      
        URL wsdlURLCalculator = new URL("http://localhost:8080/calculator?wsdl");
        QName SERVICE_NAME_CALCULATOR = new QName("http://calculator.soap.etse.usc.gal/", "Calculator");
        Service service1 = Service.create(wsdlURLCalculator, SERVICE_NAME_CALCULATOR);
        Calculator clienteCalculadora = service1.getPort(Calculator.class);
        URL wsdlURLTextAnalyzer = new URL("http://localhost:8080/analizadorTextos?wsdl");
        QName SERVICE_NAME_TEXTANALYZER = new QName("http://analizadorTextos.soap.etse.usc.gal/", "AnalizadorTextos");
        Service service2 = Service.create(wsdlURLTextAnalyzer, SERVICE_NAME_TEXTANALYZER);
        AnalizadorTextos clienteTextos = service2.getPort(AnalizadorTextos.class);
        
        do {
            System.out.println("Selecciona el servicio que quieres utilizar, pulsando el número correspondiente:");
            System.out.println("\t1. Suma de dos números.");
            System.out.println("\t2. Resta de dos números");
            System.out.println("\t3. Multiplicación de dos números");
            System.out.println("\t4. División de dos números");
            System.out.println("\t5. Potencia de un número.");
            System.out.println("\t6. Raíz cuadrada de un número. ");
            System.out.println("\t7. Logaritmo neperiano de un número.");
            System.out.println("\t8. Máximo de una lista de números.");
            System.out.println("\t9. Mínimo de una lista de números.");
            System.out.println("\t10. Media de una lista de números.");
            System.out.println("\t11. Mediana de una lista de números");
            System.out.println("\t12. Moda de una lista de números.");
            System.out.println("\t13. Desviación típica de una lista de números");
            System.out.println("\t14. Contar palabras.");
            System.out.println("\t15. Contar caracteres (incluyendo espacios, signos de puntuación, etc.)");
            System.out.println("\t16. Contar frases.");
            System.out.println("\t17. Número de veces que aparece una palabra.");
            System.out.println("\t18. Palabra más usada.");
            System.out.println("\t19. Palabra menos usada.");
            System.out.println("\t20. Reemplazar palabra.");
            System.out.println("\t-1. Para salir.\n");

            opcion = lector.nextInt();
            lector.nextLine();
            switch(opcion) {
                case 1:
                    valores = leerDosValores();
                    resultadoInt = clienteCalculadora.suma(valores[0], valores[1]);
                    System.out.println("Resultado: " +resultadoInt);
                    break;
                case 2:
                    valores = leerDosValores();
                    resultadoInt = clienteCalculadora.resta(valores[0], valores[1]);
                    System.out.println("Resultado: " +resultadoInt);
                    break;
                case 3:
                    valores = leerDosValores();
                    resultadoInt = clienteCalculadora.multiplicacion(valores[0], valores[1]);
                    System.out.println("Resultado: " +resultadoInt);
                    break;
                case 4:
                    valores = leerDosValores();
                    resultadoDouble = clienteCalculadora.division(valores[0], valores[1]);
                    System.out.println("Resultado: " +resultadoDouble);
                    break;
                case 5:
                    valores = leerDosValores();
                    resultadoInt = clienteCalculadora.potencia(valores[0], valores[1]);
                    System.out.println("Resultado: " +resultadoInt);
                    break;
                case 6:
                    
                    resultadoDouble = clienteCalculadora.raizCuadrada(leerUnValor());
                    System.out.println("Resultado: " +resultadoDouble);
                    break;
                case 7:
                    resultadoDouble = clienteCalculadora.ln(leerUnValor());
                    System.out.println("Resultado: " +resultadoDouble);
                    break;
                case 8:
                    arrlist = leerLista();
                    resultadoInt = clienteCalculadora.maximo(arrlist);
                    System.out.println("Resultado: " +resultadoInt);
                    break;
                case 9:
                    arrlist = leerLista();
                    resultadoInt = clienteCalculadora.min(arrlist);
                    System.out.println("Resultado: " +resultadoInt);
                    break;
                case 10:
                    arrlist = leerLista();
                    resultadoDouble = clienteCalculadora.media(arrlist);
                    System.out.println("Resultado: " +resultadoDouble);
                    break;
                case 11:
                    arrlist = leerLista();
                    resultadoDouble = clienteCalculadora.mediana(arrlist);
                    System.out.println("Resultado: " +resultadoDouble);
                    break;
                case 12:
                    arrlist = leerLista();
                    resultadoInt = clienteCalculadora.moda(arrlist);
                    System.out.println("Resultado: " + resultadoInt);
                    break;
                case 13:
                    arrlist = leerLista();
                    resultadoDouble = clienteCalculadora.desviacionTipica(arrlist);
                    System.out.println("Resultado: " + resultadoDouble);
                    break;
                case 14:
                    aux = leerString();
                    resultadoInt= clienteTextos.contadorPalabras(aux);
                    System.out.println("Resultado: " + resultadoInt);
                    break;
                case 15:
                    aux = leerString();
                    resultadoInt= clienteTextos.contadorDCaracteres(aux);
                    System.out.println("Resultado: " + resultadoInt);
                    break;
                case 16:
                    aux = leerString();
                    resultadoInt= clienteTextos.contadorDFrases(aux);
                    System.out.println("Resultado: " + resultadoInt);
                    break;
                case 17:
                    aux = leerString();
                    System.out.println("Que palabra quieres comprobar?");
                    aux2 = lector.nextLine();
                    resultadoInt= clienteTextos.numeroOcurrencias(aux,aux2);
                    System.out.println("Resultado: " + resultadoInt);
                    break;
                case 18:
                    aux = leerString();
                    resultadoString = clienteTextos.palabraMasUsada(aux);
                    System.out.println("Resultado: " + resultadoString);
                    break;
                case 19:
                    aux = leerString();
                    resultadoString = clienteTextos.palabraMenosUsada(aux);
                    System.out.println("Resultado: " + resultadoString);
                    break;
                case 20:
                    aux = leerString();
                    System.out.println("Que palabra quieres reemplazar?");
                    aux2 = lector.nextLine();
                    System.out.println("Que palabra quieres poner en su lugar?");
                    aux3 = lector.nextLine();
                    resultadoString = clienteTextos.reemplazarPalabra(aux,aux2,aux3);
                    System.out.println("Resultado: " + resultadoString);
                    break;
                case -1:
                    System.exit(0);
                default:
                    System.out.println("Opción incorrecta");
                    break;
            }
        } while (opcion != -1);
    }
    
    private static int[] leerDosValores() {
        int[] valores = new int[2];
        Scanner lector = new Scanner(System.in);
        
        System.out.println("Valor del operador 1: ");
        valores[0] = lector.nextInt();
        System.out.println("Valor del operador 2: ");
        valores[1] = lector.nextInt();
        return valores;
    }
    
    private static int leerUnValor() {
        int valor;
        Scanner lector = new Scanner(System.in);
        
        System.out.println("Valor del operador: ");
        valor = lector.nextInt();
        return valor;
    }
    
    private static ArrayList<Integer> leerLista() {
        ArrayList<Integer> arrlist = new ArrayList();
        char parada = 's';
        int aux;
        Scanner lector = new Scanner(System.in);
        
        while (parada == 's') {
            System.out.println("Introduce un nuevo valor en la lista: ");
            aux = lector.nextInt();
            arrlist.add(aux);
            lector.nextLine();
            System.out.println("Pulsa 's' para seguir añadiendo valores: ");
            parada = lector.next().charAt(0);
        }
        
        return arrlist;
    }
    
    private static String leerString() {
        String aux;
        Scanner lector = new Scanner(System.in);
        
        System.out.println("Introduce el string a analizar:");
        aux = lector.nextLine();
        
        return aux;
    }
}
