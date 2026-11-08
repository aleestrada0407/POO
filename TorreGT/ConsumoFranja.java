/**
 * kWh que una oficina acumula en una franja tarifaria.
 */
public class ConsumoFranja {
    private FranjaTarifaria franja;
    private double kWh;

    public ConsumoFranja(FranjaTarifaria franja) {
        this.franja = franja;
        this.kWh = 0;
    }

    public ConsumoFranja(FranjaTarifaria franja, double kWh) {
        this.franja = franja;
        this.kWh = kWh;
    }

    public FranjaTarifaria getFranja() {
        return franja;
    }

    public double getKWh() {
        return kWh;
    }

    public void sumar(double kWh) {
        this.kWh = this.kWh + kWh;
    }

    public double getMonto() {
        return franja.calcularMonto(kWh);
    }

    public void reiniciar() {
        kWh = 0;
    }

    // Crea un objeto nuevo con los mismos datos. La factura guarda esta copia,
    // asi no cambia cuando la oficina reinicia sus acumulados.
    public ConsumoFranja copiar() {
        return new ConsumoFranja(franja, kWh);
    }
}
