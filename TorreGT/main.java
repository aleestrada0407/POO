import java.util.ArrayList;
import java.util.Scanner;

/**
 * Punto de entrada del programa TorreGT.
 * Crea el sistema, carga los datos iniciales y muestra el menu hasta que el usuario elige salir.
 */
public class main {

    public static void main(String[] args) {
        // Se crea el sistema (empieza en la hora de simulacion 0) y el lector de teclado
        SistemaTorreGt sistema = new SistemaTorreGt();
        Scanner sc = new Scanner(System.in);

        // ---------------- CARGA INICIAL ----------------
        // Tres oficinas y dos dispositivos de cada tipo, sin consumos, alertas ni facturas.
        Oficina of101 = sistema.registrarOficina("OF-101", "Café Antigua, S.A.");
        Oficina of202 = sistema.registrarOficina("OF-202", "Estudio Contable Maya");
        Oficina of303 = sistema.registrarOficina("OF-303", "TechNova GT");

        sistema.registrarDispositivo(new Luminaria("LUM-01", of101, 40));
        sistema.registrarDispositivo(new Luminaria("LUM-02", of202, 60));
        sistema.registrarDispositivo(new Climatizador("CLI-01", of101, 1500));
        sistema.registrarDispositivo(new Climatizador("CLI-02", of303, 2000));
        sistema.registrarDispositivo(new ClimatizadorInverter("INV-01", of202, 1500));
        sistema.registrarDispositivo(new ClimatizadorInverter("INV-02", of303, 1200));
        sistema.registrarDispositivo(new SensorHumo("HUM-01", of101, 5));
        sistema.registrarDispositivo(new SensorHumo("HUM-02", of303, 5));
        sistema.registrarDispositivo(new CamaraSeguridad("CAM-01", of101, 12));
        sistema.registrarDispositivo(new CamaraSeguridad("CAM-02", of202, 15));

        // Para tener un inverter con nivel menor que 20: se enciende, se ajusta a 15 y se apaga.
        // Como no se avanza la hora, no registra consumo, y el nivel 15 se conserva apagado.
        sistema.encenderDispositivo("INV-01");
        sistema.ajustarNivel("INV-01", 15);
        sistema.apagarDispositivo("INV-01");

        // ---------------- MENU ----------------
        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("========== TorreGT ==========");
            System.out.println("Hora de simulación: " + sistema.getHoraSimulacion()
                    + "  (día " + sistema.getDia() + ", " + sistema.getHoraDia() + ":00)");
            System.out.println("1. Registrar oficina");
            System.out.println("2. Registrar dispositivo");
            System.out.println("3. Encender dispositivo");
            System.out.println("4. Apagar dispositivo");
            System.out.println("5. Ajustar nivel");
            System.out.println("6. Registrar lectura");
            System.out.println("7. Registrar movimiento");
            System.out.println("8. Avanzar una hora");
            System.out.println("9. Avanzar varias horas");
            System.out.println("10. Atender la alerta más urgente");
            System.out.println("11. Consultar oficinas");
            System.out.println("12. Consultar un dispositivo");
            System.out.println("13. Reporte: dispositivos de una oficina");
            System.out.println("14. Reporte: alertas (bandeja y atendidas con retraso)");
            System.out.println("15. Reporte: facturas, totales por franja y créditos");
            System.out.println("0. Salir");
            System.out.print("Opción: ");

            // Todo lo que pasa en una opcion va dentro de try: si el usuario escribe algo invalido
            // o una operacion se rechaza, se muestra el mensaje y el menu sigue funcionando.
            try {
                int opcion = Integer.parseInt(sc.nextLine().trim());

                switch (opcion) {
                    case 1: {
                        // Registrar oficina: el sistema rechaza codigos vacios o repetidos
                        System.out.print("Código de la oficina: ");
                        String codigo = sc.nextLine();
                        System.out.print("Nombre del inquilino: ");
                        String inquilino = sc.nextLine();
                        Oficina nueva = sistema.registrarOficina(codigo, inquilino);
                        System.out.println("Oficina registrada: " + nueva);
                        break;
                    }
                    case 2: {
                        // Registrar dispositivo: se pide el tipo y se crea el objeto de esa clase.
                        // A partir de aqui, el resto del programa lo trata solo como Dispositivo.
                        System.out.println("Tipo: 1. Luminaria  2. Climatizador  3. Climatizador inverter  4. Sensor de humo  5. Cámara");
                        System.out.print("Tipo: ");
                        int tipo = Integer.parseInt(sc.nextLine().trim());
                        // Se valida el tipo antes de pedir los demas datos
                        if (tipo < 1 || tipo > 5) {
                            throw new IllegalArgumentException("Tipo de dispositivo no válido.");
                        }
                        System.out.print("Código del dispositivo: ");
                        String codigo = sc.nextLine();
                        System.out.print("Código de la oficina: ");
                        Oficina oficina = sistema.buscarOficina(sc.nextLine());
                        System.out.print("Potencia nominal (W): ");
                        double potencia = Double.parseDouble(sc.nextLine().trim());

                        Dispositivo nuevo = null;
                        switch (tipo) {
                            case 1:
                                nuevo = new Luminaria(codigo, oficina, potencia);
                                break;
                            case 2:
                                nuevo = new Climatizador(codigo, oficina, potencia);
                                break;
                            case 3:
                                nuevo = new ClimatizadorInverter(codigo, oficina, potencia);
                                break;
                            case 4:
                                nuevo = new SensorHumo(codigo, oficina, potencia);
                                break;
                            case 5:
                                nuevo = new CamaraSeguridad(codigo, oficina, potencia);
                                break;
                            default:
                                throw new IllegalArgumentException("Tipo de dispositivo no válido.");
                        }
                        // El constructor ya valido el codigo y la potencia; el sistema valida que no se repita
                        sistema.registrarDispositivo(nuevo);
                        System.out.println("Dispositivo registrado: " + nuevo);
                        break;
                    }
                    case 3: {
                        System.out.print("Código del dispositivo: ");
                        String codigo = sc.nextLine();
                        sistema.encenderDispositivo(codigo);
                        System.out.println("Encendido: " + sistema.buscarDispositivo(codigo));
                        break;
                    }
                    case 4: {
                        // Si es un sensor de humo, su propia clase rechaza la operacion
                        System.out.print("Código del dispositivo: ");
                        String codigo = sc.nextLine();
                        sistema.apagarDispositivo(codigo);
                        System.out.println("Apagado: " + sistema.buscarDispositivo(codigo));
                        break;
                    }
                    case 5: {
                        // Si el dispositivo no es regulable, esta apagado o el nivel esta fuera de rango,
                        // el objeto lanza una excepcion y el nivel no cambia
                        System.out.print("Código del dispositivo: ");
                        String codigo = sc.nextLine();
                        System.out.print("Nuevo nivel (%): ");
                        int nivel = Integer.parseInt(sc.nextLine().trim());
                        sistema.ajustarNivel(codigo, nivel);
                        System.out.println("Nivel ajustado: " + sistema.buscarDispositivo(codigo));
                        break;
                    }
                    case 6: {
                        // Si la lectura alcanza un umbral, el sistema recibe la alerta y la pone en la bandeja
                        System.out.print("Código del dispositivo: ");
                        String codigo = sc.nextLine();
                        System.out.print("Valor de la lectura: ");
                        double valor = Double.parseDouble(sc.nextLine().trim());
                        Alerta alerta = sistema.registrarLectura(codigo, valor);
                        if (alerta == null) {
                            System.out.println("Lectura registrada. No se generó alerta.");
                        } else {
                            System.out.println("Lectura registrada. Alerta generada: " + alerta);
                        }
                        break;
                    }
                    case 7: {
                        System.out.print("Código de la cámara: ");
                        String codigo = sc.nextLine();
                        Alerta alerta = sistema.registrarMovimiento(codigo);
                        if (alerta == null) {
                            System.out.println("Movimiento registrado dentro del horario laboral. No se generó alerta.");
                        } else {
                            System.out.println("Movimiento registrado. Alerta generada: " + alerta);
                        }
                        break;
                    }
                    case 8: {
                        // Avanza una hora; si se pasa de la hora 23 a la 0, se muestran las facturas del dia cerrado
                        sistema.avanzarHora();
                        System.out.println("Hora actual: " + sistema.getHoraSimulacion());
                        if (sistema.getHoraDia() == 0) {
                            System.out.println("Se cerró el día " + (sistema.getDia() - 1) + ". Facturas generadas:");
                            for (Oficina oficina : sistema.getOficinas()) {
                                ArrayList<Factura> facturas = oficina.getFacturas();
                                System.out.println(facturas.get(facturas.size() - 1));
                            }
                        }
                        break;
                    }
                    case 9: {
                        // Igual que la opcion 8, repetida la cantidad de veces indicada
                        System.out.print("¿Cuántas horas desea avanzar? ");
                        int horas = Integer.parseInt(sc.nextLine().trim());
                        if (horas <= 0) {
                            throw new IllegalArgumentException("La cantidad de horas debe ser mayor que cero.");
                        }
                        for (int i = 0; i < horas; i++) {
                            sistema.avanzarHora();
                            if (sistema.getHoraDia() == 0) {
                                System.out.println("Se cerró el día " + (sistema.getDia() - 1) + ". Facturas generadas:");
                                for (Oficina oficina : sistema.getOficinas()) {
                                    ArrayList<Factura> facturas = oficina.getFacturas();
                                    System.out.println(facturas.get(facturas.size() - 1));
                                }
                            }
                        }
                        System.out.println("Hora actual: " + sistema.getHoraSimulacion());
                        break;
                    }
                    case 10: {
                        // Se atiende siempre la primera de la bandeja (la mas urgente)
                        Alerta alerta = sistema.atenderAlertaMasUrgente();
                        System.out.println("Alerta atendida: " + alerta);
                        if (alerta.fueAtendidaConRetraso()) {
                            System.out.printf("Se acreditaron Q%.2f a la oficina %s.%n",
                                    alerta.getCredito(), alerta.getDispositivo().getOficina().getCodigo());
                        } else {
                            System.out.println("Atendida dentro de su plazo. No genera crédito.");
                        }
                        break;
                    }
                    case 11: {
                        System.out.println("Oficinas registradas:");
                        for (Oficina oficina : sistema.getOficinas()) {
                            System.out.println("  " + oficina);
                        }
                        break;
                    }
                    case 12: {
                        System.out.print("Código del dispositivo: ");
                        Dispositivo dispositivo = sistema.buscarDispositivo(sc.nextLine());
                        System.out.println(dispositivo);
                        break;
                    }
                    case 13: {
                        // La oficina guarda sus dispositivos ya ordenados por codigo
                        System.out.print("Código de la oficina: ");
                        Oficina oficina = sistema.buscarOficina(sc.nextLine());
                        System.out.println("Dispositivos de " + oficina.getCodigo() + " (" + oficina.getInquilino() + "):");
                        for (Dispositivo dispositivo : oficina.getDispositivos()) {
                            System.out.println("  " + dispositivo);
                        }
                        break;
                    }
                    case 14: {
                        // La bandeja ya entrega las alertas en orden de urgencia
                        System.out.println("Bandeja de alertas pendientes (orden de urgencia):");
                        ArrayList<Alerta> pendientes = sistema.getAlertasPendientes();
                        if (pendientes.isEmpty()) {
                            System.out.println("  No hay alertas pendientes.");
                        }
                        for (Alerta alerta : pendientes) {
                            System.out.println("  " + alerta);
                        }
                        System.out.println("Alertas atendidas con retraso:");
                        ArrayList<Alerta> conRetraso = sistema.getAlertasAtendidasConRetraso();
                        if (conRetraso.isEmpty()) {
                            System.out.println("  No hay alertas atendidas con retraso.");
                        }
                        for (Alerta alerta : conRetraso) {
                            System.out.println("  " + alerta);
                        }
                        break;
                    }
                    case 15: {
                        // Facturas de cada oficina, y luego los totales de todas las facturas
                        for (Oficina oficina : sistema.getOficinas()) {
                            System.out.println("--- Facturas de " + oficina.getCodigo() + " ---");
                            ArrayList<Factura> facturas = oficina.getFacturas();
                            if (facturas.isEmpty()) {
                                System.out.println("  Aún no tiene facturas (se generan al cerrar el día).");
                            }
                            for (Factura factura : facturas) {
                                System.out.println(factura);
                            }
                        }
                        System.out.println("--- Totales facturados por franja ---");
                        for (FranjaTarifaria franja : sistema.getFranjas()) {
                            System.out.printf("  %-8s Q%.2f%n", franja.getNombre(), sistema.getTotalPorFranja(franja));
                        }
                        System.out.printf("  Créditos aplicados: Q%.2f%n", sistema.getTotalCreditosAplicados());
                        break;
                    }
                    case 0: {
                        salir = true;
                        System.out.println("Fin del programa.");
                        break;
                    }
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                // El usuario escribio letras u otro formato donde se esperaba un numero
                System.out.println("Entrada inválida: debe escribir un número.");
            } catch (IllegalArgumentException e) {
                // Un dato fuera de rango, un codigo vacio o repetido, o algo que no existe
                System.out.println("Operación rechazada: " + e.getMessage());
            } catch (IllegalStateException e) {
                // El objeto no puede hacer la operacion (apagado, no regulable, bandeja vacia, etc.)
                System.out.println("Operación rechazada: " + e.getMessage());
            }
        }
        sc.close();
    }
}
