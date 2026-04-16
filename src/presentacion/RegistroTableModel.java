package presentacion;

import entidades.RegistroParqueo;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;

public class RegistroTableModel extends AbstractTableModel {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final NumberFormat FORMATO_MONTO = NumberFormat.getIntegerInstance(Locale.US);
    private static final String[] COLUMNAS_ACTIVOS = {"Placa", "Tipo", "Hora de entrada"};
    private static final String[] COLUMNAS_HISTORIAL = {"Placa", "Tipo", "Hora de entrada", "Hora de salida", "Monto"};
    private final VistaTabla vista;
    private List<RegistroParqueo> registros;

    public RegistroTableModel(VistaTabla vista) {
        this.vista = vista;
        this.registros = new ArrayList<>();
    }

    public void setRegistros(List<RegistroParqueo> registros) {
        this.registros = new ArrayList<>(registros);
        fireTableDataChanged();
    }

    public RegistroParqueo getRegistroEn(int fila) {
        if (fila < 0 || fila >= registros.size()) {
            return null;
        }
        return registros.get(fila);
    }

    @Override
    public int getRowCount() {
        return registros.size();
    }

    @Override
    public int getColumnCount() {
        return vista == VistaTabla.ACTIVOS ? COLUMNAS_ACTIVOS.length : COLUMNAS_HISTORIAL.length;
    }

    @Override
    public String getColumnName(int column) {
        return vista == VistaTabla.ACTIVOS ? COLUMNAS_ACTIVOS[column] : COLUMNAS_HISTORIAL[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RegistroParqueo registro = registros.get(rowIndex);
        if (vista == VistaTabla.ACTIVOS) {
            return switch (columnIndex) {
                case 0 -> registro.getPlaca();
                case 1 -> registro.getTipoVehiculo().getDescripcion();
                case 2 -> FORMATO_FECHA.format(registro.getHoraEntrada());
                default -> "";
            };
        }
        return switch (columnIndex) {
            case 0 -> registro.getPlaca();
            case 1 -> registro.getTipoVehiculo().getDescripcion();
            case 2 -> FORMATO_FECHA.format(registro.getHoraEntrada());
            case 3 -> registro.getHoraSalida() == null ? "" : FORMATO_FECHA.format(registro.getHoraSalida());
            case 4 -> "₡" + FORMATO_MONTO.format(registro.getMontoCobrado());
            default -> "";
        };
    }

    public enum VistaTabla {
        ACTIVOS,
        HISTORIAL
    }
}
