# Lab 4 - Polimorfismo: TorreGT

Aplicación de consola en Java que administra los dispositivos de las oficinas de una torre inteligente: calcula su consumo eléctrico por franja tarifaria, genera las facturas diarias, maneja las alertas en una bandeja ordenada por urgencia y aplica créditos por retraso.

**Estudiante:** José Alejandro Estrada Corado (26558)
**Curso:** CC2008 - Programación Orientada a Objetos, UVG

## Requisitos

- Java JDK 17 o superior.

## Cómo compilar y ejecutar

Desde la carpeta que contiene los archivos `.java`:

```bash
javac -encoding UTF-8 *.java
java main
```

## Estructura

| Archivo | Descripción |
|---|---|
| `main.java` | Punto de entrada. Carga los datos iniciales y muestra el menú. |
| `SistemaTorreGt.java` | Coordina oficinas, dispositivos, franjas, bandeja y hora de simulación. |
| `Dispositivo.java` | Clase abstracta con los datos comunes de los dispositivos. |
| `DispositivoRegulable.java` | Clase abstracta de los dispositivos con nivel de intensidad. |
| `Luminaria.java`, `Climatizador.java`, `ClimatizadorInverter.java`, `SensorHumo.java`, `CamaraSeguridad.java` | Tipos concretos de dispositivo. |
| `Regulable.java`, `Medidor.java`, `DetectorMovimiento.java` | Interfaces de las capacidades compartidas. |
| `Severidad.java` y sus subclases `SeveridadBaja`, `SeveridadAlta`, `SeveridadCritica` | Plazo, crédito y prioridad de cada severidad. |
| `Oficina.java`, `Factura.java`, `ConsumoFranja.java`, `FranjaTarifaria.java` | Consumo y facturación. |
| `Alerta.java`, `BandejaAlerta.java` | Alertas y bandeja ordenada por urgencia. |

## Datos iniciales

El programa inicia en la hora de simulación 0 con 3 oficinas (OF-101, OF-202 y OF-303) y 2 dispositivos de cada tipo. El climatizador inverter INV-01 inicia con nivel 15.
