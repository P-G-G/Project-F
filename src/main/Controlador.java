package main;
import java.sql.SQLException;

import DataBase.Archivo;
import DataBase.Familiar;
import DataBase.GestorBD;
import GUI.Ventana;

public class Controlador {
    private GestorBD gestor;
    private Ventana gui;

    public Controlador(GestorBD gestor, Ventana gui) {
        this.gestor = gestor;
        this.gui = gui;

        gui.setAccionGuardarFamiliar(e -> guardarFamiliar());
        gui.setAccionGuardarTipo(e -> guardarTipo());
        gui.setAccionGuardarArchivo(e -> guardarArchivo());

        gui.setAccionPedirFamilia(e -> pedirFamilia());
        gui.setAccionPedirTipos(e -> pedirTipos());
        gui.setAccionPedirArchivos(e -> pedirArchivos());

        gui.setAccionBorrarFamiliar(e -> borrarFamiliar());
        gui.setAccionBorrarTipo(e -> borrarTipo());
        gui.setAccionBorrarArchivos(e -> borrarArchivos());

        gui.setAccionCerrarVentana(() -> gestor.cerrarConexion());

        gui.setTraductorDni(dni -> gestor.getNombre(dni));
    }

    private void borrarArchivos() {
        for (Archivo archivo : gui.getArchivosBorrados()) {
            try {
                    gestor.eliminarArchivo(archivo);
                    gui.mostrarExito("Archivo eliminado con éxito");
            } catch (SQLException e) {
                gui.mostrarError("Error al intentar eliminar el archivo: " + e.getMessage());
            }
        }
        
        pedirArchivos();
    }

    private void borrarTipo() {
        try {
            gestor.eliminarTipo(gui.getTipoBorrado());
            pedirTipos();
            gui.mostrarExito("Tipo eliminado con éxito");
        } catch (SQLException e) {
            gui.mostrarError("Error al intentar eliminar el tipo de archivo: " + e.getMessage());
        }

        pedirTipos();
    }

    private void borrarFamiliar() {
        try {
            gestor.eliminarFamiliar(gui.getFamiliarBorrado());
            pedirFamilia(); // Actualizar información si se ha eliminado con éxito
            gui.mostrarExito("Familiar eliminado con éxito");
        } catch (SQLException e) {
            gui.mostrarError("Error al intentar eliminar el familiar: " + e.getMessage());
        }

        pedirFamilia();
    }

    public void guardarFamiliar() {
        Familiar familiar = gui.getFamiliarGuardado();
        try {
            gestor.insertarFamiliar(familiar);
            gui.mostrarExito("Familiar guardado con éxito");
        } catch (SQLException e) {
            gui.mostrarError("Error al intentar guardar el familiar en la base de datos:\n" + e.getMessage());
        }
    }

    public void guardarTipo() {
        String tipo = gui.getTipoGuardado();
        try {
            gestor.insertarTipo(tipo);
            gui.mostrarExito("Tipo de documento guardado con éxito");
        } catch (SQLException e) {
            gui.mostrarError("Error al intentar guardar el tipo en la base de datos:\n" + e.getMessage());
        }
    }

    public void guardarArchivo() {
        Archivo archivo = gui.getArchivoGuardado();

        try {
            if (gestor.insertarArchivo(archivo)) {
                gui.mostrarExito("Archivo guardado con éxito");
            }
        } catch (SQLException e) {
            gui.mostrarError("Error al intentar guardar el archivo en la base de datos:\n" + e.getMessage());
        }
    }

    public void pedirFamilia() {
        gui.setFamilia(gestor.getFamilia());
    }

    public void pedirTipos() {
        gui.setTipos(gestor.getTipos());
    }

    public void pedirArchivos() {
        gui.setArchivos(gestor.getArchivos());
    }
}
