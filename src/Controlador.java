import java.sql.SQLException;

import DataBase.Archivo;
import DataBase.GestorBD;
import UI.Ventana;

public class Controlador {
    private GestorBD gestor;
    private Ventana ui;

    public Controlador(GestorBD gestor, Ventana ui) {
        this.gestor = gestor;
        this.ui = ui;

        ui.setAccionGuardarArchivo(e -> guardarArchivo());
        ui.setAccionPedirFamilia(e -> pedirFamilia());
        ui.setAccionPedirTipos(e -> pedirTipos());
    }

    public void guardarArchivo() {
        Archivo archivo = ui.getArchivoGuardo();
        try {
            gestor.insertarArchivo(archivo);
        } catch (SQLException e) {
            System.err.println("Error al intentar guardar el archivo en la base de datos");
            System.err.println(e.getMessage());
        }
    }

    public void pedirFamilia() {
        ui.setFamilia(gestor.getFamiliaString());
    }

    public void pedirTipos() {
        ui.setTipos(gestor.getTipos());
    }
}
