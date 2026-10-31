import java.util.Scanner;

/** Vista de texto: recibe datos y presenta la informacion del programa. */
public class VistaConsola {

    // Se utiliza el nombre teclado, tal como se ha trabajado en clase.
    private Scanner teclado;

    /** Constructor que prepara el Scanner para leer desde la consola. */
    public VistaConsola() {
        teclado = new Scanner(System.in);
    }

    /** Presenta las opciones generales, sin realizar operaciones del modelo. */
    public void mostrarMenu() {
        System.out.println("\n========== INTEGRACIONES EXTERNAS ==========");
        System.out.println("1. Listar integraciones activas");
        System.out.println("2. Buscar integracion y ver configuracion");
        System.out.println("3. Ordenar catalogo por latencia");
        System.out.println("4. Seleccionar integracion y realizar operaciones");
        System.out.println("0. Salir");
    }

    /** Muestra el texto recibido del controlador. */
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    /** Muestra una fila con los datos generales de una conexion. */
    public void mostrarIntegracion(Integracion integracion) throws IllegalArgumentException {
        if (integracion == null) {
            throw new IllegalArgumentException("No se puede mostrar una integracion nula.");
        }
        System.out.println("Id: " + integracion.getId()
                + " | " + integracion.getNombre()
                + " | Proveedor: " + integracion.getProveedor()
                + " | Conexion: " + integracion.getEstadoConexion()
                + " | Activa: " + integracion.isActiva()
                + " | Latencia: " + integracion.getLatenciaPromedioMs() + " ms");
    }

    /** Usa toString para mostrar la configuracion del objeto que recibe. */
    public void mostrarConfiguracion(Integracion integracion) throws IllegalArgumentException {
        if (integracion == null) {
            throw new IllegalArgumentException("No se puede mostrar una integracion nula.");
        }
        System.out.println("\n--- CONFIGURACION ---");
        System.out.println(integracion.toString());
    }

    /** Repite la lectura hasta obtener un entero cuyo formato sea valido. */
    public int leerEntero(String mensaje) {
        boolean valido = false;
        int numero = 0;
        while (!valido) {
            String texto = leerTexto(mensaje);
            try {
                numero = Integer.parseInt(texto);
                valido = true;
            } catch (NumberFormatException excepcion) {
                mostrarMensaje("Ingrese un numero entero valido, por ejemplo 1.");
            }
        }
        return numero;
    }

    /** Lee un decimal y rechaza textos, NaN e infinito mediante una excepcion. */
    public double leerDecimal(String mensaje) {
        boolean valido = false;
        double numero = 0;
        while (!valido) {
            String texto = leerTexto(mensaje);
            try {
                // Se acepta punto o coma como separador decimal, sin separadores de miles.
                numero = Double.parseDouble(texto.replace(',', '.'));
                if (Double.isNaN(numero) || Double.isInfinite(numero)) {
                    throw new IllegalArgumentException("El numero debe ser finito.");
                }
                valido = true;
            } catch (IllegalArgumentException excepcion) {
                mostrarMensaje("Ingrese un numero decimal finito, por ejemplo 25.50.");
            }
        }
        return numero;
    }

    /** Solicita un texto y utiliza if para impedir una entrada vacia. */
    public String leerTexto(String mensaje) {
        boolean valido = false;
        String texto = "";
        while (!valido) {
            System.out.print(mensaje);
            // Siempre se lee una linea para evitar el salto pendiente de nextInt().
            texto = teclado.nextLine().trim();
            if (texto.isEmpty()) {
                mostrarMensaje("La entrada no puede estar vacia.");
            } else {
                valido = true;
            }
        }
        return texto;
    }
}
