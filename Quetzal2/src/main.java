import java.util.Scanner;

public class main {

	// Comentario: punto de entrada; crea la misión y el controlador, hace la carga inicial y mantiene el menú hasta que el usuario elija salir
	public static void main(String[] args) {
		// Comentario: se crea la misión con 40 de energía, 25 MB en órbita y 300 MB descargados (consistentes con los históricos de los módulos)
		Mision mision = new Mision(40, 25, 300);
		// Comentario: se crea el controlador que coordina las operaciones sobre la misión
		Quetzal2 controlador = new Quetzal2(mision);
		// Comentario: se piden al controlador los 10 módulos de la carga inicial
		controlador.cargarMisionInicial();
		Scanner teclado = new Scanner(System.in);
		boolean salir = false;
		// Comentario: el menú se repite hasta que salir sea true
		while (!salir) {
			// Comentario: se muestran las opciones del menú
			System.out.println("\n===== DEFENSA DE QTZ2 =====");
			System.out.println("1. Listar módulos construidos");
			System.out.println("2. Buscar un módulo (por ID o nombre)");
			System.out.println("3. Consultar catálogo por costo de construcción");
			System.out.println("4. Avanzar un ciclo del simulador");
			System.out.println("5. Consultar resumen de comunicaciones");
			System.out.println("6. Salir");
			System.out.print("Seleccione una opción: ");
			// Comentario: se lee la opción como texto para que una letra no cause un error de conversión
			String opcion = teclado.nextLine().trim();
			// Comentario: según la opción se delega la tarea al controlador
			switch (opcion) {
				case "1":
					// Comentario: se pide el listado en el orden de la carga inicial (false = sin ordenar por costo) y se imprime
					System.out.println(controlador.mostrarModulo(false));
					break;
				case "2":
					// Comentario: se pide al usuario el ID o el nombre del módulo
					System.out.print("Ingrese el ID o el nombre del módulo: ");
					String criterio = teclado.nextLine();
					// Comentario: try-catch para mostrar el mensaje si el criterio está vacío en lugar de cerrar el programa
					try {
						// Comentario: se pide al controlador buscar el módulo
						Modulo encontrado = controlador.buscarModulo(criterio);
						// Comentario: si no se encontró se informa al usuario
						if (encontrado == null) {
							System.out.println("No existe un módulo con ese ID o nombre.");
						} else {
							// Comentario: si se encontró se imprime toda su información (toString según su tipo)
							System.out.println(encontrado.toString());
						}
					} catch (IllegalArgumentException e) {
						// Comentario: se muestra el motivo del error
						System.out.println("Error: " + e.getMessage());
					}
					break;
				case "3":
					// Comentario: se pide el catálogo ordenado por costo (true) y se imprime
					System.out.println(controlador.mostrarModulo(true));
					break;
				case "4":
					// Comentario: se avanza un ciclo y se imprime el resultado de cada módulo y los recursos
					System.out.println(controlador.avanzarCiclo());
					break;
				case "5":
					// Comentario: se pide el resumen de comunicaciones y los recursos y se imprime
					System.out.println(controlador.mostrarRecursos());
					break;
				case "6":
					// Comentario: se cambia salir a true para terminar el ciclo del menú
					salir = true;
					// Comentario: mensaje de despedida
					System.out.println("Gracias por usar Defensa de QTZ2.");
					break;
				default:
					// Comentario: cualquier otra entrada se informa como inválida y el menú se repite
					System.out.println("Opción inválida. Intente de nuevo.");
			}
		}
		// Comentario: se cierra el Scanner al terminar el programa
		teclado.close();
	}
}
