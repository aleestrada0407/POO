/**
 * Severidad Baja: plazo de atención de 24 horas, crédito de Q0.50 por hora de retraso
 * y prioridad 1 (la menos urgente) en la bandeja.
 */
public class SeveridadBaja extends Severidad {

    public SeveridadBaja() {
        super();
    }

    @Override
    public String getNombre() {
        return "Baja";
    }

    @Override
    public int getPlazoHoras() {
        return 24;
    }

    @Override
    public double getCreditoPorHora() {
        return 0.50;
    }

    @Override
    public int getPrioridad() {
        return 1;
    }
}
