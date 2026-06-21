package UI;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Ventana extends JFrame {

    // Variables a nivel de clase para poder leerlas al hacer clic en "Guardar"
    private JComboBox<String> comboFamiliares;
    private JComboBox<String> comboTipos;
    private JLabel lblRutaSeleccionada;
    private JPanel panel;

    private File archivoPendiente; // Guardaremos aquí el archivo temporalmente hasta darle a guardar

    public Ventana() {

        // 1. Configuración básica de la ventana
        setTitle("Gestor de Archivos Familiares");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Que el programa acabe al cerrar la X
        setLocationRelativeTo(null); // Centrar en la pantalla

        // 2. Crear un panel principal
        panel = new JPanel(new BorderLayout(10, 10)); // Un diseño que divide en Norte, Sur, Centro...
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(panel);
    }

    public void setMenuPrincipal() {
        // TODO setMenuPrincipal
    }

    public void setMenuAñadirTipo() {
        // TODO setMenuAñadirTipo
    }

    public void setMenuAñadirPersona() {
        // TODO setMenuAñadirPersona
    }

    public void setMenuAñadirArchivo() {
        // 2. Título
        JLabel titulo = new JLabel("Registrar Nuevo Documento", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        // 3. EL FORMULARIO (Cuadrícula de 3 filas y 2 columnas)
        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 20));

        // --- FILA 1: Desplegable de Familiares ---
        panelFormulario.add(new JLabel("Seleccionar Familiar:"));
        // TODO Pedir datos de los familiares a la BD con un SELECT
        String[] datosFamiliares = {"[12345678A] Pablo Pérez", "[11111111A] María García", "[22222222B] Andrés López"};
        comboFamiliares = new JComboBox<>(datosFamiliares);
        panelFormulario.add(comboFamiliares);

        // --- FILA 2: Desplegable de Tipos de Archivo ---
        panelFormulario.add(new JLabel("Tipo de documento:"));
        // TODO Pedir datos de los tipos a la BD con un SELECT
        String[] tiposDisponibles = {"Analítica de Sangre", "Radiografía", "Ecografía", "Resonancia", "Receta Médica", "Otro..."};
        comboTipos = new JComboBox<>(tiposDisponibles);
        panelFormulario.add(comboTipos);

        // --- FILA 3: Tu botón de archivo ---
        panelFormulario.add(new JLabel("Documento:"));
        
        JPanel panelArchivo = new JPanel(new BorderLayout(10, 0));
        JButton btnSeleccionarArchivo = new JButton("📁 Buscar...");
        lblRutaSeleccionada = new JLabel("Ningún archivo");
        lblRutaSeleccionada.setForeground(Color.GRAY);
        
        btnSeleccionarArchivo.addActionListener(e -> seleccionarArchivo());
        
        panelArchivo.add(btnSeleccionarArchivo, BorderLayout.WEST);
        panelArchivo.add(lblRutaSeleccionada, BorderLayout.CENTER);
        panelFormulario.add(panelArchivo);

        panel.add(panelFormulario, BorderLayout.CENTER);

        // 4. BOTÓN FINAL DE GUARDAR
        JButton btnGuardar = new JButton("Guardar en Base de Datos");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(40, 150, 80)); // Un tono verde oscuro
        btnGuardar.setForeground(Color.WHITE);
        
        btnGuardar.addActionListener(e -> guardarArchivo());
        
        panel.add(btnGuardar, BorderLayout.SOUTH);
    }

    private void seleccionarArchivo(){
        String ruta = abrirExploradorDeArchivos();
        System.out.println("Ruta del archivo seleccionado " + ruta);
        if (ruta != null){
            archivoPendiente = new File(ruta);
        }
    }

     /**
     * Método que abre el explorador de archivos y extrae la ruta del documento.
     */
    private String abrirExploradorDeArchivos() {
        String rutaAbsoluta = null;

        // Usamos la ventana nativa de Windows en modo "Cargar" (LOAD)
        FileDialog explorador = new FileDialog(this, "Buscar Análisis", FileDialog.LOAD);
        
        // Al hacer setVisible(true), el programa se pausa hasta que el usuario elija o cancele
        explorador.setVisible(true);

        // Extraemos la información por separado
        String carpeta = explorador.getDirectory();
        String archivo = explorador.getFile();

        // Si no son null, significa que el usuario seleccionó algo y le dio a "Abrir"
        if (carpeta != null && archivo != null) {
            // Concatenamos la carpeta y el archivo para tener la ruta total
            rutaAbsoluta = carpeta + archivo;
            
            lblRutaSeleccionada.setText(rutaAbsoluta);
            lblRutaSeleccionada.setForeground(Color.WHITE);
        }

        return rutaAbsoluta;
    }

    private void guardarArchivo(){
        if (archivoPendiente == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un archivo primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String familiarSeleccionado = (String) comboFamiliares.getSelectedItem();
        String tipoSeleccionado = (String) comboTipos.getSelectedItem();

        // Extraeríamos el DNI cortando el texto (ej: "[12345678A] Pablo" -> "12345678A")
        String dniCortado = familiarSeleccionado.substring(1, familiarSeleccionado.indexOf("]"));

        System.out.println("--- LISTO PARA INSERTAR ---");
        System.out.println("DNI: " + dniCortado);
        System.out.println("Tipo: " + tipoSeleccionado);
        System.out.println("Archivo original: " + archivoPendiente.getAbsolutePath());
        
        // TODO ¡Aquí iría la lógica de Files.move y tu PreparedStatement!
        
        JOptionPane.showMessageDialog(this, "Análisis guardado con éxito.", "Correcto", JOptionPane.INFORMATION_MESSAGE);
        
        // Limpiamos el formulario para el siguiente
        archivoPendiente = null;
        lblRutaSeleccionada.setText("Ningún archivo");
        lblRutaSeleccionada.setForeground(Color.GRAY);
    }
}