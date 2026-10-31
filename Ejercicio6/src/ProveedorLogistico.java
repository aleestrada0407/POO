import java.util.ArrayList;

/** Servicio de envios que consulta, cotiza, genera guias y registra eventos. */
public class ProveedorLogistico extends Integracion
        implements Consultable, Cotizable, Documentable, Auditable {

    private String cobertura;
    private double tarifaBase;
    private ArrayList<String> historialEventos;

    /** Constructor que valida la cobertura y la tarifa e inicia el historial vacio. */
    public ProveedorLogistico(int id, String nombre, String proveedor, String urlBase,
            boolean activa, String estadoConexion, double latenciaPromedioMs,
            String cobertura, double tarifaBase) throws IllegalArgumentException {
        super(id, nombre, proveedor, urlBase, activa, estadoConexion, latenciaPromedioMs);
        if (cobertura == null || cobertura.trim().isEmpty()) {
            throw new IllegalArgumentException("La cobertura no puede estar vacia.");
        }
        if (tarifaBase < 0 || Double.isNaN(tarifaBase) || Double.isInfinite(tarifaBase)) {
            throw new IllegalArgumentException("La tarifa debe ser finita y no negativa.");
        }
        this.cobertura = cobertura.trim();
        this.tarifaBase = tarifaBase;
        historialEventos = new ArrayList<String>();
    }

    /** Devuelve la zona en la que se ofrecen entregas. */
    public String getCobertura() {
        return cobertura;
    }

    /** Devuelve la tarifa fija utilizada por esta simulacion. */
    public double getTarifaBase() {
        return tarifaBase;
    }

    /** Simula el rastreo de una referencia y deja constancia de la consulta. */
    @Override
    public String consultarEstado(String referencia)
            throws IllegalArgumentException, IllegalStateException {
        if (!isActiva() || !getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("El proveedor logistico no esta activo y conectado.");
        }
        if (referencia == null || referencia.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo de seguimiento no puede estar vacio.");
        }
        registrarEvento("Consulta del envio " + referencia.trim());
        return "Envio " + referencia.trim() + ": en transito (estado simulado).";
    }

    /** Devuelve la tarifa base como cotizacion fija, tal como permite el analisis. */
    @Override
    public double cotizar(String datos) throws IllegalArgumentException, IllegalStateException {
        if (!isActiva() || !getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("El proveedor logistico no esta activo y conectado.");
        }
        if (datos == null || datos.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripcion del envio no puede estar vacia.");
        }
        registrarEvento("Cotizacion de " + datos.trim() + ": " + tarifaBase + " GTQ");
        return tarifaBase;
    }

    /** Genera una guia textual simulada con los datos del envio y del proveedor. */
    @Override
    public String generarDocumento(String datos)
            throws IllegalArgumentException, IllegalStateException {
        if (!isActiva() || !getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("El proveedor logistico no esta activo y conectado.");
        }
        if (datos == null || datos.trim().isEmpty()) {
            throw new IllegalArgumentException("Los datos de la guia no pueden estar vacios.");
        }
        String referencia = "GUIA-" + getId() + "-" + (historialEventos.size() + 1);
        String documento = "GUIA DE ENVIO SIMULADA"
                + "\nReferencia: " + referencia
                + "\nProveedor: " + getProveedor()
                + "\nCobertura: " + cobertura
                + "\nDatos del envio: " + datos.trim();
        registrarEvento("Generacion de la guia " + referencia);
        return documento;
    }

    /** Agrega un mensaje valido al historial del proveedor. */
    @Override
    public void registrarEvento(String evento) throws IllegalArgumentException, IllegalStateException {
        if (!isActiva() || !getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("El proveedor logistico no esta activo y conectado.");
        }
        if (evento == null || evento.trim().isEmpty()) {
            throw new IllegalArgumentException("El evento no puede estar vacio.");
        }
        historialEventos.add(evento.trim());
    }

    /** Devuelve una copia del historial sin exponer la lista interna. */
    @Override
    public ArrayList<String> consultarHistorialEventos() throws IllegalStateException {
        if (!isActiva() || !getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("El proveedor logistico no esta activo y conectado.");
        }
        return new ArrayList<String>(historialEventos);
    }

    /** Incluye la cobertura y la tarifa en la configuracion del proveedor. */
    @Override
    public String toString() {
        return super.toString()
                + "\nCobertura: " + cobertura
                + "\nTarifa base: " + tarifaBase + " GTQ";
    }
}
