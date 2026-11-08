/**
 * Clase abstracta de los dispositivos que funcionan a un nivel de intensidad (porcentaje).
 * El nivel se conserva mientras el dispositivo esta apagado.
 */
public abstract class DispositivoRegulable extends Dispositivo implements Regulable {
    private int nivel;

    protected DispositivoRegulable(String codigo, Oficina oficina, double potenciaNominal, int nivelInicial) {
        // Todos los regulables se registran apagados
        super(codigo, oficina, potenciaNominal, false);
        this.nivel = nivelInicial;
    }

    @Override
    public int getNivel() {
        return nivel;
    }

    @Override
    public void ajustarNivel(int nivel) {
        // 1) Debe estar encendido
        validarEncendido();
        // 2) El nivel debe estar en el rango del tipo. getNivelMinimo() lo decide cada subclase
        //    (Luminaria 10, Climatizador 20, inverter 10). Si no cumple, se lanza la excepcion
        //    antes de cambiar el nivel, asi el estado queda igual.
        if (nivel < getNivelMinimo() || nivel > getNivelMaximo()) {
            throw new IllegalArgumentException("El nivel de " + getTipo() + " debe estar entre "
                    + getNivelMinimo() + " y " + getNivelMaximo() + ".");
        }
        // 3) Solo si todo es valido se cambia el nivel
        this.nivel = nivel;
    }

    // Cada subclase indica el minimo de su tipo
    @Override
    public abstract int getNivelMinimo();

    @Override
    public int getNivelMaximo() {
        return 100;
    }

    @Override
    public String toString() {
        // Agrega el nivel a la informacion comun de Dispositivo
        return super.toString() + "   nivel: " + nivel;
    }
}
