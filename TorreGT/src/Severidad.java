/**
 * Clase abstracta de las severidades de las alertas.
 * Cada subclase indica su nombre, plazo, credito por hora y prioridad.
 */
public abstract class Severidad {

    protected Severidad() {
    }

    // Estos cuatro valores cambian segun la severidad; cada subclase los devuelve
    public abstract String getNombre();

    public abstract int getPlazoHoras();

    public abstract double getCreditoPorHora();

    public abstract int getPrioridad();

    /** Horas de retraso: horas transcurridas menos el plazo, nunca menor que cero. */
    public int calcularHorasRetraso(int horasTranscurridas) {
        int retraso = horasTranscurridas - getPlazoHoras();
        if (retraso < 0) {
            return 0;
        }
        return retraso;
    }

    // Credito = horas de retraso x credito por hora de esta severidad
    public double calcularCredito(int horasRetraso) {
        return horasRetraso * getCreditoPorHora();
    }

    @Override
    public String toString() {
        return getNombre();
    }
}
