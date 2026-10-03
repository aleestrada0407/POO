import java.util.ArrayList;

public class Quetzal2 {
	private Mision mision;

	public Quetzal2(Mision mision) {
		this.mision = mision;
	}

	// Comentario: agrega a la misión 10 módulos de los tres tipos, con valores que emulan una misión ya en progreso, en el orden en que participarán en cada ciclo
	public void cargarMisionInicial() {
		// Comentario: 1) panel solar activo, genera 20 de energía por ciclo
		this.mision.agregarModulos(new ModuloEnergia(1, "Panel Solar Alfa", 100, true, 20, 5000, 12, 240));
		// Comentario: 2) cámara activa (instrumento 1), consume 8 de energía y recolecta 15 MB por ciclo
		this.mision.agregarModulos(new ModuloVuelo(2, "Camara Orbital Alfa", 100, true, 8, 8000, 1, 15, 8, 120));
		// Comentario: 3) antena activa, usa 5 de energía y descarga hasta 30 MB por ciclo
		this.mision.agregarModulos(new ModuloTierra(3, "Antena Campus Alfa", 100, true, 5, 6000, 30, 120, "Estacion Central"));
		// Comentario: 4) segundo panel solar activo, genera 15 de energía por ciclo
		this.mision.agregarModulos(new ModuloEnergia(4, "Panel Solar Beta", 90, true, 15, 4500, 10, 150));
		// Comentario: 5) sensor activo (instrumento 2), consume 6 de energía y recolecta 10 MB por ciclo
		this.mision.agregarModulos(new ModuloVuelo(5, "Sensor Termico", 95, true, 6, 3500, 2, 10, 9, 90));
		// Comentario: 6) segunda antena activa, usa 4 de energía y descarga hasta 20 MB por ciclo
		this.mision.agregarModulos(new ModuloTierra(6, "Antena Campus Beta", 90, true, 4, 5500, 20, 120, "Estacion Norte"));
		// Comentario: 7) segunda cámara activa, consume 10 de energía y recolecta 20 MB por ciclo
		this.mision.agregarModulos(new ModuloVuelo(7, "Camara Orbital Beta", 85, true, 10, 9000, 1, 20, 5, 100));
		// Comentario: 8) tercer panel solar INACTIVO, no producirá energía
		this.mision.agregarModulos(new ModuloEnergia(8, "Panel Solar Gamma", 60, false, 25, 7000, 4, 100));
		// Comentario: 9) segundo sensor INACTIVO, no recolectará datos
		this.mision.agregarModulos(new ModuloVuelo(9, "Sensor Radiacion", 70, false, 3, 3000, 2, 5, 3, 15));
		// Comentario: 10) tercera antena INACTIVA, no descargará datos
		this.mision.agregarModulos(new ModuloTierra(10, "Antena Campus Gamma", 65, false, 6, 6500, 25, 60, "Estacion Sur"));
	}

	// Comentario: prepara el texto con los módulos; si porCosto es false respeta el orden de la carga inicial, si es true muestra el catálogo ordenado por costo de construcción
	public String mostrarModulo(boolean porCosto) {
		// Comentario: si no hay módulos se avisa y se termina el método
		if (this.mision.getModulos().isEmpty()) {
			return "No hay módulos en la misión.";
		}
		// Comentario: se trabaja sobre una copia de la lista para no alterar el orden original de la misión
		ArrayList<Modulo> lista = new ArrayList<>(this.mision.getModulos());
		String resultado;
		// Comentario: solo se ordena la copia cuando se pidió el catálogo por costo
		if (porCosto) {
			// Comentario: ordenamiento burbuja; el ciclo externo repite las pasadas sobre la copia
			for (int i = 0; i < lista.size() - 1; i++) {
				// Comentario: el ciclo interno compara cada módulo con el siguiente
				for (int j = 0; j < lista.size() - 1 - i; j++) {
					// Comentario: si el módulo actual cuesta más que el siguiente, se intercambian de posición
					if (lista.get(j).GetcostoConstruccion() > lista.get(j + 1).GetcostoConstruccion()) {
						// Comentario: se guarda el módulo actual para no perderlo durante el intercambio
						Modulo auxiliar = lista.get(j);
						// Comentario: el siguiente módulo (más barato) pasa a la posición actual
						lista.set(j, lista.get(j + 1));
						// Comentario: el módulo guardado (más caro) pasa a la posición siguiente
						lista.set(j + 1, auxiliar);
					}
				}
			}
			// Comentario: título del catálogo ordenado
			resultado = "-- CATÁLOGO POR COSTO DE CONSTRUCCIÓN (menor a mayor) --";
		} else {
			// Comentario: título del listado en el orden de la carga inicial
			resultado = "-- MÓDULOS DE LA MISIÓN (orden de carga inicial) --";
		}
		// Comentario: se recorre la lista y se agrega cada módulo al texto; toString() se ejecuta según el tipo real de cada objeto (polimorfismo)
		for (Modulo actual : lista) {
			// Comentario: se agrega la información del módulo en una nueva línea
			resultado += "\n" + actual.toString();
		}
		// Comentario: se devuelve el texto para que main lo muestre en pantalla
		return resultado;
	}

	// Comentario: busca un módulo por ID o por nombre y devuelve el módulo encontrado, o null si no existe
	public Modulo buscarModulo(String criterio) {
		// Comentario: se valida que el criterio de búsqueda no esté vacío
		if (criterio == null || criterio.trim().isEmpty()) {
			throw new IllegalArgumentException("El criterio de búsqueda no puede estar vacío");
		}
		// Comentario: se recorre la lista de módulos de la misión
		for (Modulo actual : this.mision.getModulos()) {
			// Comentario: coincide si el ID escrito es igual al ID del módulo o si el nombre es igual sin importar mayúsculas
			if (String.valueOf(actual.Getid()).equals(criterio.trim()) || actual.Getnombre().equalsIgnoreCase(criterio.trim())) {
				// Comentario: se devuelve el primer módulo que coincide
				return actual;
			}
		}
		// Comentario: si ninguno coincidió se devuelve null
		return null;
	}

	// Comentario: avanza un ciclo; todos los módulos operan en el orden de la carga inicial y se devuelve el resultado de cada uno y el estado final de los recursos
	public String avanzarCiclo() {
		String resultado = "-- RESULTADO DEL CICLO --";
		// Comentario: se recorre la lista en el orden de la carga inicial
		for (Modulo actual : this.mision.getModulos()) {
			// Comentario: se le pide a cada módulo que opere sin importar su tipo (polimorfismo); los recursos se actualizan al instante
			String mensaje = actual.procesarCiclo(this.mision);
			// Comentario: se agrega el resultado de ese módulo y los recursos que quedaron después de su participación
			resultado += "\n" + mensaje + "\n    Recursos -> " + this.mision.toString();
		}
		// Comentario: al terminar el ciclo se agrega el estado final de los recursos
		resultado += "\n\nEstado final de recursos: " + this.mision.toString();
		// Comentario: se devuelve el texto para que main lo muestre
		return resultado;
	}

	// Comentario: prepara el resumen de comunicaciones (módulos de tierra) y el estado de los recursos de la misión
	public String mostrarRecursos() {
		int totalTierra = 0;
		int activosTierra = 0;
		int capacidadActivos = 0;
		int totalDescargadoTierra = 0;
		int maximoDescargado = 0;
		// Comentario: primera pasada, se recorre la lista para contar y sumar solo los módulos de tierra
		for (Modulo actual : this.mision.getModulos()) {
			// Comentario: se filtran los módulos que son de tipo ModuloTierra
			if (actual instanceof ModuloTierra) {
				// Comentario: se convierte el módulo a ModuloTierra para poder usar sus métodos propios
				ModuloTierra antena = (ModuloTierra) actual;
				// Comentario: se cuenta una antena más
				totalTierra++;
				// Comentario: si la antena está activa se cuenta y se suma su capacidad de descarga
				if (antena.Getactivo()) {
					activosTierra++;
					capacidadActivos += antena.GetCapacidadDescarga();
				}
				// Comentario: se suma lo descargado históricamente por esta antena al total de todas
				totalDescargadoTierra += antena.GettotalDescargado();
				// Comentario: se guarda el mayor valor histórico descargado encontrado hasta ahora
				if (antena.GettotalDescargado() > maximoDescargado) {
					maximoDescargado = antena.GettotalDescargado();
				}
			}
		}
		String resultado = "-- RESUMEN DE COMUNICACIONES --";
		resultado += "\nTotal de módulos de tierra: " + totalTierra;
		resultado += "\nMódulos de tierra activos: " + activosTierra;
		resultado += "\nCapacidad total de descarga por ciclo (activos): " + capacidadActivos + " MB";
		resultado += "\nTotal histórico descargado por módulos de tierra: " + totalDescargadoTierra + " MB";
		resultado += "\nMódulo(s) de tierra con mayor descarga histórica (" + maximoDescargado + " MB):";
		// Comentario: segunda pasada, se buscan todas las antenas que igualan el máximo para mostrar los empates
		for (Modulo actual : this.mision.getModulos()) {
			// Comentario: solo se revisan los módulos de tierra
			if (actual instanceof ModuloTierra) {
				// Comentario: se convierte a ModuloTierra para acceder a la estación y al total descargado
				ModuloTierra antena = (ModuloTierra) actual;
				// Comentario: si esta antena tiene el máximo, se muestran su nombre, ID y estación
				if (antena.GettotalDescargado() == maximoDescargado) {
					resultado += "\n    Nombre: " + antena.Getnombre() + " || ID: " + antena.Getid() + " || Estación: " + antena.Getestacion();
				}
			}
		}
		// Comentario: se agrega el estado actual de los recursos de la misión
		resultado += "\n\nRecursos de la misión: " + this.mision.toString();
		// Comentario: se devuelve el texto para que main lo muestre
		return resultado;
	}
}
