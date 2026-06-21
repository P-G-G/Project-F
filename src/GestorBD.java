import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class GestorBD {
    private static final String BD_URL = "jdbc:sqlite:BD.db";
    private Connection conexion;

    public GestorBD() {
        // 1. Abrimos la conexión global
        try (Connection conexion = DriverManager.getConnection(BD_URL)) {
            System.out.println("Se ha establecido conexión con la base de datos");

            this.conexion = conexion;

            System.out.println("Inicializando..."); // DEBUG
            // 2. Estructuramos la base de datos aquí
            inicializar();
            System.out.println("Se ha inicializado correctamente"); // DEBUG

        } catch (Exception e) {
            System.err.println(
                "Error al intentar conectarse a la base de datos de la aplicación: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private void inicializar(){

        // Sentencias CREATE TABLE de la BD
        String crearFamiliares = "CREATE TABLE IF NOT EXISTS familiares ("
                                + "dni TEXT PRIMARY KEY,"
                                + "nombre TEXT NOT NULL"
                                + ");";

        String crearArchivos = "CREATE TABLE IF NOT EXISTS archivos ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "tipo TEXT,"
                            + "ruta TEXT,"
                            + "fecha TEXT,"
                            + "familiar_dni TEXT,"
                            + "FOREIGN KEY (familiar_dni) REFERENCES familiares(dni)"
                            + ");";

        // Ejecutar los Creates
        ejecutarSQL(crearFamiliares);
        ejecutarSQL(crearArchivos);
    }

    /**
     * Ejecuta cualquier sentencia SQL DML y DDL
     * @param sql La sentencia SQL con interrogantes (?).
     * @param parametros Los valores para rellenar los interrogantes (separados por comas).
     */
    public boolean ejecutarSQL(String sql, Object... parametros) {
        boolean ejecutado = true;
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            
            // Recorre cada parámetro y lo inyecta en su '?' correspondiente
            for (int i = 0; i < parametros.length; i++) {
                // Usamos setObject para que Java decida automáticamente si es String, int, etc.
                statement.setObject(i + 1, parametros[i]); 
            }
            
            statement.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("Error al ejecutar [" + sql + "]: " + e.getMessage());
            ejecutado = false;
        }

        return ejecutado;
    }
}
