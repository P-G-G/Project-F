package DataBase;

public record Archivo(String nombre, String tipo, String hash, String ruta, String fecha, String dni) { 
    public String toString() {
        return nombre;  // CHECK nombre o ruta?
    }
}
