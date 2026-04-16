package presentacion;

import entidades.RegistroParqueo;
import entidades.TipoVehiculo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import logica.ParqueoException;
import logica.ParqueoServicio;

public class ParqueoFrame extends JFrame {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FORMATO_RELOJ = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter FORMATO_DIA = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM yyyy", new Locale("es", "CR"));
    private static final NumberFormat FORMATO_MONTO = NumberFormat.getIntegerInstance(Locale.US);
    private static final Color COLOR_FONDO = new Color(10, 14, 22);
    private static final Color COLOR_PANEL = new Color(21, 28, 38);
    private static final Color COLOR_PANEL_ALT = new Color(27, 35, 47);
    private static final Color COLOR_PANEL_ALT_2 = new Color(34, 43, 58);
    private static final Color COLOR_BORDE = new Color(53, 66, 84);
    private static final Color COLOR_TEXTO = new Color(244, 247, 251);
    private static final Color COLOR_TEXTO_SUAVE = new Color(155, 168, 186);
    private static final Color COLOR_AZUL = new Color(70, 126, 255);
    private static final Color COLOR_AZUL_HOVER = new Color(95, 146, 255);
    private static final Color COLOR_VERDE = new Color(33, 191, 115);
    private static final Color COLOR_VERDE_HOVER = new Color(52, 211, 133);
    private static final Color COLOR_ROJO = new Color(239, 68, 68);
    private static final Color COLOR_ROJO_HOVER = new Color(248, 113, 113);
    private static final Color COLOR_DORADO = new Color(250, 204, 21);
    private static final Color COLOR_ENTRADA = new Color(245, 247, 251);
    private static final Color COLOR_TEXTO_ENTRADA = new Color(23, 31, 42);
    private final ParqueoServicio servicio;
    private final RegistroTableModel modeloActivos;
    private final RegistroTableModel modeloHistorial;
    private final JTextField txtPlaca;
    private final JComboBox<TipoVehiculo> cmbTipo;
    private final JTable tablaActivos;
    private final JTable tablaHistorial;
    private final JTextArea txtBitacora;
    private final JLabel lblSeleccion;
    private final JLabel lblHistorialSeleccionado;
    private final JLabel lblEstadoChip;
    private final JLabel lblMetricActivos;
    private final JLabel lblMetricHistorial;
    private final JLabel lblResumenActivosTabla;
    private final JLabel lblResumenHistorialTabla;
    private final JLabel lblReloj;
    private final JLabel lblFecha;
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
        this.txtBitacora = new JTextArea(8, 24);
        this.lblSeleccion = new JLabel();
        this.lblHistorialSeleccionado = new JLabel();
        this.lblEstadoChip = new JLabel("Sistema listo");
        this.lblMetricActivos = new JLabel("0");
        this.lblMetricHistorial = new JLabel("0");
        this.lblResumenActivosTabla = new JLabel("Sin vehiculos activos por el momento.");
        this.lblResumenHistorialTabla = new JLabel("Historial vacio.");
        this.lblReloj = new JLabel();
        this.lblFecha = new JLabel();
        this.btnRegistrarSalida = crearBotonAccion("Registrar salida", COLOR_VERDE, COLOR_VERDE_HOVER);
        this.btnEliminarHistorial = crearBotonAccion("Eliminar registro", COLOR_ROJO, COLOR_ROJO_HOVER);
        configurarComponentes();
        inicializarVentana();
        iniciarReloj();
        cargarDatos();
    }

    private void configurarComponentes() {
        txtPlaca.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPlaca.setBackground(COLOR_ENTRADA);
        txtPlaca.setForeground(COLOR_TEXTO_ENTRADA);
        txtPlaca.setCaretColor(COLOR_TEXTO_ENTRADA);
        txtPlaca.setEditable(true);
        txtPlaca.setEnabled(true);
        txtPlaca.setFocusable(true);
        txtPlaca.setRequestFocusEnabled(true);
        txtPlaca.setOpaque(true);
        txtPlaca.setSelectionColor(COLOR_AZUL);
        txtPlaca.setSelectedTextColor(COLOR_TEXTO);
        txtPlaca.setPreferredSize(new Dimension(0, 46));
        txtPlaca.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 213, 225)),
                new EmptyBorder(12, 14, 12, 14)));
        txtPlaca.addActionListener(evento -> registrarIngreso());
        txtPlaca.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                txtPlaca.requestFocus();
                txtPlaca.requestFocusInWindow();
            }
        });

        cmbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        cmbTipo.setBackground(COLOR_ENTRADA);
        cmbTipo.setForeground(COLOR_TEXTO_ENTRADA);
        cmbTipo.setPreferredSize(new Dimension(0, 46));
        cmbTipo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 213, 225)),
                new EmptyBorder(8, 10, 8, 10)));

        tablaActivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaActivos.getSelectionModel().addListSelectionListener(evento -> actualizarSeleccionActivos());
        tablaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaHistorial.getSelectionModel().addListSelectionListener(evento -> actualizarSeleccionHistorial());
        estilizarTabla(tablaActivos);
        estilizarTabla(tablaHistorial);

        txtBitacora.setEditable(false);
        txtBitacora.setLineWrap(true);
        txtBitacora.setWrapStyleWord(true);
        txtBitacora.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBitacora.setBackground(new Color(14, 20, 29));
        txtBitacora.setForeground(COLOR_TEXTO);
        txtBitacora.setCaretColor(COLOR_TEXTO);
        txtBitacora.setBorder(new EmptyBorder(14, 14, 14, 14));
        txtBitacora.setText("La actividad reciente del parqueo aparecera aqui.");

        lblEstadoChip.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEstadoChip.setOpaque(true);
        lblEstadoChip.setBorder(new EmptyBorder(7, 12, 7, 12));

        btnRegistrarSalida.setEnabled(false);
        btnRegistrarSalida.addActionListener(this::registrarSalida);
        btnEliminarHistorial.setEnabled(false);
        btnEliminarHistorial.addActionListener(this::eliminarHistorial);
    }

    private void inicializarVentana() {
        setTitle("Centro de Control de Parqueo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1320, 820));
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                enfocarCampoPlaca();
            }
        });

        JPanel contenedor = new JPanel(new BorderLayout(18, 18));
        contenedor.setBackground(COLOR_FONDO);
        contenedor.setBorder(new EmptyBorder(18, 18, 18, 18));
        setContentPane(contenedor);

        contenedor.add(crearHeroPanel(), BorderLayout.NORTH);
        contenedor.add(crearContenidoPrincipal(), BorderLayout.CENTER);
    }

    private JPanel crearHeroPanel() {
        GradientCardPanel hero = new GradientCardPanel(34, new Color(21, 71, 124), new Color(26, 124, 141), new Color(69, 169, 196));
        hero.setLayout(new BorderLayout(18, 18));
        hero.setBorder(new EmptyBorder(24, 28, 24, 28));

        JPanel izquierda = new JPanel();
        izquierda.setOpaque(false);
        izquierda.setLayout(new BoxLayout(izquierda, BoxLayout.Y_AXIS));

        JLabel badge = crearBadge("CENTRO OPERATIVO");
        JLabel titulo = new JLabel("Parqueo Privado Universidad Latina");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titulo.setForeground(Color.WHITE);

        izquierda.add(badge);
        izquierda.add(Box.createVerticalStrut(14));
        izquierda.add(titulo);

        RoundedPanel derecha = new RoundedPanel(28, new Color(7, 18, 33, 190), new Color(108, 176, 207));
        derecha.setLayout(new GridBagLayout());
        derecha.setBorder(new EmptyBorder(18, 20, 18, 20));
        derecha.setPreferredSize(new Dimension(280, 140));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(2, 0, 2, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel etiquetaReloj = new JLabel("Operacion en tiempo real");
        etiquetaReloj.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        etiquetaReloj.setForeground(new Color(191, 218, 231));

        lblReloj.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblReloj.setForeground(Color.WHITE);

        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblFecha.setForeground(new Color(191, 218, 231));

        JLabel tarifa = crearMiniIndicador("Tarifa oficial  \u20A1500 / hora o fraccion");

        gbc.gridy = 0;
        derecha.add(etiquetaReloj, gbc);
        gbc.gridy = 1;
        derecha.add(lblReloj, gbc);
        gbc.gridy = 2;
        derecha.add(lblFecha, gbc);
        gbc.gridy = 3;
        gbc.insets = new Insets(12, 0, 0, 0);
        derecha.add(tarifa, gbc);

        hero.add(izquierda, BorderLayout.CENTER);
        hero.add(derecha, BorderLayout.EAST);
        return hero;
    }

    private JSplitPane crearContenidoPrincipal() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, crearScrollOperaciones(), crearPanelDashboard());
        splitPane.setDividerLocation(430);
        splitPane.setDividerSize(12);
        splitPane.setBorder(null);
        splitPane.setOpaque(false);
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(0.34);
        return splitPane;
    }

    private JScrollPane crearScrollOperaciones() {
        JScrollPane scrollPane = new JScrollPane(crearPanelOperaciones());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(COLOR_FONDO);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel crearPanelOperaciones() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 14, 0);

        gbc.gridy = 0;
        panel.add(crearTarjetaIngreso(), gbc);

        gbc.gridy = 1;
        panel.add(crearTarjetaSalida(), gbc);

        gbc.gridy = 2;
        panel.add(crearTarjetaHistorial(), gbc);

        gbc.gridy = 3;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(crearTarjetaBitacora(), gbc);

        return panel;
    }

    private JPanel crearPanelDashboard() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);
        panel.add(crearFranjaMetricas(), BorderLayout.NORTH);

        JSplitPane splitTablas = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                crearTarjetaTabla("Vehiculos activos en el parqueo",
                        "Monitorea los ingresos abiertos listos para procesar salida.", lblResumenActivosTabla, tablaActivos),
                crearTarjetaTabla("Historial consolidado",
                        "Consulta las operaciones finalizadas y elimina registros cuando lo necesites.", lblResumenHistorialTabla, tablaHistorial));
        splitTablas.setResizeWeight(0.55);
        splitTablas.setDividerSize(12);
        splitTablas.setBorder(null);
        splitTablas.setOpaque(false);
        panel.add(splitTablas, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearFranjaMetricas() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 14, 0));
        panel.setOpaque(false);
        panel.add(crearTarjetaMetrica("Activos", lblMetricActivos, "Vehiculos pendientes de salida", COLOR_AZUL));
        panel.add(crearTarjetaMetrica("Historial", lblMetricHistorial, "Registros completados guardados", COLOR_VERDE));
        panel.add(crearTarjetaMetrica("Tarifa", crearEtiquetaMetrica("\u20A1500"), "Cobro fijo por hora o fraccion", COLOR_DORADO));
        return panel;
    }

    private RoundedPanel crearTarjetaIngreso() {
        RoundedPanel tarjeta = crearTarjetaBase();
        tarjeta.setLayout(new BorderLayout(0, 16));
        tarjeta.add(crearEncabezadoSeccion("Registrar ingreso", "Captura la placa y el tipo del vehiculo con un flujo rapido."), BorderLayout.NORTH);

        JPanel contenido = new JPanel(new GridBagLayout());
        contenido.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);

        gbc.gridy = 0;
        contenido.add(crearEtiquetaCampo("Placa del vehiculo"), gbc);
        gbc.gridy = 1;
        contenido.add(txtPlaca, gbc);
        gbc.gridy = 2;
        contenido.add(crearEtiquetaCampo("Tipo de vehiculo"), gbc);
        gbc.gridy = 3;
        contenido.add(cmbTipo, gbc);
        gbc.gridy = 4;
        gbc.insets = new Insets(16, 0, 0, 0);
        JButton btnRegistrarEntrada = crearBotonAccion("Registrar entrada", COLOR_AZUL, COLOR_AZUL_HOVER);
        btnRegistrarEntrada.addActionListener(evento -> registrarIngreso());
        contenido.add(btnRegistrarEntrada, gbc);

        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }

    private RoundedPanel crearTarjetaSalida() {
        RoundedPanel tarjeta = crearTarjetaBase();
        tarjeta.setLayout(new BorderLayout(0, 16));
        tarjeta.add(crearEncabezadoSeccion("Registrar salida", "Selecciona un activo y genera el cobro automaticamente."), BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(0, 14));
        contenido.setOpaque(false);
        contenido.add(crearInfoPanel(lblSeleccion, "Selecciona un vehiculo activo desde la tabla de la derecha."), BorderLayout.CENTER);
        contenido.add(btnRegistrarSalida, BorderLayout.SOUTH);

        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }

    private RoundedPanel crearTarjetaHistorial() {
        RoundedPanel tarjeta = crearTarjetaBase();
        tarjeta.setLayout(new BorderLayout(0, 16));
        tarjeta.add(crearEncabezadoSeccion("Gestion del historial", "Mantiene limpio el registro historico eliminando operaciones no deseadas."), BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(0, 14));
        contenido.setOpaque(false);
        contenido.add(crearInfoPanel(lblHistorialSeleccionado, "Selecciona un registro del historial para revisarlo o eliminarlo."), BorderLayout.CENTER);
        contenido.add(btnEliminarHistorial, BorderLayout.SOUTH);

        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }

    private RoundedPanel crearTarjetaBitacora() {
        RoundedPanel tarjeta = crearTarjetaBase();
        tarjeta.setLayout(new BorderLayout(0, 14));

        JPanel encabezado = new JPanel(new BorderLayout(10, 0));
        encabezado.setOpaque(false);
        JPanel textos = crearEncabezadoSeccion("Actividad reciente", "Sigue cada movimiento del sistema en tiempo real.");
        encabezado.add(textos, BorderLayout.CENTER);
        encabezado.add(lblEstadoChip, BorderLayout.EAST);

        tarjeta.add(encabezado, BorderLayout.NORTH);
        tarjeta.add(crearScrollTransparente(txtBitacora), BorderLayout.CENTER);
        return tarjeta;
    }

    private RoundedPanel crearTarjetaTabla(String titulo, String subtitulo, JLabel resumen, JTable tabla) {
        RoundedPanel tarjeta = crearTarjetaBase();
        tarjeta.setLayout(new BorderLayout(0, 14));

        JPanel encabezado = new JPanel(new BorderLayout(16, 0));
        encabezado.setOpaque(false);
        encabezado.add(crearEncabezadoSeccion(titulo, subtitulo), BorderLayout.CENTER);
        resumen.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resumen.setForeground(COLOR_TEXTO_SUAVE);
        resumen.setHorizontalAlignment(SwingConstants.RIGHT);
        encabezado.add(resumen, BorderLayout.EAST);

        tarjeta.add(encabezado, BorderLayout.NORTH);
        JScrollPane scrollTabla = crearScrollTabla(tabla);
        scrollTabla.setPreferredSize(new Dimension(0, 220));
        tarjeta.add(scrollTabla, BorderLayout.CENTER);
        return tarjeta;
    }

    private RoundedPanel crearTarjetaMetrica(String titulo, JLabel valor, String subtitulo, Color acento) {
        RoundedPanel tarjeta = new RoundedPanel(28, COLOR_PANEL, COLOR_BORDE);
        tarjeta.setLayout(new BorderLayout(0, 12));
        tarjeta.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel tituloLabel = new JLabel(titulo.toUpperCase());
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tituloLabel.setForeground(acento);

        valor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valor.setForeground(COLOR_TEXTO);

        JLabel subtituloLabel = new JLabel(subtitulo);
        subtituloLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtituloLabel.setForeground(COLOR_TEXTO_SUAVE);

        tarjeta.add(tituloLabel, BorderLayout.NORTH);
        tarjeta.add(valor, BorderLayout.CENTER);
        tarjeta.add(subtituloLabel, BorderLayout.SOUTH);
        return tarjeta;
    }

    private JPanel crearEncabezadoSeccion(String titulo, String subtitulo) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 21));
        lblTitulo.setForeground(COLOR_TEXTO);

        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(COLOR_TEXTO_SUAVE);

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblSubtitulo);
        return panel;
    }

    private JPanel crearInfoPanel(JLabel etiqueta, String textoInicial) {
        RoundedPanel panel = new RoundedPanel(22, COLOR_PANEL_ALT, COLOR_BORDE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));
        etiqueta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        etiqueta.setForeground(COLOR_TEXTO);
        etiqueta.setVerticalAlignment(SwingConstants.TOP);
        panel.add(etiqueta, BorderLayout.CENTER);
        establecerTextoInfo(etiqueta, textoInicial);
        return panel;
    }

    private RoundedPanel crearTarjetaBase() {
        RoundedPanel tarjeta = new RoundedPanel(30, COLOR_PANEL, COLOR_BORDE);
        tarjeta.setBorder(new EmptyBorder(20, 20, 20, 20));
        return tarjeta;
    }

    private JLabel crearEtiquetaCampo(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        etiqueta.setForeground(COLOR_TEXTO_SUAVE);
        etiqueta.setBorder(new EmptyBorder(0, 2, 0, 0));
        return etiqueta;
    }

    private JLabel crearBadge(String texto) {
        JLabel badge = new JLabel(texto);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setForeground(new Color(225, 244, 255));
        badge.setOpaque(true);
        badge.setBackground(new Color(7, 27, 50, 170));
        badge.setBorder(new EmptyBorder(7, 12, 7, 12));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        return badge;
    }

    private JLabel crearMiniIndicador(String texto) {
        JLabel indicador = new JLabel(texto);
        indicador.setFont(new Font("Segoe UI", Font.BOLD, 12));
        indicador.setForeground(new Color(44, 31, 0));
        indicador.setOpaque(true);
        indicador.setBackground(new Color(252, 226, 90));
        indicador.setBorder(new EmptyBorder(7, 12, 7, 12));
        return indicador;
    }

    private JLabel crearEtiquetaMetrica(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 28));
        etiqueta.setForeground(COLOR_TEXTO);
        return etiqueta;
    }

    private JButton crearBotonAccion(String texto, Color colorBase, Color colorHover) {
        ActionButton boton = new ActionButton(texto, colorBase, colorHover);
        boton.setPreferredSize(new Dimension(0, 46));
        return boton;
    }

    private JScrollPane crearScrollTransparente(Component componente) {
        JScrollPane scrollPane = new JScrollPane(componente);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
        return scrollPane;
    }

    private JScrollPane crearScrollTabla(JTable tabla) {
        JScrollPane scrollPane = crearScrollTransparente(tabla);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setOpaque(false);
        tabla.setRowHeight(34);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setForeground(COLOR_TEXTO);
        tabla.setGridColor(COLOR_BORDE);
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setSelectionBackground(new Color(46, 97, 191));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setFillsViewportHeight(true);

        DashboardTableRenderer renderer = new DashboardTableRenderer();
        for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setOpaque(false);
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new HeaderRenderer());
        header.setPreferredSize(new Dimension(0, 40));
    }

    private void iniciarReloj() {
        actualizarReloj();
        Timer timer = new Timer(1000, evento -> actualizarReloj());
        timer.start();
    }

    private void actualizarReloj() {
        LocalDateTime ahora = LocalDateTime.now();
        lblReloj.setText(FORMATO_RELOJ.format(ahora));
        String fecha = FORMATO_DIA.format(ahora);
        lblFecha.setText(Character.toUpperCase(fecha.charAt(0)) + fecha.substring(1));
    }

    private void registrarIngreso() {
        try {
            RegistroParqueo registro = servicio.registrarIngreso(txtPlaca.getText(), (TipoVehiculo) cmbTipo.getSelectedItem());
            txtPlaca.setText("");
            cmbTipo.setSelectedIndex(0);
            enfocarCampoPlaca();
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

            lblMetricActivos.setText(String.valueOf(activos.size()));
            lblMetricHistorial.setText(String.valueOf(historial.size()));
            lblResumenActivosTabla.setText(activos.isEmpty()
                    ? "Sin vehiculos activos por el momento."
                    : activos.size() == 1 ? "1 vehiculo activo listo para salida."
                    : activos.size() + " vehiculos activos listos para salida.");
            lblResumenHistorialTabla.setText(historial.isEmpty()
                    ? "Historial vacio."
                    : historial.size() == 1 ? "1 operacion registrada en historial."
                    : historial.size() + " operaciones registradas en historial.");

            actualizarSeleccionActivos();
            actualizarSeleccionHistorial();
        } catch (ParqueoException ex) {
            mostrarMensaje(ex.getMessage(), true);
        }
    }

    private void actualizarSeleccionActivos() {
        RegistroParqueo seleccionado = modeloActivos.getRegistroEn(tablaActivos.getSelectedRow());
        if (seleccionado == null) {
            establecerTextoInfo(lblSeleccion, "Selecciona un vehiculo activo para visualizar su informacion y procesar la salida.");
            btnRegistrarSalida.setEnabled(false);
            return;
        }
        btnRegistrarSalida.setEnabled(true);
        establecerTextoInfo(lblSeleccion, "Placa: " + seleccionado.getPlaca()
                + "<br>Tipo: " + seleccionado.getTipoVehiculo().getDescripcion()
                + "<br>Entrada: " + FORMATO_FECHA.format(seleccionado.getHoraEntrada()));
    }

    private void actualizarSeleccionHistorial() {
        RegistroParqueo seleccionado = modeloHistorial.getRegistroEn(tablaHistorial.getSelectedRow());
        if (seleccionado == null) {
            establecerTextoInfo(lblHistorialSeleccionado, "Selecciona un registro historico para revisar su salida o eliminarlo del listado.");
            btnEliminarHistorial.setEnabled(false);
            return;
        }
        btnEliminarHistorial.setEnabled(true);
        establecerTextoInfo(lblHistorialSeleccionado, "Placa: " + seleccionado.getPlaca()
                + "<br>Salida: " + FORMATO_FECHA.format(seleccionado.getHoraSalida())
                + "<br>Cobro: " + formatearMonto(seleccionado.getMontoCobrado()));
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        if (!txtBitacora.getText().isBlank()) {
            txtBitacora.append(System.lineSeparator() + System.lineSeparator());
        }
        txtBitacora.append(mensaje);
        txtBitacora.setCaretPosition(txtBitacora.getDocument().getLength());

        if (esError) {
            lblEstadoChip.setText("Atencion");
            lblEstadoChip.setBackground(new Color(91, 27, 27));
            lblEstadoChip.setForeground(new Color(255, 183, 183));
        } else {
            lblEstadoChip.setText("Operacion exitosa");
            lblEstadoChip.setBackground(new Color(18, 74, 52));
            lblEstadoChip.setForeground(new Color(187, 247, 208));
        }
    }

    private void establecerTextoInfo(JLabel etiqueta, String contenidoHtml) {
        etiqueta.setText("<html><div style='width:270px; line-height:1.5;'>" + contenidoHtml + "</div></html>");
    }

    private String formatearMonto(long monto) {
        return "\u20A1" + FORMATO_MONTO.format(monto);
    }

    private void enfocarCampoPlaca() {
        txtPlaca.requestFocus();
        txtPlaca.requestFocusInWindow();
        txtPlaca.selectAll();
    }

    private static class RoundedPanel extends JPanel {
        private final int radio;
        private final Color fondo;
        private final Color borde;

        RoundedPanel(int radio, Color fondo, Color borde) {
            this.radio = radio;
            this.fondo = fondo;
            this.borde = borde;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 55));
            g2.fillRoundRect(3, 5, getWidth() - 6, getHeight() - 8, radio, radio);
            g2.setColor(fondo);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 5, radio, radio);
            g2.setColor(borde);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 5, radio, radio);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class GradientCardPanel extends JPanel {
        private final int radio;
        private final Color inicio;
        private final Color fin;
        private final Color borde;

        GradientCardPanel(int radio, Color inicio, Color fin, Color borde) {
            this.radio = radio;
            this.inicio = inicio;
            this.fin = fin;
            this.borde = borde;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 65));
            g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 10, radio, radio);
            g2.setPaint(new GradientPaint(0, 0, inicio, getWidth(), getHeight(), fin));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 6, radio, radio);
            g2.setColor(borde);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 6, radio, radio);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class ActionButton extends JButton {
        private final Color base;
        private final Color hover;
        private final Color disabledBase = new Color(77, 87, 103);
        private final Color textoActivo = Color.WHITE;
        private final Color textoDeshabilitado = new Color(191, 198, 208);

        ActionButton(String texto, Color base, Color hover) {
            super(texto);
            this.base = base;
            this.hover = hover;
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color fondo = !isEnabled() ? disabledBase
                    : getModel().isPressed() ? hover.darker()
                    : getModel().isRollover() ? hover : base;

            g2.setColor(fondo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.setColor(new Color(255, 255, 255, 40));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            g2.dispose();

            setForeground(isEnabled() ? textoActivo : textoDeshabilitado);
            super.paintComponent(g);
        }
    }

    private static class DashboardTableRenderer extends DefaultTableCellRenderer {
        DashboardTableRenderer() {
            setOpaque(true);
            setBorder(new EmptyBorder(0, 12, 0, 12));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
            setForeground(Color.WHITE);
            setBackground(row % 2 == 0 ? COLOR_PANEL_ALT : COLOR_PANEL_ALT_2);
            if (isSelected) {
                setBackground(new Color(46, 97, 191));
                setForeground(Color.WHITE);
            }
            setHorizontalAlignment(value != null && value.toString().startsWith("\u20A1")
                    ? SwingConstants.RIGHT : SwingConstants.LEFT);
            return this;
        }
    }

    private static class HeaderRenderer extends DefaultTableCellRenderer {
        HeaderRenderer() {
            setOpaque(true);
            setBorder(new EmptyBorder(0, 12, 0, 12));
            setFont(new Font("Segoe UI", Font.BOLD, 13));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBackground(new Color(34, 44, 60));
            setForeground(Color.WHITE);
            return this;
        }
    }
}
