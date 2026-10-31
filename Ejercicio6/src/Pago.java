import java.util.ArrayList;

/** Pasarela de pago simulada con consulta, cobro, reintegro y auditoria. */
public class Pago extends Integracion implements Consultable, Cobrable, Reintegrable, Auditable {

    private String medioPago;
    private String moneda;
    private double comisionPorcentaje;
    private ArrayList<String> historialEventos;

    /** Constructor que inicializa la configuracion y crea un historial vacio. */
    public Pago(int id, String nombre, String proveedor, String urlBase,
            boolean activa, String estadoConexion, double latenciaPromedioMs,
            String medioPago, String moneda, double comisionPorcentaje)
            throws IllegalArgumentException {
        super(id, nombre, proveedor, urlBase, activa, estadoConexion, latenciaPromedioMs);
        if (medioPago == null || medioPago.trim().isEmpty()) {
            throw new IllegalArgumentException("El medio de pago no puede estar vacio.");
        }
        if (moneda == null || moneda.trim().isEmpty()) {
            throw new IllegalArgumentException("La moneda no puede estar vacia.");
        }
        if (comisionPorcentaje < 0 || comisionPorcentaje > 100
                || Double.isNaN(comisionPorcentaje) || Double.isInfinite(comisionPorcentaje)) {
            throw new IllegalArgumentException("La comision debe estar entre 0 y 100.");
        }
        this.medioPago = medioPago.trim();
        this.moneda = moneda.trim();
        this.comisionPorcentaje = comisionPorcentaje;
        historialEventos = new ArrayList<String>();
    }

    /** Devuelve el medio de pago de esta pasarela. */
    public String getMedioPago() {
        return medioPago;
    }

    /** Devuelve la moneda de las transacciones simuladas. */
    public String getMoneda() {
        return moneda;
    }

    /** Devuelve la comision configurada para la pasarela. */
    public double getComisionPorcentaje() {
        return comisionPorcentaje;
    }

    /** Busca el cobro y sus reintegros en el historial para indicar su estado. */
    @Override
    public String consultarEstado(String referencia)
            throws IllegalArgumentException, IllegalStateException {
        if (!isActiva() || !getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("La pasarela no esta activa y conectada.");
        }
        if (referencia == null || referencia.trim().isEmpty()) {
            throw new IllegalArgumentException("La referencia no puede estar vacia.");
        }
        referencia = referencia.trim();
        boolean encontrada = false;
        double montoCobrado = 0;
        double montoReintegrado = 0;
        // Los registros internos guardan tipo, referencia y monto separados por |.
        for (int i = 0; i < historialEventos.size(); i++) {
            String[] partes = historialEventos.get(i).split("\\|");
            if (partes.length == 3 && partes[1].equals(referencia)) {
                if (partes[0].equals("COBRO")) {
                    encontrada = true;
                    montoCobrado = Double.parseDouble(partes[2]);
                } else if (partes[0].equals("REINTEGRO")) {
                    montoReintegrado = montoReintegrado + Double.parseDouble(partes[2]);
                }
            }
        }
        if (!encontrada) {
            throw new IllegalArgumentException("La transaccion no existe en esta pasarela.");
        }
        String estado = "Cobro realizado";
        if (montoReintegrado > 0) {
            if (montoReintegrado >= montoCobrado) {
                estado = "Reintegro total realizado";
            } else {
                estado = "Reintegro parcial realizado";
            }
        }
        registrarEvento("Consulta de la transaccion " + referencia);
        return referencia + ": " + estado + " (simulado).";
    }

    /** Valida el monto, registra el cobro simulado y devuelve su referencia. */
    @Override
    public String cobrar(double monto) throws IllegalArgumentException, IllegalStateException {
        if (!isActiva() || !getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("La pasarela no esta activa y conectada.");
        }
        if (monto <= 0 || Double.isNaN(monto) || Double.isInfinite(monto)) {
            throw new IllegalArgumentException("El monto debe ser positivo y finito.");
        }
        String referencia = "PAGO-" + getId() + "-" + (historialEventos.size() + 1);
        // Se utiliza el historial ya declarado; no se agrega otra lista de transacciones.
        historialEventos.add("COBRO|" + referencia + "|" + monto);
        return referencia;
    }

    /** Reintegra un monto valido sin exceder el saldo del cobro registrado. */
    @Override
    public boolean reintegrar(String idTransaccion, double monto) throws IllegalStateException {
        if (!isActiva() || !getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("La pasarela no esta activa y conectada.");
        }
        // El analisis especifica false para los datos invalidos de un reintegro.
        if (idTransaccion == null || idTransaccion.trim().isEmpty()
                || monto <= 0 || Double.isNaN(monto) || Double.isInfinite(monto)) {
            return false;
        }
        idTransaccion = idTransaccion.trim();
        boolean encontrada = false;
        double montoCobrado = 0;
        double montoReintegrado = 0;
        for (int i = 0; i < historialEventos.size(); i++) {
            String[] partes = historialEventos.get(i).split("\\|");
            if (partes.length == 3 && partes[1].equals(idTransaccion)) {
                if (partes[0].equals("COBRO")) {
                    encontrada = true;
                    montoCobrado = Double.parseDouble(partes[2]);
                } else if (partes[0].equals("REINTEGRO")) {
                    montoReintegrado = montoReintegrado + Double.parseDouble(partes[2]);
                }
            }
        }
        double disponible = montoCobrado - montoReintegrado;
        if (!encontrada || disponible <= 0) {
            return false;
        }
        if (monto > disponible) {
            // Se admite la pequena diferencia de representacion de los double.
            if (monto - disponible > Math.ulp(montoCobrado) * 4) {
                return false;
            }
            monto = disponible;
        }
        historialEventos.add("REINTEGRO|" + idTransaccion + "|" + monto);
        return true;
    }

    /** Registra una nota sin permitir que se confunda con un cobro o reintegro. */
    @Override
    public void registrarEvento(String evento) throws IllegalArgumentException, IllegalStateException {
        if (!isActiva() || !getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("La pasarela no esta activa y conectada.");
        }
        if (evento == null || evento.trim().isEmpty()) {
            throw new IllegalArgumentException("El evento no puede estar vacio.");
        }
        historialEventos.add("EVENTO|" + evento.trim());
    }

    /** Entrega una copia para que no se puedan modificar los registros internos. */
    @Override
    public ArrayList<String> consultarHistorialEventos() throws IllegalStateException {
        if (!isActiva() || !getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("La pasarela no esta activa y conectada.");
        }
        return new ArrayList<String>(historialEventos);
    }

    /** Agrega los parametros de pago a la configuracion heredada. */
    @Override
    public String toString() {
        return super.toString()
                + "\nMedio de pago: " + medioPago
                + "\nMoneda: " + moneda
                + "\nComision: " + comisionPorcentaje + "%";
    }
}
