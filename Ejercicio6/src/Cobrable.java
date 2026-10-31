/** Contrato exclusivo para las integraciones que realizan cobros. */
public interface Cobrable {

    /** Simula un cobro y devuelve el identificador de la transaccion. */
    String cobrar(double monto);
}
