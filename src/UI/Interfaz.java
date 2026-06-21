package UI;

import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatDarkLaf;

public class Interfaz {

    Ventana ventana;

    public Interfaz() {
        // Arrancamos la interfaz gráfica en su propio hilo de ejecución
        SwingUtilities.invokeLater(() -> {
            try {
                // Instalamos el tema moderno ANTES de crear la ventana
                FlatDarkLaf.setup();
            } catch (Exception e) {
                System.err.println("No se pudo iniciar FlatLaf");
            }

            // Creamos y mostramos la ventana
            ventana = new Ventana();
            ventana.setVisible(true);

            ventana.setMenuAñadirArchivo();
        });
    }
}
