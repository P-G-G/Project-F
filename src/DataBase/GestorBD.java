package DataBase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import Utils.Utils;

public class GestorBD {

    private final static String NOMBRE_CARPETA_DOCUMENTOS = "Documentos";
    private File documentos;

    private final BD bd;

    public GestorBD() {
        bd = new BD();

        // *** Carpeta documentos ***
        documentos = new File(NOMBRE_CARPETA_DOCUMENTOS);

        // 2. Crear la carpeta si no existe
        if (!documentos.exists()) {
            // mkdirs() crea la carpeta
            if (documentos.mkdir()) {
                System.out.println("Carpeta '" + NOMBRE_CARPETA_DOCUMENTOS + "' creada con éxito en el proyecto.");
            }
        }
    }

    public void insertarFamiliar(Familiar familiar) throws SQLException {
        bd.ejecutarSQL("INSERT INTO " + BD.TABLA_FAMILIA + " (dni, nombre) VALUES (?, ?);", 
                        familiar.toArray());
        System.out.println("Familiar registrado con éxito.");
    }

    public boolean insertarArchivo(Archivo archivo) throws SQLException {
        Path archivoRuta = Path.of(archivo.ruta());
        String hash = Utils.calcularHash(archivoRuta);

        Path archivoRutaNueva = documentos.toPath().resolve(archivo.hash());

        bd.ejecutarSinConfirmarSQL("INSERT INTO " + BD.TABLA_ARCHIVOS + 
        " (nombre, tipo, ruta, hash, fecha, familiar) VALUES (?, ?, ?, ?, ?, ?);",
                    archivo.nombre(), archivo.tipo(), archivoRutaNueva.toString(), 
                    hash, archivo.fecha(), archivo.dni());
        try {
            // Movemos el archivo
            Files.move(archivoRuta, archivoRutaNueva);
            System.out.println("Fichero movido con éxito");
            bd.confirmarSQL();
            return true;
        } catch (IOException e) {
            System.err.println("Error al mover el archivo al proyecto");
            System.err.println(e.getMessage());
            System.out.println("Deshaciendo operación en la base de datos");
            bd.deshacerSQL();
            return false;
        }
    }

    public void insertarTipo(String tipo) throws SQLException {
        bd.ejecutarSQL("INSERT INTO " + BD.TABLA_TIPOS + " (nombre) VALUES (?);", tipo);
        System.out.println("Tipo registrado con éxito.");
    }

    public void eliminarTipo(String tipo) throws SQLException {
        bd.ejecutarSQL("DELETE FROM " + BD.TABLA_TIPOS + " WHERE nombre = (?);", tipo);
    }

    public void eliminarFamiliar(Familiar familiar) throws SQLException {
        bd.ejecutarSQL("DELETE FROM " + BD.TABLA_FAMILIA + " WHERE dni = (?);", familiar.dni());
    }

    public void eliminarArchivo(Archivo archivo) throws SQLException {
        bd.ejecutarSQL("DELETE FROM " + BD.TABLA_ARCHIVOS + " WHERE hash = (?);", archivo.hash());
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
    
    public Archivo[] getArchivos() {
        Archivo[] archivos = null;
        try {
            archivos = bd.seleccionarSQL("SELECT * FROM " + BD.TABLA_ARCHIVOS,
                    rs -> new Archivo(rs.getString("nombre"), 
                                      rs.getString("tipo"),
                                      rs.getString("hash"),
                                      rs.getString("ruta"),
                                      rs.getString("fecha"),
                                      rs.getString("familiar")))
                                      .toArray(new Archivo[0]);
        } catch (SQLException e) {
            System.err.println("Error al intentar seleccionar todos los tipos");
            System.err.println(e.getMessage());
        }

        return archivos;
    }
}
