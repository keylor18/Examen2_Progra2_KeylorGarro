package logica;

import accesodatos.AccesoDatosException;
import accesodatos.RepositorioParqueo;
import entidades.RegistroParqueo;
import entidades.TipoVehiculo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class ParqueoServicio {
    private static final Pattern PATRON_PLACA = Pattern.compile("^[A-Z0-9-]{4,10}$");
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
            throw new ParqueoException("No se pudieron cargar los vehículos activos.", ex);
        }
    }

    public List<RegistroParqueo> obtenerHistorial() throws ParqueoException {
        try {
            List<RegistroParqueo> registros = new ArrayList<>(repositorio.cargarHistorial());
            registros.sort(Comparator.comparing(RegistroParqueo::getHoraEntrada).reversed());
            return registros;
        } catch (AccesoDatosException ex) {
            throw new ParqueoException("No se pudo cargar el historial.", ex);
        }
    }

    public RegistroParqueo registrarIngreso(String placa, TipoVehiculo tipoVehiculo) throws ParqueoException {
        String placaNormalizada = normalizarPlaca(placa);
        validarPlaca(placaNormalizada);
        if (tipoVehiculo == null) {
            throw new ParqueoException("Debe seleccionar el tipo de vehículo.");
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
            throw new ParqueoException("No se pudo registrar el ingreso del vehículo.", ex);
        }
    }

    private void validarPlaca(String placa) throws ParqueoException {
        if (placa == null || placa.isBlank()) {
            throw new ParqueoException("La placa es obligatoria.");
        }
        if (!PATRON_PLACA.matcher(placa).matches()) {
            throw new ParqueoException("La placa debe tener entre 4 y 10 caracteres alfanuméricos o guiones.");
        }
    }

    private String normalizarPlaca(String placa) {
        return placa == null ? "" : placa.trim().toUpperCase();
    }
}
