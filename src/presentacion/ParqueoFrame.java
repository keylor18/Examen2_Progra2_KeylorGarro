package presentacion;

import entidades.RegistroParqueo;
import entidades.TipoVehiculo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import logica.ParqueoException;
import logica.ParqueoServicio;

public class ParqueoFrame extends JFrame {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final NumberFormat FORMATO_MONTO = NumberFormat.getIntegerInstance(Locale.US);
    private static final Color COLOR_FONDO = new Color(15, 18, 24);
    private static final Color COLOR_SUPERFICIE = new Color(24, 29, 37);
    private static final Color COLOR_SUPERFICIE_ALTA = new Color(31, 37, 47);
    private static final Color COLOR_BORDE = new Color(62, 71, 86);
    private static final Color COLOR_TEXTO = new Color(236, 239, 244);
    private static final Color COLOR_TEXTO_SUAVE = new Color(152, 161, 176);
    private static final Color COLOR_ACENTO = new Color(58, 134, 255);
    private static final Color COLOR_EXITO = new Color(46, 204, 113);
    private static final Color COLOR_PELIGRO = new Color(231, 76, 60);
    private static final Color COLOR_CAMPO = new Color(246, 248, 252);
    private static final Color COLOR_TEXTO_CAMPO = new Color(18, 23, 30);
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
        aplicarTemaOscuroComponentes();
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
        contenedor.setBackground(COLOR_FONDO);
        setContentPane(contenedor);

        contenedor.add(crearCabecera(), BorderLayout.NORTH);
        contenedor.add(crearPanelFormulario(), BorderLayout.WEST);
        contenedor.add(crearPanelTablas(), BorderLayout.CENTER);
        contenedor.add(crearBarraEstado(), BorderLayout.SOUTH);
    }

    private JPanel crearCabecera() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE),
                new EmptyBorder(14, 16, 14, 16)));
        panel.setBackground(COLOR_SUPERFICIE);

        JLabel titulo = new JLabel("Sistema de Parqueo Publico");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(COLOR_TEXTO);

        JLabel subtitulo = new JLabel("Registro de ingreso, salida, cobro y control historico");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(COLOR_TEXTO_SUAVE);

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
        lblActivos.setForeground(COLOR_TEXTO);

        lblHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblHistorial.setForeground(COLOR_TEXTO_SUAVE);
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
        panel.setBackground(COLOR_SUPERFICIE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE),
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
        tituloIngreso.setForeground(COLOR_TEXTO);
        panel.add(tituloIngreso, gbc);

        gbc.gridy++;
        panel.add(crearEtiquetaSecundaria("Placa"), gbc);

        gbc.gridy++;
        panel.add(txtPlaca, gbc);

        gbc.gridy++;
        panel.add(crearEtiquetaSecundaria("Tipo de vehiculo"), gbc);

        gbc.gridy++;
        panel.add(cmbTipo, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(12, 0, 8, 0);
        JButton btnRegistrar = new JButton("Registrar entrada");
        estilizarBoton(btnRegistrar, COLOR_ACENTO);
        btnRegistrar.addActionListener(evento -> registrarIngreso());
        panel.add(btnRegistrar, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(18, 0, 6, 0);
        JLabel tituloSalida = new JLabel("Registrar salida");
        tituloSalida.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tituloSalida.setForeground(COLOR_TEXTO);
        panel.add(tituloSalida, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        lblSeleccion.setForeground(COLOR_TEXTO_SUAVE);
        panel.add(lblSeleccion, gbc);

        gbc.gridy++;
        btnRegistrarSalida.setEnabled(false);
        btnRegistrarSalida.addActionListener(this::registrarSalida);
        panel.add(btnRegistrarSalida, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(18, 0, 6, 0);
        JLabel tituloHistorial = new JLabel("Gestion del historial");
        tituloHistorial.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tituloHistorial.setForeground(COLOR_TEXTO);
        panel.add(tituloHistorial, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        lblHistorialSeleccionado.setForeground(COLOR_TEXTO_SUAVE);
        panel.add(lblHistorialSeleccionado, gbc);

        gbc.gridy++;
        btnEliminarHistorial.setEnabled(false);
        btnEliminarHistorial.addActionListener(this::eliminarHistorial);
        panel.add(btnEliminarHistorial, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(18, 0, 8, 0);
        JLabel tituloBitacora = new JLabel("Mensajes del sistema");
        tituloBitacora.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tituloBitacora.setForeground(COLOR_TEXTO);
        panel.add(tituloBitacora, gbc);

        gbc.gridy++;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        txtBitacora.setEditable(false);
        txtBitacora.setLineWrap(true);
        txtBitacora.setWrapStyleWord(true);
        txtBitacora.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBitacora.setText("La aplicacion mostrara aqui confirmaciones, advertencias y errores.");
        txtBitacora.setCaretPosition(txtBitacora.getDocument().getLength());
        panel.add(crearScrollOscuro(txtBitacora), gbc);

        return panel;
    }

    private JPanel crearPanelTablas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

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
        splitPane.setBackground(COLOR_FONDO);
        panel.add(splitPane, BorderLayout.CENTER);

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pie.setOpaque(false);
        JLabel lblTarifa = new JLabel("La tarifa es de \u20A1500 por hora o fraccion.");
        lblTarifa.setForeground(COLOR_TEXTO_SUAVE);
        pie.add(lblTarifa);
        panel.add(pie, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearContenedorTabla(String titulo, JTable tabla) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(COLOR_SUPERFICIE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE),
                new EmptyBorder(14, 14, 14, 14)));

        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 18));
        etiqueta.setForeground(COLOR_TEXTO);
        panel.add(etiqueta, BorderLayout.NORTH);
        panel.add(crearScrollOscuro(tabla), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearBarraEstado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_SUPERFICIE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE),
                new EmptyBorder(10, 14, 10, 14)));
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEstado.setForeground(COLOR_TEXTO_SUAVE);
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
        lblEstado.setForeground(esError ? new Color(255, 128, 128) : new Color(126, 231, 135));
        lblEstado.setText(mensaje);
        if (!txtBitacora.getText().isBlank()) {
            txtBitacora.append(System.lineSeparator() + System.lineSeparator());
        }
        txtBitacora.append(mensaje);
        txtBitacora.setCaretPosition(txtBitacora.getDocument().getLength());
    }

    private String formatearMonto(long monto) {
        return "\u20A1" + FORMATO_MONTO.format(monto);
    }

    private void aplicarTemaOscuroComponentes() {
        estilizarCampoTexto(txtPlaca);
        estilizarComboBox(cmbTipo);
        estilizarBoton(btnRegistrarSalida, COLOR_EXITO);
        estilizarBoton(btnEliminarHistorial, COLOR_PELIGRO);
        estilizarTabla(tablaActivos);
        estilizarTabla(tablaHistorial);
        estilizarBitacora(txtBitacora);
    }

    private void estilizarCampoTexto(JTextField campo) {
        campo.setBackground(COLOR_CAMPO);
        campo.setForeground(COLOR_TEXTO_CAMPO);
        campo.setCaretColor(COLOR_TEXTO_CAMPO);
        campo.setSelectionColor(COLOR_ACENTO);
        campo.setSelectedTextColor(COLOR_TEXTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE),
                new EmptyBorder(8, 10, 8, 10)));
    }

    private void estilizarComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(COLOR_CAMPO);
        comboBox.setForeground(COLOR_TEXTO_CAMPO);
        comboBox.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component componente = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    componente.setBackground(COLOR_ACENTO);
                    componente.setForeground(COLOR_TEXTO);
                } else {
                    componente.setBackground(COLOR_CAMPO);
                    componente.setForeground(COLOR_TEXTO_CAMPO);
                }
                return componente;
            }
        });
    }

    private void estilizarBoton(JButton boton, Color colorBase) {
        boton.setFocusPainted(false);
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setBackground(COLOR_CAMPO);
        boton.setForeground(COLOR_TEXTO_CAMPO);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBase),
                new EmptyBorder(10, 14, 10, 14)));
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setBackground(COLOR_SUPERFICIE_ALTA);
        tabla.setForeground(COLOR_TEXTO);
        tabla.setSelectionBackground(COLOR_ACENTO);
        tabla.setSelectionForeground(COLOR_TEXTO);
        tabla.setGridColor(COLOR_BORDE);
        tabla.setShowGrid(true);
        tabla.setFillsViewportHeight(true);
        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setBackground(COLOR_CAMPO);
        encabezado.setForeground(COLOR_TEXTO_CAMPO);
        encabezado.setReorderingAllowed(false);
        encabezado.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component componente = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                componente.setBackground(COLOR_CAMPO);
                componente.setForeground(COLOR_TEXTO_CAMPO);
                return componente;
            }
        });
    }

    private void estilizarBitacora(JTextArea area) {
        area.setBackground(COLOR_SUPERFICIE_ALTA);
        area.setForeground(COLOR_TEXTO);
        area.setCaretColor(COLOR_TEXTO);
        area.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private JScrollPane crearScrollOscuro(Component componente) {
        JScrollPane scrollPane = new JScrollPane(componente);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));
        scrollPane.setBackground(COLOR_SUPERFICIE_ALTA);
        scrollPane.getViewport().setBackground(COLOR_SUPERFICIE_ALTA);
        return scrollPane;
    }

    private JLabel crearEtiquetaSecundaria(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(COLOR_TEXTO_SUAVE);
        return etiqueta;
    }
}
