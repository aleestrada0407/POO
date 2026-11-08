/**
 * Capacidad de los dispositivos que funcionan a un nivel de intensidad.
 */
public interface Regulable {
    int getNivel();

    void ajustarNivel(int nivel);

    int getNivelMinimo();

    int getNivelMaximo();
}
