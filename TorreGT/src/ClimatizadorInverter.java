/**
 * Climatizador que modula su compresor: mismo comportamiento que un climatizador,
 * nivel minimo de 10 y 80 % de su consumo.
 */
public class ClimatizadorInverter extends Climatizador {
    private final int nivelMin = 10;
    private final double factorInverter = 0.80;

    public ClimatizadorInverter(String codigo, Oficina oficina, double potenciaNominal) {
        // Llama al constructor de Climatizador, que inicia el nivel en 50
        super(codigo, oficina, potenciaNominal);
    }

    @Override
    // El inverter permite bajar hasta 10 (el climatizador normal solo hasta 20)
    public int getNivelMinimo() {
        return nivelMin;
    }

    @Override
    // Reutiliza la formula del climatizador (super) y la multiplica por 0.80, sin copiarla
    public double calcularConsumoHoraW() {
        return super.calcularConsumoHoraW() * factorInverter;
    }

    @Override
    public String getTipo() {
        return "Climatizador inverter";
    }
}
