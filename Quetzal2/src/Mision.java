import java.util.ArrayList;

public class Mision {
	private ArrayList<Modulo> modulosTotales;
	private int energiaDisponible;
	private int datosEnOrbita;
	private int datosDescargados;

	public Mision(int energiaDisponible, int datosEnOrbita, int datosDescargados) {
		this.modulosTotales = new ArrayList<>();
		this.energiaDisponible = energiaDisponible;
		this.datosEnOrbita = datosEnOrbita;
		this.datosDescargados = datosDescargados;
		if (energiaDisponible < 0 || datosEnOrbita < 0 || datosDescargados < 0) {
			throw new IllegalArgumentException("Los recursos iniciales no pueden ser menores que 0");
		}
	}

	// Comentario: agrega un módulo a la lista; al recibir un Modulo acepta cualquiera de sus subclases (polimorfismo)
	public void agregarModulos(Modulo modulo) {
		if (modulo == null) {
			throw new IllegalArgumentException("El módulo no puede ser vacío");
		}
		this.modulosTotales.add(modulo);
	}

	public ArrayList<Modulo> getModulos() {
		return this.modulosTotales;
	}

	public int getEnergiaDisponible() {
		return this.energiaDisponible;
	}

	public int getDatosEnOrbita() {
		return this.datosEnOrbita;
	}

	public int getDatosDescargados() {
		return this.datosDescargados;
	}

	// Comentario: aumenta la energía disponible de la misión y devuelve la energía que queda disponible
	public int agregarEnergia(int cantidad) {
		if (cantidad < 0) {
			throw new IllegalArgumentException("La cantidad no puede ser menor que 0");
		}
		this.energiaDisponible += cantidad;
		return this.energiaDisponible;
	}

	// Comentario: disminuye la energía disponible de la misión y devuelve la energía que queda disponible
	public int consumirEnergia(int cantidad) {
		if (cantidad < 0 || cantidad > this.energiaDisponible) {
			throw new IllegalArgumentException("No hay energía suficiente para consumir esa cantidad");
		}
		this.energiaDisponible -= cantidad;
		return this.energiaDisponible;
	}

	// Comentario: aumenta los datos que están en órbita pendientes de descarga
	public void agregarDatosOrbita(int cantidad) {
		if (cantidad < 0) {
			throw new IllegalArgumentException("La cantidad no puede ser menor que 0");
		}
		this.datosEnOrbita += cantidad;
	}

	// Comentario: registra una descarga; los datos dejan de estar en órbita y pasan a contarse como descargados
	public void registrarDescarga(int cantidad) {
		if (cantidad < 0 || cantidad > this.datosEnOrbita) {
			throw new IllegalArgumentException("No hay esa cantidad de datos en órbita para descargar");
		}
		this.datosEnOrbita -= cantidad;
		this.datosDescargados += cantidad;
	}

	@Override
	public String toString() {
		return "Energía disponible: " + this.energiaDisponible + " || Datos en órbita: " + this.datosEnOrbita + " MB || Datos descargados: " + this.datosDescargados + " MB";
	}
}
