package accesodatos;

import entidades.RegistroParqueo;
import java.util.List;

public interface RepositorioParqueo {
    List<RegistroParqueo> cargarActivos() throws AccesoDatosException;

    List<RegistroParqueo> cargarHistorial() throws AccesoDatosException;

    void guardarActivos(List<RegistroParqueo> registros) throws AccesoDatosException;

    void guardarHistorial(List<RegistroParqueo> registros) throws AccesoDatosException;
}
