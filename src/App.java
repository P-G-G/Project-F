import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatDarkLaf;

import DataBase.GestorBD;
import UI.Ventana;

public class App {

    public static void main(String[] args) {
        GestorBD gestor = new GestorBD();

        // Arrancamos la interfaz gráfica en su propio hilo de ejecución
        SwingUtilities.invokeLater(() -> {
            try {
                // Instalamos el tema moderno ANTES de crear la ventana
                FlatDarkLaf.setup();
            } catch (Exception e) {
                System.err.println("No se pudo iniciar FlatLaf");
            }

            // Creamos y mostramos la ventana
            Ventana ventana = new Ventana();
            
            new Controlador(gestor, ventana);

            ventana.setVisible(true);
        });
    }
}