package Paquete;

public class Alerta {
    private CallbackClientInterface referenciaCliente;
    private String nombreEmpresa;
    private float cotizaciónUmbral;
    private boolean venta;

    public Alerta(CallbackClientInterface referenciaCliente, String nombreEmpresa, float cotizaciónUmbral, boolean venta) {
        this.referenciaCliente = referenciaCliente;
        this.nombreEmpresa = nombreEmpresa;
        this.cotizaciónUmbral = cotizaciónUmbral;
        this.venta = venta;
    }

    public CallbackClientInterface getReferenciaCliente() {
        return referenciaCliente;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public float getCotizaciónUmbral() {
        return cotizaciónUmbral;
    }

    public boolean isVenta() {
        return venta;
    }
}
