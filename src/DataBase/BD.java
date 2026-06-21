package DataBase;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;

public class BD {
    private static final String BD_URL = "jdbc:sqlite:BD.db";
    private static final int NUM_TABLES = 3;

    private Connection conexion;

    public BD() {
        // Nos conectamos con la base de datos mediante SQLite
        try (Connection conexion = DriverManager.getConnection(BD_URL)) {
            System.out.println("Se ha establecido conexión con la base de datos");
            conexion.setAutoCommit(false);

            this.conexion = conexion;

             System.out.println("Creando tablas..."); // DEBUG
            String[] createTables = getCreateTables();

            System.out.println("Ejecutando sentencias..."); // DEBUG
            if(!ejecutarSQL(createTables)){
                System.err.println("Error ejecutando las sentencias para inicializar la base de datos");
                System.exit(1); // CHECK
            }
            System.out.println("Se ha terminado de preparar la base de datos");
        } catch (Exception e) {
            System.err.println(
                "Error al intentar conectarse a la base de datos de la aplicación: " + e.getMessage());
            System.exit(1);
        }
    }

    private String[] getCreateTables() {
        String[] createTables = new String[NUM_TABLES];
        // Sentencias CREATE TABLE de la BD
        createTables[0] = "CREATE TABLE IF NOT EXISTS familiares ("
                            + "dni TEXT PRIMARY KEY,"
                            + "nombre TEXT NOT NULL"
                            + ");";

        createTables[1] = "CREATE TABLE IF NOT EXISTS tipos ("
                            + "nombre TEXT PRIMARY KEY"
                            + ");";

        createTables[2] = "CREATE TABLE IF NOT EXISTS archivos ("
                            + "nombre TEXT PRIMARY KEY,"
                            + "tipo TEXT,"
                            + "ruta TEXT NOT NULL,"
                            + "fecha TEXT NULL,"
                            + "familiar_dni TEXT,"
                            + "FOREIGN KEY (tipo) REFERENCES tipos(nombre),"
                            + "FOREIGN KEY (familiar_dni) REFERENCES familiares(dni)"
                            + ");";

        return createTables;
    }

    public void insertarFamiliar(Familiar f) throws SQLException, SQLTimeoutException {
        String insert = "INSERT INTO familiares (dni, nombre) VALUES (?, ?);";
        ejecutarSQL(insert, f.toArray());
        System.out.println("Familiar registrado con éxito.");
    }

    public void insertarFamiliar(String dni, String nombre) throws SQLException, SQLTimeoutException {
        String insert = "INSERT INTO familiares (dni, nombre) VALUES (?, ?);";
        ejecutarSQL(insert, dni, nombre);
        System.out.println("Familiar registrado con éxito.");
    }

    public void insertarTipo(String tipo) throws SQLException, SQLTimeoutException {
        String insert = "INSERT INTO tipos (nombre) VALUES (?);";
        ejecutarSQL(insert, tipo);
        System.out.println("Tipo registrado con éxito.");
    }

    public void insertarArchivo(Archivo a) throws SQLException, SQLTimeoutException {
        String insert = "INSERT INTO archivos (nombre, tipo, ruta, fecha, familiar) VALUES (?, ?, ?, ?, ?);";
        ejecutarSQL(insert, a.toArray());
    }

    public void insertarArchivo(String nombre, String tipo, String ruta, String fecha, String dni_familiar) throws SQLException, SQLTimeoutException {
        String insert = "INSERT INTO archivos (nombre, tipo, ruta, fecha, familiar) VALUES (?, ?, ?, ?, ?);";
        ejecutarSQL(insert, nombre, tipo, ruta, fecha, dni_familiar);
        System.out.println("Archivo registrado con éxito.");
    }

    /**
     * Ejecuta cualquier sentencia SQL: DML y DDL
     * @param sql La sentencia SQL con interrogantes (?).
     * @param parametros Los valores para rellenar los interrogantes (separados por comas).
     */
    public void ejecutarSQL(String sql, Object... parametros) throws SQLException, SQLTimeoutException {
        PreparedStatement statement = conexion.prepareStatement(sql);
            
        // Recorre cada parámetro y lo inyecta en su '?' correspondiente
        for (int i = 0; i < parametros.length; i++) {
            // Usamos setObject para que Java decida automáticamente si es String, int, etc.
            statement.setObject(i + 1, parametros[i]); 
        }
        
        statement.executeUpdate();
        conexion.commit();
    }

    /**
     * Ejecuta sentencias SQL DDL en un mismo statement
     * @param sentencias Las sentencias SQL a ejecutar sin parámetros.
     */
    public boolean ejecutarSQL(String[] sentencias) {
        boolean ejecutado = false;
        String sentencia_actual = "";
        try (Statement orden = conexion.createStatement()) {

            for (String sentencia : sentencias){
                sentencia_actual = sentencia;
                orden.addBatch(sentencia);
            }

            orden.executeBatch();

            conexion.commit();
            ejecutado = true;
        } catch (BatchUpdateException e) {
            System.err.println("Error la sentencia no se ha ejecutado correctamente:\n" + sentencia_actual);
            System.err.println("Información del error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al intentar ejecutar sentencias en la base de datos: " + e.getMessage());

            try {
                conexion.rollback();
                System.out.println("Sentencias deshechas por seguridad");
            } catch (SQLException ex) {
                System.err.println("Error al intentar hacer el rollback de las sentencias " + ex.getMessage());
                throw new RuntimeException("¿Conexión perdida al intentar hacer el rollback?");
            }
        }
        
        return ejecutado;
    }
}
