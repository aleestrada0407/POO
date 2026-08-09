public class Pal {
	private String nombre;
	private TipoPal tipo;
	private int ataque;
	private int defensa;
	private ArmamentoEspecial armamentoEspecial;
	private int bonPendiente = 0;
	private int penalizacionRecibida = 0;
	
	public enum TipoPal {
		FUEGO, PLANTA, AGUA, ELECTRICO
	}
	
	public enum TipoEfecto {
		ATAQUE, DEFENSA, DAÑO_DIRECTO
	}
	
	public Pal(String nombre, TipoPal tipo, int ataque, int defensa, ArmamentoEspecial armamentoEspecial) {
		this.nombre = nombre;
		this.tipo = tipo; 
		this.ataque = ataque;
		this.defensa = defensa;
		this.armamentoEspecial = armamentoEspecial;
	}
	
	public Pal(String nombre, TipoPal tipo, int ataque, int defensa) {
		this.nombre = nombre;
		this.tipo = tipo;
		this.ataque = ataque;
		this.defensa = defensa;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public TipoPal getTipo() {
		return tipo;
	}
	
	public int getAtaque() {
		return ataque;
	}
	
	public int getDefensa() {
		return defensa;
	}
	
	public ArmamentoEspecial getArmamentoEspecial() {
		return armamentoEspecial;
	}
	
	public void setArmamentoEspecial(ArmamentoEspecial armamentoEspecial) {
		this.armamentoEspecial = armamentoEspecial;
	}
	
	public int calcularEfectoTipo(Pal rival) {
		switch (this.tipo) {
			case FUEGO:
				if (rival.getTipo() == TipoPal.PLANTA) return 20;
				if (rival.getTipo() == TipoPal.AGUA) return -10;
				return 0;
			case PLANTA:
				if (rival.getTipo() == TipoPal.AGUA) return 20;
				if (rival.getTipo() == TipoPal.FUEGO) return -10;
				return 0;
			case AGUA:
				if (rival.getTipo() == TipoPal.FUEGO) return 20;
				if (rival.getTipo() == TipoPal.ELECTRICO) return -10;
				return 0;
			case ELECTRICO:
				if (rival.getTipo() == TipoPal.AGUA) return 20;
				return 0;
			default:
				return 0;
		}
	}
	
	public void recibirDañoDirecto(int valor) {
		penalizacionRecibida += valor;
	}
	
	public int calcularAtaqueTotal(Pal rival, boolean usarHabilidad) {
		int total = this.ataque + this.calcularEfectoTipo(rival) + bonPendiente - penalizacionRecibida;
		bonPendiente = 0;
		penalizacionRecibida = 0;

		if (usarHabilidad && armamentoEspecial != null && armamentoEspecial.activar()) {
			switch (armamentoEspecial.getEfecto()) {
				case ATAQUE:
					total += armamentoEspecial.getValor();
					bonPendiente += armamentoEspecial.getValor();
					break;
				case DEFENSA:
					total += armamentoEspecial.getValor();
					bonPendiente += armamentoEspecial.getValor();
					break;
				case DAÑO_DIRECTO:
					rival.recibirDañoDirecto(armamentoEspecial.getValor());
					break;
			}
		}
		return total;
	}
	
	public static Pal[] crearPalsPredefinidos() {
		Pal[] pals = new Pal[4];
		
		ArmamentoEspecial lanzallamas = new ArmamentoEspecial("Lanzallamas", TipoEfecto.ATAQUE, 15, 30);
		pals[0] = new Pal("Ignis", TipoPal.FUEGO, 50, 30, lanzallamas);
		
		ArmamentoEspecial escudoSimbiotico = new ArmamentoEspecial("Escudo simbiótico", TipoEfecto.DEFENSA, 20, 25);
		pals[1] = new Pal("Aquano", TipoPal.AGUA, 45, 40, escudoSimbiotico);
		
		ArmamentoEspecial corazaVenenosa = new ArmamentoEspecial("Coraza venenosa", TipoEfecto.DAÑO_DIRECTO, 10, 20);
		pals[2] = new Pal("Florvid", TipoPal.PLANTA, 40, 35, corazaVenenosa);
		
		ArmamentoEspecial descargaViva = new ArmamentoEspecial("Descarga viva", TipoEfecto.ATAQUE, 18, 35);
		pals[3] = new Pal("Voltis", TipoPal.ELECTRICO, 48, 25, descargaViva);
		
		return pals;
	}
}