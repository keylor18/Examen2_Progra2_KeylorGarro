package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class ErrorFrame extends JFrame {
    public ErrorFrame(String mensaje) {
        setTitle("Error al iniciar la aplicación");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(520, 260));
        setLocationRelativeTo(null);

        JLabel titulo = new JLabel("No fue posible iniciar el sistema");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(163, 34, 30));

        JTextArea area = new JTextArea(mensaje);
        area.setEditable(false);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setBorder(new EmptyBorder(8, 0, 0, 0));

        javax.swing.JPanel contenedor = new javax.swing.JPanel(new BorderLayout(0, 10));
        contenedor.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        contenedor.add(titulo, BorderLayout.NORTH);
        contenedor.add(new JScrollPane(area), BorderLayout.CENTER);
        setContentPane(contenedor);
    }
}
