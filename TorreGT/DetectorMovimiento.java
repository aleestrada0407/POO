/**
 * Capacidad de los dispositivos en los que se puede registrar un movimiento detectado.
 * registrarMovimiento devuelve la alerta generada, o null si no corresponde alerta.
 */
public interface DetectorMovimiento {
    Alerta registrarMovimiento(int horaSimulacion);
}
