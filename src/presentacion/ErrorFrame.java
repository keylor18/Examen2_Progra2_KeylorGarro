package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class ErrorFrame extends JFrame {
    private static final Color COLOR_FONDO = new Color(15, 18, 24);
    private static final Color COLOR_SUPERFICIE = new Color(24, 29, 37);
    private static final Color COLOR_BORDE = new Color(62, 71, 86);
    private static final Color COLOR_TEXTO = new Color(236, 239, 244);

    public ErrorFrame(String mensaje) {
        setTitle("Error al iniciar la aplicacion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(520, 260));
        setLocationRelativeTo(null);

        JLabel titulo = new JLabel("No fue posible iniciar el sistema");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(255, 128, 128));

        JTextArea area = new JTextArea(mensaje);
        area.setEditable(false);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setBackground(COLOR_SUPERFICIE);
        area.setForeground(COLOR_TEXTO);
        area.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));
        scrollPane.setBackground(COLOR_SUPERFICIE);
        scrollPane.getViewport().setBackground(COLOR_SUPERFICIE);

        JPanel contenedor = new JPanel(new BorderLayout(0, 10));
        contenedor.setBackground(COLOR_FONDO);
        contenedor.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        contenedor.add(titulo, BorderLayout.NORTH);
        contenedor.add(scrollPane, BorderLayout.CENTER);
        setContentPane(contenedor);
    }
}
