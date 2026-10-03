public class ModuloEnergia extends Modulo {
	private int ciclosAcumulados;
	private int energiaGeneradaHistorica;

	// Comentario: la energia heredada es la cantidad fija de energía que el módulo genera por ciclo
	public ModuloEnergia(int id, String nombre, int salud, boolean activo, int energia, int costoConstruccion, int ciclosAcumulados, int energiaGeneradaHistorica) {
		super(id, nombre, salud, activo, costoConstruccion, energia);
		this.ciclosAcumulados = ciclosAcumulados;
		this.energiaGeneradaHistorica = energiaGeneradaHistorica;
		if (energia <= 0) {
			throw new IllegalArgumentException("La energía generada por ciclo debe ser mayor que 0");
		}
		if (ciclosAcumulados < 0 || energiaGeneradaHistorica < 0) {
			throw new IllegalArgumentException("Los acumulados no pueden ser menores que 0");
		}
	}

	// Comentario: mientras el módulo está activo entrega su energía a la misión y actualiza sus acumulados; si está inactivo no produce nada e informa el motivo
	@Override
	public String procesarCiclo(Mision mision) {
		// Comentario: un módulo inactivo no produce energía
		if (!this.activo) {
			return this.nombre + " (Energía): no produjo energía porque está inactivo.";
		}
		// Comentario: la energía generada queda disponible en la misión para los demás módulos
		mision.agregarEnergia(this.energia);
		// Comentario: se cuenta este ciclo como un ciclo en el que el módulo generó energía
		this.ciclosAcumulados++;
		// Comentario: se suma la energía al total histórico generado por el módulo
		this.energiaGeneradaHistorica += this.energia;
		return this.nombre + " (Energía): generó " + this.energia + " unidades de energía.";
	}

	public int getCiclosAcumulados() {
		return this.ciclosAcumulados;
	}

	public int getEnergiaGeneradaHistorica() {
		return this.energiaGeneradaHistorica;
	}

	@Override
	public String toString() {
		return "-- Módulo de Energía --\n" + super.toString() + " || Energía generada por ciclo: " + this.energia + " || Ciclos acumulados: " + this.ciclosAcumulados + " || Energía generada histórica: " + this.energiaGeneradaHistorica;
	}
}
