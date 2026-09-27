
import java.util.ArrayList;


public class DulceEstacion {
	private ArrayList<Maquina> maquinas;
	private double IngresoAcumulados;
	
	public DulceEstacion() { 
		this.maquinas = new ArrayList<>();
		this.IngresoAcumulados = 0.0;
		
	}
	
	public void registrarMaquina(Maquina maquina) {
		if (maquina == null){
			throw new IllegalArgumentException("Objeto no puede ser vacio");
		}
		for (Maquina existente : this.maquinas){
			if (existente.getCodigoInventario().equals(maquina.getCodigoInventario()))
				throw new IllegalArgumentException("Este código ya existe en el inventario");
		}
		maquinas.add(maquina);
	}
	
	public Maquina buscarMaquina(String codigo) {

		for (Maquina actual : this.maquinas){
			if (actual.codigoInventario.equals(codigo))
			return actual;
		}
		if (codigo == null){
			throw new IllegalArgumentException("El código esta vacío");
		}
		if (codigo.trim().isEmpty()){
			throw new IllegalArgumentException("El código esta vacío");
		}
		return null;
		
	}
	
	public String consultarInventario() {
		if(this.maquinas == null || this.maquinas.isEmpty()){
			return "El inventario esta vacio";
		}
		
		String inventario = "-- INVENTARIO --";
		for (Maquina actual : this.maquinas){
			String estado;

			if (actual.isDisponibilidad() == true){
				estado =  "Esta disponible";
			}
			else {
				estado = "No esta disponible";
			}

			 
			
			inventario += "\n Codigo: "+ actual.getCodigoInventario() + "|| Tipo: " +actual.getCategoria() + "|| Marca: " + actual.getMarca() + "|| Modelo: " + actual.getModelo() + "|| Tarifa Diaria: " + actual.getTarifaDiaria() + "|| Disponibilidad: " + estado + "|| Más detalles: " + actual.obtenerDetalles();
			}
			return inventario;
		}
		
	
	public double cotizarAlquiler(String codigo, int dias) {
		Maquina maquina = buscarMaquina(codigo);
		if (maquina == null){
			throw new IllegalArgumentException("El código no existe.");
		}
		if (dias <= 0){
			throw new IllegalArgumentException("No puede ser menor a un día");
		}
		
		return maquina.calcularCosto(dias);

	}
	
	public double confirmarAlquiler(String codigo, int dias) {
	Maquina maquina = buscarMaquina(codigo);
	if (maquina == null) {
		throw new IllegalArgumentException("No existe la maquina.");
	}
	if (dias <= 0) {
		throw new IllegalArgumentException("Debe alquilar por día");
	}
	if (!maquina.isDisponibilidad()) {
		throw new IllegalArgumentException("La maquina no esta disponible");
	}
	double costo = maquina.calcularCosto(dias);
	maquina.alquilar();
	IngresoAcumulados += costo;
	return costo;
}
public void registrarDevolucion(String codigo) {
	Maquina maquina = buscarMaquina(codigo);
	if (maquina == null) {
		throw new IllegalArgumentException("El código no existe.");
	}
	if (maquina.isDisponibilidad()) {
		throw new IllegalArgumentException("La máquina ya está disponible.");
	}
	maquina.devolver();
}

public int getTotalMaquinas() {
	return maquinas.size();
}

public int contarPorCategoria(String categoria) {
	int contador = 0;
	for (Maquina actual : this.maquinas) {
		if (actual.getCategoria().equals(categoria)) {
			contador++;
		}
	}
	return contador;
}

public int contarDisponiblesPorCategoria(String categoria) {
	int contador = 0;
	for (Maquina actual : this.maquinas) {
		if (actual.getCategoria().equals(categoria) && actual.isDisponibilidad()) {
			contador++;
		}
	}
	return contador;
}

public int contarAlquiladasPorCategoria(String categoria) {
	int contador = 0;
	for (Maquina actual : this.maquinas) {
		if (actual.getCategoria().equals(categoria) && !actual.isDisponibilidad()) {
			contador++;
		}
	}
	return contador;
}

public double getIngresosAcumulados() {
	return IngresoAcumulados;
}

public String generarReporte() {
	String reporte = "-- REPORTE DULCE ESTACIÓN --";
	reporte += "\nTotal de máquinas: " + getTotalMaquinas();
	String[] categorias = {"Palomitas", "Algodon", "Chocolate"};
	for (String categoria : categorias) {
		reporte += "\n" + categoria + " -> Total: " + contarPorCategoria(categoria)
				+ " | Disponibles: " + contarDisponiblesPorCategoria(categoria)
				+ " | Alquiladas: " + contarAlquiladasPorCategoria(categoria);
	}
	reporte += String.format("\nIngresos acumulados: Q%.2f", IngresoAcumulados);
	return reporte;
}
}