package DataBase;

import java.sql.SQLException;
import java.sql.SQLTimeoutException;

public class GestorBD {
    private final BD bd;

    public GestorBD() {
        bd = new BD();
    }

    public void insertarFamiliar(Familiar familiar) throws SQLTimeoutException, SQLException {
        bd.ejecutarSQL("INSERT INTO " + BD.TABLA_FAMILIA + " (dni, nombre) VALUES (?, ?);", 
                        familiar.toArray());
        System.out.println("Familiar registrado con éxito.");
    }

    public void insertarArchivo(Archivo archivo) throws SQLTimeoutException, SQLException {
        bd.ejecutarSQL("INSERT INTO " + BD.TABLA_ARCHIVOS + " (nombre, tipo, ruta, fecha, familiar) VALUES (?, ?, ?, ?, ?);",
                     archivo.toArray());
    }

    public void insertarTipo(String tipo) throws SQLTimeoutException, SQLException {
        bd.ejecutarSQL("INSERT INTO " + BD.TABLA_TIPOS + " (nombre) VALUES (?);", tipo);
        System.out.println("Tipo registrado con éxito.");
    }

    public Familiar[] getFamilia() {
        Familiar[] familia = null;
        try {
            familia = bd.seleccionarSQL("SELECT * FROM " + BD.TABLA_FAMILIA,
                    rs -> new Familiar(rs.getString("dni"), rs.getString("nombre")))
                    .toArray(new Familiar[0]);
        } catch (SQLException e) {
            System.err.println("Error al intentar seleccionar a todos los familiares");
        }

        return familia;
    }

    public String[] getFamiliaString() {
        String[] familia = null;
        try {
            familia = bd.seleccionarSQL("SELECT * FROM " + BD.TABLA_FAMILIA,
                    rs -> "[" + rs.getString("dni") + "] " + rs.getString("nombre"))
                    .toArray(new String[0]);
        } catch (SQLException e) {
            System.err.println("Error al intentar seleccionar a todos los familiares en String");
            System.err.println(e.getMessage());
        }

        return familia;
    }

    public String[] getTipos() {
        String[] tipos = null;
        try {
            tipos = bd.seleccionarSQL("SELECT * FROM " + BD.TABLA_TIPOS,
                    rs -> rs.getString("nombre"))
                    .toArray(new String[0]);
        } catch (SQLException e) {
            System.err.println("Error al intentar seleccionar todos los tipos");
            System.err.println(e.getMessage());
        }

        return tipos;
    }     
}
