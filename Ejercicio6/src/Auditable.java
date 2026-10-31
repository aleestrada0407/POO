import java.util.ArrayList;

/** Contrato de los servicios que registran y permiten consultar eventos. */
public interface Auditable {

    /** Agrega un mensaje al historial de la integracion. */
    void registrarEvento(String evento);

    /** Devuelve los mensajes registrados para que puedan consultarse. */
    ArrayList<String> consultarHistorialEventos();
}
