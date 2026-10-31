/** Contrato de las integraciones que pueden devolver dinero. */
public interface Reintegrable {

    /** Intenta reintegrar un monto de una transaccion registrada. */
    boolean reintegrar(String idTransaccion, double monto);
}
