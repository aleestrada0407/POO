/**
 * Severidad Crítica: plazo de atención de 1 hora, crédito de Q5.00 por hora de retraso
 * y prioridad 3 (la más urgente) en la bandeja.
 */
public class SeveridadCritica extends Severidad {

    public SeveridadCritica() {
        super();
    }

    @Override
    public String getNombre() {
        return "Crítica";
    }

    @Override
    public int getPlazoHoras() {
        return 1;
    }

    @Override
    public double getCreditoPorHora() {
        return 5.00;
    }

    @Override
    public int getPrioridad() {
        return 3;
    }
}
