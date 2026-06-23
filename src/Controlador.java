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
        
        // TODO crear subcarpeta con nombre del tipo
    }

    public void guardarArchivo() {
        Archivo archivo = ui.getArchivoGuardado();
        // TODO Mover archivo
        try {
            gestor.insertarArchivo(archivo);
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
}
