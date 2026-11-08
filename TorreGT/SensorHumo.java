/**
 * Sensor de humo: siempre encendido, no se regula y mide la concentracion de humo en ppm.
 */
public class SensorHumo extends Dispositivo implements Medidor {
    private final double ppmMin = 0;
    private final double ppmMax = 1000;
    private double ultimaLectura;

    public SensorHumo(String codigo, Oficina oficina, double potenciaNominal) {
        // true: el sensor de humo se registra encendido y nunca se apaga
        super(codigo, oficina, potenciaNominal, true);
        this.ultimaLectura = 0;
    }

    @Override
    // Sobrescribe apagar() de Dispositivo para rechazar la operacion
    public void apagar() {
        throw new IllegalStateException("El sensor de humo " + getCodigo() + " no se puede apagar.");
    }

    @Override
    // Consume su potencia nominal cada hora
    public double calcularConsumoHoraW() {
        return getPotenciaNominal();
    }

    @Override
    public Alerta registrarLectura(double valor, int horaSimulacion) {
        validarEncendido();
        if (Double.isNaN(valor) || valor < ppmMin || valor > ppmMax) {
            throw new IllegalArgumentException("La concentración de humo debe estar entre 0 y 1000 ppm.");
        }
        ultimaLectura = valor;
        Severidad severidad = evaluarSeveridad(valor);
        if (severidad == null) {
            return null;
        }
        return new Alerta(this, horaSimulacion, severidad, "Humo de " + valor + " ppm");
    }

    @Override
    public String getUnidad() {
        return "ppm";
    }

    @Override
    public double getUltimaLectura() {
        return ultimaLectura;
    }

    @Override
    public String getTipo() {
        return "Sensor de humo";
    }

    // Umbrales: 300 ppm o mas = Critica, 150 ppm o mas = Alta, menos de 150 = sin alerta
    private Severidad evaluarSeveridad(double ppm) {
        if (ppm >= 300) {
            return new SeveridadCritica();
        }
        if (ppm >= 150) {
            return new SeveridadAlta();
        }
        return null;
    }
}
