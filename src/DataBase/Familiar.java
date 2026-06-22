package DataBase;
public record Familiar(String dni, String nombre) {
    public Object[] toArray() {
        return new Object[]{dni, nombre};
    }

    @Override
    public String toString() {
        return "[" + dni + "] " + nombre; 
    }

    public static Familiar fromString(String familiar) {
        // Extraemos el familiar (ej: "[12345678A] Pablo")
        int sep = familiar.indexOf("]");
        return new Familiar(familiar.substring(1, sep), familiar.substring(sep + 2));
    }
}
