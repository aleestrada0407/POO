/** Contrato de las integraciones que permiten consultar una referencia. */
public interface Consultable {

    /** Consulta el estado simulado de una transaccion, un envio o un documento. */
    String consultarEstado(String referencia);
}
