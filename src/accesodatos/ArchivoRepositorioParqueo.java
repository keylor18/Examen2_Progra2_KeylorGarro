package accesodatos;

import entidades.RegistroParqueo;
import entidades.TipoVehiculo;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ArchivoRepositorioParqueo implements RepositorioParqueo {
    private static final String SEPARADOR = "|";
    private static final DateTimeFormatter FORMATO_ARCHIVO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final Path archivoActivos;
    private final Path archivoHistorial;

    public ArchivoRepositorioParqueo() throws AccesoDatosException {
        this(Paths.get("data", "vehiculos_activos.txt"), Paths.get("data", "historial_vehiculos.txt"));
    }

    public ArchivoRepositorioParqueo(Path archivoActivos, Path archivoHistorial) throws AccesoDatosException {
        this.archivoActivos = archivoActivos;
        this.archivoHistorial = archivoHistorial;
        prepararArchivos();
    }

    @Override
    public synchronized List<RegistroParqueo> cargarActivos() throws AccesoDatosException {
        return leerArchivo(archivoActivos);
    }

    @Override
    public synchronized List<RegistroParqueo> cargarHistorial() throws AccesoDatosException {
        return leerArchivo(archivoHistorial);
    }

    @Override
    public synchronized void guardarActivos(List<RegistroParqueo> registros) throws AccesoDatosException {
        escribirArchivo(archivoActivos, registros);
    }

    @Override
    public synchronized void guardarHistorial(List<RegistroParqueo> registros) throws AccesoDatosException {
        escribirArchivo(archivoHistorial, registros);
    }

    private void prepararArchivos() throws AccesoDatosException {
        try {
            if (archivoActivos.getParent() != null) {
                Files.createDirectories(archivoActivos.getParent());
            }
            if (archivoHistorial.getParent() != null) {
                Files.createDirectories(archivoHistorial.getParent());
            }
            if (Files.notExists(archivoActivos)) {
                Files.createFile(archivoActivos);
            }
            if (Files.notExists(archivoHistorial)) {
                Files.createFile(archivoHistorial);
            }
        } catch (IOException ex) {
            throw new AccesoDatosException("No fue posible preparar los archivos de datos.", ex);
        }
    }

    private List<RegistroParqueo> leerArchivo(Path archivo) throws AccesoDatosException {
        try {
            List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
            List<RegistroParqueo> registros = new ArrayList<>();
            for (String linea : lineas) {
                if (linea == null || linea.isBlank()) {
                    continue;
                }
                registros.add(desdeLinea(linea));
            }
            return registros;
        } catch (IOException ex) {
            throw new AccesoDatosException("No fue posible leer el archivo " + archivo.getFileName() + ".", ex);
        } catch (RuntimeException ex) {
            throw new AccesoDatosException("El archivo " + archivo.getFileName()
                    + " contiene datos invalidos y no pudo procesarse.", ex);
        }
    }

    private void escribirArchivo(Path archivo, List<RegistroParqueo> registros) throws AccesoDatosException {
        List<String> lineas = new ArrayList<>();
        for (RegistroParqueo registro : registros) {
            lineas.add(aLinea(registro));
        }
        try {
            Files.write(archivo, lineas, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new AccesoDatosException("No fue posible guardar el archivo " + archivo.getFileName() + ".", ex);
        }
    }

    private RegistroParqueo desdeLinea(String linea) throws AccesoDatosException {
        String[] partes = linea.split("\\|", -1);
        if (partes.length != 5) {
            throw new AccesoDatosException("Se encontro un registro invalido en el archivo de datos.");
        }
        TipoVehiculo tipo = TipoVehiculo.desdeTexto(partes[1]);
        if (tipo == null) {
            throw new AccesoDatosException("Se encontro un tipo de vehiculo invalido en el archivo de datos.");
        }
        LocalDateTime entrada = LocalDateTime.parse(partes[2], FORMATO_ARCHIVO);
        LocalDateTime salida = partes[3].isBlank() ? null : LocalDateTime.parse(partes[3], FORMATO_ARCHIVO);
        long monto = partes[4].isBlank() ? 0L : Long.parseLong(partes[4]);
        return new RegistroParqueo(partes[0], tipo, entrada, salida, monto);
    }

    private String aLinea(RegistroParqueo registro) {
        String salida = registro.getHoraSalida() == null ? "" : FORMATO_ARCHIVO.format(registro.getHoraSalida());
        String monto = registro.getHoraSalida() == null ? "" : String.valueOf(registro.getMontoCobrado());
        return registro.getPlaca() + SEPARADOR
                + registro.getTipoVehiculo().name() + SEPARADOR
                + FORMATO_ARCHIVO.format(registro.getHoraEntrada()) + SEPARADOR
                + salida + SEPARADOR
                + monto;
    }
}
