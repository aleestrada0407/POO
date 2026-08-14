




public class Usuario {
	private String nombre;
	private String nombreUsuario;
	private int edad;
	private Pelicula[] calificaciones;
	public Usuario(String nombre, String nombreUsuario, int edad) {
    this.nombre = nombre;
    this.nombreUsuario = nombreUsuario;
    this.edad = edad;
    this.calificaciones = new Pelicula[10];
}
	public String getNombre() {
		return nombre;
	}
	
	public String getNombreUsuario() {
		return nombreUsuario;

	}
	
	public int getEdad() {
		return edad;
	}
	
	public boolean registrarCalificacion(int calificacion) {

    if (calificacion < 1 || calificacion > 10) {
        return false;
    }

    for (int i = 0; i < calificaciones.length; i++) {

        if (calificaciones[i] == null) {
            calificaciones[i] = new Pelicula(calificacion);
            return true;
        }
    }

    return false;
}
	public Pelicula consultarCalificacion(int numeroPelicula) {
		int posicion = numeroPelicula -1;
		if (posicion < 0 || posicion >= calificaciones.length) {
			return null;
		}
		if (calificaciones[posicion] == null){
			return null;
		}
		return calificaciones[posicion];

	}
	
	public Pelicula[] getCalificaciones() {
		return calificaciones;
	}
	
	public boolean modificarCalificacion(int numeroPelicula, int nuevaCalificacion) {
		if (nuevaCalificacion <1 || nuevaCalificacion > 10 ){
			return false;
		}
		Pelicula pelicula = consultarCalificacion(numeroPelicula);
		if (pelicula == null){
			return false;
		}
		pelicula.setCalificacion(nuevaCalificacion);
		return true;
	}
	
	public double calcularPromedio() {
		int cantidad = obtenerCantidadRegistradas();
		if (cantidad == 0 ){
			return 0;
		}
		int suma = 0;

		for (int i = 0; i < cantidad; i++){
			suma += calificaciones[i].getCalificacion();

		}
		return (double) suma/cantidad;

	}
	
	public int obtenerCalificacionMasAlta() {
		int cantidad = obtenerCantidadRegistradas();
		if(cantidad == 0){
			return 0;
		}

		int mayor = calificaciones[0].getCalificacion();

		for (int i = 1; i < cantidad; i++){
			if(calificaciones[i].getCalificacion() > mayor){
				mayor = calificaciones[i].getCalificacion();
			}
		}
		return mayor;
	}
	
	public int obtenerCalificacionMasBaja() {
		 int cantidad = obtenerCantidadRegistradas();

        if (cantidad == 0) {
            return 0;
        }

        int menor = calificaciones[0].getCalificacion();

        for (int i = 1; i < cantidad; i++) {

            if (calificaciones[i].getCalificacion() < menor) {
                menor = calificaciones[i].getCalificacion();
            }
        }

        return menor;
	}
	
	public int obtenerCantidadRegistradas() {
		 int cantidad = 0;

        for (int i = 0; i < calificaciones.length; i++) {

            if (calificaciones[i] != null) {
                cantidad++;
            }
        }

        return cantidad;
	}
	
	public int obtenerEspaciosDisponibles() {
		 return calificaciones.length - obtenerCantidadRegistradas();
	}
}
