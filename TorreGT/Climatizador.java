/**
 * Climatizador regulable (nivel 20 a 100, inicia en 50) que mide la temperatura del ambiente.
 * Hereda el nivel de DispositivoRegulable e implementa la capacidad de medir (Medidor).
 */
public class Climatizador extends DispositivoRegulable implements Medidor {
    private final int nivelMinimo = 20;
    private final int nivelInicial = 50;
    private final int tempMin = -10;
    private final int tempMax = 60;
    private double ultimaLectura;

    public Climatizador(String codigo, Oficina oficina, double potenciaNominal) {
        // Inicia en el nivel 50
        super(codigo, oficina, potenciaNominal, 50);
        this.ultimaLectura = 0;
    }

    @Override
    public int getNivelMinimo() {
        return nivelMinimo;
    }

    @Override
    // Consumo por hora: potencia x (0.30 + 0.70 x nivel / 100). Se usa 100.0 para que la division sea decimal.
    public double calcularConsumoHoraW() {
        return getPotenciaNominal() * (0.30 + 0.70 * getNivel() / 100.0);
    }

    @Override
    public Alerta registrarLectura(double valor, int horaSimulacion) {
        // Solo se registran lecturas de dispositivos encendidos y dentro del rango permitido
        validarEncendido();
        if (Double.isNaN(valor) || valor < tempMin || valor > tempMax) {
            throw new IllegalArgumentException("La temperatura debe estar entre " + tempMin + " y " + tempMax + " °C.");
        }
        ultimaLectura = valor;
        // Se decide la severidad segun los umbrales; null significa que no hay alerta
        Severidad severidad = evaluarSeveridad(valor);
        if (severidad == null) {
            return null;
        }
        return new Alerta(this, horaSimulacion, severidad, "Temperatura de " + valor + " °C");
    }

    @Override
    public String getUnidad() {
        return "°C";
    }

    @Override
    public double getUltimaLectura() {
        return ultimaLectura;
    }

    @Override
    public String getTipo() {
        return "Climatizador";
    }

    // Umbrales: 40 o mas = Critica, 32 o mas = Alta, 28 o mas = Baja, menos de 28 = sin alerta.
    // Se revisa de mayor a menor para que cada lectura caiga en un solo rango.
    private Severidad evaluarSeveridad(double temperatura) {
        if (temperatura >= 40) {
            return new SeveridadCritica();
        }
        if (temperatura >= 32) {
            return new SeveridadAlta();
        }
        if (temperatura >= 28) {
            return new SeveridadBaja();
        }
        return null;
    }
}
