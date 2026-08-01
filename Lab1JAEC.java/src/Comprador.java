public class Comprador {
    private String nombre;
    private String email;
    private int cantidadBoletos;
    private int presupuesto;

    public Comprador(String nombre, String email, int cantidadBoletos, int presupuesto){
        this.nombre = nombre;
        this.email = email;
        this.cantidadBoletos = cantidadBoletos;
        this.presupuesto = presupuesto;

    }
    public String getNombre(){
        return nombre;
    }
    public String getEmail(){
        return email;
    }
    public int getCantidadBoletos(){
        return cantidadBoletos;
    }
    public int getPresupuesto(){
        return presupuesto;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setCantidadBoletos(int cantidadBoletos){
        this.cantidadBoletos = cantidadBoletos;
    }
    public void setPresupuesto(int presupuesto){
        this.presupuesto = presupuesto;
    }
}
