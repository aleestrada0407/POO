/** Contrato de los servicios que pueden calcular una cotizacion. */
public interface Cotizable {

    /** Recibe la descripcion de la solicitud y devuelve su costo simulado. */
    double cotizar(String datos);
}
