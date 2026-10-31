import java.util.ArrayList;
import java.util.NoSuchElementException;

/** Coordina la vista, el catalogo y las capacidades de las integraciones. */
public class IntegracionesControlador {

    private IntegracionesCatalogo catalogo;
    private VistaConsola vista;

    /** Constructor que recibe y valida las referencias del modelo y de la vista. */
    public IntegracionesControlador(IntegracionesCatalogo catalogo, VistaConsola vista)
            throws IllegalArgumentException {
        if (catalogo == null || vista == null) {
            throw new IllegalArgumentException("El catalogo y la vista son obligatorios.");
        }
        this.catalogo = catalogo;
        this.vista = vista;
    }

    /** Mantiene los menus y maneja las entradas invalidas sin detener el programa. */
    public void iniciar() {
        boolean continuar = true;
        vista.mostrarMensaje("Todas las conexiones y operaciones se simulan en memoria.");
        while (continuar) {
            try {
                vista.mostrarMenu();
                int opcion = vista.leerEntero("Seleccione una opcion: ");
                switch (opcion) {
                    case 1:
                        listarActivas();
                        break;
                    case 2:
                        buscarIntegracion(vista.leerEntero("Id de la integracion: "));
                        break;
                    case 3:
                        ordenarCatalogo();
                        break;
                    case 4:
                        int id = vista.leerEntero("Id de la integracion: ");
                        if (id <= 0) {
                            throw new IllegalArgumentException("El identificador debe ser positivo.");
                        }
                        Integracion seleccionada = catalogo.buscarPorId(id);
                        if (seleccionada == null) {
                            vista.mostrarMensaje("No se encontro la integracion.");
                        } else if (!seleccionada.isActiva()
                                || !seleccionada.getEstadoConexion().equalsIgnoreCase("Conectada")) {
                            vista.mostrarMensaje("La integracion no esta activa y conectada.");
                        } else {
                            boolean continuarOperaciones = true;
                            while (continuarOperaciones) {
                                try {
                                    mostrarOperaciones(id);
                                    int operacion = vista.leerEntero("Seleccione una operacion: ");
                                    // Se comprueba la interfaz antes de solicitar datos de la operacion.
                                    if (operacion == 0) {
                                        continuarOperaciones = false;
                                    } else if (operacion == 1) {
                                        if (seleccionada instanceof Consultable) {
                                            consultarEstado(id, vista.leerTexto("Referencia: "));
                                        } else {
                                            vista.mostrarMensaje("Operacion no disponible.");
                                        }
                                    } else if (operacion == 2) {
                                        if (seleccionada instanceof Cotizable) {
                                            cotizar(id, vista.leerTexto("Descripcion del envio: "));
                                        } else {
                                            vista.mostrarMensaje("Operacion no disponible.");
                                        }
                                    } else if (operacion == 3) {
                                        if (seleccionada instanceof Cobrable) {
                                            cobrar(id, vista.leerDecimal("Monto del cobro: "));
                                        } else {
                                            vista.mostrarMensaje("Operacion no disponible.");
                                        }
                                    } else if (operacion == 4) {
                                        if (seleccionada instanceof Reintegrable) {
                                            String transaccion = vista.leerTexto("Id de la transaccion: ");
                                            double monto = vista.leerDecimal("Monto del reintegro: ");
                                            reintegrar(id, transaccion, monto);
                                        } else {
                                            // Un proveedor logistico no recibe una llamada a reintegrar.
                                            vista.mostrarMensaje("Operacion no disponible.");
                                        }
                                    } else if (operacion == 5) {
                                        if (seleccionada instanceof Documentable) {
                                            generarDocumento(id, vista.leerTexto("Datos del documento: "));
                                        } else {
                                            vista.mostrarMensaje("Operacion no disponible.");
                                        }
                                    } else if (operacion == 6) {
                                        if (seleccionada instanceof Auditable) {
                                            registrarEvento(id, vista.leerTexto("Descripcion del evento: "));
                                        } else {
                                            vista.mostrarMensaje("Operacion no disponible.");
                                        }
                                    } else if (operacion == 7) {
                                        consultarHistorialEventos(id);
                                    } else {
                                        vista.mostrarMensaje("La opcion no existe.");
                                    }
                                } catch (IllegalArgumentException excepcion) {
                                    vista.mostrarMensaje("Dato invalido: " + excepcion.getMessage());
                                } catch (IllegalStateException excepcion) {
                                    vista.mostrarMensaje("No se pudo realizar la operacion: "
                                            + excepcion.getMessage());
                                }
                            }
                        }
                        break;
                    case 0:
                        continuar = false;
                        vista.mostrarMensaje("Programa finalizado.");
                        break;
                    default:
                        vista.mostrarMensaje("La opcion no existe.");
                        break;
                }
            } catch (IllegalArgumentException excepcion) {
                vista.mostrarMensaje("Dato invalido: " + excepcion.getMessage());
            } catch (IllegalStateException excepcion) {
                vista.mostrarMensaje("No se pudo realizar la operacion: " + excepcion.getMessage());
            } catch (NoSuchElementException excepcion) {
                // Scanner produce esta excepcion si se cierra la entrada de la consola.
                continuar = false;
                vista.mostrarMensaje("La entrada de consola se cerro. Programa finalizado.");
            }
        }
    }

    /** Recorre la unica lista y muestra solamente las integraciones habilitadas. */
    public void listarActivas() {
        boolean hayActivas = false;
        vista.mostrarMensaje("\n--- INTEGRACIONES ACTIVAS ---");
        for (int i = 0; i < catalogo.getCantidad(); i++) {
            Integracion integracion = catalogo.getIntegracion(i);
            if (integracion.isActiva()) {
                vista.mostrarIntegracion(integracion);
                hayActivas = true;
            }
        }
        if (!hayActivas) {
            vista.mostrarMensaje("No hay integraciones activas.");
        }
    }

    /** Busca por identificador y solicita a la vista mostrar su configuracion. */
    public void buscarIntegracion(int id) throws IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo.");
        }
        Integracion integracion = catalogo.buscarPorId(id);
        if (integracion == null) {
            vista.mostrarMensaje("No se encontro la integracion.");
        } else {
            vista.mostrarConfiguracion(integracion);
        }
    }

    /** Ordena el catalogo mediante su orden natural y muestra todas sus conexiones. */
    public void ordenarCatalogo() {
        catalogo.ordenarPorLatencia();
        vista.mostrarMensaje("\n--- CATALOGO ORDENADO POR LATENCIA E ID ---");
        if (catalogo.getCantidad() == 0) {
            vista.mostrarMensaje("El catalogo esta vacio.");
        }
        for (int i = 0; i < catalogo.getCantidad(); i++) {
            vista.mostrarIntegracion(catalogo.getIntegracion(i));
        }
    }

    /** Muestra solo las operaciones de las interfaces implementadas por el objeto. */
    public void mostrarOperaciones(int id) throws IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo.");
        }
        Integracion integracion = catalogo.buscarPorId(id);
        if (integracion == null) {
            vista.mostrarMensaje("No se encontro la integracion.");
        } else {
            vista.mostrarMensaje("\n--- OPERACIONES DE " + integracion.getNombre() + " ---");
            if (integracion instanceof Consultable) {
                vista.mostrarMensaje("1. Consultar estado");
            }
            if (integracion instanceof Cotizable) {
                vista.mostrarMensaje("2. Cotizar envio");
            }
            if (integracion instanceof Cobrable) {
                vista.mostrarMensaje("3. Cobrar");
            }
            if (integracion instanceof Reintegrable) {
                vista.mostrarMensaje("4. Reintegrar");
            }
            if (integracion instanceof Documentable) {
                vista.mostrarMensaje("5. Generar documento");
            }
            if (integracion instanceof Auditable) {
                vista.mostrarMensaje("6. Registrar evento");
                vista.mostrarMensaje("7. Consultar historial de eventos");
            }
            vista.mostrarMensaje("0. Volver al menu principal");
        }
    }

    /** Utiliza Consultable para solicitar el estado de una referencia. */
    public void consultarEstado(int id, String referencia)
            throws IllegalArgumentException, IllegalStateException {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo.");
        }
        Integracion integracion = catalogo.buscarPorId(id);
        if (integracion == null) {
            vista.mostrarMensaje("No se encontro la integracion.");
        } else if (!integracion.isActiva()
                || !integracion.getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("La integracion no esta activa y conectada.");
        } else if (integracion instanceof Consultable) {
            Consultable servicio = (Consultable) integracion;
            vista.mostrarMensaje(servicio.consultarEstado(referencia));
        } else {
            vista.mostrarMensaje("Operacion no disponible.");
        }
    }

    /** Utiliza Cotizable y muestra el costo simulado de la solicitud. */
    public void cotizar(int id, String datos)
            throws IllegalArgumentException, IllegalStateException {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo.");
        }
        Integracion integracion = catalogo.buscarPorId(id);
        if (integracion == null) {
            vista.mostrarMensaje("No se encontro la integracion.");
        } else if (!integracion.isActiva()
                || !integracion.getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("La integracion no esta activa y conectada.");
        } else if (integracion instanceof Cotizable) {
            Cotizable servicio = (Cotizable) integracion;
            vista.mostrarMensaje("Cotizacion simulada: " + servicio.cotizar(datos) + " GTQ");
        } else {
            vista.mostrarMensaje("Operacion no disponible.");
        }
    }

    /** Utiliza Cobrable para realizar un cobro y presentar su referencia. */
    public void cobrar(int id, double monto)
            throws IllegalArgumentException, IllegalStateException {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo.");
        }
        Integracion integracion = catalogo.buscarPorId(id);
        if (integracion == null) {
            vista.mostrarMensaje("No se encontro la integracion.");
        } else if (!integracion.isActiva()
                || !integracion.getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("La integracion no esta activa y conectada.");
        } else if (integracion instanceof Cobrable) {
            Cobrable servicio = (Cobrable) integracion;
            vista.mostrarMensaje("Cobro simulado realizado. Referencia: " + servicio.cobrar(monto));
        } else {
            vista.mostrarMensaje("Operacion no disponible.");
        }
    }

    /** Comprueba Reintegrable para que un proveedor sin esa capacidad no la ejecute. */
    public void reintegrar(int id, String idTransaccion, double monto)
            throws IllegalArgumentException, IllegalStateException {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo.");
        }
        Integracion integracion = catalogo.buscarPorId(id);
        if (integracion == null) {
            vista.mostrarMensaje("No se encontro la integracion.");
        } else if (!integracion.isActiva()
                || !integracion.getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("La integracion no esta activa y conectada.");
        } else if (integracion instanceof Reintegrable) {
            Reintegrable servicio = (Reintegrable) integracion;
            boolean realizado = servicio.reintegrar(idTransaccion, monto);
            if (realizado) {
                vista.mostrarMensaje("Reintegro simulado realizado.");
            } else {
                vista.mostrarMensaje("No se realizo el reintegro. Verifique la referencia, "
                        + "el monto positivo y el saldo disponible.");
            }
        } else {
            vista.mostrarMensaje("Operacion no disponible.");
        }
    }

    /** Utiliza Documentable sin preguntar por la clase concreta del proveedor. */
    public void generarDocumento(int id, String datos)
            throws IllegalArgumentException, IllegalStateException {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo.");
        }
        Integracion integracion = catalogo.buscarPorId(id);
        if (integracion == null) {
            vista.mostrarMensaje("No se encontro la integracion.");
        } else if (!integracion.isActiva()
                || !integracion.getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("La integracion no esta activa y conectada.");
        } else if (integracion instanceof Documentable) {
            Documentable servicio = (Documentable) integracion;
            vista.mostrarMensaje(servicio.generarDocumento(datos));
        } else {
            vista.mostrarMensaje("Operacion no disponible.");
        }
    }

    /** Solicita a Auditable registrar el evento ingresado por el administrador. */
    public void registrarEvento(int id, String evento)
            throws IllegalArgumentException, IllegalStateException {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo.");
        }
        Integracion integracion = catalogo.buscarPorId(id);
        if (integracion == null) {
            vista.mostrarMensaje("No se encontro la integracion.");
        } else if (!integracion.isActiva()
                || !integracion.getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("La integracion no esta activa y conectada.");
        } else if (integracion instanceof Auditable) {
            Auditable servicio = (Auditable) integracion;
            servicio.registrarEvento(evento);
            vista.mostrarMensaje("Evento registrado.");
        } else {
            vista.mostrarMensaje("Operacion no disponible.");
        }
    }

    /** Obtiene los eventos mediante Auditable y los muestra uno por uno. */
    public void consultarHistorialEventos(int id)
            throws IllegalArgumentException, IllegalStateException {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo.");
        }
        Integracion integracion = catalogo.buscarPorId(id);
        if (integracion == null) {
            vista.mostrarMensaje("No se encontro la integracion.");
        } else if (!integracion.isActiva()
                || !integracion.getEstadoConexion().equalsIgnoreCase("Conectada")) {
            throw new IllegalStateException("La integracion no esta activa y conectada.");
        } else if (integracion instanceof Auditable) {
            Auditable servicio = (Auditable) integracion;
            ArrayList<String> eventos = servicio.consultarHistorialEventos();
            if (eventos.isEmpty()) {
                vista.mostrarMensaje("El historial esta vacio.");
            } else {
                vista.mostrarMensaje("\n--- HISTORIAL DE EVENTOS ---");
                for (int i = 0; i < eventos.size(); i++) {
                    vista.mostrarMensaje((i + 1) + ". " + eventos.get(i));
                }
            }
        } else {
            vista.mostrarMensaje("Operacion no disponible.");
        }
    }
}
