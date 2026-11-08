import java.util.ArrayList;

/**
 * Bandeja de alertas. Las pendientes siempre estan ordenadas de la mas urgente a la menos urgente,
 * por lo que quien la recorre no necesita conocer ni repetir el criterio de orden.
 */
public class BandejaAlerta {
    private ArrayList<Alerta> pendientes;
    private ArrayList<Alerta> atendidas;

    public BandejaAlerta() {
        pendientes = new ArrayList<>();
        atendidas = new ArrayList<>();
    }

    /** Inserta la alerta antes de la primera pendiente que sea menos urgente que ella. */
    public void agregar(Alerta alerta) {
        // Se recorre la lista ya ordenada; la nueva alerta queda antes de la primera que sea menos urgente.
        // Si ninguna es menos urgente, queda al final.
        int posicion = pendientes.size();
        for (int i = 0; i < pendientes.size(); i++) {
            if (alerta.esMasUrgenteQue(pendientes.get(i))) {
                posicion = i;
                break;
            }
        }
        pendientes.add(posicion, alerta);
    }

    public boolean estaVacia() {
        return pendientes.isEmpty();
    }

    public int cantidadPendientes() {
        return pendientes.size();
    }

    public Alerta atenderMasUrgente(int horaActual) {
        if (pendientes.isEmpty()) {
            throw new IllegalStateException("No hay alertas pendientes en la bandeja.");
        }
        // La posicion 0 siempre es la mas urgente: se quita, se atiende y pasa a la lista de atendidas
        Alerta alerta = pendientes.remove(0);
        alerta.atender(horaActual);
        atendidas.add(alerta);
        return alerta;
    }

    // Copia de la lista: ya viene en orden de urgencia y no expone la lista interna
    public ArrayList<Alerta> getPendientes() {
        return new ArrayList<>(pendientes);
    }

    public ArrayList<Alerta> getAtendidasConRetraso() {
        ArrayList<Alerta> resultado = new ArrayList<>();
        for (Alerta alerta : atendidas) {
            if (alerta.fueAtendidaConRetraso()) {
                resultado.add(alerta);
            }
        }
        return resultado;
    }
}
