package presentacion;

import accesodatos.AccesoDatosException;
import accesodatos.ArchivoRepositorioParqueo;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import logica.ParqueoServicio;

public class ParqueoPublicoApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ParqueoPublicoApp::iniciarAplicacion);
    }

    private static void iniciarAplicacion() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Si no se puede aplicar el estilo nativo, Swing usa el predeterminado.
        }

        try {
            ArchivoRepositorioParqueo repositorio = new ArchivoRepositorioParqueo();
            ParqueoServicio servicio = new ParqueoServicio(repositorio);
            new ParqueoFrame(servicio).setVisible(true);
        } catch (AccesoDatosException ex) {
            new ErrorFrame(ex.getMessage()).setVisible(true);
        }
    }
}
