public abstract class Modulo {
	protected int id;
	protected String nombre;
	protected int salud;
	protected boolean activo;
	protected int costoConstruccion;
	protected int energia;

	public Modulo(int id, String nombre, int salud, boolean activo, int costoConstruccion, int energia) {
		this.id = id;
		this.nombre = nombre;
		this.salud = salud;
		this.activo = activo;
		this.costoConstruccion = costoConstruccion;
		this.energia = energia;
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre no debe estar vacío");
		}
		if (salud < 0) {
			throw new IllegalArgumentException("La salud no puede ser menor que 0");
		}
		if (costoConstruccion < 0) {
			throw new IllegalArgumentException("El costo de construcción no puede ser menor que 0");
		}
		if (energia < 0) {
			throw new IllegalArgumentException("La energía no puede ser menor que 0");
		}
	}

	// Comentario: método abstracto; cada subclase define qué hace el módulo cuando el simulador avanza un ciclo y devuelve el resultado (o la causa por la que no pudo operar)
	protected abstract String procesarCiclo(Mision mision);

	public void Setid(int newid) {
		this.id = newid;
	}

	public int Getid() {
		return this.id;
	}

	public void Setnombre(String newnombre) {
		this.nombre = newnombre;
	}

	public String Getnombre() {
		return this.nombre;
	}

	public void Setsalud(int newsalud) {
		this.salud = newsalud;
	}

	public int Getsalud() {
		return this.salud;
	}

	public void Setactivo(boolean newactivo) {
		this.activo = newactivo;
	}

	public boolean Getactivo() {
		return this.activo;
	}

	public void SetcostoConstruccion(int newcostoConstruccion) {
		this.costoConstruccion = newcostoConstruccion;
	}

	public int GetcostoConstruccion() {
		return this.costoConstruccion;
	}

	public int Getenergia() {
		return this.energia;
	}

	@Override
	public String toString() {
		String estado;
		if (this.activo == true) {
			estado = "Activo";
		} else {
			estado = "Inactivo";
		}
		return "ID: " + this.id + " || Nombre: " + this.nombre + " || Salud: " + this.salud + " || Estado: " + estado + " || Costo de construcción: " + this.costoConstruccion;
	}
}
