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

import java.util.logging.Logger;

public class BD {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private static final String BD_URL = "jdbc:sqlite:.db";     // Base de datos oculta al usuario

    public static final String TABLA_FAMILIA = "familia";
    public static final String TABLA_ARCHIVOS = "archivos";
    public static final String TABLA_TIPOS = "tipos";

    private static final int NUM_TABLES = 3;

    private Connection conexion;

    public BD() throws SQLException {

        // Nos intentamos conectar con la base de datos mediante SQLite
        conexion = DriverManager.getConnection(BD_URL);     // Throws SQL exceptions
        logger.info("Se ha establecido conexión con la base de datos");

        // Desactivamos auto commit
        conexion.setAutoCommit(false);          // Throws SQL exceptions
        logger.fine("Auto commit desactivado");

        // Activamos las foreign keys para las relaciones
        Statement statement = conexion.createStatement();   // Throws SQL exceptions
        statement.execute("PRAGMA foreign_keys = ON;");
        logger.fine("Foreign keys activadas");


        // Inicializamos las tablas si no existen todavia
        String[] createTables = getCreateTables();
        if(ejecutarSQL(createTables) == false){
            logger.severe("Error ejecutando las sentencias para inicializar la base de datos");
            throw new SQLException();                       // Throws SQL exceptions
        }
        logger.fine("Tablas inicializadas");

        logger.info("Se ha terminado de preparar la base de datos");
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
                            + "ruta TEXT NOT NULL,"
                            + "hash TEXT PRIMARY KEY,"
                            + "fecha TEXT NOT NULL,"    // CHECK hacer que fecha sea obligatoria?
                            + "familiar TEXT,"
                            + "FOREIGN KEY (tipo) REFERENCES tipos(nombre),"
                            + "FOREIGN KEY (familiar) REFERENCES familiares(dni)"
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
            logger.warning("Error la sentencia no se ha ejecutado correctamente:\n" + sentencia_actual);
            logger.warning("Información del error: " + e.getMessage());
        } catch (Exception e) {
            logger.warning("Error al intentar ejecutar sentencias en la base de datos: " + e.getMessage());

            try {
                conexion.rollback();
                logger.info("Sentencias deshechas por seguridad");
            } catch (SQLException ex) {
                logger.severe("Error al intentar hacer el rollback de las sentencias " + ex.getMessage());
                // CHECK ¿ Qué hacer ?
                // logger.info("Intentando cerrar la conexión");
                //     cerrarConexion();
            }
        }
        
        return ejecutado;
    }

    public void cerrarConexion() {
        try {
            conexion.close();
            logger.info("Conexión de la base de datos cerrada correctamente");
        } catch (SQLException e) {
            logger.warning("No se pudo cerrar la conexión con la base de datos");
        }
    }

    /**
     * Selecciona a partir de un comando SQL SELECT con los parámetros
     * y la información según el mapeador indicado
     * @param <T>
     * @param sql
     * @param mapeador
     * @param parametros
     * @return
     * @throws SQLException
     */
    public <T> List<T> seleccionarSQL(String sql, MapeadorFila<T> mapeador, Object... parametros) throws SQLException {
        List<T> resultados = new LinkedList<>();

        //  Envolvemos el PreparedStatement
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            for (int i = 0; i < parametros.length; i++) {
                pstmt.setObject(i + 1, parametros[i]);
            }

            // Envolvemos el ResultSet
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    resultados.add(mapeador.mapear(rs));
                }
            }
            
        }

        return resultados;
    }

}
