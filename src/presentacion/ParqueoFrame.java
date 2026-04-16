package presentacion;

import entidades.RegistroParqueo;
import entidades.TipoVehiculo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import logica.ParqueoException;
import logica.ParqueoServicio;

public class ParqueoFrame extends JFrame {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final NumberFormat FORMATO_MONTO = NumberFormat.getIntegerInstance(Locale.US);
    private final ParqueoServicio servicio;
    private final RegistroTableModel modeloActivos;
    private final RegistroTableModel modeloHistorial;
    private final JTextField txtPlaca;
    private final JComboBox<TipoVehiculo> cmbTipo;
    private final JTable tablaActivos;
    private final JTable tablaHistorial;
    private final JLabel lblActivos;
    private final JLabel lblHistorial;
    private final JLabel lblEstado;
    private final JTextArea txtBitacora;
    private final JLabel lblSeleccion;
    private final JLabel lblHistorialSeleccionado;
    private final JButton btnRegistrarSalida;
    private final JButton btnEliminarHistorial;

    public ParqueoFrame(ParqueoServicio servicio) {
        this.servicio = servicio;
        this.modeloActivos = new RegistroTableModel(RegistroTableModel.VistaTabla.ACTIVOS);
        this.modeloHistorial = new RegistroTableModel(RegistroTableModel.VistaTabla.HISTORIAL);
        this.txtPlaca = new JTextField(16);
        this.cmbTipo = new JComboBox<>(TipoVehiculo.values());
        this.tablaActivos = new JTable(modeloActivos);
        this.tablaHistorial = new JTable(modeloHistorial);
        this.lblActivos = new JLabel("Vehiculos activos: 0");
        this.lblHistorial = new JLabel("Historial: 0");
        this.lblEstado = new JLabel("Sistema listo.");
        this.txtBitacora = new JTextArea(8, 24);
        this.lblSeleccion = new JLabel("Seleccione un vehiculo activo.");
        this.lblHistorialSeleccionado = new JLabel("Seleccione un registro del historial.");
        this.btnRegistrarSalida = new JButton("Registrar salida");
        this.btnEliminarHistorial = new JButton("Eliminar historial");
        inicializarVentana();
        cargarDatos();
    }

    private void inicializarVentana() {
        setTitle("Gestion de Parqueo Publico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1160, 720));
        setLocationRelativeTo(null);

        JPanel contenedor = new JPanel(new BorderLayout(16, 16));
        contenedor.setBorder(new EmptyBorder(16, 16, 16, 16));
        setContentPane(contenedor);

        contenedor.add(crearCabecera(), BorderLayout.NORTH);
        contenedor.add(crearPanelFormulario(), BorderLayout.WEST);
        contenedor.add(crearPanelTablas(), BorderLayout.CENTER);
        contenedor.add(crearBarraEstado(), BorderLayout.SOUTH);
    }

    private JPanel crearCabecera() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 232)),
                new EmptyBorder(14, 16, 14, 16)));
        panel.setBackground(new Color(244, 248, 252));

        JLabel titulo = new JLabel("Sistema de Parqueo Publico");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel subtitulo = new JLabel("Registro de ingreso, salida, cobro y control historico");
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

        JPanel resumen = new JPanel(new GridBagLayout());
        resumen.setOpaque(false);
        GridBagConstraints resumenGbc = new GridBagConstraints();
        resumenGbc.gridx = 0;
        resumenGbc.anchor = GridBagConstraints.EAST;
        resumenGbc.insets = new Insets(2, 0, 2, 0);

        lblActivos.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblActivos.setHorizontalAlignment(SwingConstants.RIGHT);
        lblHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblHistorial.setForeground(new Color(70, 82, 96));
        lblHistorial.setHorizontalAlignment(SwingConstants.RIGHT);

        resumenGbc.gridy = 0;
        resumen.add(lblActivos, resumenGbc);
        resumenGbc.gridy = 1;
        resumen.add(lblHistorial, resumenGbc);

        panel.add(textos, BorderLayout.WEST);
        panel.add(resumen, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(360, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 221, 228)),
                new EmptyBorder(18, 18, 18, 18)));

        txtPlaca.addActionListener(evento -> registrarIngreso());

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
        panel.add(new JLabel("Tipo de vehiculo"), gbc);

        gbc.gridy++;
        panel.add(cmbTipo, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(12, 0, 8, 0);
        JButton btnRegistrar = new JButton("Registrar entrada");
        btnRegistrar.addActionListener(evento -> registrarIngreso());
        panel.add(btnRegistrar, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(18, 0, 6, 0);
        JLabel tituloSalida = new JLabel("Registrar salida");
        tituloSalida.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(tituloSalida, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        lblSeleccion.setForeground(new Color(70, 82, 96));
        panel.add(lblSeleccion, gbc);

        gbc.gridy++;
        btnRegistrarSalida.setEnabled(false);
        btnRegistrarSalida.addActionListener(this::registrarSalida);
        panel.add(btnRegistrarSalida, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(18, 0, 6, 0);
        JLabel tituloHistorial = new JLabel("Gestion del historial");
        tituloHistorial.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(tituloHistorial, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        lblHistorialSeleccionado.setForeground(new Color(70, 82, 96));
        panel.add(lblHistorialSeleccionado, gbc);

        gbc.gridy++;
        btnEliminarHistorial.setEnabled(false);
        btnEliminarHistorial.addActionListener(this::eliminarHistorial);
        panel.add(btnEliminarHistorial, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(18, 0, 8, 0);
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
        txtBitacora.setText("La aplicacion mostrara aqui confirmaciones, advertencias y errores.");
        panel.add(new JScrollPane(txtBitacora), gbc);

        return panel;
    }

    private JPanel crearPanelTablas() {
        JPanel panel = new JPanel(new BorderLayout());

        tablaActivos.setRowHeight(26);
        tablaActivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaActivos.getSelectionModel().addListSelectionListener(evento -> actualizarSeleccionActivos());

        tablaHistorial.setRowHeight(24);
        tablaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaHistorial.getSelectionModel().addListSelectionListener(evento -> actualizarSeleccionHistorial());

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                crearContenedorTabla("Vehiculos actualmente en el parqueo", tablaActivos),
                crearContenedorTabla("Historial de vehiculos", tablaHistorial));
        splitPane.setResizeWeight(0.55);
        splitPane.setBorder(null);
        panel.add(splitPane, BorderLayout.CENTER);

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pie.add(new JLabel("La tarifa es de ₡500 por hora o fraccion."));
        panel.add(pie, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearContenedorTabla(String titulo, JTable tabla) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(etiqueta, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(214, 221, 228)));
        panel.add(scrollPane, BorderLayout.CENTER);
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
            cargarDatos();
            mostrarMensaje("Ingreso registrado para " + registro.getPlaca()
                    + " a las " + FORMATO_FECHA.format(registro.getHoraEntrada()) + ".", false);
        } catch (ParqueoException ex) {
            mostrarMensaje(ex.getMessage(), true);
        }
    }

    private void registrarSalida(ActionEvent evento) {
        RegistroParqueo seleccionado = modeloActivos.getRegistroEn(tablaActivos.getSelectedRow());
        if (seleccionado == null) {
            mostrarMensaje("Seleccione un vehiculo activo antes de registrar la salida.", true);
            return;
        }
        try {
            RegistroParqueo registro = servicio.registrarSalida(seleccionado.getPlaca());
            cargarDatos();
            mostrarMensaje("Salida registrada para " + registro.getPlaca()
                    + ". Monto a pagar: " + formatearMonto(registro.getMontoCobrado()) + ".", false);
        } catch (ParqueoException ex) {
            mostrarMensaje(ex.getMessage(), true);
        }
    }

    private void eliminarHistorial(ActionEvent evento) {
        RegistroParqueo seleccionado = modeloHistorial.getRegistroEn(tablaHistorial.getSelectedRow());
        if (seleccionado == null) {
            mostrarMensaje("Seleccione un registro del historial antes de eliminar.", true);
            return;
        }
        try {
            servicio.eliminarRegistroHistorial(seleccionado);
            cargarDatos();
            mostrarMensaje("Registro historico eliminado para " + seleccionado.getPlaca() + ".", false);
        } catch (ParqueoException ex) {
            mostrarMensaje(ex.getMessage(), true);
        }
    }

    private void cargarDatos() {
        try {
            List<RegistroParqueo> activos = servicio.obtenerActivos();
            List<RegistroParqueo> historial = servicio.obtenerHistorial();
            modeloActivos.setRegistros(activos);
            modeloHistorial.setRegistros(historial);
            tablaActivos.clearSelection();
            tablaHistorial.clearSelection();
            lblActivos.setText("Vehiculos activos: " + activos.size());
            lblHistorial.setText("Historial: " + historial.size());
            actualizarSeleccionActivos();
            actualizarSeleccionHistorial();
        } catch (ParqueoException ex) {
            mostrarMensaje(ex.getMessage(), true);
        }
    }

    private void actualizarSeleccionActivos() {
        RegistroParqueo seleccionado = modeloActivos.getRegistroEn(tablaActivos.getSelectedRow());
        if (seleccionado == null) {
            lblSeleccion.setText("Seleccione un vehiculo activo.");
            btnRegistrarSalida.setEnabled(false);
            return;
        }
        btnRegistrarSalida.setEnabled(true);
        lblSeleccion.setText(seleccionado.getPlaca() + " | "
                + seleccionado.getTipoVehiculo().getDescripcion() + " | Entrada: "
                + FORMATO_FECHA.format(seleccionado.getHoraEntrada()));
    }

    private void actualizarSeleccionHistorial() {
        RegistroParqueo seleccionado = modeloHistorial.getRegistroEn(tablaHistorial.getSelectedRow());
        if (seleccionado == null) {
            lblHistorialSeleccionado.setText("Seleccione un registro del historial.");
            btnEliminarHistorial.setEnabled(false);
            return;
        }
        btnEliminarHistorial.setEnabled(true);
        lblHistorialSeleccionado.setText(seleccionado.getPlaca() + " | Salida: "
                + FORMATO_FECHA.format(seleccionado.getHoraSalida()) + " | Cobro: "
                + formatearMonto(seleccionado.getMontoCobrado()));
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        lblEstado.setForeground(esError ? new Color(163, 34, 30) : new Color(22, 113, 62));
        lblEstado.setText(mensaje);
        txtBitacora.setText(mensaje + System.lineSeparator() + System.lineSeparator() + txtBitacora.getText());
    }

    private String formatearMonto(long monto) {
        return "₡" + FORMATO_MONTO.format(monto);
    }
}
