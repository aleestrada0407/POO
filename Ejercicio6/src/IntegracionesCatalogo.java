import java.util.ArrayList;
import java.util.Collections;

/** Modelo que conserva la unica lista polimorfica de integraciones. */
public class IntegracionesCatalogo {

    private ArrayList<Integracion> integraciones;

    /** Constructor que inicia el catalogo sin conexiones registradas. */
    public IntegracionesCatalogo() {
        integraciones = new ArrayList<Integracion>();
    }

    /** Registra una sola vez las diez conexiones propuestas en el analisis. */
    public void cargarInicial() throws IllegalStateException {
        if (!integraciones.isEmpty()) {
            throw new IllegalStateException("La carga inicial necesita un catalogo vacio.");
        }
        agregarIntegracion(new Pago(1, "Tarjeta Uno", "Pagos Uno",
                "https://tarjeta-uno.example", true, "Conectada", 80,
                "Tarjeta", "GTQ", 3));
        agregarIntegracion(new Pago(2, "Transferencia Uno", "Banco Uno",
                "https://transferencia-uno.example", true, "Conectada", 120,
                "Transferencia bancaria", "GTQ", 1));
        agregarIntegracion(new Pago(3, "Billetera Uno", "Billetera Uno",
                "https://billetera-uno.example", true, "Conectada", 60,
                "Billetera digital", "GTQ", 2));
        agregarIntegracion(new Pago(4, "Tarjeta Dos", "Pagos Dos",
                "https://tarjeta-dos.example", true, "Conectada", 95,
                "Tarjeta", "GTQ", 2.5));
        agregarIntegracion(new ProveedorLogistico(5, "Envios Centro", "Logistica Centro",
                "https://envios-centro.example", true, "Conectada", 100,
                "Ciudad de Guatemala", 25));
        agregarIntegracion(new ProveedorLogistico(6, "Envios Nacional", "Logistica Nacional",
                "https://envios-nacional.example", true, "Conectada", 120,
                "Guatemala", 45));
        agregarIntegracion(new CertificadorFEL(7, "FEL Uno", "Certificador Uno",
                "https://fel-uno.example", true, "Conectada", 70,
                "1234567-8", "FEL-A"));
        agregarIntegracion(new CertificadorFEL(8, "FEL Dos", "Certificador Dos",
                "https://fel-dos.example", true, "Conectada", 90,
                "7654321-0", "FEL-B"));
        agregarIntegracion(new Rastreo(9, "Rastreo Uno", "Seguimiento Uno",
                "https://rastreo-uno.example", true, "Conectada", 50));
        agregarIntegracion(new Rastreo(10, "Rastreo Dos", "Seguimiento Dos",
                "https://rastreo-dos.example", true, "Conectada", 80));
    }

    /** Agrega una conexion no nula solo si su identificador no esta registrado. */
    public boolean agregarIntegracion(Integracion integracion) {
        if (integracion == null) {
            return false;
        }
        for (int i = 0; i < integraciones.size(); i++) {
            if (integraciones.get(i).getId() == integracion.getId()) {
                return false;
            }
        }
        integraciones.add(integracion);
        return true;
    }

    /** Busca una conexion por su identificador y devuelve null si no existe. */
    public Integracion buscarPorId(int id) {
        for (int i = 0; i < integraciones.size(); i++) {
            if (integraciones.get(i).getId() == id) {
                return integraciones.get(i);
            }
        }
        return null;
    }

    /** Ordena la misma lista mediante el compareTo heredado de Integracion. */
    public void ordenarPorLatencia() {
        Collections.sort(integraciones);
    }

    /** Devuelve la cantidad actual de conexiones registradas. */
    public int getCantidad() {
        return integraciones.size();
    }

    /** Devuelve una posicion valida del catalogo y rechaza indices fuera del rango. */
    public Integracion getIntegracion(int indice) throws IllegalArgumentException {
        if (indice < 0 || indice >= integraciones.size()) {
            throw new IllegalArgumentException("El indice esta fuera del catalogo.");
        }
        return integraciones.get(indice);
    }
}
