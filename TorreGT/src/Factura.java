import java.util.ArrayList;

/**
 * Factura de un dia para una oficina. Calcula sus montos al construirse.
 */
public class Factura {
    private Oficina oficina;
    private int dia;
    private ArrayList<ConsumoFranja> detalles;
    private double creditosRegistrados;
    private double creditosAplicados;
    private double total;

    public Factura(Oficina oficina, int dia, ArrayList<ConsumoFranja> consumos, double creditos) {
        this.oficina = oficina;
        this.dia = dia;
        // Se guardan copias de los consumos, porque la oficina los reiniciara despues
        this.detalles = new ArrayList<>();
        for (ConsumoFranja consumo : consumos) {
            detalles.add(consumo.copiar());
        }
        this.creditosRegistrados = creditos;
        double subtotal = getSubtotal();
        // Los creditos no pueden dejar el total por debajo de cero; el exceso no se traslada
        if (creditos > subtotal) {
            this.creditosAplicados = subtotal;
        } else {
            this.creditosAplicados = creditos;
        }
        // Total = subtotal - creditos aplicados, redondeado a dos decimales
        // (se multiplica por 100, se redondea al entero y se divide entre 100.0)
        this.total = Math.round((subtotal - creditosAplicados) * 100) / 100.0;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public int getDia() {
        return dia;
    }

    public ArrayList<ConsumoFranja> getDetalles() {
        return new ArrayList<>(detalles);
    }

    // Suma de los montos de las tres franjas
    public double getSubtotal() {
        double suma = 0;
        for (ConsumoFranja detalle : detalles) {
            suma = suma + detalle.getMonto();
        }
        return suma;
    }

    public double getCreditosAplicados() {
        return creditosAplicados;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        String texto = "Factura día " + dia + " - Oficina " + oficina.getCodigo() + " (" + oficina.getInquilino() + ")\n";
        for (ConsumoFranja detalle : detalles) {
            texto = texto + String.format("   %-8s %10.3f kWh   Q%8.2f%n",
                    detalle.getFranja().getNombre(), detalle.getKWh(), detalle.getMonto());
        }
        texto = texto + String.format("   Subtotal:                   Q%8.2f%n", getSubtotal());
        texto = texto + String.format("   Créditos registrados:       Q%8.2f%n", creditosRegistrados);
        texto = texto + String.format("   Créditos aplicados:         Q%8.2f%n", creditosAplicados);
        texto = texto + String.format("   Total a pagar:              Q%8.2f", total);
        return texto;
    }
}
