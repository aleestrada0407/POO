/**
 * Ejercicio 6: polimorfismo mediante interfaces.
 * Clase abstracta con la configuracion comun de todas las conexiones.
 */
public abstract class Integracion implements Comparable<Integracion> {

    private int id;
    private String nombre;
    private String proveedor;
    private String urlBase;
    private boolean activa;
    private String estadoConexion;
    private double latenciaPromedioMs;

    /**
     * Constructor protegido: valida e inicializa los datos comunes.
     * Las clases hijas lo llaman mediante super(...).
     */
    protected Integracion(int id, String nombre, String proveedor, String urlBase,
            boolean activa, String estadoConexion, double latenciaPromedioMs)
            throws IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }
        if (proveedor == null || proveedor.trim().isEmpty()) {
            throw new IllegalArgumentException("El proveedor no puede estar vacio.");
        }
        if (urlBase == null || urlBase.trim().isEmpty()) {
            throw new IllegalArgumentException("La URL de configuracion no puede estar vacia.");
        }
        if (estadoConexion == null || estadoConexion.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado de conexion no puede estar vacio.");
        }
        if (latenciaPromedioMs < 0 || Double.isNaN(latenciaPromedioMs)
                || Double.isInfinite(latenciaPromedioMs)) {
            throw new IllegalArgumentException("La latencia debe ser finita y no negativa.");
        }
        this.id = id;
        this.nombre = nombre.trim();
        this.proveedor = proveedor.trim();
        this.urlBase = urlBase.trim();
        this.activa = activa;
        this.estadoConexion = estadoConexion.trim();
        // Tanto 0.0 como -0.0 representan una latencia de cero milisegundos.
        if (latenciaPromedioMs == 0) {
            this.latenciaPromedioMs = 0;
        } else {
            this.latenciaPromedioMs = latenciaPromedioMs;
        }
    }

    /** Devuelve el identificador unico de la integracion. */
    public int getId() {
        return id;
    }

    /** Devuelve el nombre mostrado en el catalogo. */
    public String getNombre() {
        return nombre;
    }

    /** Devuelve el nombre de la empresa que ofrece el servicio. */
    public String getProveedor() {
        return proveedor;
    }

    /** Devuelve la URL configurada, sin realizar ninguna conexion real. */
    public String getUrlBase() {
        return urlBase;
    }

    /** Indica si la integracion esta habilitada. */
    public boolean isActiva() {
        return activa;
    }

    /** Devuelve el estado simulado de conexion. */
    public String getEstadoConexion() {
        return estadoConexion;
    }

    /** Devuelve la latencia utilizada para ordenar las integraciones. */
    public double getLatenciaPromedioMs() {
        return latenciaPromedioMs;
    }

    /** Compara primero la latencia y, si hay empate, el identificador numerico. */
    @Override
    public int compareTo(Integracion otra) {
        if (otra == null) {
            throw new NullPointerException("No se puede comparar con una integracion nula.");
        }
        int resultado = Double.compare(latenciaPromedioMs, otra.getLatenciaPromedioMs());
        if (resultado == 0) {
            resultado = Integer.compare(id, otra.getId());
        }
        return resultado;
    }

    /** Reune en un texto los parametros comunes de configuracion. */
    @Override
    public String toString() {
        return "Id: " + id
                + "\nNombre: " + nombre
                + "\nProveedor: " + proveedor
                + "\nURL configurada: " + urlBase
                + "\nActiva: " + activa
                + "\nEstado de conexion: " + estadoConexion
                + "\nLatencia promedio: " + latenciaPromedioMs + " ms";
    }
}
