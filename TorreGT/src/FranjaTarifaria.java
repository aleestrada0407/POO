/**
 * Franja de horario con su tarifa por kWh (valle, normal o punta).
 */
public class FranjaTarifaria {
    private String nombre;
    private double tarifa;
    private int horaInicio;
    private int horaFin;

    public FranjaTarifaria(String nombre, double tarifa, int horaInicio, int horaFin) {
        this.nombre = nombre;
        this.tarifa = tarifa;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public String getNombre() {
        return nombre;
    }

    public double getTarifa() {
        return tarifa;
    }

    /** Indica si una hora del dia (0 a 23) pertenece a la franja. */
    public boolean perteneceAHora(int horaDia) {
        if (horaInicio <= horaFin) {
            return horaDia >= horaInicio && horaDia <= horaFin;
        }
        // La franja cruza la medianoche (por ejemplo, de 22 a 5)
        return horaDia >= horaInicio || horaDia <= horaFin;
    }

    // Monto en quetzales = kWh x tarifa de la franja
    public double calcularMonto(double kWh) {
        return kWh * tarifa;
    }

    @Override
    public String toString() {
        return String.format("%s (%d:00 a %d:59) Q%.2f por kWh", nombre, horaInicio, horaFin, tarifa);
    }
}
