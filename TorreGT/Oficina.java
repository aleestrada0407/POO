import java.util.ArrayList;

/**
 * Oficina de la torre: agrupa sus dispositivos, acumula su consumo por franja
 * y sus creditos del dia, y genera sus facturas.
 */
public class Oficina {
    private String codigo;
    private String inquilino;
    private ArrayList<Dispositivo> dispositivos;
    private ArrayList<ConsumoFranja> consumos;
    private double creditosDia;
    private ArrayList<Factura> facturas;

    public Oficina(String codigo, String inquilino, ArrayList<FranjaTarifaria> franjas) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de la oficina no puede estar vacío.");
        }
        if (inquilino == null || inquilino.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del inquilino no puede estar vacío.");
        }
        this.codigo = codigo.trim();
        this.inquilino = inquilino.trim();
        this.dispositivos = new ArrayList<>();
        this.consumos = new ArrayList<>();
        // Un acumulado en cero por cada franja (valle, normal y punta)
        for (FranjaTarifaria franja : franjas) {
            consumos.add(new ConsumoFranja(franja));
        }
        this.creditosDia = 0;
        this.facturas = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getInquilino() {
        return inquilino;
    }

    /** Inserta el dispositivo en su posicion para que la lista quede ordenada por codigo. */
    public void agregarDispositivo(Dispositivo dispositivo) {
        // Se busca el primer dispositivo con codigo mayor; el nuevo se inserta antes de el.
        // Si no hay ninguno, se agrega al final.
        int posicion = dispositivos.size();
        for (int i = 0; i < dispositivos.size(); i++) {
            if (dispositivo.tieneCodigoMenorQue(dispositivos.get(i))) {
                posicion = i;
                break;
            }
        }
        dispositivos.add(posicion, dispositivo);
    }

    // Devuelve una copia para que nadie de afuera pueda modificar la lista interna
    public ArrayList<Dispositivo> getDispositivos() {
        return new ArrayList<>(dispositivos);
    }

    /** Suma el consumo de la hora de todos sus dispositivos en la franja indicada. */
    public void registrarHora(FranjaTarifaria franja) {
        // Cada dispositivo calcula su propio consumo (polimorfismo) y se suma el total de la oficina
        double totalHora = 0;
        for (Dispositivo dispositivo : dispositivos) {
            totalHora = totalHora + dispositivo.registrarHora();
        }
        // El total se suma solo al acumulado de la franja que corresponde a esta hora
        for (ConsumoFranja consumo : consumos) {
            if (consumo.getFranja() == franja) {
                consumo.sumar(totalHora);
            }
        }
    }

    public ArrayList<ConsumoFranja> getConsumos() {
        return new ArrayList<>(consumos);
    }

    public void agregarCredito(double monto) {
        creditosDia = creditosDia + monto;
    }

    public double getCreditosDia() {
        return creditosDia;
    }

    /** Genera la factura del dia y deja los acumulados en cero. */
    public Factura cerrarDia(int dia) {
        // 1) Se crea la factura con los consumos y creditos del dia
        Factura factura = new Factura(this, dia, consumos, creditosDia);
        facturas.add(factura);
        // 2) Los acumulados de la oficina y de cada dispositivo vuelven a cero para el dia siguiente
        for (ConsumoFranja consumo : consumos) {
            consumo.reiniciar();
        }
        creditosDia = 0;
        for (Dispositivo dispositivo : dispositivos) {
            dispositivo.reiniciarConsumoDia();
        }
        return factura;
    }

    public ArrayList<Factura> getFacturas() {
        return new ArrayList<>(facturas);
    }

    @Override
    public String toString() {
        return String.format("%-8s %-28s dispositivos: %d", codigo, inquilino, dispositivos.size());
    }
}
