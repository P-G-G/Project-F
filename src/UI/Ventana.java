package UI;

import java.awt.*;
import javax.swing.*;

import DataBase.Archivo;
import DataBase.Familiar;

import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ventana extends JFrame {

    private String[] familia;
    private String[] tipos;

    private JComboBox<String> comboFamiliares;
    private JComboBox<String> comboTipos;
    private JSpinner selectorFecha;

    private ActionListener accionGuardarArchivo;
    private ActionListener accionGuardarFamiliar;
    private ActionListener accionGuardarTipo;

    private ActionListener accionPedirFamilia;
    private ActionListener accionPedirTipos;

    private JButton botonSeleccionarArchivo;
    private JPanel panel;

    private Archivo archivoGuardado;
    private File archivo; // Guardaremos aquí el archivo temporalmente hasta darle a guardar


    public Ventana() {

        // 1. Configuración básica de la ventana
        setTitle("Gestor de Archivos Familiares");
        setSize(700, 600);
        // TODO DETECTAR CIERRE Y CERRAR CONEXIÓN EN LA BD
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Que el programa acabe al cerrar la X
        setLocationRelativeTo(null); // Centrar en la pantalla

        // 2. Crear un panel principal
        panel = new JPanel(new BorderLayout(10, 10)); // Un diseño que divide en Norte, Sur, Centro...
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(panel);
    }

    public void setMenuPrincipal() {
        // 1. Título
        JLabel titulo = new JLabel("Gestor de Documentos", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel opciones = new JPanel(new GridLayout(4, 1, 0, 10));

        // --- Fila 1 ---
        JButton añadirArchivo = new JButton("Añadir Archivo");
        añadirArchivo.addActionListener(e -> {
            accionPedirFamilia.actionPerformed(e);
            accionPedirTipos.actionPerformed(e);

            if (familia != null && tipos != null) {
                setMenuAñadirArchivo();
            }
        });
        opciones.add(añadirArchivo);

        // --- Fila 2 ---
        JButton añadirFamiliar = new JButton("Añadir Familiar");
        añadirArchivo.addActionListener(e -> setMenuAñadirFamiliar());
        opciones.add(añadirFamiliar);

        // --- Fila 3 ---
        JButton añadirTipo = new JButton("Añadir Tipo");
        añadirArchivo.addActionListener(e -> setMenuAñadirTipo());
        opciones.add(añadirTipo);

        // --- Fila 4 ---
        JButton buscar = new JButton("Buscar Documentos");
        // TODO añadirArchivo.addActionListener();
        opciones.add(buscar);

        panel.add(opciones);
    }

    public void setMenuAñadirTipo() {
        // TODO setMenuAñadirTipo
    }

    public void setMenuAñadirFamiliar() {
        // TODO setMenuAñadirFamiliar
    }

    public void setMenuAñadirArchivo() {
        // 1. Título
        JLabel titulo = new JLabel("Registrar Nuevo Documento", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        // 2. EL FORMULARIO (Cuadrícula de 3 filas y 2 columnas)
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 20));

        // --- FILA 1: Desplegable de Familiares ---
        panelFormulario.add(new JLabel("Familiar:"));
        comboFamiliares = new JComboBox<>(familia);
        panelFormulario.add(comboFamiliares);

        // --- FILA 2: Desplegable de Tipos de Archivo ---
        panelFormulario.add(new JLabel("Tipo de documento:"));
        comboTipos = new JComboBox<>(tipos);
        panelFormulario.add(comboTipos);

        // --- FILA 3: Botón de Fecha
        SpinnerDateModel modeloFecha = new SpinnerDateModel();
        selectorFecha = new JSpinner(modeloFecha);

        JSpinner.DateEditor editor = new JSpinner.DateEditor(selectorFecha, "dd/MM/yyyy");
        selectorFecha.setEditor(editor);

        panelFormulario.add(new JLabel("Fecha:"));
        panelFormulario.add(selectorFecha);

        // --- FILA 4: Tu botón de archivo ---
        panelFormulario.add(new JLabel("Documento:"));

        botonSeleccionarArchivo = new JButton("📁 Buscar...");
        botonSeleccionarArchivo.addActionListener(e -> seleccionarArchivo());
        
        panelFormulario.add(botonSeleccionarArchivo);

        panel.add(panelFormulario, BorderLayout.CENTER);

        // 3. BOTÓN FINAL DE GUARDAR
        JButton botonGuardar = new JButton("Guardar en Base de Datos");
        botonGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonGuardar.setBackground(new Color(40, 150, 80)); // Un tono verde oscuro
        botonGuardar.setForeground(Color.WHITE);
        
        botonGuardar.addActionListener(e -> {
            guardarArchivo();

            accionGuardarArchivo.actionPerformed(e);
            
            JOptionPane.showMessageDialog(this, "Datos guardados con éxito.", "Correcto", JOptionPane.INFORMATION_MESSAGE);
            // Limpiamos el formulario para el siguiente
            archivo = null;
            botonSeleccionarArchivo.setText("📁 Buscar...");
        });
        
        panel.add(botonGuardar, BorderLayout.SOUTH);
    }

     /**
     * Método que abre el explorador de archivos y extrae la ruta del documento.
     */
    private void seleccionarArchivo() {
        // Usamos la ventana nativa de Windows en modo "Cargar" (LOAD)
        FileDialog explorador = new FileDialog(this, "Buscar Análisis", FileDialog.LOAD);
        
        // Al hacer setVisible(true), el programa se pausa hasta que el usuario elija o cancele
        explorador.setVisible(true);

        // Extraemos la información por separado
        String carpeta = explorador.getDirectory();
        String fichero = explorador.getFile();

        // Si no son null, significa que el usuario seleccionó algo y le dio a "Abrir"
        if (carpeta != null && fichero != null) {
            // Concatenamos la carpeta y el archivo para tener la ruta total
            archivo = new File(carpeta + fichero);
            
            botonSeleccionarArchivo.setText(fichero);   // CHECK
        }
    }

    private void guardarArchivo(){
        if (archivo == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un archivo primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dni = Familiar.fromString((String) comboFamiliares.getSelectedItem()).dni();
        String tipo = (String) comboTipos.getSelectedItem();
        Date date_fecha = (Date) selectorFecha.getValue();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = formato.format(date_fecha);

        archivoGuardado = new Archivo(archivo.getName(), tipo, archivo.getAbsolutePath(), fecha, dni);

        System.out.println("Archvo listo para guardar: " + archivoGuardado);    // DEBUG
    }

    // GETTERS DE variables
    public Archivo getArchivoGuardo() {
        return archivoGuardado;
    }

    // SETTERS DE variables
    public void setFamilia(String[] familia) {
        this.familia = familia;
    }

    public void setTipos(String[] tipos) {
        this.tipos = tipos;
    }

    // SETTERS DE ACCIONES
    public void setAccionGuardarArchivo(ActionListener accion) {
        accionGuardarArchivo = accion;
    }

    public void setAccionGuardarFamiliar(ActionListener accion) {
        accionGuardarFamiliar = accion;
    }

    public void setAccionGuardarTipo(ActionListener accion) {
        accionGuardarTipo = accion;
    }

    public void setAccionPedirTipos(ActionListener accion) {
        accionPedirTipos = accion;
    }

    public void setAccionPedirFamilia(ActionListener accion) {
        accionPedirFamilia = accion;
    }
}