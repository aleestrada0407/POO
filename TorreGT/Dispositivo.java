/**
 * Clase abstracta con los datos y el comportamiento comunes de todos los dispositivos.
 * Las capacidades opcionales (regular, medir, registrar movimientos) se rechazan por defecto;
 * las subclases que si las tienen sobrescriben esos metodos.
 */
public abstract class Dispositivo {
    private String codigo;          // identificador unico del dispositivo
    private Oficina oficina;        // oficina donde esta instalado
    private double potenciaNominal; // watts
    private boolean encendido;      // true = encendido, false = apagado
    private double consumoDiaKWh;   // kWh acumulados en el dia en curso

    // Constructor protegido: solo lo llaman las subclases con super(...).
    // Primero valida todo y solo despues asigna, asi un dato invalido no deja un objeto a medias.
    protected Dispositivo(String codigo, Oficina oficina, double potenciaNominal, boolean encendido) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del dispositivo no puede estar vacío.");
        }
        if (oficina == null) {
            throw new IllegalArgumentException("La oficina del dispositivo no existe.");
        }
        if (Double.isNaN(potenciaNominal) || Double.isInfinite(potenciaNominal) || potenciaNominal <= 0) {
            throw new IllegalArgumentException("La potencia nominal debe ser un número mayor que cero.");
        }
        this.codigo = codigo.trim();
        this.oficina = oficina;
        this.potenciaNominal = potenciaNominal;
        this.encendido = encendido;
        this.consumoDiaKWh = 0;
    }

    public String getCodigo() {
        return codigo;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public double getPotenciaNominal() {
        return potenciaNominal;
    }

    public boolean estaEncendido() {
        return encendido;
    }

    public double getConsumoDiaKWh() {
        return consumoDiaKWh;
    }

    public void encender() {
        encendido = true;
    }

    public void apagar() {
        encendido = false;
    }

    /** Calcula el consumo de una hora segun el estado actual, lo acumula y lo devuelve en kWh. */
    public double registrarHora() {
        if (!encendido) {
            return 0;
        }
        // calcularConsumoHoraW() es abstracto: Java ejecuta la version de la clase real del objeto
        // (Luminaria, Climatizador, etc.). Los watts se pasan a kWh dividiendo entre 1000.
        double kWh = calcularConsumoHoraW() / 1000;
        consumoDiaKWh = consumoDiaKWh + kWh;
        return kWh;
    }

    public void reiniciarConsumoDia() {
        consumoDiaKWh = 0;
    }

    // Lo usan las subclases antes de regular, medir o registrar movimientos
    protected void validarEncendido() {
        if (!encendido) {
            throw new IllegalStateException("El dispositivo " + codigo + " está apagado.");
        }
    }

    // compareTo de String devuelve un numero negativo si este codigo va antes alfabeticamente
    public boolean tieneCodigoMenorQue(Dispositivo otro) {
        return codigo.compareTo(otro.getCodigo()) < 0;
    }

    /** Watts que consume en una hora encendido. Cada tipo tiene su formula. */
    public abstract double calcularConsumoHoraW();

    public abstract String getTipo();

    // Versiones por defecto de las capacidades opcionales: rechazan la operacion.
    // DispositivoRegulable, Climatizador, SensorHumo y CamaraSeguridad las sobrescriben.
    // Gracias a esto el sistema llama al mismo metodo sobre cualquier dispositivo sin usar instanceof.
    public void ajustarNivel(int nivel) {
        throw new IllegalStateException("El dispositivo " + codigo + " (" + getTipo() + ") no es regulable.");
    }

    public Alerta registrarLectura(double valor, int horaSimulacion) {
        throw new IllegalStateException("El dispositivo " + codigo + " (" + getTipo() + ") no mide.");
    }

    public Alerta registrarMovimiento(int horaSimulacion) {
        throw new IllegalStateException("El dispositivo " + codigo + " (" + getTipo() + ") no es una cámara.");
    }

    @Override
    public String toString() {
        String estado = "Apagado";
        if (encendido) {
            estado = "Encendido";
        }
        return String.format("%-8s %-22s %-10s %8.1f W   consumo del día: %.3f kWh",
                codigo, getTipo(), estado, potenciaNominal, consumoDiaKWh);
    }
}
