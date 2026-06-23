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

    public static String MENU_PRINCIPAL = "MENU_PRINCIPAL";

    public static String MENU_AÑADIR_ARCHIVO = "MENU_AÑADIR_ARCHIVO";
    public static String MENU_AÑADIR_FAMILIAR = "MENU_AÑADIR_FAMILIAR";
    public static String MENU_AÑADIR_TIPO = "MENU_AÑADIR_TIPO";

    public static String MENU_BORRAR_ARCHIVO = "MENU_BORRAR_ARCHIVO";
    public static String MENU_BORRAR_FAMILIAR = "MENU_BORRAR_FAMILIAR";
    public static String MENU_BORRAR_TIPO = "MENU_BORRAR_TIPO";

    // 1. El gestor de los menus
    public CardLayout gestor;
    
    // 2. el panel contenedor
    private JPanel contenedor;

    // Menus
    private JPanel menuPrincipal;

    private JPanel menuAñadirArchivo;
    private JPanel menuAñadirFamiliar;
    private JPanel menuAñadirTipo;

    private JPanel menuBorrarArchivo;
    private JPanel menuBorrarTipo;
    private JPanel menuBorrarFamiliar;

    // Acciones
    private ActionListener accionGuardarArchivo;
    private ActionListener accionGuardarFamiliar;
    private ActionListener accionGuardarTipo;

    private ActionListener accionPedirFamilia;
    private ActionListener accionPedirTipos;

    // Variables para guardar
    // private Familiar[] familia;
    // private String[] tipos;

    JComboBox<Familiar> desplegableFamilia;
    JComboBox<String> desplegableTipos;

    private Familiar familiarGuardado;
    private String tipoGuardado;
    private Archivo archivoGuardado;


    public Ventana() {

        // Configuración básica de la ventana
        setTitle("Gestor de Archivos Familiares");
        setSize(700, 600);
        // TODO DETECTAR CIERRE Y CERRAR CONEXIÓN EN LA BD
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Que el programa acabe al cerrar la X
        setLocationRelativeTo(null); // Centrar en la pantalla

        // Inicializamos el gestor y el contenedor
        gestor = new CardLayout();
        contenedor = new JPanel(gestor);

        // Inicializamos botones
        desplegableFamilia = new JComboBox<Familiar>();
        desplegableTipos = new JComboBox<String>();

        // Inicializamos todos los menus
        menuPrincipal = getMenuPrincipal();

        menuAñadirArchivo = getMenuAñadirArchivo();
        menuAñadirFamiliar = getMenuAñadirFamiliar();
        menuAñadirTipo = getMenuAñadirTipo();

        menuBorrarArchivo = getMenuBorrarArchivo();
        menuBorrarTipo = getMenuBorrarTipo();
        menuBorrarFamiliar = getMenuBorrarFamiliar();

        // Añadimos todos los menus
        contenedor.add(menuPrincipal, MENU_PRINCIPAL);

        contenedor.add(menuAñadirArchivo, MENU_AÑADIR_ARCHIVO);
        contenedor.add(menuAñadirFamiliar, MENU_AÑADIR_FAMILIAR);
        contenedor.add(menuAñadirTipo, MENU_AÑADIR_TIPO);

        contenedor.add(menuBorrarArchivo, MENU_BORRAR_ARCHIVO);
        contenedor.add(menuBorrarTipo, MENU_BORRAR_TIPO);
        contenedor.add(menuBorrarFamiliar, MENU_BORRAR_FAMILIAR);

        this.add(contenedor);

        // Enseñamos el menú principal
        gestor.show(contenedor, MENU_PRINCIPAL);
    }

    private JPanel getMenuBorrarFamiliar() {
        JPanel panel = new JPanel();
        // TODO getMenuBorrarFamiliar
        return panel;
    }

    private JPanel getMenuBorrarTipo() {
        JPanel panel = new JPanel();
        // TODO getMenuBorrarTipo
        return panel;
    }

    private JPanel getMenuBorrarArchivo() {
        JPanel panel = new JPanel();
        // TODO getMenuBorrarArchivo
        return panel;
    }

    private JButton crearBotonBorrar() {
        JButton botonBorrar = new JButton("Borrar de la Base de Datos");
        botonBorrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonBorrar.setBackground(new Color(150, 40, 80)); // CHANGE a rojo oscuro
        botonBorrar.setForeground(Color.WHITE);

        return botonBorrar;
    }

    private JButton crearBotonGuardar() {
        JButton botonGuardar = new JButton("Guardar en Base de Datos");
        botonGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonGuardar.setBackground(new Color(40, 150, 80)); // Un tono verde oscuro
        botonGuardar.setForeground(Color.WHITE);

        return botonGuardar;
    }

    public JPanel getMenuPrincipal() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));

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

            if (desplegableFamilia.getItemCount() > 0 && desplegableTipos.getItemCount() > 0) {
                gestor.show(contenedor, MENU_AÑADIR_ARCHIVO);
            }
        });

        opciones.add(añadirArchivo);

        // --- Fila 2 ---
        JButton añadirFamiliar = new JButton("Añadir Familiar");
        añadirFamiliar.addActionListener(e -> gestor.show(contenedor, MENU_AÑADIR_FAMILIAR));
        opciones.add(añadirFamiliar);

        // --- Fila 3 ---
        JButton añadirTipo = new JButton("Añadir Tipo");
        añadirTipo.addActionListener(e -> gestor.show(contenedor, MENU_AÑADIR_TIPO));
        opciones.add(añadirTipo);

        // --- Fila 4 ---
        JButton buscar = new JButton("Buscar Documentos");
        // TODO buscar.addActionListener();
        opciones.add(buscar);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.add(opciones);

        panel.add(panelCentral, BorderLayout.CENTER);
        return panel;
    }

    public JPanel getMenuAñadirTipo() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel titulo = new JLabel("Registrar Nuevo Tipo de Documento", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        JTextField txtTipo = new JTextField();
        JButton botonGuardar = crearBotonGuardar();
        botonGuardar.addActionListener(e -> {
            String tipo = txtTipo.getText();

            if (tipo != null) {
                tipoGuardado = tipo;
                accionGuardarTipo.actionPerformed(e);
            }

            txtTipo.setText(null);
        });

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.add(txtTipo);

        panel.add(panelCentral);
        panel.add(botonGuardar, BorderLayout.SOUTH);

        return panel;
    }

    public JPanel getMenuAñadirFamiliar() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel titulo = new JLabel("Registrar Nuevo Familiar", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel panelTextos = new JPanel(new GridLayout(2, 2));

        JTextField txtDni = new JTextField();
        JTextField txtNombre = new JTextField();

        JButton botonGuardar = crearBotonGuardar();
        botonGuardar.addActionListener(e -> {
            String dni = txtDni.getText();
            String nombre = txtNombre.getText();

            if (dni != null && nombre != null) {
                familiarGuardado = new Familiar(dni.trim(), nombre.trim());
                accionGuardarFamiliar.actionPerformed(e);
            }

            txtDni.setText(null);
            txtNombre.setText(null);
        });

        // CHANGE para q se pueda introducir más comodamente la info
        panelTextos.add(new JLabel("DNI"));
        panelTextos.add(new JLabel("Nombre"));
        panelTextos.add(txtDni);
        panelTextos.add(txtNombre);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.add(panelTextos);

        panel.add(panelCentral, BorderLayout.CENTER);
        
        panel.add(botonGuardar, BorderLayout.SOUTH);

        return panel;
    }

    public JPanel getMenuAñadirArchivo() {
        JPanel panel = new JPanel(new BorderLayout());

        // 1. Título
        JLabel titulo = new JLabel("Registrar Nuevo Documento", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        // 2. EL FORMULARIO (Cuadrícula de 3 filas y 2 columnas)
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 20));

        // --- FILA 1: Desplegable de Familiares ---
        panelFormulario.add(new JLabel("Familiar:"));
        panelFormulario.add(desplegableFamilia);

        // --- FILA 2: Desplegable de Tipos de Archivo ---
        panelFormulario.add(new JLabel("Tipo de documento:"));
        panelFormulario.add(desplegableTipos);

        // --- FILA 3: Botón de Fecha
        SpinnerDateModel modeloFecha = new SpinnerDateModel();
        JSpinner selectorFecha = new JSpinner(modeloFecha);

        JSpinner.DateEditor editor = new JSpinner.DateEditor(selectorFecha, "dd/MM/yyyy");
        selectorFecha.setEditor(editor);

        panelFormulario.add(new JLabel("Fecha:"));
        panelFormulario.add(selectorFecha);

        // --- FILA 4: Tu botón de archivo ---
        panelFormulario.add(new JLabel("Documento:"));

        JButton botonSeleccionarArchivo = new JButton("📁 Buscar...");
        botonSeleccionarArchivo.addActionListener(e -> seleccionarArchivo(botonSeleccionarArchivo));
        
        panelFormulario.add(botonSeleccionarArchivo);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.add(panelFormulario);

        panel.add(panelCentral, BorderLayout.CENTER);

        // 3. BOTÓN FINAL DE GUARDAR
        JButton botonGuardar = crearBotonGuardar();
        
        botonGuardar.addActionListener(e -> {
            String rutaArchivo = botonSeleccionarArchivo.getText();
            Familiar familiar = (Familiar) desplegableFamilia.getSelectedItem();
            String tipo = (String) desplegableTipos.getSelectedItem();
            if (familiar != null && tipo != null && rutaArchivo != null) {
                guardarArchivo(familiar, 
                                tipo, 
                                (Date) selectorFecha.getValue(), 
                                rutaArchivo);

                accionGuardarArchivo.actionPerformed(e);
            }
            
            // Limpiamos el formulario para el siguiente
            botonSeleccionarArchivo.setText("📁 Buscar...");
        });
        
        panel.add(botonGuardar, BorderLayout.SOUTH);

        return panel;
    }

     /**
     * Método que abre el explorador de archivos y extrae la ruta del documento.
     */
    private void seleccionarArchivo(JButton botonSeleccionarArchivo) {
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
            botonSeleccionarArchivo.setText(carpeta + fichero);
        }
    }

    private void guardarArchivo(Familiar familiar, String tipo, Date date, String rutaArchivo) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        String dni = familiar.dni();
        String fecha = formato.format(date);
        File archivo = new File(rutaArchivo);

        archivoGuardado = new Archivo(archivo.getName(), tipo, archivo.getAbsolutePath(), fecha, dni);

        System.out.println("Archvo listo para guardar: " + archivoGuardado);    // DEBUG
    }

    // GETTERS DE variables
    public Archivo getArchivoGuardado() {
        return archivoGuardado;
    }

    public Familiar getFamiliarGuardado() {
        return familiarGuardado;
    }

    public String getTipoGuardado() {
        return tipoGuardado;
    }

    // SETTERS DE variables
    public void setFamilia(Familiar[] familia) {
        desplegableFamilia.setModel(new DefaultComboBoxModel<>(familia));
    }

    public void setTipos(String[] tipos) {
        desplegableTipos.setModel(new DefaultComboBoxModel<>(tipos));
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