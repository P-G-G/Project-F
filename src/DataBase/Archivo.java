package DataBase;
public record Archivo(String nombre, String tipo, String ruta, String fecha, String dni_familiar) {
    public Object[] toArray() {
        return new Object[]{nombre, tipo, ruta, fecha, dni_familiar};
    }
}
