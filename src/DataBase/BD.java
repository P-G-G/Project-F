package DataBase;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.LinkedList;
import java.util.List;

public class BD {
    private static final String BD_URL = "jdbc:sqlite:BD.db";
    public static final String TABLA_FAMILIA = "familia";
    public static final String TABLA_ARCHIVOS = "archivos";
    public static final String TABLA_TIPOS = "tipos";
    private static final int NUM_TABLES = 3;

    private Connection conexion;

    public BD() {
        // Nos conectamos con la base de datos mediante SQLite
        try {
            this.conexion = DriverManager.getConnection(BD_URL);
            System.out.println("Se ha establecido conexión con la base de datos");
            conexion.setAutoCommit(false);

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
        createTables[0] = "CREATE TABLE IF NOT EXISTS " + TABLA_FAMILIA + " ("
                            + "dni TEXT PRIMARY KEY,"
                            + "nombre TEXT NOT NULL"
                            + ");";

        createTables[1] = "CREATE TABLE IF NOT EXISTS " + TABLA_TIPOS + " ("
                            + "nombre TEXT PRIMARY KEY"
                            + ");";

        createTables[2] = "CREATE TABLE IF NOT EXISTS " + TABLA_ARCHIVOS + " ("
                            + "nombre TEXT NOT NULL,"
                            + "tipo TEXT,"
                            + "ruta TEXT PRIMARY KEY,"
                            + "fecha TEXT NULL,"
                            + "familiar TEXT,"
                            + "FOREIGN KEY (tipo) REFERENCES tipos(nombre),"
                            + "FOREIGN KEY (familiar_dni) REFERENCES familiares(dni)"
                            + ");";

        return createTables;
    }

    /**
     * Ejecuta cualquier sentencia SQL: DML y DDL
     * @param sql La sentencia SQL con interrogantes (?).
     * @param parametros Los valores para rellenar los interrogantes (separados por comas).
     */
    public void ejecutarSQL(String sql, Object... parametros) throws SQLException {
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                
            // Recorre cada parámetro y lo inyecta en su '?' correspondiente
            for (int i = 0; i < parametros.length; i++) {
                // Usamos setObject para que Java decida automáticamente si es String, int, etc.
                statement.setObject(i + 1, parametros[i]); 
            }
            
            statement.executeUpdate();
            conexion.commit();
        }
    }

    public void ejecutarSinConfirmarSQL(String sql, Object... parametros) throws SQLException {
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                
            // Recorre cada parámetro y lo inyecta en su '?' correspondiente
            for (int i = 0; i < parametros.length; i++) {
                // Usamos setObject para que Java decida automáticamente si es String, int, etc.
                statement.setObject(i + 1, parametros[i]); 
            }
            
            statement.executeUpdate();
        }
    }

    public void confirmarSQL() throws SQLException {
        conexion.commit();
    }

    public void deshacerSQL() throws SQLException {
        conexion.rollback();
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

    /**
     * TODO
     * @param <T>
     * @param sql
     * @param mapeador
     * @param parametros
     * @return
     * @throws SQLException
     */
    public <T> List<T> seleccionarSQL(String sql, MapeadorFila<T> mapeador, Object... parametros) throws SQLException {
    List<T> resultados = new LinkedList<>();

    // 1. Envolvemos SOLO el PreparedStatement
    try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {

        for (int i = 0; i < parametros.length; i++) {
            pstmt.setObject(i + 1, parametros[i]);
        }

        // 2. Envolvemos SOLO el ResultSet
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                resultados.add(mapeador.mapear(rs));
            }
        }
        
    }

    return resultados;
}
}
