/**
 * Capacidad de los dispositivos que miden algo del ambiente.
 * registrarLectura devuelve la alerta generada, o null si la lectura no alcanza ningun umbral.
 */
public interface Medidor {
    Alerta registrarLectura(double valor, int horaSimulacion);

    String getUnidad();

    double getUltimaLectura();
}
