package entidades;

import java.time.LocalDateTime;

public class RegistroParqueo {
    private final String placa;
    private final TipoVehiculo tipoVehiculo;
    private final LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;
    private long montoCobrado;

    public RegistroParqueo(String placa, TipoVehiculo tipoVehiculo, LocalDateTime horaEntrada) {
        this(placa, tipoVehiculo, horaEntrada, null, 0L);
    }

    public RegistroParqueo(String placa, TipoVehiculo tipoVehiculo, LocalDateTime horaEntrada,
            LocalDateTime horaSalida, long montoCobrado) {
        this.placa = placa;
        this.tipoVehiculo = tipoVehiculo;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.montoCobrado = montoCobrado;
    }

    public String getPlaca() {
        return placa;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public long getMontoCobrado() {
        return montoCobrado;
    }

    public boolean estaActivo() {
        return horaSalida == null;
    }

    public void registrarSalida(LocalDateTime salida, long monto) {
        this.horaSalida = salida;
        this.montoCobrado = monto;
    }
}
