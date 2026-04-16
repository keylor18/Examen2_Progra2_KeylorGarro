package presentacion;

import entidades.RegistroParqueo;
import entidades.TipoVehiculo;
import logica.ParqueoException;
import logica.ParqueoServicio;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ParqueoFrame extends JFrame {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final ParqueoServicio servicio;
    private final RegistroTableModel modeloActivos;
    private final JTextField txtPlaca;
    private final JComboBox<TipoVehiculo> cmbTipo;
    private final JTable tablaActivos;
    private final JLabel lblActivos;
    private final JLabel lblEstado;
    private final JTextArea txtBitacora;
    private final JLabel lblSeleccion;

    public ParqueoFrame(ParqueoServicio servicio) {
        this.servicio = servicio;
        this.modeloActivos = new RegistroTableModel();
        this.txtPlaca = new JTextField(16);
        this.cmbTipo = new JComboBox<>(TipoVehiculo.values());
        this.tablaActivos = new JTable(modeloActivos);
        this.lblActivos = new JLabel("Vehículos activos: 0");
        this.lblEstado = new JLabel("Sistema listo.");
        this.txtBitacora = new JTextArea(8, 24);
        this.lblSeleccion = new JLabel("Seleccione un vehículo en la tabla.");
        inicializarVentana();
        cargarActivos();
    }

    private void inicializarVentana() {
        setTitle("Gestión de Parqueo Público");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 680));
        setLocationRelativeTo(null);

        JPanel contenedor = new JPanel(new BorderLayout(16, 16));
        contenedor.setBorder(new EmptyBorder(16, 16, 16, 16));
        setContentPane(contenedor);

        contenedor.add(crearCabecera(), BorderLayout.NORTH);
        contenedor.add(crearPanelFormulario(), BorderLayout.WEST);
        contenedor.add(crearPanelTabla(), BorderLayout.CENTER);
        contenedor.add(crearBarraEstado(), BorderLayout.SOUTH);
    }

    private JPanel crearCabecera() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 232)),
                new EmptyBorder(14, 16, 14, 16)));
        panel.setBackground(new Color(244, 248, 252));

        JLabel titulo = new JLabel("Sistema de Parqueo Público");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel subtitulo = new JLabel("Registro de ingreso y control de vehículos activos");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(70, 82, 96));

        JPanel textos = new JPanel(new GridBagLayout());
        textos.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 0, 2, 0);
        gbc.gridy = 0;
        textos.add(titulo, gbc);
        gbc.gridy = 1;
        textos.add(subtitulo, gbc);

        lblActivos.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblActivos.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(textos, BorderLayout.WEST);
        panel.add(lblActivos, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(340, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 221, 228)),
                new EmptyBorder(18, 18, 18, 18)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 8, 0);

        JLabel tituloIngreso = new JLabel("Registrar ingreso");
        tituloIngreso.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(tituloIngreso, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Placa"), gbc);

        gbc.gridy++;
        panel.add(txtPlaca, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Tipo de vehículo"), gbc);

        gbc.gridy++;
        panel.add(cmbTipo, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(12, 0, 8, 0);
        JButton btnRegistrar = new JButton("Registrar entrada");
        btnRegistrar.addActionListener(evento -> registrarIngreso());
        panel.add(btnRegistrar, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(16, 0, 6, 0);
        JLabel tituloSeleccion = new JLabel("Vehículo seleccionado");
        tituloSeleccion.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(tituloSeleccion, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 16, 0);
        lblSeleccion.setForeground(new Color(70, 82, 96));
        panel.add(lblSeleccion, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        JLabel tituloBitacora = new JLabel("Mensajes del sistema");
        tituloBitacora.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(tituloBitacora, gbc);

        gbc.gridy++;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        txtBitacora.setEditable(false);
        txtBitacora.setLineWrap(true);
        txtBitacora.setWrapStyleWord(true);
        txtBitacora.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBitacora.setText("La aplicación mostrará aquí las confirmaciones y validaciones.");
        panel.add(new JScrollPane(txtBitacora), gbc);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));

        JLabel tituloTabla = new JLabel("Vehículos actualmente en el parqueo");
        tituloTabla.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(tituloTabla, BorderLayout.NORTH);

        tablaActivos.setRowHeight(26);
        tablaActivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaActivos.getSelectionModel().addListSelectionListener(evento -> actualizarSeleccion());

        JScrollPane scrollPane = new JScrollPane(tablaActivos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(214, 221, 228)));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pie.add(new JLabel("La hora de entrada se registra automáticamente al guardar."));
        panel.add(pie, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearBarraEstado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 221, 228)),
                new EmptyBorder(10, 14, 10, 14)));
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(lblEstado, BorderLayout.WEST);
        return panel;
    }

    private void registrarIngreso() {
        try {
            RegistroParqueo registro = servicio.registrarIngreso(txtPlaca.getText(), (TipoVehiculo) cmbTipo.getSelectedItem());
            txtPlaca.setText("");
            cmbTipo.setSelectedIndex(0);
            cargarActivos();
            mostrarMensaje("Ingreso registrado para " + registro.getPlaca()
                    + " a las " + FORMATO_FECHA.format(registro.getHoraEntrada()) + ".", false);
        } catch (ParqueoException ex) {
            mostrarMensaje(ex.getMessage(), true);
        }
    }

    private void cargarActivos() {
        try {
            List<RegistroParqueo> activos = servicio.obtenerActivos();
            modeloActivos.setRegistros(activos);
            lblActivos.setText("Vehículos activos: " + activos.size());
            actualizarSeleccion();
        } catch (ParqueoException ex) {
            mostrarMensaje(ex.getMessage(), true);
        }
    }

    private void actualizarSeleccion() {
        RegistroParqueo seleccionado = modeloActivos.getRegistroEn(tablaActivos.getSelectedRow());
        if (seleccionado == null) {
            lblSeleccion.setText("Seleccione un vehículo en la tabla.");
            return;
        }
        lblSeleccion.setText(seleccionado.getPlaca() + " | "
                + seleccionado.getTipoVehiculo().getDescripcion() + " | Entrada: "
                + FORMATO_FECHA.format(seleccionado.getHoraEntrada()));
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        lblEstado.setForeground(esError ? new Color(163, 34, 30) : new Color(22, 113, 62));
        lblEstado.setText(mensaje);
        txtBitacora.setText(mensaje + System.lineSeparator() + System.lineSeparator() + txtBitacora.getText());
    }
}
