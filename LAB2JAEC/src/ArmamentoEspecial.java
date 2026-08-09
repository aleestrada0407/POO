public class ArmamentoEspecial {
	private String nombre;
	private Pal.TipoEfecto efecto;
	private int valor;
	private int probabilidadActivacion;
	
	public ArmamentoEspecial(String nombre, Pal.TipoEfecto efecto, int valor, int probabilidadActivacion) {
		this.nombre = nombre;
		this.efecto = efecto;
		this.valor = valor;
		this.probabilidadActivacion = probabilidadActivacion;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public Pal.TipoEfecto getEfecto() {
		return efecto;
	}
	
	public int getValor() {
		return valor;
	}
	
	public int getProbabilidadActivacion() {
		return probabilidadActivacion;
	}
	
	public boolean activar() {
		int numeroAleatorio = (int) (Math.random() * 101);
		return numeroAleatorio <= probabilidadActivacion;
	}
}