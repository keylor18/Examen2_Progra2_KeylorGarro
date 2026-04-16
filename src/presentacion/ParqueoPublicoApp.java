package presentacion;

import accesodatos.AccesoDatosException;
import accesodatos.ArchivoRepositorioParqueo;
import java.awt.Color;
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
        configurarTemaOscuro();

        try {
            ArchivoRepositorioParqueo repositorio = new ArchivoRepositorioParqueo();
            ParqueoServicio servicio = new ParqueoServicio(repositorio);
            new ParqueoFrame(servicio).setVisible(true);
        } catch (AccesoDatosException ex) {
            new ErrorFrame(ex.getMessage()).setVisible(true);
        }
    }

    private static void configurarTemaOscuro() {
        Color fondo = new Color(15, 18, 24);
        Color superficie = new Color(24, 29, 37);
        Color superficieAlta = new Color(31, 37, 47);
        Color borde = new Color(62, 71, 86);
        Color texto = new Color(236, 239, 244);
        Color textoSuave = new Color(152, 161, 176);
        Color acento = new Color(58, 134, 255);
        Color campoClaro = new Color(246, 248, 252);
        Color textoCampo = new Color(18, 23, 30);

        UIManager.put("Panel.background", fondo);
        UIManager.put("Viewport.background", superficie);
        UIManager.put("ScrollPane.background", superficie);
        UIManager.put("Label.foreground", texto);
        UIManager.put("Button.background", superficieAlta);
        UIManager.put("Button.foreground", texto);
        UIManager.put("Button.disabledText", textoCampo);
        UIManager.put("Button.select", acento.darker());
        UIManager.put("Button.focus", acento);
        UIManager.put("TextField.background", campoClaro);
        UIManager.put("TextField.foreground", textoCampo);
        UIManager.put("TextField.caretForeground", textoCampo);
        UIManager.put("TextField.selectionBackground", acento);
        UIManager.put("TextField.selectionForeground", texto);
        UIManager.put("TextArea.background", superficieAlta);
        UIManager.put("TextArea.foreground", texto);
        UIManager.put("TextArea.caretForeground", texto);
        UIManager.put("TextArea.selectionBackground", acento);
        UIManager.put("ComboBox.background", campoClaro);
        UIManager.put("ComboBox.foreground", textoCampo);
        UIManager.put("ComboBox.selectionBackground", acento);
        UIManager.put("ComboBox.selectionForeground", texto);
        UIManager.put("Table.background", superficieAlta);
        UIManager.put("Table.foreground", texto);
        UIManager.put("Table.gridColor", borde);
        UIManager.put("Table.selectionBackground", acento);
        UIManager.put("Table.selectionForeground", texto);
        UIManager.put("TableHeader.background", superficie);
        UIManager.put("TableHeader.foreground", texto);
        UIManager.put("List.background", campoClaro);
        UIManager.put("List.foreground", textoCampo);
        UIManager.put("List.selectionBackground", acento);
        UIManager.put("List.selectionForeground", texto);
        UIManager.put("ToolTip.background", superficie);
        UIManager.put("ToolTip.foreground", textoSuave);
    }
}
