/** Punto de entrada de la aplicacion de consola del ejercicio 6. */
public class Principal {

    /** Crea los componentes, carga diez integraciones e inicia el controlador. */
    public static void main(String[] args) {
        IntegracionesCatalogo catalogo = new IntegracionesCatalogo();
        catalogo.cargarInicial();
        VistaConsola vista = new VistaConsola();
        IntegracionesControlador controlador = new IntegracionesControlador(catalogo, vista);
        controlador.iniciar();
    }
}
