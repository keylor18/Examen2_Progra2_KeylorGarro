package presentacion;

import accesodatos.AccesoDatosException;
import accesodatos.ArchivoRepositorioParqueo;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import logica.ParqueoServicio;

public class ParqueoPublicoApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> iniciarAplicacion());
    }

    private static void iniciarAplicacion() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Si el sistema no soporta el look and feel nativo, Swing usa el predeterminado.
        }

        try {
            ArchivoRepositorioParqueo repositorio = new ArchivoRepositorioParqueo();
            ParqueoServicio servicio = new ParqueoServicio(repositorio);
            new ParqueoFrame(servicio).setVisible(true);
        } catch (AccesoDatosException ex) {
            ErrorFrame errorFrame = new ErrorFrame(ex.getMessage());
            errorFrame.setVisible(true);
        }
    }
}
