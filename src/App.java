import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class App {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:medicos.db";

        // Conexión
        try (Connection conexion = DriverManager.getConnection(url)) {
            System.out.println("¡Conexión a SQLite establecida!");

            String sqlCrearTabla = "CREATE TABLE IF NOT EXISTS analiticas ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "nombre TEXT NOT NULL,"
                    + "fecha TEXT NOT NULL"
                    + ");";
            
            try (Statement orden = conexion.createStatement()) {
                orden.execute(sqlCrearTabla);
                System.out.println("Tabla verificada o creada.");
            }

            // ... (resto de tu código: Insertar y Buscar) ...

        } catch (Exception e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        }
    }
}