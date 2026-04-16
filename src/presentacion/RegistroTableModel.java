package presentacion;

import entidades.RegistroParqueo;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class RegistroTableModel extends AbstractTableModel {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String[] COLUMNAS_ACTIVOS = {"Placa", "Tipo", "Hora de entrada"};
    private List<RegistroParqueo> registros;

    public RegistroTableModel() {
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
        return COLUMNAS_ACTIVOS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNAS_ACTIVOS[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RegistroParqueo registro = registros.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> registro.getPlaca();
            case 1 -> registro.getTipoVehiculo().getDescripcion();
            case 2 -> FORMATO_FECHA.format(registro.getHoraEntrada());
            default -> "";
        };
    }
}
