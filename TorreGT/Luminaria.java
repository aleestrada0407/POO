/**
 * Luminaria regulable: nivel de 10 a 100, inicia en 100. No mide ni genera alertas.
 */
public class Luminaria extends DispositivoRegulable {
    private final int nivelMinimo = 10;
    private final int nivelMaximo = 100;

    public Luminaria(String codigo, Oficina oficina, double potenciaNominal) {
        // Inicia en el nivel maximo (100)
        super(codigo, oficina, potenciaNominal, 100);
    }

    @Override
    public int getNivelMinimo() {
        return nivelMinimo;
    }

    @Override
    // Consumo por hora: potencia x nivel / 100
    public double calcularConsumoHoraW() {
        return getPotenciaNominal() * getNivel() / nivelMaximo;
    }

    @Override
    public String getTipo() {
        return "Luminaria";
    }
}
