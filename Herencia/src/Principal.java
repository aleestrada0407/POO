
import java.util.Scanner;
 
public class Principal {
	private DulceEstacion sistema;
	private Scanner teclado;
 
	public Principal() {
		this.sistema = new DulceEstacion();
		this.teclado = new Scanner(System.in);
	}
 
	public static void main(String[] args) {
		Principal app = new Principal();
		app.ejecutar();
	}
 
	public void ejecutar() {
		cargarDatosIniciales();
		boolean salir = false;
		while (!salir) {
			mostrarMenu();
			int opcion = leerEnteroPositivo("Seleccione una opción: ");
			switch (opcion) {
				case 1:
					registrarMaquina();
					break;
				case 2:
					mostrarInventario();
					break;
				case 3:
					cotizarAlquiler();
					break;
				case 4:
					realizarAlquiler();
					break;
				case 5:
					registrarDevolucion();
					break;
				case 6:
					mostrarReporte();
					break;
				case 7:
					salir = true;
					System.out.println("Gracias por usar Dulce Estación.");
					break;
				default:
					System.out.println("Opción inválida. Intente de nuevo.");
			}
		}
		teclado.close();
	}
 
	private void cargarDatosIniciales() {
		MaquinaPalomitas maquina1 = new MaquinaPalomitas(
			"PAL001",
			"Marca A",
			"Modelo X",
			150.0,
			100,
			true);
 
		MaquinaAlgodon maquina2 = new MaquinaAlgodon(
			"PAL002",
			"Marca B",
			"Modelo Z",
			60,
			400);
 
		FuenteChocolate maquina3 = new FuenteChocolate(
			"PAL003",
			"Marca C",
			"Modelo Y",
			100,
			5);
 
		MaquinaPalomitas maquina4 = new MaquinaPalomitas(
			"PAL004",
			"Marca D",
			"Modelo W",
			120.0,
			80,
			false);
 
		MaquinaAlgodon maquina5 = new MaquinaAlgodon(
			"ALG005",
			"Marca E",
			"Modelo V",
			55,
			1200);
 
		FuenteChocolate maquina6 = new FuenteChocolate(
			"CHO006",
			"Marca F",
			"Modelo U",
			90,
			3);
 
		sistema.registrarMaquina(maquina1);
		sistema.registrarMaquina(maquina2);
		sistema.registrarMaquina(maquina3);
		sistema.registrarMaquina(maquina4);
		sistema.registrarMaquina(maquina5);
		sistema.registrarMaquina(maquina6);
	}
 
	private void mostrarMenu() {
		System.out.println("\n===== Dulce Estación =====");
		System.out.println("1. Registrar máquina");
		System.out.println("2. Mostrar inventario");
		System.out.println("3. Cotizar alquiler");
		System.out.println("4. Realizar alquiler");
		System.out.println("5. Registrar devolución");
		System.out.println("6. Mostrar reporte");
		System.out.println("7. Salir");
	}
 
	private void registrarMaquina() {
		System.out.println("Categorías: 1) Palomitas 2) Algodón 3) Chocolate");
		int categoria = leerEnteroPositivo("Seleccione categoría: ");
		String codigo = leerTextoNoVacio("Código de inventario: ");
		String marca = leerTextoNoVacio("Marca: ");
		String modelo = leerTextoNoVacio("Modelo: ");
		double tarifa = leerDecimalPositivo("Tarifa diaria: ");
		try {
			switch (categoria) {
				case 1:
					int porciones = leerEnteroPositivo("Porciones por hora: ");
					boolean carrito = leerConfirmacion("¿Incluye carrito integrado? (s/n): ");
					sistema.registrarMaquina(new MaquinaPalomitas(codigo, marca, modelo, tarifa, porciones, carrito));
					break;
				case 2:
					int potencia = leerEnteroPositivo("Potencia en vatios: ");
					sistema.registrarMaquina(new MaquinaAlgodon(codigo, marca, modelo, tarifa, potencia));
					break;
				case 3:
					double capacidad = leerDecimalPositivo("Capacidad en kg: ");
					sistema.registrarMaquina(new FuenteChocolate(codigo, marca, modelo, tarifa, capacidad));
					break;
				default:
					System.out.println("Categoría inválida.");
					return;
			}
			System.out.println("Máquina registrada correctamente.");
		} catch (IllegalArgumentException e) {
			System.out.println("Error al registrar: " + e.getMessage());
		}
	}
 
	private void mostrarInventario() {
		System.out.println(sistema.consultarInventario());
	}
 
	private void cotizarAlquiler() {
		String codigo = leerTextoNoVacio("Código de la máquina: ");
		int dias = leerEnteroPositivo("Cantidad de días: ");
		try {
			Maquina m = sistema.buscarMaquina(codigo);
			double costo = sistema.cotizarAlquiler(codigo, dias);
			System.out.println("Detalles: " + m.obtenerDetalles());
			System.out.println("Disponibilidad: " + (m.isDisponibilidad() ? "Disponible" : "Ocupada"));
			System.out.printf("Costo por %d día(s): Q%.2f%n", dias, costo);
		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
 
	private void realizarAlquiler() {
		String codigo = leerTextoNoVacio("Código de la máquina: ");
		int dias = leerEnteroPositivo("Cantidad de días: ");
		try {
			Maquina m = sistema.buscarMaquina(codigo);
			if (m == null) {
				System.out.println("Error: no existe una máquina con ese código.");
				return;
			}
			if (!m.isDisponibilidad()) {
				System.out.println("Error: la máquina ya está ocupada.");
				return;
			}
			double costo = sistema.cotizarAlquiler(codigo, dias);
			System.out.printf("Total a pagar: Q%.2f%n", costo);
			boolean aceptar = leerConfirmacion("¿Confirmar alquiler? (s/n): ");
			if (aceptar) {
				double cobrado = sistema.confirmarAlquiler(codigo, dias);
				System.out.printf("Alquiler confirmado. Cobrado: Q%.2f%n", cobrado);
			} else {
				System.out.println("Alquiler cancelado.");
			}
		} catch (IllegalArgumentException | IllegalStateException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
 
	private void registrarDevolucion() {
		String codigo = leerTextoNoVacio("Código de la máquina: ");
		try {
			sistema.registrarDevolucion(codigo);
			System.out.println("Devolución registrada correctamente.");
		} catch (IllegalArgumentException | IllegalStateException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
 
	private void mostrarReporte() {
		System.out.println(sistema.generarReporte());
	}
 
	private int leerEnteroPositivo(String mensaje) {
		int valor;
		while (true) {
			System.out.print(mensaje);
			String linea = teclado.nextLine().trim();
			try {
				valor = Integer.parseInt(linea);
				if (valor <= 0) {
					System.out.println("Debe ingresar un número entero mayor que cero.");
					continue;
				}
				return valor;
			} catch (NumberFormatException e) {
				System.out.println("Entrada inválida. Ingrese un número entero.");
			}
		}
	}
 
	private double leerDecimalPositivo(String mensaje) {
		double valor;
		while (true) {
			System.out.print(mensaje);
			String linea = teclado.nextLine().trim();
			try {
				valor = Double.parseDouble(linea);
				if (valor <= 0 || Double.isInfinite(valor) || Double.isNaN(valor)) {
					System.out.println("Debe ingresar un número positivo y finito.");
					continue;
				}
				return valor;
			} catch (NumberFormatException e) {
				System.out.println("Entrada inválida. Ingrese un número.");
			}
		}
	}
 
	private String leerTextoNoVacio(String mensaje) {
		String valor;
		while (true) {
			System.out.print(mensaje);
			valor = teclado.nextLine().trim();
			if (valor.isEmpty()) {
				System.out.println("El texto no puede estar vacío.");
				continue;
			}
			return valor;
		}
	}
 
	private boolean leerConfirmacion(String mensaje) {
		while (true) {
			System.out.print(mensaje);
			String linea = teclado.nextLine().trim().toLowerCase();
			if (linea.equals("s") || linea.equals("si") || linea.equals("sí")) {
				return true;
			} else if (linea.equals("n") || linea.equals("no")) {
				return false;
			}
			System.out.println("Respuesta inválida. Escriba 's' o 'n'.");
		}
	}
}
 