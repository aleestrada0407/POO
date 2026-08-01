public class Localidad {
    
    private int numeroLocalidad;
    private int precioBoleto;
    private int capacidad;
    private int boletosVendidos;

    public Localidad(int numeroLocalidad, int precioBoleto) {
        this.numeroLocalidad = numeroLocalidad;
        this.precioBoleto = precioBoleto;
        this.capacidad = 20;
        this.boletosVendidos = 0;
    }
    public int espaciosDisponibles(){
        return capacidad - boletosVendidos;
    }
    public void ventaBoletos(int cantidad){
        boletosVendidos = boletosVendidos + cantidad;
    }
}

