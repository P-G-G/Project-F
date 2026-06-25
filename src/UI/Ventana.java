package UI;

import java.awt.*;

import javax.swing.*;

import DataBase.Archivo;
import DataBase.Familiar;
import Utils.Utils;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

public class Ventana extends JFrame {

    public static String MENU_PRINCIPAL = "MENU_PRINCIPAL";

    public static String MENU_AÑADIR_ARCHIVO = "MENU_AÑADIR_ARCHIVO";
    public static String MENU_AÑADIR_FAMILIAR = "MENU_AÑADIR_FAMILIAR";
    public static String MENU_AÑADIR_TIPO = "MENU_AÑADIR_TIPO";

    public static String MENU_BORRAR_FAMILIAR = "MENU_BORRAR_FAMILIAR";
    public static String MENU_BORRAR_TIPO = "MENU_BORRAR_TIPO";

    public static String MENU_ARCHIVOS = "MENU_ARCHIVOS";

    private static String DEFAULT_TEXT_BUTTON_SEARCH = "📁 Buscar...";

    // 1. El gestor de los menus
    public CardLayout gestor;
    
    // 2. el panel contenedor
    private JPanel contenedor;

    // Menus
    private JPanel menuPrincipal;

    private JPanel menuAñadirArchivo;
    private JPanel menuAñadirFamiliar;
    private JPanel menuAñadirTipo;

    private JPanel menuBorrarTipo;
    private JPanel menuBorrarFamiliar;

    private JPanel menuArchivos;

    // Acciones
    private ActionListener accionGuardarArchivo;
    private ActionListener accionGuardarFamiliar;
    private ActionListener accionGuardarTipo;

    private ActionListener accionBorrarArchivos;
    private ActionListener accionBorrarFamiliar;
    private ActionListener accionBorrarTipo;

    private ActionListener accionPedirFamilia;
    private ActionListener accionPedirTipos;
    private ActionListener accionPedirArchivos;

    // private 

    // Variables para guardar
    private Familiar[] familia;
    private String[] tipos;
    private Archivo[] archivos;

    private JComboBox<Familiar> desplegableFamiliaAñadir;
    private JComboBox<String> desplegableTiposAñadir;

    private JComboBox<Familiar> desplegableFamiliaBorrar;
    private JComboBox<String> desplegableTiposBorrar;

    private JList<Archivo> listaArchivos;

    private Familiar familiarGuardado;
    private String tipoGuardado;
    private Archivo archivoGuardado;

    private Familiar familiarBorrado;
    private String tipoBorrado;
    private List<Archivo> archivosBorrados;
    
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

    public Ventana() {

        // Configuración básica de la ventana
        setTitle("Gestor de Archivos Familiares");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en la pantalla

        // Inicializamos el gestor y el contenedor
        gestor = new CardLayout();
        contenedor = new JPanel(gestor);

        // Botones
        desplegableFamiliaAñadir = new JComboBox<>();
        desplegableTiposAñadir = new JComboBox<>();

        desplegableFamiliaBorrar = new JComboBox<>();
        desplegableTiposBorrar = new JComboBox<>();

        listaArchivos = new JList<>();

        // Inicializamos todos los menus
        setMenuPrincipal();

        setMenuAñadirArchivo();
        setMenuAñadirFamiliar();
        setMenuAñadirTipo();

        setMenuBorrarTipo();
        setMenuBorrarFamiliar();

        setMenuArchivos();

        // Añadimos todos los menus
        contenedor.add(menuPrincipal, MENU_PRINCIPAL);

        contenedor.add(menuAñadirArchivo, MENU_AÑADIR_ARCHIVO);
        contenedor.add(menuAñadirFamiliar, MENU_AÑADIR_FAMILIAR);
        contenedor.add(menuAñadirTipo, MENU_AÑADIR_TIPO);

        contenedor.add(menuBorrarTipo, MENU_BORRAR_TIPO);
        contenedor.add(menuBorrarFamiliar, MENU_BORRAR_FAMILIAR);

        contenedor.add(menuArchivos, MENU_ARCHIVOS);

        this.add(contenedor);

        // Enseñamos el menú principal
        gestor.show(contenedor, MENU_PRINCIPAL);
    }

    private JButton crearBotonBorrar() {
        JButton botonBorrar = new JButton("Borrar de la Base de Datos");
        botonBorrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonBorrar.setBackground(new Color(150, 40, 80));
        botonBorrar.setForeground(Color.WHITE);

        return botonBorrar;
    }

    private JButton crearBotonVolver() {
        JButton botonVolver = new JButton("Volver");
        botonVolver.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonVolver.setBackground(new Color(150, 150, 150));
        botonVolver.setForeground(Color.WHITE);

        botonVolver.addActionListener(e -> {
            gestor.show(contenedor, MENU_PRINCIPAL);
        });

        return botonVolver;
    }

    private JButton crearBotonGuardar() {
        JButton botonGuardar = new JButton("Guardar en Base de Datos");
        botonGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonGuardar.setBackground(new Color(40, 150, 80)); // Un tono verde oscuro
        botonGuardar.setForeground(Color.WHITE);

        return botonGuardar;
    }

    private JPanel crearPanelSuperior(String str_titulo) {
        JPanel panelSuperior = new JPanel(new BorderLayout());

        JLabel titulo = new JLabel(str_titulo, SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JButton botonVolver = crearBotonVolver();

        panelSuperior.add(botonVolver, BorderLayout.WEST);
        panelSuperior.add(titulo, BorderLayout.CENTER);
        panelSuperior.add(Box.createRigidArea(botonVolver.getPreferredSize()), BorderLayout.EAST);

        return panelSuperior;
    }

    public void setMenuPrincipal() {
        menuPrincipal = new JPanel(new BorderLayout(0, 20));

        // 1. Título
        JLabel titulo = new JLabel("Gestor de Documentos", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        menuPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel opciones = new JPanel(new GridLayout(3, 1, 0, 10));
        JPanel opcionesAñadirBorrar = new JPanel(new GridLayout(2, 2, 10, 10));

        // --- Fila 1 ---
        JButton añadirArchivo = new JButton("Añadir Archivo");
        añadirArchivo.addActionListener(e -> {
            accionPedirFamilia.actionPerformed(e);
            accionPedirTipos.actionPerformed(e);

            if (familia != null && tipos != null) {     // CHECK
                gestor.show(contenedor, MENU_AÑADIR_ARCHIVO);
            }
        });

        opciones.add(añadirArchivo);

        // --- Fila 2 ---
        JButton añadirFamiliar = new JButton("Añadir Familiar");
        añadirFamiliar.addActionListener(e -> gestor.show(contenedor, MENU_AÑADIR_FAMILIAR));
        opcionesAñadirBorrar.add(añadirFamiliar);

        JButton borrarFamiliar = new JButton("Borrar Familiar");
        borrarFamiliar.addActionListener(e -> {
            accionPedirFamilia.actionPerformed(e);
            if (familia != null)
                gestor.show(contenedor, MENU_BORRAR_FAMILIAR);
        });

        opcionesAñadirBorrar.add(borrarFamiliar);

        // --- Fila 3 ---
        JButton añadirTipo = new JButton("Añadir Tipo");
        añadirTipo.addActionListener(e -> gestor.show(contenedor, MENU_AÑADIR_TIPO));
        opcionesAñadirBorrar.add(añadirTipo);

        JButton borrarTipo = new JButton("Borrar Tipo");
        borrarTipo.addActionListener(e -> {
            accionPedirTipos.actionPerformed(e);
            if (tipos != null)
                gestor.show(contenedor, MENU_BORRAR_TIPO);
        });

        opcionesAñadirBorrar.add(borrarTipo);

        opciones.add(opcionesAñadirBorrar);

        // --- Fila 4 ---
        JButton buscar = new JButton("Documentos");
        buscar.addActionListener(e -> {
            accionPedirArchivos.actionPerformed(e);

            if (archivos != null) {
                gestor.show(contenedor, MENU_ARCHIVOS);
            }
        });

        opciones.add(buscar);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.add(opciones);

        menuPrincipal.add(panelCentral, BorderLayout.CENTER);
    }

    public void setMenuAñadirTipo() {
        menuAñadirTipo = new JPanel(new BorderLayout());
        
        JPanel panelSuperior = crearPanelSuperior("Registrar Nuevo Tipo de Documento");
        JPanel panelCentral = new JPanel(new GridBagLayout());

        JPanel panelTipo = new JPanel(new GridLayout(2, 1));

        JLabel labelTipo = new JLabel("Nombre del tipo");
        JTextField txtTipo = new JTextField();

        panelTipo.add(labelTipo);
        panelTipo.add(txtTipo);

        JButton botonGuardar = crearBotonGuardar();
        botonGuardar.addActionListener(e -> {
            String tipo = txtTipo.getText();

            if (tipo != null && !tipo.isBlank()) {
                tipoGuardado = tipo;
                accionGuardarTipo.actionPerformed(e);
            }

            txtTipo.setText(null);
        });

        panelCentral.add(panelTipo);

        menuAñadirTipo.add(panelSuperior, BorderLayout.NORTH);
        menuAñadirTipo.add(panelCentral, BorderLayout.CENTER);
        menuAñadirTipo.add(botonGuardar, BorderLayout.SOUTH);
    }

    public void setMenuAñadirFamiliar() {
        menuAñadirFamiliar = new JPanel(new BorderLayout());

        JPanel panelSuperior = crearPanelSuperior("Registrar Nuevo Familiar");
        JPanel panelCentral = new JPanel(new GridBagLayout());

        JPanel panelTextos = new JPanel(new GridLayout(2, 2));

        JTextField txtDni = new JTextField();
        JTextField txtNombre = new JTextField();

        JButton botonGuardar = crearBotonGuardar();
        botonGuardar.addActionListener(e -> {
            String dni = txtDni.getText();
            String nombre = txtNombre.getText();

            if (dni != null && nombre != null && !dni.isBlank() && !nombre.isBlank()) {
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

        panelCentral.add(panelTextos);

        menuAñadirFamiliar.add(panelSuperior, BorderLayout.NORTH);
        menuAñadirFamiliar.add(panelCentral, BorderLayout.CENTER);
        menuAñadirFamiliar.add(botonGuardar, BorderLayout.SOUTH);
    }

    public void setMenuAñadirArchivo() {
        menuAñadirArchivo = new JPanel(new BorderLayout());

        JPanel panelSuperior = crearPanelSuperior("Registrar Nuevo Documento");
        JPanel panelCentral = new JPanel(new GridBagLayout());

        // 2. EL FORMULARIO (Cuadrícula de 3 filas y 2 columnas)
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 20));

        // --- FILA 1: Desplegable de Familiares ---
        panelFormulario.add(new JLabel("Familiar:"));
        panelFormulario.add(desplegableFamiliaAñadir);

        // --- FILA 2: Desplegable de Tipos de Archivo ---
        panelFormulario.add(new JLabel("Tipo de documento:"));
        panelFormulario.add(desplegableTiposAñadir);

        // --- FILA 3: Botón de Fecha
        JSpinner selectorFecha = new JSpinner(new SpinnerDateModel());

        JSpinner.DateEditor editor = new JSpinner.DateEditor(selectorFecha, "dd/MM/yyyy");
        selectorFecha.setEditor(editor);

        panelFormulario.add(new JLabel("Fecha:"));
        panelFormulario.add(selectorFecha);

        // --- FILA 4: Tu botón de archivo ---

        JButton botonSeleccionarArchivo = new JButton(DEFAULT_TEXT_BUTTON_SEARCH);
        botonSeleccionarArchivo.addActionListener(e -> seleccionarArchivo(botonSeleccionarArchivo));

        panelFormulario.add(new JLabel("Documento:"));
        panelFormulario.add(botonSeleccionarArchivo);

        panelCentral.add(panelFormulario);

        // 3. BOTÓN FINAL DE GUARDAR
        JButton botonGuardar = crearBotonGuardar();
        
        botonGuardar.addActionListener(e -> {
            String rutaArchivo = botonSeleccionarArchivo.getText();
            Familiar familiar = (Familiar) desplegableFamiliaAñadir.getSelectedItem();
            String tipo = (String) desplegableTiposAñadir.getSelectedItem();
            Date fechaDate = (Date) selectorFecha.getValue();

            if (familiar != null && tipo != null && !rutaArchivo.equals(DEFAULT_TEXT_BUTTON_SEARCH)) {
                File archivo = new File(rutaArchivo);
                String hash = Utils.calcularHash(rutaArchivo);
                String fecha = formato.format(fechaDate);
                archivoGuardado = 
                    new Archivo(archivo.getName(), tipo, hash, archivo.getAbsolutePath(), fecha, familiar.dni());

                accionGuardarArchivo.actionPerformed(e);
            }
            
            // Limpiamos el formulario para el siguiente
            botonSeleccionarArchivo.setText(DEFAULT_TEXT_BUTTON_SEARCH);
        });
        
        menuAñadirArchivo.add(panelSuperior, BorderLayout.NORTH);
        menuAñadirArchivo.add(panelCentral, BorderLayout.CENTER);
        menuAñadirArchivo.add(botonGuardar, BorderLayout.SOUTH);
    }

    private void setMenuBorrarFamiliar() {
        menuBorrarFamiliar = new JPanel(new BorderLayout());
        JPanel panelSuperior = crearPanelSuperior("Borrar Familiar");
        JPanel panelCentral = new JPanel(new GridBagLayout());

        JLabel labelBoton = new JLabel("Elegir familiar: ");
        panelCentral.add(labelBoton);
        panelCentral.add(desplegableFamiliaBorrar);

        JButton botonBorrar = crearBotonBorrar();
        botonBorrar.addActionListener(e -> {
            familiarBorrado = (Familiar) desplegableFamiliaBorrar.getSelectedItem();

            if (familiarBorrado != null) {
                accionBorrarFamiliar.actionPerformed(e);
            }
        });

        menuBorrarFamiliar.add(panelSuperior, BorderLayout.NORTH);
        menuBorrarFamiliar.add(panelCentral, BorderLayout.CENTER);
        menuBorrarFamiliar.add(botonBorrar, BorderLayout.SOUTH);
    }

    private void setMenuBorrarTipo () {
        menuBorrarTipo = new JPanel(new BorderLayout());
        JPanel panelSuperior = crearPanelSuperior("Borrar Tipo");
        JPanel panelCentral = new JPanel(new GridBagLayout());

        JLabel labelBoton = new JLabel("Elegir Tipo: ");
        panelCentral.add(labelBoton);
        panelCentral.add(desplegableTiposBorrar);

        JButton botonBorrar = crearBotonBorrar();
        botonBorrar.addActionListener(e -> {
            tipoBorrado = (String) desplegableTiposBorrar.getSelectedItem();

            if (tipoBorrado != null) {
                accionBorrarTipo.actionPerformed(e);
            }
        });

        menuBorrarTipo.add(panelSuperior, BorderLayout.NORTH);
        menuBorrarTipo.add(panelCentral, BorderLayout.CENTER);
        menuBorrarTipo.add(botonBorrar, BorderLayout.SOUTH);
    }
     
    private void setMenuArchivos() {
        // TODO AÑADIR BUSCADOR
        menuArchivos = new JPanel(new BorderLayout());

        // --- PANEL SUPERIOR ---
        menuArchivos.add(
            crearPanelSuperior("Documentos"), BorderLayout.NORTH);

        // --- PANEL CENTRAL (La Lista) ---
        JScrollPane scroll = new JScrollPane(listaArchivos);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
        listaArchivos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        menuArchivos.add(scroll, BorderLayout.CENTER);

        // --- PANEL INFERIOR (El Botón de Abrir) ---
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton botonAbrirDocumento = new JButton("Abrir Documento Seleccionado");
        botonAbrirDocumento.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonAbrirDocumento.setEnabled(false); // Empieza pagado para que no le den si no han seleccionado nada
        botonAbrirDocumento.addActionListener(e -> abrirArchivo(listaArchivos.getSelectedValue()));
        
        // Hacemos que el botón se encienda solo cuando el usuario hace clic en un archivo
        listaArchivos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                botonAbrirDocumento.setEnabled(listaArchivos.getSelectedIndex() != -1);
            }
        });
        
        JButton botonEliminarDocumento = crearBotonBorrar();
        botonEliminarDocumento.addActionListener(e -> {
            archivosBorrados = listaArchivos.getSelectedValuesList();

            if (!archivosBorrados.isEmpty()) {
                accionBorrarArchivos.actionPerformed(e);
            }
        });

        panelInferior.add(botonAbrirDocumento);
        panelInferior.add(botonEliminarDocumento);
        menuArchivos.add(panelInferior, BorderLayout.SOUTH);
    }
    
    // Método que abre el explorador de archivos y extrae la ruta del documento.
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

    private void abrirArchivo(Archivo archivoSeleccionado) {
        try {

            // Creamos el objeto File apuntando a la ruta del documento
            File archivo = new File(archivoSeleccionado.ruta());

            // Seguridad básica: Comprobar que el archivo realmente sigue ahí
            if (!archivo.exists()) {
                System.err.println("No se puede abrir: El archivo no existe en la ruta especificada.");
                return;
            }

            // Le dice a Windows/Mac que abra el archivo
            Desktop.getDesktop().open(archivo);

            Utils.dormir(1000);
        } catch (IOException e) {
            System.err.println("Error del sistema al intentar abrir el archivo: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("La ruta del archivo está corrupta o no es válida: " + e.getMessage());
        }
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

    public List<Archivo> getArchivosBorrados() {
        return archivosBorrados;
    }

    public Familiar getFamiliarBorrado() {
        return familiarBorrado;
    }

    public String getTipoBorrado() {
        return tipoBorrado;
    }

    // SETTERS DE VARIABLES
    public void setFamilia(Familiar[] familia) {
        this.familia = familia;
        desplegableFamiliaAñadir.setModel(new DefaultComboBoxModel<>(familia));
        desplegableFamiliaBorrar.setModel(new DefaultComboBoxModel<>(familia));
    }

    public void setTipos(String[] tipos) {
        this.tipos = tipos;
        desplegableTiposAñadir.setModel(new DefaultComboBoxModel<>(tipos));
        desplegableTiposBorrar.setModel(new DefaultComboBoxModel<>(tipos));
    }

    public void setArchivos(Archivo[] archivos) {
        this.archivos = archivos;
        DefaultListModel<Archivo> modelo = new DefaultListModel<>();

        // Lo protegemos por si el array viene vacío
        if (archivos != null) { 
            for (Archivo arch : archivos) {
                modelo.addElement(arch); // Los metemos uno a uno
            }
        }

        listaArchivos.setModel(modelo);
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

    public void setAccionPedirArchivos(ActionListener accion) {
        accionPedirArchivos = accion;
    }

    public void setAccionBorrarArchivos(ActionListener accion) {
        accionBorrarArchivos = accion;
    }

    public void setAccionBorrarFamiliar(ActionListener accion) {
        accionBorrarFamiliar = accion;
    }

    public void setAccionBorrarTipo(ActionListener accion) {
        accionBorrarTipo = accion;
    }

    // Añadimos un método para que el Controlador pueda inyectar su código de cierre
    public void setAccionCerrarVentana(Runnable accionCerrar) {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Ejecutamos lo que el controlador nos diga
                accionCerrar.run();
                // Matamos el programa limpiamente
                System.exit(0); 
            }
        });
    }
}