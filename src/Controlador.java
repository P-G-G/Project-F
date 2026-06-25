import java.sql.SQLException;

import DataBase.Archivo;
import DataBase.Familiar;
import DataBase.GestorBD;
import UI.Ventana;

public class Controlador {
    private GestorBD gestor;
    private Ventana ui;

    public Controlador(GestorBD gestor, Ventana ui) {
        this.gestor = gestor;
        this.ui = ui;

        ui.setAccionGuardarFamiliar(e -> guardarFamiliar());
        ui.setAccionGuardarTipo(e -> guardarTipo());
        ui.setAccionGuardarArchivo(e -> guardarArchivo());

        ui.setAccionPedirFamilia(e -> pedirFamilia());
        ui.setAccionPedirTipos(e -> pedirTipos());
        ui.setAccionPedirArchivos(e -> pedirArchivos());

        ui.setAccionBorrarFamiliar(e -> borrarFamiliar());
        ui.setAccionBorrarTipo(e -> borrarTipo());
        ui.setAccionBorrarArchivos(e -> borrarArchivos());
    }

    private void borrarArchivos() {
        for (Archivo archivo : ui.getArchivosBorrados()) {
            try {
                    gestor.eliminarArchivo(archivo);
                    System.out.println("Archivo eliminado con éxito");
            } catch (SQLException e) {
                System.err.println("Error al intentar eliminar el archivo: " + e.getMessage());
            }
        }
        
        pedirArchivos();
    }

    private void borrarTipo() {
        try {
            gestor.eliminarTipo(ui.getTipoBorrado());
            System.out.println("Tipo eliminado con éxito");
        } catch (SQLException e) {
            System.err.println("Error al intentar eliminar el tipo de archivo: " + e.getMessage());
        }

        pedirTipos();
    }

    private void borrarFamiliar() {
        try {
            gestor.eliminarFamiliar(ui.getFamiliarBorrado());
            System.out.println("Familiar eliminado con éxito");
        } catch (SQLException e) {
            System.err.println("Error al intentar eliminar el familiar: " + e.getMessage());
        }

        pedirFamilia();
    }

    public void guardarFamiliar() {
        Familiar familiar = ui.getFamiliarGuardado();
        try {
            gestor.insertarFamiliar(familiar);
        } catch (SQLException e) {
            System.err.println("Error al intentar guardar el familiar en la base de datos");
            System.err.println(e.getMessage());
        }
    }

    public void guardarTipo() {
        String tipo = ui.getTipoGuardado();
        try {
            gestor.insertarTipo(tipo);
        } catch (SQLException e) {
            System.err.println("Error al intentar guardar el tipo en la base de datos");
            System.err.println(e.getMessage());
        }
    }

    public void guardarArchivo() {
        Archivo archivo = ui.getArchivoGuardado();

        try {
            if (gestor.insertarArchivo(archivo)) {  // BUG puede ser el mismo archivo pero con distinto nombre
                System.out.println("Archivo guardado con éxito");
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar guardar el archivo en la base de datos");
            System.err.println(e.getMessage());
        }
    }

    public void pedirFamilia() {
        ui.setFamilia(gestor.getFamilia());
    }

    public void pedirTipos() {
        ui.setTipos(gestor.getTipos());
    }

    public void pedirArchivos() {
        ui.setArchivos(gestor.getArchivos());
    }
}
