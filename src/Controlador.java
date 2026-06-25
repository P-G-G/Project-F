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

        // TODO acciones de borrado

        ui.setAccionGuardarFamiliar(e -> guardarFamiliar());
        ui.setAccionGuardarTipo(e -> guardarTipo());
        ui.setAccionGuardarArchivo(e -> guardarArchivo());

        ui.setAccionPedirFamilia(e -> pedirFamilia());
        ui.setAccionPedirTipos(e -> pedirTipos());
        ui.setAccionPedirArchivos(e -> pedirArchivos());

        ui.setAccionBorrarFamiliar(e -> borrarFamiliar());
        ui.setAccionBorrarTipo(e -> borrarTipo());
        ui.setAccionBorrarArchivo(e -> borrarArchivo());
    }

    private Object borrarArchivo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'borrarArchivo'");
    }

    private Object borrarTipo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'borrarTipo'");
    }

    private Object borrarFamiliar() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'borrarFamiliar'");
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
