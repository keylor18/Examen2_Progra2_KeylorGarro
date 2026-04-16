package entidades;

public enum TipoVehiculo {
    CARRO("Carro"),
    MOTO("Moto");

    private final String descripcion;

    TipoVehiculo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoVehiculo desdeTexto(String valor) {
        if (valor == null) {
            return null;
        }
        for (TipoVehiculo tipo : values()) {
            if (tipo.descripcion.equalsIgnoreCase(valor.trim())
                    || tipo.name().equalsIgnoreCase(valor.trim())) {
                return tipo;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
