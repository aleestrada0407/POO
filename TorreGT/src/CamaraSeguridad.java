/**
 * Camara de seguridad: se enciende y se apaga, y en ella se registran movimientos detectados.
 * Un movimiento antes de las 7:00 o desde las 19:00 genera una alerta Alta.
 */
public class CamaraSeguridad extends Dispositivo implements DetectorMovimiento {
    private final int horaInicioLaboral = 7;
    private final int horaFinLaboral = 19;

    public CamaraSeguridad(String codigo, Oficina oficina, double potenciaNominal) {
        super(codigo, oficina, potenciaNominal, false);
    }

    @Override
    // Consume su potencia nominal por cada hora encendida
    public double calcularConsumoHoraW() {
        return getPotenciaNominal();
    }

    @Override
    public Alerta registrarMovimiento(int horaSimulacion) {
        validarEncendido();
        // La hora del dia es el residuo de dividir la hora de simulacion entre 24
        int horaDia = horaSimulacion % 24;
        // Fuera del horario laboral (antes de las 7 o desde las 19) se genera una alerta Alta
        if (horaDia < horaInicioLaboral || horaDia >= horaFinLaboral) {
            return new Alerta(this, horaSimulacion, new SeveridadAlta(),
                    "Movimiento fuera del horario laboral (" + horaDia + ":00)");
        }
        // Dentro del horario laboral no se genera nada
        return null;
    }

    @Override
    public String getTipo() {
        return "Cámara de seguridad";
    }
}
