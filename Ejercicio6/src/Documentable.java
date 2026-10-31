/** Contrato de los servicios que generan documentos simulados. */
public interface Documentable {

    /** Construye una guia o una factura simulada a partir de los datos recibidos. */
    String generarDocumento(String datos);
}
