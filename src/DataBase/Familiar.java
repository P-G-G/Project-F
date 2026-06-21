package DataBase;
public record Familiar(String dni, String nombre) {
    public Object[] toArray() {
        return new Object[]{dni, nombre};
    }
}
