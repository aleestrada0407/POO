/** API simulada que solo ofrece consultas de seguimiento de paquetes. */
public class Rastreo extends Integracion implements Consultable {

    /** Constructor que envia todos los datos comunes a Integracion. */
    public Rastreo(int id, String nombre, String proveedor, String urlBase,
            boolean activa, String estadoConexion, double latenciaPromedioMs) {
        super(id, nombre, proveedor, urlBase, activa, estadoConexion, latenciaPromedioMs);
    }

    /** Valida el codigo y devuelve el estado de un paquete de forma simulada. */
    @Override
    public String consultarEstado(String referencia)
            throws IllegalArgumentException, IllegalStateException {
        if (!isActiva() || !getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("El servicio de rastreo no esta activo y conectado.");
        }
        if (referencia == null || referencia.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo de seguimiento no puede estar vacio.");
        }
        return "Paquete " + referencia.trim() + ": en ruta de entrega (estado simulado).";
    }
}
