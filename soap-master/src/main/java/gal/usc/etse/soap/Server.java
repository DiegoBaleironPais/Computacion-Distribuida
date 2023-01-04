package gal.usc.etse.soap;

import gal.usc.etse.soap.analizadorTextos.AnalizadorTextosImp;
import gal.usc.etse.soap.calculator.Calculator;
import gal.usc.etse.soap.calculator.CalculatorImpl;
import javax.xml.ws.Endpoint;
import org.apache.log4j.BasicConfigurator;
import gal.usc.etse.soap.analizadorTextos.AnalizadorTextos;

public class Server {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        Calculator c = new CalculatorImpl();
        String address = "http://localhost:8080/calculator";
        Endpoint.publish(address, c);
        AnalizadorTextos a = new AnalizadorTextosImp();
        String address2 = "http://localhost:8080/analizadorTextos";
        Endpoint.publish(address2, a);
    }
}
