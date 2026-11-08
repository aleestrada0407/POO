/**
 * Alerta generada por un dispositivo. Esta pendiente hasta que se atiende.
 */
public class Alerta {
    // static: lo comparten todas las alertas, asi cada nueva recibe el siguiente numero
    private static int siguienteCodigo = 1;
    private int codigo;
    private Dispositivo dispositivo;
    private int horaGeneracion;
    private Severidad severidad;
    private String mensaje;
    private boolean atendida;
    private int horaAtencion;
    private int horasRetraso;
    private double credito;

    public Alerta(Dispositivo dispositivo, int horaGeneracion, Severidad severidad, String mensaje) {
        // Toma el codigo secuencial y deja listo el siguiente
        this.codigo = siguienteCodigo;
        siguienteCodigo++;
        this.dispositivo = dispositivo;
        this.horaGeneracion = horaGeneracion;
        this.severidad = severidad;
        this.mensaje = mensaje;
        // Toda alerta nace pendiente; -1 indica que todavia no tiene hora de atencion
        this.atendida = false;
        this.horaAtencion = -1;
        this.horasRetraso = 0;
        this.credito = 0;
    }

    public int getCodigo() {
        return codigo;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public int getHoraGeneracion() {
        return horaGeneracion;
    }

    public Severidad getSeveridad() {
        return severidad;
    }

    public String getMensaje() {
        return mensaje;
    }

    public boolean estaAtendida() {
        return atendida;
    }

    public int getHoraAtencion() {
        return horaAtencion;
    }

    public int getHorasRetraso() {
        return horasRetraso;
    }

    public double getCredito() {
        return credito;
    }

    /** Marca la alerta como atendida y calcula su retraso y su credito segun la severidad. */
    public void atender(int horaActual) {
        if (atendida) {
            throw new IllegalStateException("La alerta " + codigo + " ya fue atendida.");
        }
        atendida = true;
        horaAtencion = horaActual;
        // La severidad (Baja, Alta o Critica) sabe su plazo y su credito: no hace falta preguntar cual es
        horasRetraso = severidad.calcularHorasRetraso(horaActual - horaGeneracion);
        credito = severidad.calcularCredito(horasRetraso);
    }

    public boolean fueAtendidaConRetraso() {
        return atendida && horasRetraso > 0;
    }

    /** Orden de urgencia: mayor severidad, luego la generada primero, luego el codigo menor. */
    public boolean esMasUrgenteQue(Alerta otra) {
        // 1) Gana la de mayor prioridad (Critica 3, Alta 2, Baja 1)
        int miPrioridad = severidad.getPrioridad();
        int otraPrioridad = otra.getSeveridad().getPrioridad();
        if (miPrioridad != otraPrioridad) {
            return miPrioridad > otraPrioridad;
        }
        // 2) Si empatan, gana la que se genero primero
        if (horaGeneracion != otra.getHoraGeneracion()) {
            return horaGeneracion < otra.getHoraGeneracion();
        }
        // 3) Si siguen empatadas, gana la de codigo menor
        return codigo < otra.getCodigo();
    }

    @Override
    public String toString() {
        String texto = String.format("#%-3d %-8s %-8s hora %-4d %s", codigo, severidad.getNombre(),
                dispositivo.getCodigo(), horaGeneracion, mensaje);
        if (atendida) {
            texto = texto + String.format("  | atendida en hora %d, retraso %d h, crédito Q%.2f",
                    horaAtencion, horasRetraso, credito);
        }
        return texto;
    }
}
