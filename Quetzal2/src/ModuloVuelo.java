public class ModuloVuelo extends Modulo {
	private int instrumento;
	private int datosPorCiclo;
	private int ciclosAcumulados;
	private int datosRecolectadosHistoricos;

	// Comentario: instrumento usa códigos numéricos: 1 = Cámara, 2 = Sensor. La energia heredada es el consumo por ciclo
	public ModuloVuelo(int id, String nombre, int salud, boolean activo, int energia, int costoConstruccion, int instrumento, int datosPorCiclo, int ciclosAcumulados, int datosRecolectadosHistoricos) {
		super(id, nombre, salud, activo, costoConstruccion, energia);
		this.instrumento = instrumento;
		this.datosPorCiclo = datosPorCiclo;
		this.ciclosAcumulados = ciclosAcumulados;
		this.datosRecolectadosHistoricos = datosRecolectadosHistoricos;
		if (instrumento != 1 && instrumento != 2) {
			throw new IllegalArgumentException("El instrumento debe ser 1 (Cámara) o 2 (Sensor)");
		}
		if (datosPorCiclo <= 0) {
			throw new IllegalArgumentException("Los datos por ciclo deben ser mayores que 0");
		}
		if (ciclosAcumulados < 0 || datosRecolectadosHistoricos < 0) {
			throw new IllegalArgumentException("Los acumulados no pueden ser menores que 0");
		}
	}

	public int Getinstrumento() {
		return this.instrumento;
	}

	// Comentario: el módulo de vuelo solo recolecta si está activo y la misión tiene la energía que necesita; si no, informa la causa y no modifica los recursos
	@Override
	public String procesarCiclo(Mision mision) {
		// Comentario: primera condición, el módulo debe estar activo
		if (!this.activo) {
			return this.nombre + " (Vuelo): no operó porque está inactivo.";
		}
		// Comentario: segunda condición, la misión debe tener energía suficiente para cubrir el consumo (no hay recolecciones parciales)
		if (mision.getEnergiaDisponible() < this.energia) {
			return this.nombre + " (Vuelo): no operó por energía insuficiente (necesita " + this.energia + " y hay " + mision.getEnergiaDisponible() + ").";
		}
		// Comentario: si se cumplen ambas condiciones, se descuenta la energía de la misión
		mision.consumirEnergia(this.energia);
		// Comentario: los datos recolectados quedan en órbita, pendientes de descarga
		mision.agregarDatosOrbita(this.datosPorCiclo);
		// Comentario: se cuenta este ciclo como un ciclo en el que el módulo operó
		this.ciclosAcumulados++;
		// Comentario: se suman los datos al total histórico del módulo
		this.datosRecolectadosHistoricos += this.datosPorCiclo;
		return this.nombre + " (Vuelo): consumió " + this.energia + " de energía y recolectó " + this.datosPorCiclo + " MB.";
	}

	public int getDatosPorCiclo() {
		return this.datosPorCiclo;
	}

	public int getCiclosAcumulados() {
		return this.ciclosAcumulados;
	}

	public int getDatosRecolectadosHistorico() {
		return this.datosRecolectadosHistoricos;
	}

	@Override
	public String toString() {
		String tipoInstrumento;
		if (this.instrumento == 1) {
			tipoInstrumento = "Cámara";
		} else {
			tipoInstrumento = "Sensor";
		}
		return "-- Módulo de Vuelo --\n" + super.toString() + " || Consumo por ciclo: " + this.energia + " || Instrumento: " + tipoInstrumento + " || Datos por ciclo: " + this.datosPorCiclo + " MB || Ciclos acumulados: " + this.ciclosAcumulados + " || Datos recolectados históricos: " + this.datosRecolectadosHistoricos + " MB";
	}
}
