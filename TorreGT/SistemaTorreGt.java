import java.util.ArrayList;

/**
 * Coordina el sistema: oficinas, dispositivos, franjas, bandeja de alertas y hora de simulacion.
 * Delega cada regla en el objeto que le corresponde; nunca pregunta por el tipo concreto.
 */
public class SistemaTorreGt {
    private ArrayList<Oficina> oficinas;
    private ArrayList<Dispositivo> dispositivos;
    private ArrayList<FranjaTarifaria> franjas;
    private BandejaAlerta bandeja;
    private int horaSimulacion;

    public SistemaTorreGt() {
        oficinas = new ArrayList<>();
        dispositivos = new ArrayList<>();
        franjas = new ArrayList<>();
        // Las tres franjas tarifarias: nombre, tarifa por kWh, hora inicial y hora final
        franjas.add(new FranjaTarifaria("Valle", 0.85, 22, 5));
        franjas.add(new FranjaTarifaria("Normal", 1.40, 6, 17));
        franjas.add(new FranjaTarifaria("Punta", 2.10, 18, 21));
        bandeja = new BandejaAlerta();
        horaSimulacion = 0;
    }

    public Oficina registrarOficina(String codigo, String inquilino) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de la oficina no puede estar vacío.");
        }
        // Se revisa que el codigo no este repetido antes de crear la oficina
        for (Oficina oficina : oficinas) {
            if (oficina.getCodigo().equalsIgnoreCase(codigo.trim())) {
                throw new IllegalArgumentException("Ya existe una oficina con el código " + codigo.trim() + ".");
            }
        }
        Oficina nueva = new Oficina(codigo, inquilino, franjas);
        oficinas.add(nueva);
        return nueva;
    }

    public void registrarDispositivo(Dispositivo dispositivo) {
        for (Dispositivo existente : dispositivos) {
            if (existente.getCodigo().equalsIgnoreCase(dispositivo.getCodigo())) {
                throw new IllegalArgumentException("Ya existe un dispositivo con el código " + dispositivo.getCodigo() + ".");
            }
        }
        // Se guarda en la lista general (para buscarlo por codigo) y en su oficina
        dispositivos.add(dispositivo);
        dispositivo.getOficina().agregarDispositivo(dispositivo);
    }

    public Oficina buscarOficina(String codigo) {
        for (Oficina oficina : oficinas) {
            if (oficina.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return oficina;
            }
        }
        throw new IllegalArgumentException("No existe una oficina con el código " + codigo + ".");
    }

    public Dispositivo buscarDispositivo(String codigo) {
        for (Dispositivo dispositivo : dispositivos) {
            if (dispositivo.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return dispositivo;
            }
        }
        throw new IllegalArgumentException("No existe un dispositivo con el código " + codigo + ".");
    }

    public void encenderDispositivo(String codigo) {
        buscarDispositivo(codigo).encender();
    }

    public void apagarDispositivo(String codigo) {
        buscarDispositivo(codigo).apagar();
    }

    // El sistema no revisa el tipo: cada dispositivo acepta o rechaza el ajuste por si mismo
    public void ajustarNivel(String codigo, int nivel) {
        buscarDispositivo(codigo).ajustarNivel(nivel);
    }

    public Alerta registrarLectura(String codigo, double valor) {
        // Si el dispositivo no mide o esta apagado, el objeto lanza la excepcion y no se crea alerta
        Alerta alerta = buscarDispositivo(codigo).registrarLectura(valor, horaSimulacion);
        if (alerta != null) {
            bandeja.agregar(alerta);
        }
        return alerta;
    }

    public Alerta registrarMovimiento(String codigo) {
        Alerta alerta = buscarDispositivo(codigo).registrarMovimiento(horaSimulacion);
        if (alerta != null) {
            bandeja.agregar(alerta);
        }
        return alerta;
    }

    /** Calcula el consumo de la hora actual, avanza una hora y cierra el dia al llegar a la hora 0. */
    public void avanzarHora() {
        // 1) Se busca la franja de la hora actual del dia
        FranjaTarifaria franja = obtenerFranja(getHoraDia());
        // 2) Cada oficina calcula y acumula el consumo de sus dispositivos en esa franja
        for (Oficina oficina : oficinas) {
            oficina.registrarHora(franja);
        }
        // 3) Avanza la hora. Si llega a un multiplo de 24 (paso de la hora 23 a la 0), se cierra el dia
        horaSimulacion++;
        if (horaSimulacion % 24 == 0) {
            cerrarDia();
        }
    }

    private FranjaTarifaria obtenerFranja(int horaDia) {
        for (FranjaTarifaria franja : franjas) {
            if (franja.perteneceAHora(horaDia)) {
                return franja;
            }
        }
        throw new IllegalStateException("Ninguna franja cubre la hora " + horaDia + ".");
    }

    private void cerrarDia() {
        // El dia que se cierra es el anterior al actual; cada oficina genera su factura
        int dia = horaSimulacion / 24 - 1;
        for (Oficina oficina : oficinas) {
            oficina.cerrarDia(dia);
        }
    }

    public Alerta atenderAlertaMasUrgente() {
        // La bandeja entrega y atiende la mas urgente; su credito (0 si fue a tiempo) se suma
        // a la oficina del dispositivo, y se aplicara en la factura del dia en curso
        Alerta alerta = bandeja.atenderMasUrgente(horaSimulacion);
        alerta.getDispositivo().getOficina().agregarCredito(alerta.getCredito());
        return alerta;
    }

    public int getHoraSimulacion() {
        return horaSimulacion;
    }

    public int getHoraDia() {
        return horaSimulacion % 24;
    }

    public int getDia() {
        return horaSimulacion / 24;
    }

    public ArrayList<Oficina> getOficinas() {
        return new ArrayList<>(oficinas);
    }

    public ArrayList<FranjaTarifaria> getFranjas() {
        return new ArrayList<>(franjas);
    }

    public ArrayList<Alerta> getAlertasPendientes() {
        return bandeja.getPendientes();
    }

    public ArrayList<Alerta> getAlertasAtendidasConRetraso() {
        return bandeja.getAtendidasConRetraso();
    }

    public ArrayList<Factura> getFacturas() {
        ArrayList<Factura> todas = new ArrayList<>();
        for (Oficina oficina : oficinas) {
            for (Factura factura : oficina.getFacturas()) {
                todas.add(factura);
            }
        }
        return todas;
    }

    // Recorre todas las facturas y suma el monto de la franja pedida
    public double getTotalPorFranja(FranjaTarifaria franja) {
        double total = 0;
        for (Factura factura : getFacturas()) {
            for (ConsumoFranja detalle : factura.getDetalles()) {
                if (detalle.getFranja() == franja) {
                    total = total + detalle.getMonto();
                }
            }
        }
        return total;
    }

    public double getTotalCreditosAplicados() {
        double total = 0;
        for (Factura factura : getFacturas()) {
            total = total + factura.getCreditosAplicados();
        }
        return total;
    }
}
