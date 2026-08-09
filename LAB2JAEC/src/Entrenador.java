public class Entrenador {
	private String nombre;
	private Pal[] equipo;
	private int rondasGanadas;

	public Entrenador(String nombre) {
		this.nombre = nombre;
		this.equipo = Pal.crearPalsPredefinidos();
		this.rondasGanadas = 0;
	}
	
	public Pal elegirPal(int indice) {
		Pal palElegido = equipo[indice];
		equipo[indice] = null;
		return palElegido;
	}
	
	public Pal[] getEquipo() {
		return equipo;
	}
	
	public void sumarVictoria() {
		rondasGanadas++;
	}
	
	public int getRondasGanadas() {
		return rondasGanadas;
	}
	
	public String getNombre() {
		return nombre;
	}
}