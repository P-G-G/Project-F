package main;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatDarkLaf;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;

import DataBase.GestorBD;
import GUI.Ventana;

public class App {

    private static final String LOGGING_FILENAME = "logging.log" ;
    private static Logger logger;

    private static void configurar_logging() {
        try {
            // Configuramos el archivo físico
            FileHandler fileHandler = new FileHandler(LOGGING_FILENAME, false);
            fileHandler.setFormatter(new SimpleFormatter());
            
            // Atrapamos el logger global de todo el sistema Java y le enchufamos nuestro archivo
            logger = Logger.getLogger(""); 
            
            // Opcional: quitamos los handlers por defecto para que no salga por consola duplicado
            for (Handler h : logger.getHandlers()) {
                logger.removeHandler(h);
            }

            logger.addHandler(fileHandler);
            
        } catch (Exception e) {
            System.err.println("Fallo al iniciar el sistema de logs");
        }
    }

    public static void main(String[] args) {
        configurar_logging();

        try {
            GestorBD gestor = new GestorBD();
        
            // Arrancamos la interfaz gráfica en su propio hilo de ejecución
            SwingUtilities.invokeLater(() -> {
                if (FlatDarkLaf.setup()) {
                    logger.fine("FlatLaf se inició con éxito");
                } else {
                    logger.warning("No se pudo iniciar FlatLaf, se usará el tema visual por defecto de Java");
                }

                // Creamos la ventana
                Ventana ventana = new Ventana();
                
                // Creamos el controlador principal del programa
                new Controlador(gestor, ventana);

                // Mostramos la ventana
                ventana.setVisible(true);
            });

        } catch (SQLException e) {
            logger.severe("No se pudo crear el gestor de la base de datos: " + e.getMessage());
            System.exit(1);
        }
    }
}