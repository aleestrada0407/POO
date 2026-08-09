public class Batalla {
	private Entrenador entrenador1;
	private Entrenador entrenador2;
	
	public Batalla(Entrenador entrenador1, Entrenador entrenador2) {
		this.entrenador1 = entrenador1;
		this.entrenador2 = entrenador2;
	}
	
	public Entrenador jugarRonda(Pal pal1, Pal pal2, boolean usarHabilidad1, boolean usarHabilidad2) {
		int ataqueTotal1 = pal1.calcularAtaqueTotal(pal2, usarHabilidad1);
		int ataqueTotal2 = pal2.calcularAtaqueTotal(pal1, usarHabilidad2);
		
		if (ataqueTotal1 > ataqueTotal2) {
			entrenador1.sumarVictoria();
			return entrenador1;
		} else if (ataqueTotal2 > ataqueTotal1) {
			entrenador2.sumarVictoria();
			return entrenador2;
		} else {
			return null;
		}
	}
	
	public Entrenador determinarGanador() {
		if (entrenador1.getRondasGanadas() > entrenador2.getRondasGanadas()) {
			return entrenador1;
		} else if (entrenador2.getRondasGanadas() > entrenador1.getRondasGanadas()) {
			return entrenador2;
		} else {
			return null;
		}
	}
	
	public Entrenador getEntrenador1() {
		return entrenador1;
	}
	
	public Entrenador getEntrenador2() {
		return entrenador2;
	}
}