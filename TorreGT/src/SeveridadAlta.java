/**
 * Severidad Alta: plazo de atención de 4 horas, crédito de Q2.00 por hora de retraso
 * y prioridad 2 en la bandeja.
 */
public class SeveridadAlta extends Severidad {

    public SeveridadAlta() {
        super();
    }

    @Override
    public String getNombre() {
        return "Alta";
    }

    @Override
    public int getPlazoHoras() {
        return 4;
    }

    @Override
    public double getCreditoPorHora() {
        return 2.00;
    }

    @Override
    public int getPrioridad() {
        return 2;
    }
}
