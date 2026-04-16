package logica;

import accesodatos.AccesoDatosException;
import accesodatos.RepositorioParqueo;
import entidades.RegistroParqueo;
import entidades.TipoVehiculo;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class ParqueoServicio {
    private static final Pattern PATRON_PLACA = Pattern.compile("^[A-Z0-9-]{4,10}$");
    private static final long TARIFA_POR_HORA = 500L;
    private final RepositorioParqueo repositorio;

    public ParqueoServicio(RepositorioParqueo repositorio) {
        this.repositorio = repositorio;
    }

    public List<RegistroParqueo> obtenerActivos() throws ParqueoException {
        try {
            List<RegistroParqueo> registros = new ArrayList<>(repositorio.cargarActivos());
            registros.sort(Comparator.comparing(RegistroParqueo::getHoraEntrada).reversed());
            return registros;
        } catch (AccesoDatosException ex) {
            throw new ParqueoException("No se pudieron cargar los vehiculos activos.", ex);
        }
    }

    public List<RegistroParqueo> obtenerHistorial() throws ParqueoException {
        try {
            List<RegistroParqueo> registros = new ArrayList<>(repositorio.cargarHistorial());
            registros.sort(Comparator.comparing(RegistroParqueo::getHoraSalida).reversed());
            return registros;
        } catch (AccesoDatosException ex) {
            throw new ParqueoException("No se pudo cargar el historial.", ex);
        }
    }

    public RegistroParqueo registrarIngreso(String placa, TipoVehiculo tipoVehiculo) throws ParqueoException {
        String placaNormalizada = normalizarPlaca(placa);
        validarPlaca(placaNormalizada);
        if (tipoVehiculo == null) {
            throw new ParqueoException("Debe seleccionar el tipo de vehiculo.");
        }
        try {
            List<RegistroParqueo> activos = new ArrayList<>(repositorio.cargarActivos());
            for (RegistroParqueo registro : activos) {
                if (registro.getPlaca().equalsIgnoreCase(placaNormalizada)) {
                    throw new ParqueoException("La placa " + placaNormalizada + " ya tiene un ingreso activo.");
                }
            }
            RegistroParqueo nuevoRegistro = new RegistroParqueo(placaNormalizada, tipoVehiculo, LocalDateTime.now());
            activos.add(nuevoRegistro);
            repositorio.guardarActivos(activos);
            return nuevoRegistro;
        } catch (AccesoDatosException ex) {
            throw new ParqueoException("No se pudo registrar el ingreso del vehiculo.", ex);
        }
    }

    public RegistroParqueo registrarSalida(String placa) throws ParqueoException {
        String placaNormalizada = normalizarPlaca(placa);
        validarPlaca(placaNormalizada);
        try {
            List<RegistroParqueo> activos = new ArrayList<>(repositorio.cargarActivos());
            RegistroParqueo registroActivo = null;
            for (RegistroParqueo registro : activos) {
                if (registro.getPlaca().equalsIgnoreCase(placaNormalizada)) {
                    registroActivo = registro;
                    break;
                }
            }
            if (registroActivo == null) {
                throw new ParqueoException("Debe seleccionar un vehiculo activo para registrar la salida.");
            }

            LocalDateTime horaSalida = LocalDateTime.now();
            long monto = calcularMonto(registroActivo.getHoraEntrada(), horaSalida);
            registroActivo.registrarSalida(horaSalida, monto);
            activos.remove(registroActivo);

            List<RegistroParqueo> historial = new ArrayList<>(repositorio.cargarHistorial());
            historial.add(registroActivo);

            repositorio.guardarActivos(activos);
            repositorio.guardarHistorial(historial);
            return registroActivo;
        } catch (AccesoDatosException ex) {
            throw new ParqueoException("No se pudo registrar la salida del vehiculo.", ex);
        }
    }

    public void eliminarRegistroHistorial(RegistroParqueo registroEliminar) throws ParqueoException {
        if (registroEliminar == null) {
            throw new ParqueoException("Debe seleccionar un registro del historial para eliminarlo.");
        }
        try {
            List<RegistroParqueo> historial = new ArrayList<>(repositorio.cargarHistorial());
            boolean eliminado = historial.removeIf(registro ->
                    registro.getPlaca().equalsIgnoreCase(registroEliminar.getPlaca())
                    && registro.getHoraEntrada().equals(registroEliminar.getHoraEntrada())
                    && registro.getHoraSalida() != null
                    && registro.getHoraSalida().equals(registroEliminar.getHoraSalida()));
            if (!eliminado) {
                throw new ParqueoException("El registro seleccionado ya no existe en el historial.");
            }
            repositorio.guardarHistorial(historial);
        } catch (AccesoDatosException ex) {
            throw new ParqueoException("No se pudo eliminar el registro del historial.", ex);
        }
    }

    public long calcularMonto(LocalDateTime horaEntrada, LocalDateTime horaSalida) {
        long minutos = Duration.between(horaEntrada, horaSalida).toMinutes();
        long horasCobradas = Math.max(1L, (minutos + 59L) / 60L);
        return horasCobradas * TARIFA_POR_HORA;
    }

    private void validarPlaca(String placa) throws ParqueoException {
        if (placa == null || placa.isBlank()) {
            throw new ParqueoException("La placa es obligatoria.");
        }
        if (!PATRON_PLACA.matcher(placa).matches()) {
            throw new ParqueoException("La placa debe tener entre 4 y 10 caracteres alfanumericos o guiones.");
        }
    }

    private String normalizarPlaca(String placa) {
        return placa == null ? "" : placa.trim().replaceAll("\\s+", "").toUpperCase();
    }
}
