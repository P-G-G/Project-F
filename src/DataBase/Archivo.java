package DataBase;

public record Archivo(String nombre, String tipo, String hash, String ruta, String fecha, String dni) {
    private static final String SEP = "               ";
    
    public String toString() {
        // CHECK
        return nombre + SEP + tipo + SEP + fecha;
    }
}
