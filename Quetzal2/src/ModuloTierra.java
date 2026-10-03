public class ModuloTierra extends Modulo {
	private int capacidadDescarga;
	private int totalDescargado;
	private String estacion;

	// Comentario: la energia heredada es la energía completa que se consume en cada descarga
	public ModuloTierra(int id, String nombre, int salud, boolean activo, int energia, int costoConstruccion, int capacidadDescarga, int totalDescargado, String estacion) {
		super(id, nombre, salud, activo, costoConstruccion, energia);
		this.capacidadDescarga = capacidadDescarga;
		this.totalDescargado = totalDescargado;
		this.estacion = estacion;
		if (capacidadDescarga <= 0) {
			throw new IllegalArgumentException("La capacidad de descarga debe ser mayor que 0");
		}
		if (totalDescargado < 0) {
			throw new IllegalArgumentException("El total descargado no puede ser menor que 0");
		}
		if (estacion == null || estacion.trim().isEmpty()) {
			throw new IllegalArgumentException("La estación no debe estar vacía");
		}
	}

	// Comentario: el módulo de tierra solo descarga si está activo, la misión tiene la energía necesaria y hay datos pendientes en órbita; si no, informa la causa y no modifica los recursos
	@Override
	public String procesarCiclo(Mision mision) {
		// Comentario: primera condición, el módulo debe estar activo
		if (!this.activo) {
			return this.nombre + " (Tierra): no operó porque está inactivo.";
		}
		// Comentario: segunda condición, la misión debe tener la energía completa que requiere una descarga
		if (mision.getEnergiaDisponible() < this.energia) {
			return this.nombre + " (Tierra): no operó por energía insuficiente (necesita " + this.energia + " y hay " + mision.getEnergiaDisponible() + ").";
		}
		// Comentario: tercera condición, debe haber datos pendientes en órbita
		if (mision.getDatosEnOrbita() <= 0) {
			return this.nombre + " (Tierra): no operó porque no hay datos pendientes en órbita.";
		}
		// Comentario: se descarga lo que permite la capacidad, sin superar lo que hay en órbita
		int descarga = this.capacidadDescarga;
		if (mision.getDatosEnOrbita() < descarga) {
			descarga = mision.getDatosEnOrbita();
		}
		// Comentario: se consume la energía completa aunque se descargue menos que la capacidad máxima
		mision.consumirEnergia(this.energia);
		// Comentario: los datos dejan de estar en órbita y pasan a contarse como descargados en la misión
		mision.registrarDescarga(descarga);
		// Comentario: se suma lo descargado al acumulado histórico de este módulo
		this.totalDescargado += descarga;
		return this.nombre + " (Tierra): consumió " + this.energia + " de energía y descargó " + descarga + " MB.";
	}

	public int GetCapacidadDescarga() {
		return this.capacidadDescarga;
	}

	public int GettotalDescargado() {
		return this.totalDescargado;
	}

	public String Getestacion() {
		return this.estacion;
	}

	public void Setestacion(String newestacion) {
		this.estacion = newestacion;
	}

	@Override
	public String toString() {
		return "-- Módulo de Tierra --\n" + super.toString() + " || Energía por descarga: " + this.energia + " || Capacidad de descarga: " + this.capacidadDescarga + " MB || Total descargado: " + this.totalDescargado + " MB || Estación: " + this.estacion;
	}
}
