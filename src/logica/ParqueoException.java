package logica;

public class ParqueoException extends Exception {
    public ParqueoException(String mensaje) {
        super(mensaje);
    }

    public ParqueoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
