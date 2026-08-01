import java.util.Scanner;
import java.util.Random;
public class main {
    private Comprador comprador;
    private Localidad localidad1;
    private Localidad localidad5;
    private Localidad localidad10;
    private Scanner teclado;
    private Random aleatorio;
     public static void main(String[] args) {
        main ticket = new main();
        ticket.teclado = new Scanner(System.in);
        ticket.aleatorio = new Random();
     
        ticket.localidad1 = new Localidad (1, 100);
        ticket.localidad5 = new Localidad(5, 500);
        ticket.localidad10 = new Localidad(10, 1000);

        ticket.mostrarOpciones();
    }
    private void mostrarOpciones(){
        int opcion = 0;
        while (opcion !=6) {
            System.out.println("=== TOMORROWLAND MENU ===");
            System.out.println("1. Nuevo comprador");
            System.out.println("2. Nueva solicitud de compra");
            System.out.println("3.Consultar disponibilidad total");
            System.out.println("4.Consultar disponibilidad individual");
            System.out.println("5. Reporte de caja");
            System.out.println("6. Salir");
            System.out.print("Elige una opción:");
            opcion = teclado.nextInt();

            switch (opcion) {
                case 1: nuevoComprador(); break;
                case 2: nuevaSolicitud(); break;
                case 3: consultarDisponibilidadTotal(); break;
                case 4: disponibilidadIndividual(); break;
                case 5: reporteCaja(); break;
                default: System.out.println("Se ha salido exitosamente");
            }
                
            
        }
    }
    public void nuevoComprador () {
        System.out.print("Ingrese su nombre: ");
        teclado.nextLine();
        String nombreIngresado = teclado.nextLine();

        System.out.print("Ingrese su E-MAIL: ");
        String emailIngresado = teclado.nextLine();

        System.out.print("Ingrese la cantidad de boletos que quiere comprar:");
        int cantidadIngresada = teclado.nextInt();

        System.out.println("Ingrese su presupuesto: ");
        int presupuestoIngresado = teclado.nextInt();

        comprador = new Comprador(nombreIngresado, emailIngresado, cantidadIngresada, presupuestoIngresado);
        
        System.out.println("Comprador registrado");
    }
    public void nuevaSolicitud() {
        int ticket = aleatorio.nextInt(15000) + 1;
        System.out.println("Tu ticket generado es:" + ticket);
    }
    public void consultarDisponibilidadTotal() {}
    public void disponibilidadIndividual() {}
    public void reporteCaja() {}


}



