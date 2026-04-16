# Examen2_Progra2_KeylorGarro

Aplicacion de escritorio en Java para la gestion de un parqueo publico. El sistema fue construido con Swing, organizado por capas y con persistencia en archivos `.txt`, siguiendo el flujo obligatorio:

Presentacion -> Logica de Negocio -> Acceso a Datos -> Entidades

## Funcionalidades

- Registro de ingreso con placa, tipo de vehiculo y hora automatica.
- Validacion para impedir placas duplicadas mientras el vehiculo siga activo.
- Registro de salida desde la tabla de activos.
- Calculo automatico del monto a pagar con tarifa de `₡500` por hora o fraccion.
- Tabla de vehiculos activos.
- Tabla de historial de salidas.
- Eliminacion de registros del historial.
- Mensajes y errores mostrados dentro de la interfaz, sin `System.out` ni `JOptionPane`.

## Estructura del proyecto

```text
/src
   /entidades
   /logica
   /accesodatos
   /presentacion
/data
/prompts.txt
/CHANGELOG.md
/README.md
```

## Capas

- `entidades`: modelos del dominio como `RegistroParqueo` y `TipoVehiculo`.
- `logica`: validaciones, reglas de negocio, calculo de tarifas y coordinacion del flujo.
- `accesodatos`: lectura y escritura de archivos `.txt`.
- `presentacion`: interfaz Swing con `JFrame`, formularios y `JTable`.

## Persistencia

Los datos se almacenan en:

- `data/vehiculos_activos.txt`
- `data/historial_vehiculos.txt`

Cada linea se guarda con el formato:

```text
placa|tipo|horaEntrada|horaSalida|monto
```

## Reglas de negocio implementadas

- La placa es obligatoria y se normaliza a mayusculas.
- Solo se aceptan placas de 4 a 10 caracteres alfanumericos o guiones.
- No se puede registrar un ingreso si la placa ya esta activa.
- La salida siempre usa la hora actual.
- El cobro se calcula por hora o fraccion.
- El historial se ordena por la salida mas reciente.

## Ejecucion

### Desde NetBeans

1. Abrir la carpeta del proyecto.
2. Ejecutar la clase principal `presentacion.ParqueoPublicoApp`.

### Desde Apache Ant

```bash
ant clean jar
```

El proyecto incluye `build.xml`, `manifest.mf` y `nbproject` para trabajar como proyecto Java con Ant en NetBeans.

## Versionado local preparado

- `v1.0`: registro de entrada funcionando.
- `v1.1`: salida y calculo implementados.
- `v1.2`: mejoras de interfaz, validaciones, historial y documentacion.

## Nota sobre GitHub

El repositorio local, commits y tags fueron preparados en esta carpeta. La publicacion a GitHub requiere una sesion autenticada de `gh`, que no estaba iniciada en esta maquina durante la automatizacion.
