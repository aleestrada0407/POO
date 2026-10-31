import java.util.ArrayList;

/** Servicio que genera facturas XML simuladas y conserva eventos de auditoria. */
public class CertificadorFEL extends Integracion implements Consultable, Documentable, Auditable {

    private String nitEmisor;
    private String serie;
    private int siguienteNumero;
    private ArrayList<String> historialEventos;

    /** Constructor que configura el emisor y comienza la numeracion en uno. */
    public CertificadorFEL(int id, String nombre, String proveedor, String urlBase,
            boolean activa, String estadoConexion, double latenciaPromedioMs,
            String nitEmisor, String serie) throws IllegalArgumentException {
        super(id, nombre, proveedor, urlBase, activa, estadoConexion, latenciaPromedioMs);
        if (nitEmisor == null || nitEmisor.trim().isEmpty()) {
            throw new IllegalArgumentException("El NIT del emisor no puede estar vacio.");
        }
        if (serie == null || serie.trim().isEmpty()) {
            throw new IllegalArgumentException("La serie no puede estar vacia.");
        }
        this.nitEmisor = nitEmisor.trim();
        this.serie = serie.trim();
        siguienteNumero = 1;
        historialEventos = new ArrayList<String>();
    }

    /** Devuelve el NIT configurado para la simulacion. */
    public String getNitEmisor() {
        return nitEmisor;
    }

    /** Devuelve la serie de las facturas simuladas. */
    public String getSerie() {
        return serie;
    }

    /** Devuelve el numero que se asignara a la siguiente factura. */
    public int getSiguienteNumero() {
        return siguienteNumero;
    }

    /** Verifica en el historial si la referencia corresponde a un documento emitido. */
    @Override
    public String consultarEstado(String referencia)
            throws IllegalArgumentException, IllegalStateException {
        if (!isActiva() || !getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("El certificador no esta activo y conectado.");
        }
        if (referencia == null || referencia.trim().isEmpty()) {
            throw new IllegalArgumentException("La referencia de factura no puede estar vacia.");
        }
        referencia = referencia.trim();
        boolean encontrada = false;
        for (int i = 0; i < historialEventos.size(); i++) {
            if (historialEventos.get(i).equals("DOCUMENTO|" + referencia)) {
                encontrada = true;
            }
        }
        if (!encontrada) {
            throw new IllegalArgumentException("La factura no existe en este certificador.");
        }
        registrarEvento("Consulta de la factura " + referencia);
        return "Factura " + referencia + ": emitida (certificacion simulada).";
    }

    /** Construye un XML simulado, registra su referencia y avanza el consecutivo. */
    @Override
    public String generarDocumento(String datos)
            throws IllegalArgumentException, IllegalStateException {
        if (!isActiva() || !getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("El certificador no esta activo y conectado.");
        }
        if (datos == null || datos.trim().isEmpty()) {
            throw new IllegalArgumentException("Los datos de la factura no pueden estar vacios.");
        }
        if (siguienteNumero == Integer.MAX_VALUE) {
            throw new IllegalStateException("Se agoto la numeracion disponible para la simulacion.");
        }
        String referencia = serie + "-" + siguienteNumero;
        // Se sustituyen los caracteres que tienen un significado especial en XML.
        String datosXml = datos.trim().replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;");
        String nitXml = nitEmisor.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;");
        String serieXml = serie.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;");
        String documento = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "\n<FacturaSimulada>"
                + "\n  <NITEmisor>" + nitXml + "</NITEmisor>"
                + "\n  <Serie>" + serieXml + "</Serie>"
                + "\n  <Numero>" + siguienteNumero + "</Numero>"
                + "\n  <Datos>" + datosXml + "</Datos>"
                + "\n</FacturaSimulada>";
        // El registro distingue los documentos de las notas ingresadas por el usuario.
        historialEventos.add("DOCUMENTO|" + referencia);
        siguienteNumero = siguienteNumero + 1;
        return documento;
    }

    /** Registra una nota de auditoria sin confundirla con un documento emitido. */
    @Override
    public void registrarEvento(String evento) throws IllegalArgumentException, IllegalStateException {
        if (!isActiva() || !getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("El certificador no esta activo y conectado.");
        }
        if (evento == null || evento.trim().isEmpty()) {
            throw new IllegalArgumentException("El evento no puede estar vacio.");
        }
        historialEventos.add("EVENTO|" + evento.trim());
    }

    /** Devuelve una copia de los eventos registrados por el certificador. */
    @Override
    public ArrayList<String> consultarHistorialEventos() throws IllegalStateException {
        if (!isActiva() || !getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("El certificador no esta activo y conectado.");
        }
        return new ArrayList<String>(historialEventos);
    }

    /** Muestra la configuracion comun y los parametros propios de facturacion. */
    @Override
    public String toString() {
        return super.toString()
                + "\nNIT del emisor: " + nitEmisor
                + "\nSerie: " + serie
                + "\nSiguiente numero: " + siguienteNumero;
    }
}
