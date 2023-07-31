/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import Modelo.*;
import Negocio.UsuarioDAO;
import Negocio.AutorDAO;
import Negocio.DevolucionesDAO;
import Negocio.EditorialDAO;
import Negocio.LibroDAO;
import Negocio.PrestamoDAO;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class Admin extends javax.swing.JFrame {

    Login frmlg;
    public static int idUsuario;
    public static String nombres;

    private UsuarioDAO usuarioDAO;
    private LibroDAO libroDAO;
    private AutorDAO autorDAO;
    private EditorialDAO editorialDAO;
    private PrestamoDAO prestamoDAO;
    private DevolucionesDAO devolucionDAO;

    private DefaultTableModel tablaModelo;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Estudiante> estudiantes;
    private ArrayList<Autor> autores;
    private ArrayList<Libro> libros;
    private ArrayList<Editorial> editoriales;
    private ArrayList<Devolucion> devoluciones;

    private ArrayList<String> estado;

    private ArrayList<Prestamo> prestamo;

    //Autores
    AutorDAO cn = new AutorDAO();
    int idAuto = 0;

    //Editoriales
    EditorialDAO edi = new EditorialDAO();
    int idEdi = 0;

    public static String usu;
    public static String pss;

    public Admin() {
        initComponents();
        this.setTitle(nombres);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        panelUsuarios.setVisible(true);
        panelPrestamos.setVisible(false);
        panelEstudiantes.setVisible(false);
        panelLibros.setVisible(false);
        panelAutor.setVisible(false);
        panelEditorial.setVisible(false);
        txtCod.setVisible(false);

        panelUsuarioAdmin.setVisible(false);
        usuarioDAO = new UsuarioDAO();
        libroDAO = new LibroDAO();
        autorDAO = new AutorDAO();
        editorialDAO = new EditorialDAO();
        prestamoDAO = new PrestamoDAO();
        devolucionDAO = new DevolucionesDAO();

        tablaModelo = new DefaultTableModel();
        tablaEstudiantesModelo = new DefaultTableModel();
        tablaLibrosModelo = new DefaultTableModel();
        tablaEditorialModelo = new DefaultTableModel();
        tablaAutorModelo = new DefaultTableModel();

        usuarios = usuarioDAO.obtenerUsuarios();
        libros = libroDAO.obtenerTodosLosLibros();
        autores = autorDAO.obtenerAutores();
        editoriales = editorialDAO.obtenerTodosLasEditoriales();
        prestamo = prestamoDAO.obtenerTodosLosPrestamos();
        estado = devolucionDAO.obtenerEstadoPrestamo();

        llenarTablaUsuarios();
        llenarComboBoxAutores();
        llenarComboBoxEditoriales();
        llenarComboBoxEstados();
        llenarTablaPrestamos();
    }

    //Método para limpiar el textField
    private void limpiar1() {

        txtNombre.setText("");
        txtEmail.setText("");
        txtUsuarioEstudianteCarrera.setText("");
        txtContraseñaEstudiante.setText("");
        txtUsuarioAdmin.setText("");
        txtContraseñaAdmin.setText("");
        txtCod.setText("");
        txtTelefono.setText("");
        cbTipoUsuario.setSelectedIndex(0);
        panelUsuarioEstudiante.setVisible(false);
        panelUsuarioAdmin.setVisible(true);

    }

    private void limpiar2() {
        txtTitulo.setText("");
        cbAutor.setSelectedIndex(0);
        cbEditorial.setSelectedIndex(0);
        txtCantidad.setText("");

    }

    private void limpiar3() {

        txtidAutor.setText("");
        txtAutor.setText("");
        txtAutor.requestFocus();
    }

    private void limpiar4() {

        txtidEditorial.setText("");
        txtEditorial.setText("");
        txtEditorial.requestFocus();
    }

// Método para llenar la tabla con los usuarios obtenidos
    private void llenarTablaUsuarios() {
        tablaModelo = (DefaultTableModel) tbUsuarios.getModel();
        tablaModelo.setRowCount(0);

        for (Usuario usuario : usuarios) {
            Object[] fila = new Object[8]; // Ajusta el tamaño del arreglo según la cantidad de columnas de tu tabla
            fila[0] = usuario.getId();
            fila[1] = usuario.getNombre();
            fila[2] = usuario.getEmail();
            fila[3] = usuario.getTelefono();
            if (usuario instanceof Estudiante) {
                Estudiante estudiante = (Estudiante) usuario;
                fila[4] = estudiante.getUsuario();
                fila[5] = estudiante.getContraseña();
                fila[6] = estudiante.getCarrera();

            } else if (usuario instanceof Administrador) {
                Administrador administrador = (Administrador) usuario;
                fila[4] = administrador.getUsuario();
                fila[5] = administrador.getContraseña();
            }
            fila[7] = usuario.getTipo(); // Nueva columna para mostrar el tipo de usuario
            tablaModelo.addRow(fila);
        }
    }
    //Metodo para buscar un prestamo segun el nombre de un libro

    private void buscarUsuarioPorNombreLibPres(String nombrelibro) {
        DefaultTableModel modelo = (DefaultTableModel) tbPrestamos.getModel();
        modelo.setRowCount(0); // Limpiar el modelo de la tabla
        ArrayList<devolucionDeta> usuarios = libroDAO.obtenerTodosLosPrestamosLibroPorNom(nombrelibro);
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron estudiantes con el nombre '" + nombrelibro + "'.");
        } else {
            for (devolucionDeta d : usuarios) {
                Object[] fila = new Object[7]; // Ajusta el tamaño del arreglo según la cantidad de columnas de tu tabla
                Prestamo p = new Prestamo();
                p = d.getPrestamo();
                fila[0] = p.getId();
                fila[1] = p.getEstudiante();
                fila[2] = p.getLibro();
                fila[3] = p.getFechaPrestamo();
                fila[4] = p.getFechaDevolucion();
                fila[5] = d.getMulta();
                fila[6] = d.getEstado();

                modelo.addRow(fila);
            }
        }
        modelo.fireTableDataChanged(); // Notificar a la tabla los cambios en los datos
    }

    //Metodo para buscar un prestamo segun el nombre de un estudiante
    private void buscarUsuarioPorNombrePres(String nombreUsuario) {
        DefaultTableModel modelo = (DefaultTableModel) tbPrestamos.getModel();
        modelo.setRowCount(0); // Limpiar el modelo de la tabla
        ArrayList<devolucionDeta> usuarios = usuarioDAO.obtenerTodosLosPrestamosEstudiantesPorNom(nombreUsuario);
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron estudiantes con el nombre '" + nombreUsuario + "'.");
        } else {
            for (devolucionDeta d : usuarios) {
                Object[] fila = new Object[7]; // Ajusta el tamaño del arreglo según la cantidad de columnas de tu tabla
                Prestamo p = new Prestamo();
                p = d.getPrestamo();
                fila[0] = p.getId();
                fila[1] = p.getEstudiante();
                fila[2] = p.getLibro();
                fila[3] = p.getFechaPrestamo();
                fila[4] = p.getFechaDevolucion();
                fila[5] = d.getMulta();
                fila[6] = d.getEstado();

                modelo.addRow(fila);
            }
        }
        modelo.fireTableDataChanged(); // Notificar a la tabla los cambios en los datos
    }

    private DefaultTableModel tablaEstudiantesModelo;

    //LLENAR TABLA DE LISTADO DE ESTUDIANTES
    public void llenarTablaEstudiantes() {
        tablaEstudiantesModelo = (DefaultTableModel) tbEstudiantes.getModel();
        tablaEstudiantesModelo.setRowCount(0);
        estudiantes = (ArrayList<Estudiante>) usuarioDAO.obtenerEstudiantes();
        for (Estudiante estudiante : estudiantes) {
            Object[] fila = {
                estudiante.getId(),
                estudiante.getNombre(),
                estudiante.getUsuario(),
                estudiante.getTelefono(),
                estudiante.getEmail(),
                estudiante.getCarrera()
            };
            tablaEstudiantesModelo.addRow(fila);
        }
    }

    private DefaultTableModel tablaLibrosModelo;

    //LLENAR TABLA DE LISTADO DE LIBROS
    public void llenarTablaLibros() {
        tablaLibrosModelo = (DefaultTableModel) tbLibros.getModel();
        tablaLibrosModelo.setRowCount(0);
        libros = (ArrayList<Libro>) libroDAO.obtenerTodosLosLibros();
        for (Libro libro : libros) {
            Object[] fila = {
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getEditorial(),
                libro.getCantidad()
            };
            tablaLibrosModelo.addRow(fila);
        }
    }

    private DefaultTableModel tablaEditorialModelo;

    //LLENAR TABLA DE LISTADO DE Editorial
    public void llenarTablaEditorial() {
        tablaEditorialModelo = (DefaultTableModel) tbEditorial.getModel();
        tablaEditorialModelo.setRowCount(0);
        editoriales = editorialDAO.obtenerTodosLasEditoriales();
        for (Editorial editorial : editoriales) {
            Object[] fila = {
                editorial.getId(),
                editorial.getNombre()
            };
            tablaEditorialModelo.addRow(fila);
        }
    }

    private DefaultTableModel tablaAutorModelo;

    //LLENAR TABLA DE LISTADO DE Autor
    public void llenarTablaAutor() {
        tablaAutorModelo = (DefaultTableModel) tbAutor.getModel();
        tablaAutorModelo.setRowCount(0);
        autores = autorDAO.obtenerAutores();
        for (Autor autor : autores) {
            Object[] fila = {
                autor.getId(),
                autor.getNombre()
            };
            tablaAutorModelo.addRow(fila);
        }
    }

    //LLENAR TABLA DE PRESTAMOS
    public void llenarTablaPrestamos() {
        DefaultTableModel modelo = (DefaultTableModel) tbPrestamos.getModel();
        modelo.setRowCount(0); // Limpiar la tabla antes de llenarla nuevamente
        //libros = (ArrayList<Libro>) libroDAO.obtenerTodosLosLibros();
        ArrayList<devolucionDeta> prestamos = prestamoDAO.obtenerTodosLosPrestamosEstudiantes();
        // Llena la tabla con los datos de los prestamos
        for (devolucionDeta devolucion : prestamos) {
            Prestamo prestamo = devolucion.getPrestamo();
            double multa = devolucion.getMulta();
            String estado = devolucion.getEstado();
            Object[] fila = {
                prestamo.getId(),
                prestamo.getEstudiante(),
                prestamo.getLibro(),
                prestamo.getFechaPrestamo(),
                prestamo.getFechaDevolucion(),
                multa,
                estado
            };
            modelo.addRow(fila);
        }
        tbPrestamos.setModel(modelo);
    }

    // Método para llenar el JComboBox con los autores registrados
    private void llenarComboBoxAutores() {
        ArrayList<Autor> autores = cn.obtenerAutores(); // Obtener la lista de autores desde AutorDAO
        cbAutor.removeAllItems(); // Limpiar los elementos existentes en el JComboBox
        for (Autor autor : autores) {
            cbAutor.addItem(autor.getNombre()); // Agregar el nombre completo del autor al JComboBox
        }
    }

    // Método para llenar el JComboBox con las editoriales registradas
    private void llenarComboBoxEditoriales() {
        ArrayList<Editorial> editoriales = edi.obtenerTodasLasEditoriales(); // Obtener la lista de editoriales desde EditorialDAO
        cbEditorial.removeAllItems(); // Limpiar los elementos existentes en el JComboBox
        for (Editorial editorial : editoriales) {
            cbEditorial.addItem(editorial.getNombre()); // Agregar el nombre de la editorial al JComboBox
        }
    }

    // Método para llenar el JComboBox con los estados
    private void llenarComboBoxEstados() {
        ArrayList<String> estados = devolucionDAO.obtenerEstadoPrestamo(); // Obtener la lista de estados desde DevolucionDAO
        cbEstado.removeAllItems(); // Limpiar los elementos existentes en el JComboBox
        for (String estado : estados) {
            cbEstado.addItem(estado); // Agregar el estado al JComboBox
        }
    }

    // Método para realizar la búsqueda por nombre de usuario
    private void buscarUsuarioPorNombre(String nombreUsuario) {
        DefaultTableModel modelo = (DefaultTableModel) tbUsuarios.getModel();
        modelo.setRowCount(0); // Limpiar el modelo de la tabla
        ArrayList<Usuario> usuarios = usuarioDAO.buscarUsuarioPorNombre(nombreUsuario);
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron estudiantes con el nombre '" + nombreUsuario + "'.");
        } else {
            for (Usuario usuario : usuarios) {
                Object[] fila = new Object[8]; // Ajusta el tamaño del arreglo según la cantidad de columnas de tu tabla
                fila[0] = usuario.getId();
                fila[1] = usuario.getNombre();
                fila[2] = usuario.getEmail();
                fila[3] = usuario.getTelefono();
                if (usuario instanceof Estudiante) {
                    Estudiante estudiante = (Estudiante) usuario;
                    fila[4] = estudiante.getUsuario();
                    fila[5] = estudiante.getContraseña();
                    fila[6] = estudiante.getCarrera();
                } else if (usuario instanceof Administrador) {
                    Administrador administrador = (Administrador) usuario;
                    fila[4] = administrador.getUsuario();
                    fila[5] = administrador.getContraseña();
                }
                fila[7] = usuario.getTipo(); // Nueva columna para mostrar el tipo de usuario
                modelo.addRow(fila);
            }
        }
        modelo.fireTableDataChanged(); // Notificar a la tabla los cambios en los datos
    }

    //Método para realizar la búsqueda por nombre de estudiante
    private void buscarEstudiantePorNombre(String nombreEstudiante) {
        tablaEstudiantesModelo.setRowCount(0);
        ArrayList<Estudiante> resultados = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Estudiante && usuario.getNombre().equals(nombreEstudiante)) {
                resultados.add((Estudiante) usuario);
            }
        }
        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron estudiantes con el nombre '" + nombreEstudiante + "'.");
        } else {
            for (Estudiante estudiante : resultados) {
                Object[] fila = new Object[5]; // Ajusta el tamaño del arreglo según la cantidad de columnas de tu tabla
                fila[0] = estudiante.getId();
                fila[1] = estudiante.getNombre();
                fila[2] = estudiante.getUsuario();
                fila[3] = estudiante.getTelefono();
                fila[4] = estudiante.getEmail();
                tablaEstudiantesModelo.addRow(fila);
            }
        }
    }

    //Método para realizar la búsqueda por nombre de autor
    private void buscarAutorPorNombre(String nombreAutor) {
        DefaultTableModel modelo = (DefaultTableModel) tbAutor.getModel();
        modelo.setRowCount(0);
        ArrayList<Autor> autoresEncontrados = autorDAO.buscarAutorPorNombre(nombreAutor);
        if (autoresEncontrados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontró ningún autor con el nombre '" + nombreAutor + "'.");
        } else {
            for (Autor autor : autoresEncontrados) {
                Object[] fila = {
                    autor.getId(),
                    autor.getNombre()
                };
                modelo.addRow(fila);
            }
        }
    }

    //Método para realizar la búsqueda por nombre de editorial
    private void buscarEditorialPorNombre(String nombreEditorial) {
        DefaultTableModel modelo = (DefaultTableModel) tbEditorial.getModel();
        modelo.setRowCount(0);
        ArrayList<Editorial> editorialesEncontradas = editorialDAO.buscarEditorialPorNombre(nombreEditorial);
        if (editorialesEncontradas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontró ninguna editorial con el nombre '" + nombreEditorial + "'.");
        } else {
            for (Editorial editorial : editorialesEncontradas) {
                Object[] fila = {
                    editorial.getId(),
                    editorial.getNombre()
                };
                modelo.addRow(fila);
            }
        }
    }

    //Método para realizar la búsqueda por nombre de libro
    private void buscarLibroPorNombre(String nombreLibro) {
        DefaultTableModel modelo = (DefaultTableModel) tbLibros.getModel();
        modelo.setRowCount(0);
        ArrayList<Libro> librosEncontrados = libroDAO.buscarLibroPorTitulo(nombreLibro);
        if (librosEncontrados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontró ningún libro con el nombre '" + nombreLibro + "'.");
        } else {
            for (Libro libro : librosEncontrados) {
                Object[] fila = {
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getEditorial(),
                    libro.getCantidad()
                };
                modelo.addRow(fila);
            }
        }
    }

    // Método para llenar los campos de texto con los datos del usuario seleccionado
    private void cargarDatosUsuarioSeleccionado1() {
        int filaSeleccionada = tbUsuarios.getSelectedRow();
        if (filaSeleccionada != -1) {
            String cod = tbUsuarios.getValueAt(filaSeleccionada, 0).toString();
            String nombre = tbUsuarios.getValueAt(filaSeleccionada, 1).toString();
            String email = tbUsuarios.getValueAt(filaSeleccionada, 2).toString();
            String telefono = tbUsuarios.getValueAt(filaSeleccionada, 3).toString();
            String tipo = tbUsuarios.getValueAt(filaSeleccionada, 7).toString();
            txtCod.setText(cod);
            txtNombre.setText(nombre);
            txtEmail.setText(email);
            txtTelefono.setText(telefono);
            cbTipoUsuario.setSelectedItem(tipo);
            String usuario, contraseña, carrera;
            if (tipo.equals("Estudiante")) {
                usuario = tbUsuarios.getValueAt(filaSeleccionada, 4).toString();
                contraseña = tbUsuarios.getValueAt(filaSeleccionada, 5).toString();
                carrera = tbUsuarios.getValueAt(filaSeleccionada, 6).toString();
                txtUsuarioEstudiante.setText(usuario);
                txtContraseñaEstudiante.setText(contraseña);
                txtUsuarioEstudianteCarrera.setText(carrera);
                // Ocultar campos de Administrador
                txtUsuarioAdmin.setText("");
                txtContraseñaAdmin.setText("");
                panelUsuarioEstudiante.setVisible(true);
                panelUsuarioAdmin.setVisible(false);
            } else if (tipo.equals("Administrador")) {
                usuario = tbUsuarios.getValueAt(filaSeleccionada, 4).toString();
                contraseña = tbUsuarios.getValueAt(filaSeleccionada, 5).toString();
                txtUsuarioAdmin.setText(usuario);
                txtContraseñaAdmin.setText(contraseña);
                // Ocultar campos de Estudiante
                txtUsuarioEstudiante.setText("");
                txtContraseñaEstudiante.setText("");
                txtUsuarioEstudianteCarrera.setText("");
                panelUsuarioEstudiante.setVisible(false);
                panelUsuarioAdmin.setVisible(true);
            }
        }
    }

    // Método para llenar los campos de texto con los datos de la tabla libros
    private void cargarDatosUsuarioSeleccionado2() {
        int filaSeleccionada = tbLibros.getSelectedRow();
        if (filaSeleccionada != -1) {
            String titulo = tbLibros.getValueAt(filaSeleccionada, 1).toString();
            String autorNombre = tbLibros.getValueAt(filaSeleccionada, 2).toString();
            String editorialNombre = tbLibros.getValueAt(filaSeleccionada, 3).toString();
            String cantidad = tbLibros.getValueAt(filaSeleccionada, 4).toString();
            txtTitulo.setText(titulo);
            cbAutor.setSelectedItem(autorNombre);
            cbEditorial.setSelectedItem(editorialNombre);
            txtCantidad.setText(cantidad);
        }
    }

    // Método para llenar los campos de texto con los datos de la tabla autor
    private void cargarDatosUsuarioSeleccionado3() {
        int row = tbAutor.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "No se Selecciono");
        } else {
            idAuto = Integer.parseInt((String) tbAutor.getValueAt(row, 0).toString());
            String autor = (String) tbAutor.getValueAt(row, 1);
            txtidAutor.setText("" + idAuto);
            txtAutor.setText(autor);
        }

    }

    // Método para llenar los campos de texto con los datos de la tabla prestamos
    private void cargarDatosUsuarioSeleccionado4() {
        int filaSeleccionada = tbPrestamos.getSelectedRow();
        if (filaSeleccionada != -1) {
            LocalDate fechaPrestamo = LocalDate.parse(tbPrestamos.getValueAt(filaSeleccionada, 3).toString());
            LocalDate fechaDevolucion = LocalDate.parse(tbPrestamos.getValueAt(filaSeleccionada, 4).toString());
            String estadoNombre = tbPrestamos.getValueAt(filaSeleccionada, 6).toString();
            // String editorialNombre = tbLibros.getValueAt(filaSeleccionada, 3).toString();
            txtFechaPrestamo.setText(fechaPrestamo.toString());
            txtFechaDevolucion.setText(fechaDevolucion.toString());
            cbEstado.setSelectedItem(estadoNombre);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        barraDeOpciones = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnUsuarios = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnEstudiantes = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnLibros = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnPrestamos = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        btnAutor = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        btnEditorial = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        btnGenerarReporte = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        panelUsuarios = new javax.swing.JPanel();
        panelNuevoEstudiante = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnRegistrar = new javax.swing.JButton();
        btnEliminarUsuario = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cbTipoUsuario = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        txtCod = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        panelUs = new javax.swing.JPanel();
        panelUsuarioEstudiante = new javax.swing.JPanel();
        txtUsuarioEstudianteCarrera = new javax.swing.JTextField();
        txtContraseñaEstudiante = new javax.swing.JPasswordField();
        txtUsuarioEstudiante = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        panelUsuarioAdmin = new javax.swing.JPanel();
        txtUsuarioAdmin = new javax.swing.JTextField();
        txtContraseñaAdmin = new javax.swing.JPasswordField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbUsuarios = new javax.swing.JTable();
        txtBuscarUsuario = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        panelEstudiantes = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbEstudiantes = new javax.swing.JTable();
        btnBuscarEst = new javax.swing.JButton();
        txtBuscarEstudiante = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        panelLibros = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbLibros = new javax.swing.JTable();
        txtBuscarLibro = new javax.swing.JTextField();
        btnBuscarLibro = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtTitulo = new javax.swing.JTextField();
        cbAutor = new javax.swing.JComboBox<>();
        cbEditorial = new javax.swing.JComboBox<>();
        txtCantidad = new javax.swing.JTextField();
        btnRegistrarLibro = new javax.swing.JButton();
        btnLimpiarLibro = new javax.swing.JButton();
        btnEliminarLibro = new javax.swing.JButton();
        btnActualizarLibro = new javax.swing.JButton();
        panelPrestamos = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbPrestamos = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtFechaDevolucion = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtFechaPrestamo = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        cbEstado = new javax.swing.JComboBox<>();
        btnActualizaPrestamo = new javax.swing.JButton();
        txtBuscarEstu = new javax.swing.JTextField();
        btnBuscarPrestamo = new javax.swing.JButton();
        txtBuscarEst = new javax.swing.JTextField();
        btnBuscarLibro1 = new javax.swing.JButton();
        panelAutor = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbAutor = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtAutor = new javax.swing.JTextField();
        btnLimpiarAutor = new javax.swing.JButton();
        btnEliminarAutor = new javax.swing.JButton();
        btnAgregarAutor = new javax.swing.JButton();
        txtidAutor = new javax.swing.JTextField();
        btnModificarAutor = new javax.swing.JButton();
        txtBuscarAutor = new javax.swing.JTextField();
        btnBuscarAutor = new javax.swing.JButton();
        panelEditorial = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbEditorial = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtEditorial = new javax.swing.JTextField();
        btnEliminarEditorial = new javax.swing.JButton();
        btnRegistrarEditorial = new javax.swing.JButton();
        btnLimpiarEditorial = new javax.swing.JButton();
        btnModificarEditorial = new javax.swing.JButton();
        txtidEditorial = new javax.swing.JTextField();
        txtBuscarEditorial = new javax.swing.JTextField();
        btnBuscarEditorial = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        barraDeOpciones.setBackground(new java.awt.Color(255, 255, 255));
        barraDeOpciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnUsuarios.setFont(new java.awt.Font("Dubai Light", 1, 18)); // NOI18N
        btnUsuarios.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/usuarios.png"))); // NOI18N
        btnUsuarios.setText("Usuarios");
        btnUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUsuariosMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(btnUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        barraDeOpciones.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 180, 40));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnEstudiantes.setFont(new java.awt.Font("Dubai Light", 1, 18)); // NOI18N
        btnEstudiantes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnEstudiantes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/student.png"))); // NOI18N
        btnEstudiantes.setText("Estudiantes");
        btnEstudiantes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEstudiantesMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btnEstudiantes, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnEstudiantes, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        barraDeOpciones.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 180, 40));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnLibros.setFont(new java.awt.Font("Dubai Light", 1, 18)); // NOI18N
        btnLibros.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnLibros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/books.png"))); // NOI18N
        btnLibros.setText("Libros");
        btnLibros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLibrosMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(btnLibros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnLibros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        barraDeOpciones.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 180, 40));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnPrestamos.setFont(new java.awt.Font("Dubai Light", 1, 18)); // NOI18N
        btnPrestamos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnPrestamos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/give.png"))); // NOI18N
        btnPrestamos.setText("Devolución");
        btnPrestamos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPrestamosMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(btnPrestamos, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnPrestamos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        barraDeOpciones.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 180, 40));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnAutor.setFont(new java.awt.Font("Dubai Light", 1, 18)); // NOI18N
        btnAutor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnAutor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/autor.png"))); // NOI18N
        btnAutor.setText("Autor");
        btnAutor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAutorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(btnAutor, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnAutor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        barraDeOpciones.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 180, 40));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnEditorial.setFont(new java.awt.Font("Dubai Light", 1, 18)); // NOI18N
        btnEditorial.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnEditorial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/editorial.png"))); // NOI18N
        btnEditorial.setText("Editorial");
        btnEditorial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditorialMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(btnEditorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnEditorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        barraDeOpciones.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 180, 40));

        jPanel15.setBackground(new java.awt.Color(0, 0, 0));
        jPanel15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnGenerarReporte.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        btnGenerarReporte.setForeground(new java.awt.Color(255, 255, 255));
        btnGenerarReporte.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnGenerarReporte.setText("Generar Reporte");
        btnGenerarReporte.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGenerarReporteMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(btnGenerarReporte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnGenerarReporte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        barraDeOpciones.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 180, 40));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logo_colegio.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Eras Bold ITC", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("I.E. Emblemática ");

        jLabel7.setFont(new java.awt.Font("Eras Bold ITC", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Jose María Eguren");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel4)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(barraDeOpciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barraDeOpciones, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setForeground(new java.awt.Color(255, 255, 255));

        panelUsuarios.setBackground(new java.awt.Color(255, 255, 255));

        panelNuevoEstudiante.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelNuevoEstudiante.setForeground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Dubai Light", 1, 14)); // NOI18N
        jLabel6.setText("Nuevo Usuario");

        btnRegistrar.setBackground(new java.awt.Color(0, 0, 0));
        btnRegistrar.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        btnEliminarUsuario.setBackground(new java.awt.Color(0, 0, 0));
        btnEliminarUsuario.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnEliminarUsuario.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Trash_3.png"))); // NOI18N
        btnEliminarUsuario.setText("Eliminar");
        btnEliminarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarUsuarioActionPerformed(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(0, 0, 0));
        btnLimpiar.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Erase.png"))); // NOI18N
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Tipo");

        cbTipoUsuario.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        cbTipoUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar", "Administrador", "Estudiante" }));
        cbTipoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoUsuarioActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Nombre");

        jLabel19.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Email");

        txtCod.setEditable(false);

        jLabel23.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Telefono");

        txtUsuarioEstudianteCarrera.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        txtUsuarioEstudianteCarrera.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUsuarioEstudianteCarrera.setText("ejm: ing. industrial");
        txtUsuarioEstudianteCarrera.setBorder(null);
        txtUsuarioEstudianteCarrera.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsuarioEstudianteCarreraFocusGained(evt);
            }
        });

        txtContraseñaEstudiante.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        txtContraseñaEstudiante.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtContraseñaEstudiante.setText("********");
        txtContraseñaEstudiante.setBorder(null);
        txtContraseñaEstudiante.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtContraseñaEstudianteFocusGained(evt);
            }
        });
        txtContraseñaEstudiante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContraseñaEstudianteActionPerformed(evt);
            }
        });

        txtUsuarioEstudiante.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        txtUsuarioEstudiante.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUsuarioEstudiante.setText("username");
        txtUsuarioEstudiante.setBorder(null);
        txtUsuarioEstudiante.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsuarioEstudianteFocusGained(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        jLabel1.setText("Carrera");

        javax.swing.GroupLayout panelUsuarioEstudianteLayout = new javax.swing.GroupLayout(panelUsuarioEstudiante);
        panelUsuarioEstudiante.setLayout(panelUsuarioEstudianteLayout);
        panelUsuarioEstudianteLayout.setHorizontalGroup(
            panelUsuarioEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUsuarioEstudianteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelUsuarioEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtContraseñaEstudiante)
                    .addComponent(txtUsuarioEstudiante)
                    .addGroup(panelUsuarioEstudianteLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(txtUsuarioEstudianteCarrera, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelUsuarioEstudianteLayout.setVerticalGroup(
            panelUsuarioEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUsuarioEstudianteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelUsuarioEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsuarioEstudianteCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsuarioEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(txtContraseñaEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtUsuarioAdmin.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        txtUsuarioAdmin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUsuarioAdmin.setText("username");
        txtUsuarioAdmin.setBorder(null);
        txtUsuarioAdmin.setPreferredSize(new java.awt.Dimension(33, 21));
        txtUsuarioAdmin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsuarioAdminFocusGained(evt);
            }
        });

        txtContraseñaAdmin.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        txtContraseñaAdmin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtContraseñaAdmin.setText("********");
        txtContraseñaAdmin.setBorder(null);
        txtContraseñaAdmin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtContraseñaAdminFocusGained(evt);
            }
        });
        txtContraseñaAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContraseñaAdminActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelUsuarioAdminLayout = new javax.swing.GroupLayout(panelUsuarioAdmin);
        panelUsuarioAdmin.setLayout(panelUsuarioAdminLayout);
        panelUsuarioAdminLayout.setHorizontalGroup(
            panelUsuarioAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUsuarioAdminLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelUsuarioAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtContraseñaAdmin)
                    .addComponent(txtUsuarioAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelUsuarioAdminLayout.setVerticalGroup(
            panelUsuarioAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUsuarioAdminLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtUsuarioAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtContraseñaAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelUsLayout = new javax.swing.GroupLayout(panelUs);
        panelUs.setLayout(panelUsLayout);
        panelUsLayout.setHorizontalGroup(
            panelUsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelUsuarioEstudiante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelUsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelUsLayout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(panelUsuarioAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(12, Short.MAX_VALUE)))
        );
        panelUsLayout.setVerticalGroup(
            panelUsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUsLayout.createSequentialGroup()
                .addComponent(panelUsuarioEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelUsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelUsLayout.createSequentialGroup()
                    .addGap(28, 28, 28)
                    .addComponent(panelUsuarioAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(8, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout panelNuevoEstudianteLayout = new javax.swing.GroupLayout(panelNuevoEstudiante);
        panelNuevoEstudiante.setLayout(panelNuevoEstudianteLayout);
        panelNuevoEstudianteLayout.setHorizontalGroup(
            panelNuevoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNuevoEstudianteLayout.createSequentialGroup()
                .addGroup(panelNuevoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNuevoEstudianteLayout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(jLabel6)
                        .addGap(37, 37, 37)
                        .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(panelNuevoEstudianteLayout.createSequentialGroup()
                        .addGroup(panelNuevoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelNuevoEstudianteLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(panelNuevoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelNuevoEstudianteLayout.createSequentialGroup()
                                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelNuevoEstudianteLayout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelNuevoEstudianteLayout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cbTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelNuevoEstudianteLayout.createSequentialGroup()
                                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panelNuevoEstudianteLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(panelNuevoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panelUs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelNuevoEstudianteLayout.createSequentialGroup()
                                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnEliminarUsuario)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 3, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelNuevoEstudianteLayout.setVerticalGroup(
            panelNuevoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNuevoEstudianteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelNuevoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelNuevoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelNuevoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelNuevoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelNuevoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelUs, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelNuevoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNuevoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnLimpiar)
                        .addComponent(btnRegistrar))
                    .addComponent(btnEliminarUsuario, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jLabel8.setFont(new java.awt.Font("Dubai Light", 0, 24)); // NOI18N
        jLabel8.setText("ADMINISTRAR USUARIOS");

        tbUsuarios.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        tbUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Cod", "Nombre", "Email", "Telefono", "Usuario", "Contraseña", "Carrera", "Tipo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbUsuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbUsuarios);

        txtBuscarUsuario.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        txtBuscarUsuario.setText("Ingresar nombre de usuario a buscar");
        txtBuscarUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarUsuarioFocusGained(evt);
            }
        });

        btnBuscar.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnActualizar.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/actualizar.png"))); // NOI18N
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelUsuariosLayout = new javax.swing.GroupLayout(panelUsuarios);
        panelUsuarios.setLayout(panelUsuariosLayout);
        panelUsuariosLayout.setHorizontalGroup(
            panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUsuariosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelNuevoEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelUsuariosLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelUsuariosLayout.createSequentialGroup()
                                .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelUsuariosLayout.createSequentialGroup()
                                        .addComponent(txtBuscarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(panelUsuariosLayout.createSequentialGroup()
                                        .addGap(23, 23, 23)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(20, 20, 20))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUsuariosLayout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addContainerGap())))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUsuariosLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        panelUsuariosLayout.setVerticalGroup(
            panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUsuariosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnActualizar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelUsuariosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelNuevoEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelEstudiantes.setBackground(new java.awt.Color(255, 255, 255));
        panelEstudiantes.setPreferredSize(new java.awt.Dimension(408, 322));

        jLabel9.setFont(new java.awt.Font("Dubai Light", 0, 24)); // NOI18N
        jLabel9.setText("LISTADO DE ESTUDIANTES");

        tbEstudiantes.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        tbEstudiantes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Usuario", "Teléfono", "Correo"
            }
        ));
        jScrollPane2.setViewportView(tbEstudiantes);

        btnBuscarEst.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnBuscarEst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        btnBuscarEst.setText("Buscar");
        btnBuscarEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarEstActionPerformed(evt);
            }
        });

        txtBuscarEstudiante.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        txtBuscarEstudiante.setText("Ingresar nombre ");
        txtBuscarEstudiante.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarEstudianteFocusGained(evt);
            }
        });

        jButton1.setText("Modificar");

        jButton2.setText("Eliminar");

        javax.swing.GroupLayout panelEstudiantesLayout = new javax.swing.GroupLayout(panelEstudiantes);
        panelEstudiantes.setLayout(panelEstudiantesLayout);
        panelEstudiantesLayout.setHorizontalGroup(
            panelEstudiantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEstudiantesLayout.createSequentialGroup()
                .addGroup(panelEstudiantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEstudiantesLayout.createSequentialGroup()
                        .addGroup(panelEstudiantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelEstudiantesLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelEstudiantesLayout.createSequentialGroup()
                                .addGap(100, 100, 100)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(txtBuscarEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscarEst, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelEstudiantesLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );
        panelEstudiantesLayout.setVerticalGroup(
            panelEstudiantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEstudiantesLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(panelEstudiantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEstudiantesLayout.createSequentialGroup()
                        .addGroup(panelEstudiantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBuscarEst)
                            .addComponent(txtBuscarEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11))
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(238, 238, 238)
                .addGroup(panelEstudiantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelLibros.setBackground(new java.awt.Color(255, 255, 255));
        panelLibros.setPreferredSize(new java.awt.Dimension(417, 303));

        jLabel10.setFont(new java.awt.Font("Dubai Light", 0, 24)); // NOI18N
        jLabel10.setText("ADMINISTRAR LIBROS");

        tbLibros.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        tbLibros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Titulo", "Autor", "Editorial", "Cantidad"
            }
        ));
        tbLibros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbLibrosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbLibros);

        txtBuscarLibro.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        txtBuscarLibro.setText("Ingresar nombre del libro");
        txtBuscarLibro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarLibroFocusGained(evt);
            }
        });

        btnBuscarLibro.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnBuscarLibro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        btnBuscarLibro.setText("Buscar");
        btnBuscarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarLibroActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Dubai Light", 0, 14)); // NOI18N
        jLabel24.setText("Nuevo Libro");

        jLabel25.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        jLabel25.setText("Titulo");

        jLabel26.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        jLabel26.setText("Autor");

        jLabel27.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        jLabel27.setText("Editorial");

        jLabel28.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        jLabel28.setText("Cantidad");

        txtTitulo.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N

        cbAutor.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        cbAutor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar", "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbEditorial.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        cbEditorial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar", "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtCantidad.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTitulo))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbAutor, 0, 156, Short.MAX_VALUE)
                                    .addComponent(cbEditorial, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(cbEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        btnRegistrarLibro.setBackground(new java.awt.Color(0, 0, 0));
        btnRegistrarLibro.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnRegistrarLibro.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrarLibro.setText("Registrar");
        btnRegistrarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarLibroActionPerformed(evt);
            }
        });

        btnLimpiarLibro.setBackground(new java.awt.Color(0, 0, 0));
        btnLimpiarLibro.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnLimpiarLibro.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarLibro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Erase.png"))); // NOI18N
        btnLimpiarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarLibroActionPerformed(evt);
            }
        });

        btnEliminarLibro.setBackground(new java.awt.Color(0, 0, 0));
        btnEliminarLibro.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnEliminarLibro.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarLibro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Trash_3.png"))); // NOI18N
        btnEliminarLibro.setText("Eliminar");
        btnEliminarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarLibroActionPerformed(evt);
            }
        });

        btnActualizarLibro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/actualizar.png"))); // NOI18N
        btnActualizarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarLibroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLibrosLayout = new javax.swing.GroupLayout(panelLibros);
        panelLibros.setLayout(panelLibrosLayout);
        panelLibrosLayout.setHorizontalGroup(
            panelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLibrosLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(panelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLibrosLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelLibrosLayout.createSequentialGroup()
                                .addComponent(btnActualizarLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLimpiarLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEliminarLibro)
                                .addGap(1, 1, 1)
                                .addComponent(btnRegistrarLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1))
                            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panelLibrosLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscarLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscarLibro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(11, 11, 11))
        );
        panelLibrosLayout.setVerticalGroup(
            panelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLibrosLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(panelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtBuscarLibro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarLibro))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLibrosLayout.createSequentialGroup()
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                        .addGroup(panelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnActualizarLibro)
                            .addComponent(btnLimpiarLibro)
                            .addGroup(panelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnEliminarLibro)
                                .addComponent(btnRegistrarLibro))))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelPrestamos.setBackground(new java.awt.Color(255, 255, 255));
        panelPrestamos.setPreferredSize(new java.awt.Dimension(417, 303));

        jLabel11.setFont(new java.awt.Font("Dubai Light", 0, 24)); // NOI18N
        jLabel11.setText("DEVOLUCIONES");

        tbPrestamos.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        tbPrestamos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Estudiante", "Libro", "F Prestamo", "F Devolución", "Multa", "Estado"
            }
        ));
        tbPrestamos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPrestamosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbPrestamos);

        jLabel14.setFont(new java.awt.Font("Dubai Light", 0, 14)); // NOI18N
        jLabel14.setText("Nuevo Préstamo");

        jLabel17.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("F Devolución");

        txtFechaDevolucion.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N

        jLabel30.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("F Prestamo");

        txtFechaPrestamo.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        txtFechaPrestamo.setEnabled(false);

        jLabel31.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Estado:");

        cbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(txtFechaPrestamo, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                            .addComponent(jLabel14)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel31))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtFechaDevolucion)
                            .addComponent(cbEstado, 0, 102, Short.MAX_VALUE))))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFechaPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtFechaDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        btnActualizaPrestamo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/actualizar.png"))); // NOI18N
        btnActualizaPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizaPrestamoActionPerformed(evt);
            }
        });

        txtBuscarEstu.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        txtBuscarEstu.setText("Buscar estudiante");
        txtBuscarEstu.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarEstuFocusGained(evt);
            }
        });

        btnBuscarPrestamo.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnBuscarPrestamo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        btnBuscarPrestamo.setText("Buscar");
        btnBuscarPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPrestamoActionPerformed(evt);
            }
        });

        txtBuscarEst.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        txtBuscarEst.setText("Ingresar nombre del libro");
        txtBuscarEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarEstFocusGained(evt);
            }
        });

        btnBuscarLibro1.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnBuscarLibro1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        btnBuscarLibro1.setText("Buscar");
        btnBuscarLibro1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarLibro1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPrestamosLayout = new javax.swing.GroupLayout(panelPrestamos);
        panelPrestamos.setLayout(panelPrestamosLayout);
        panelPrestamosLayout.setHorizontalGroup(
            panelPrestamosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrestamosLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelPrestamosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPrestamosLayout.createSequentialGroup()
                        .addGroup(panelPrestamosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPrestamosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(panelPrestamosLayout.createSequentialGroup()
                                .addComponent(txtBuscarEst, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarLibro1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(8, 8, 8))))
                    .addGroup(panelPrestamosLayout.createSequentialGroup()
                        .addComponent(txtBuscarEstu, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscarPrestamo, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnActualizaPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelPrestamosLayout.setVerticalGroup(
            panelPrestamosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrestamosLayout.createSequentialGroup()
                .addGroup(panelPrestamosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPrestamosLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelPrestamosLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(panelPrestamosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscarEst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscarLibro1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelPrestamosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnActualizaPrestamo)
                            .addComponent(txtBuscarEstu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscarPrestamo))))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        panelAutor.setBackground(new java.awt.Color(255, 255, 255));
        panelAutor.setPreferredSize(new java.awt.Dimension(417, 303));

        jLabel12.setFont(new java.awt.Font("Dubai Light", 0, 24)); // NOI18N
        jLabel12.setText("AGREGAR AUTOR");

        tbAutor.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        tbAutor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Nombre de autor"
            }
        ));
        tbAutor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbAutorMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tbAutor);

        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel18.setFont(new java.awt.Font("Dubai Light", 0, 14)); // NOI18N
        jLabel18.setText("ID");

        jLabel20.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        jLabel20.setText("Nombre");

        txtAutor.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N

        btnLimpiarAutor.setBackground(new java.awt.Color(0, 0, 0));
        btnLimpiarAutor.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnLimpiarAutor.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarAutor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Erase.png"))); // NOI18N
        btnLimpiarAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarAutorActionPerformed(evt);
            }
        });

        btnEliminarAutor.setBackground(new java.awt.Color(0, 0, 0));
        btnEliminarAutor.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnEliminarAutor.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarAutor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Trash_3.png"))); // NOI18N
        btnEliminarAutor.setText("Eliminar");
        btnEliminarAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarAutorActionPerformed(evt);
            }
        });

        btnAgregarAutor.setBackground(new java.awt.Color(0, 0, 0));
        btnAgregarAutor.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnAgregarAutor.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarAutor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        btnAgregarAutor.setText("Registrar");
        btnAgregarAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarAutorActionPerformed(evt);
            }
        });

        txtidAutor.setEditable(false);
        txtidAutor.setEnabled(false);

        btnModificarAutor.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnModificarAutor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/actualizar.png"))); // NOI18N
        btnModificarAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarAutorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtidAutor, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                            .addComponent(txtAutor))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificarAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(btnLimpiarAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(btnAgregarAutor)))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtidAutor)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(txtAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEliminarAutor)
                            .addComponent(btnLimpiarAutor)
                            .addComponent(btnAgregarAutor)))
                    .addComponent(btnModificarAutor))
                .addGap(16, 16, 16))
        );

        txtBuscarAutor.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        txtBuscarAutor.setText("Ingrese nombre de autor");
        txtBuscarAutor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarAutorFocusGained(evt);
            }
        });

        btnBuscarAutor.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnBuscarAutor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        btnBuscarAutor.setText("Buscar");
        btnBuscarAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarAutorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAutorLayout = new javax.swing.GroupLayout(panelAutor);
        panelAutor.setLayout(panelAutorLayout);
        panelAutorLayout.setHorizontalGroup(
            panelAutorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAutorLayout.createSequentialGroup()
                .addGroup(panelAutorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAutorLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(txtBuscarAutor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(panelAutorLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelAutorLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        panelAutorLayout.setVerticalGroup(
            panelAutorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAutorLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(panelAutorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelAutorLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(panelAutorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscarAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscarAutor))))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        panelEditorial.setBackground(new java.awt.Color(255, 255, 255));
        panelEditorial.setPreferredSize(new java.awt.Dimension(417, 303));

        jLabel13.setFont(new java.awt.Font("Dubai Light", 0, 24)); // NOI18N
        jLabel13.setText("AGREGAR EDITORIAL");

        tbEditorial.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        tbEditorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Nombre de Editorial"
            }
        ));
        tbEditorial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbEditorialMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tbEditorial);

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel21.setFont(new java.awt.Font("Dubai Light", 0, 14)); // NOI18N
        jLabel21.setText("ID");

        jLabel22.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        jLabel22.setText("Editorial");

        txtEditorial.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N

        btnEliminarEditorial.setBackground(new java.awt.Color(0, 0, 0));
        btnEliminarEditorial.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnEliminarEditorial.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarEditorial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Trash_3.png"))); // NOI18N
        btnEliminarEditorial.setText("Eliminar");
        btnEliminarEditorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarEditorialActionPerformed(evt);
            }
        });

        btnRegistrarEditorial.setBackground(new java.awt.Color(0, 0, 0));
        btnRegistrarEditorial.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnRegistrarEditorial.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrarEditorial.setText("Registrar");
        btnRegistrarEditorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarEditorialActionPerformed(evt);
            }
        });

        btnLimpiarEditorial.setBackground(new java.awt.Color(0, 0, 0));
        btnLimpiarEditorial.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnLimpiarEditorial.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarEditorial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Erase.png"))); // NOI18N
        btnLimpiarEditorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarEditorialActionPerformed(evt);
            }
        });

        btnModificarEditorial.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnModificarEditorial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/actualizar.png"))); // NOI18N
        btnModificarEditorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarEditorialActionPerformed(evt);
            }
        });

        txtidEditorial.setEditable(false);
        txtidEditorial.setEnabled(false);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(txtEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnModificarEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtidEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(btnLimpiarEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminarEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRegistrarEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtidEditorial))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(txtEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnLimpiarEditorial)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnEliminarEditorial)
                                .addComponent(btnRegistrarEditorial))))
                    .addComponent(btnModificarEditorial))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        txtBuscarEditorial.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        txtBuscarEditorial.setText("Ingrese nombre de editorial");
        txtBuscarEditorial.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarEditorialFocusGained(evt);
            }
        });

        btnBuscarEditorial.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnBuscarEditorial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        btnBuscarEditorial.setText("Buscar");
        btnBuscarEditorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarEditorialActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelEditorialLayout = new javax.swing.GroupLayout(panelEditorial);
        panelEditorial.setLayout(panelEditorialLayout);
        panelEditorialLayout.setHorizontalGroup(
            panelEditorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditorialLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelEditorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEditorialLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(panelEditorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelEditorialLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelEditorialLayout.createSequentialGroup()
                                .addComponent(txtBuscarEditorial)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(13, 13, 13))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEditorialLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51))))
        );
        panelEditorialLayout.setVerticalGroup(
            panelEditorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditorialLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(panelEditorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEditorialLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addGroup(panelEditorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBuscarEditorial)
                            .addComponent(txtBuscarEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelEstudiantes, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                    .addComponent(panelLibros, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                    .addComponent(panelPrestamos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                    .addComponent(panelAutor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                    .addComponent(panelEditorial, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                    .addComponent(panelUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelEstudiantes, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelLibros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelPrestamos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 690, 420));

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/portada.jpg"))); // NOI18N
        jLabel29.setText("jLabel29");
        jPanel7.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 720, 190));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUsuariosMouseClicked
        panelUsuarios.setVisible(true);
        panelPrestamos.setVisible(false);
        panelEstudiantes.setVisible(false);
        panelLibros.setVisible(false);
        panelAutor.setVisible(false);
        panelEditorial.setVisible(false);

    }//GEN-LAST:event_btnUsuariosMouseClicked

    private void btnEstudiantesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEstudiantesMouseClicked
        panelUsuarios.setVisible(false);
        panelPrestamos.setVisible(false);
        panelEstudiantes.setVisible(true);
        panelLibros.setVisible(false);
        panelAutor.setVisible(false);
        panelEditorial.setVisible(false);

        llenarTablaEstudiantes();
    }//GEN-LAST:event_btnEstudiantesMouseClicked

    private void btnLibrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLibrosMouseClicked
        panelUsuarios.setVisible(false);
        panelPrestamos.setVisible(false);
        panelEstudiantes.setVisible(false);
        panelLibros.setVisible(true);
        panelAutor.setVisible(false);
        panelEditorial.setVisible(false);
        llenarTablaLibros();
    }//GEN-LAST:event_btnLibrosMouseClicked

    private void btnPrestamosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrestamosMouseClicked
        panelUsuarios.setVisible(false);
        panelPrestamos.setVisible(true);
        panelEstudiantes.setVisible(false);
        panelLibros.setVisible(false);
        panelAutor.setVisible(false);
        panelEditorial.setVisible(false);
        llenarTablaPrestamos();
    }//GEN-LAST:event_btnPrestamosMouseClicked

    private void txtContraseñaEstudianteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContraseñaEstudianteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContraseñaEstudianteActionPerformed

    private void txtContraseñaAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContraseñaAdminActionPerformed

    }//GEN-LAST:event_txtContraseñaAdminActionPerformed

    private void cbTipoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoUsuarioActionPerformed
        if (cbTipoUsuario.getSelectedIndex() == 1) {//VELOCIDAD
            panelUsuarioAdmin.setVisible(true);
            panelUsuarioEstudiante.setVisible(false);
        } else if (cbTipoUsuario.getSelectedIndex() == 2) { //TIEMPO
            panelUsuarioAdmin.setVisible(false);
            panelUsuarioEstudiante.setVisible(true);
        }

    }//GEN-LAST:event_cbTipoUsuarioActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        if (evt.getSource() == cbTipoUsuario) {
            String tipo = cbTipoUsuario.getSelectedItem().toString();

            // Mostrar u ocultar los paneles según el tipo seleccionado
            if (tipo.equals("Estudiante")) {
                panelUsuarioEstudiante.setVisible(true);
                panelUsuarioAdmin.setVisible(false);
            } else if (tipo.equals("Administrador")) {
                panelUsuarioEstudiante.setVisible(false);
                panelUsuarioAdmin.setVisible(true);
            }
        } else if (evt.getSource() == btnRegistrar) {
            // Obtener los datos de los campos de entrada
            String nombre = txtNombre.getText();
            String email = txtEmail.getText();
            String tipo = cbTipoUsuario.getSelectedItem().toString();
            String telefono = txtTelefono.getText();

            // Obtener los datos específicos según el tipo de usuario
            String usuario, contraseña, carrera = "";
            if (tipo.equals("Estudiante")) {
                usuario = txtUsuarioEstudiante.getText();
                contraseña = txtContraseñaEstudiante.getText();
                carrera = txtUsuarioEstudianteCarrera.getText();
            } else {
                usuario = txtUsuarioAdmin.getText();
                contraseña = txtContraseñaAdmin.getText();
            }

            // Verificar que todos los campos estén completos
            if (nombre.isEmpty() || email.isEmpty() || tipo.isEmpty() || telefono.isEmpty()
                    || (tipo.equals("Estudiante") && (usuario.isEmpty() || contraseña.isEmpty() || carrera.isEmpty()))) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Llamar al método del UsuarioDAO para registrar el usuario
            boolean registroExitoso = usuarioDAO.agregarUsuario(nombre, email, usuario, contraseña, tipo, telefono, carrera);
            if (registroExitoso) {
                JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente.");
                // Actualizar la tabla
                usuarios = usuarioDAO.obtenerUsuarios();
                llenarTablaUsuarios();
                llenarTablaEstudiantes();

                // Restablecer los campos de entrada
                limpiar1();
            } else {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al registrar el usuario.");
            }
        }
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void txtContraseñaEstudianteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContraseñaEstudianteFocusGained
        txtContraseñaEstudiante.setText("");
    }//GEN-LAST:event_txtContraseñaEstudianteFocusGained

    private void txtContraseñaAdminFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContraseñaAdminFocusGained
        txtContraseñaAdmin.setText("");
    }//GEN-LAST:event_txtContraseñaAdminFocusGained

    private void txtUsuarioEstudianteCarreraFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsuarioEstudianteCarreraFocusGained
        // TODO add your handling code here:
        txtUsuarioEstudianteCarrera.setText("");
    }//GEN-LAST:event_txtUsuarioEstudianteCarreraFocusGained

    private void txtUsuarioAdminFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsuarioAdminFocusGained
        txtUsuarioAdmin.setText("");
    }//GEN-LAST:event_txtUsuarioAdminFocusGained

    private void txtBuscarUsuarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarUsuarioFocusGained
        txtBuscarUsuario.setText("");
    }//GEN-LAST:event_txtBuscarUsuarioFocusGained

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        String nombreUsuario = txtBuscarUsuario.getText();
        buscarUsuarioPorNombre(nombreUsuario);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtBuscarEstudianteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarEstudianteFocusGained
        txtBuscarEstudiante.setText("");
    }//GEN-LAST:event_txtBuscarEstudianteFocusGained

    private void btnBuscarEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarEstActionPerformed
        String nombreEstudiante = txtBuscarEstudiante.getText();
        buscarEstudiantePorNombre(nombreEstudiante);
    }//GEN-LAST:event_btnBuscarEstActionPerformed

    private void btnEditorialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditorialMouseClicked
        // TODO add your handling code here:
        panelUsuarios.setVisible(false);
        panelPrestamos.setVisible(false);
        panelEstudiantes.setVisible(false);
        panelLibros.setVisible(false);
        panelAutor.setVisible(false);
        panelEditorial.setVisible(true);
        llenarTablaEditorial();
    }//GEN-LAST:event_btnEditorialMouseClicked

    private void btnAutorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAutorMouseClicked
        panelUsuarios.setVisible(false);
        panelPrestamos.setVisible(false);
        panelEstudiantes.setVisible(false);
        panelLibros.setVisible(false);
        panelAutor.setVisible(true);
        panelEditorial.setVisible(false);
        llenarTablaAutor();
    }//GEN-LAST:event_btnAutorMouseClicked

    private void tbUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbUsuariosMouseClicked
        cargarDatosUsuarioSeleccionado1();
    }//GEN-LAST:event_tbUsuariosMouseClicked

//SUS
    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        int filaSeleccionada = tbUsuarios.getSelectedRow();
        if (filaSeleccionada != -1) {
            int cod = Integer.parseInt(txtCod.getText());
            String nombre = txtNombre.getText();
            String email = txtEmail.getText();
            String tipo = cbTipoUsuario.getSelectedItem().toString();
            String telefono = txtTelefono.getText();
            String usuario, contraseña, carrera = "";
            // Obtener los datos específicos según el tipo de usuario
            if (tipo.equals("Estudiante")) {
                usuario = txtUsuarioEstudiante.getText();
                contraseña = txtContraseñaEstudiante.getText();
                carrera = txtUsuarioEstudianteCarrera.getText();
            } else {
                usuario = txtUsuarioAdmin.getText();
                contraseña = txtContraseñaAdmin.getText();
            }
            // Crear el objeto de usuario actualizado
            Usuario usuarioActualizado;
            if (tipo.equals("Estudiante")) {
                usuarioActualizado = new Estudiante(cod, nombre, email, tipo, telefono, usuario, contraseña, carrera);
            } else {
                usuarioActualizado = new Administrador(cod, nombre, tipo, email, telefono, usuario, contraseña);
            }
            // Actualizar los datos en la base de datos
            usuarioDAO.actualizarUsuario(usuarioActualizado);
            // Actualizar la vista con los datos modificados
            usuarios = usuarioDAO.obtenerUsuarios();
            llenarTablaUsuarios();
            limpiar1();
            JOptionPane.showMessageDialog(null, "Usuario actualizado exitosamente.");

        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void txtBuscarEstuFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarEstuFocusGained
        txtBuscarEstu.setText("");
    }//GEN-LAST:event_txtBuscarEstuFocusGained

    private void btnBuscarPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPrestamoActionPerformed
        String nombreUsuario = txtBuscarEstu.getText();
        buscarUsuarioPorNombrePres(nombreUsuario);

    }//GEN-LAST:event_btnBuscarPrestamoActionPerformed

    private void txtBuscarLibroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarLibroFocusGained
        txtBuscarLibro.setText("");
    }//GEN-LAST:event_txtBuscarLibroFocusGained

    private void btnBuscarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarLibroActionPerformed
        String nombreLibro = txtBuscarLibro.getText();
        buscarLibroPorNombre(nombreLibro);
    }//GEN-LAST:event_btnBuscarLibroActionPerformed

    private void txtBuscarAutorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarAutorFocusGained
        txtBuscarAutor.setText("");
    }//GEN-LAST:event_txtBuscarAutorFocusGained

    private void btnBuscarAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarAutorActionPerformed
        String nombreAutor = txtBuscarAutor.getText();
        buscarAutorPorNombre(nombreAutor);
    }//GEN-LAST:event_btnBuscarAutorActionPerformed

    private void txtBuscarEditorialFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarEditorialFocusGained
        txtBuscarEditorial.setText("");
    }//GEN-LAST:event_txtBuscarEditorialFocusGained

    private void btnBuscarEditorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarEditorialActionPerformed
        String nombreEditorial = txtBuscarEditorial.getText();
        buscarEditorialPorNombre(nombreEditorial);
    }//GEN-LAST:event_btnBuscarEditorialActionPerformed

    //SENTENCIAS AUTORES    

    private void btnAgregarAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarAutorActionPerformed
        String autor = txtAutor.getText();
        if (autor.equals("")) {
            JOptionPane.showMessageDialog(null, "Ingrese los datos del autor");
            return;
        }

        // Verificar si el autor ya está registrado
        boolean autorExistente = autorDAO.existeAutor(autor);
        if (autorExistente) {
            JOptionPane.showMessageDialog(null, "El autor ya está registrado");
            return;
        }

        Autor nuevoAutor = new Autor();
        nuevoAutor.setNombre(autor);
        autorDAO.crearAutor(nuevoAutor);
        JOptionPane.showMessageDialog(null, "Autor registrado exitosamente");
        // Actualizar la tabla
        autores = autorDAO.obtenerAutores();
        llenarTablaAutor();
        limpiar3();
        llenarComboBoxAutores();
    }//GEN-LAST:event_btnAgregarAutorActionPerformed

    private void btnEliminarAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarAutorActionPerformed

        int filaSeleccionada = tbAutor.getSelectedRow();
        if (filaSeleccionada != -1) {
            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar este autor?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                int idAutor = Integer.parseInt(tbAutor.getValueAt(filaSeleccionada, 0).toString());
                autorDAO.eliminarAutor(idAutor);
                autores = autorDAO.obtenerAutores();
                llenarTablaAutor();
                limpiar3();
            }
        }
    }//GEN-LAST:event_btnEliminarAutorActionPerformed

    private void tbAutorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbAutorMouseClicked
        cargarDatosUsuarioSeleccionado3();
    }//GEN-LAST:event_tbAutorMouseClicked

    private void btnLimpiarAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarAutorActionPerformed
        txtidAutor.setText("");
        txtAutor.setText("");
        txtAutor.requestFocus();
    }//GEN-LAST:event_btnLimpiarAutorActionPerformed

    private void btnModificarAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarAutorActionPerformed
        int filaSeleccionada = tbAutor.getSelectedRow();
        if (filaSeleccionada != -1) {
            String nombre = txtAutor.getText();
            if (nombre.equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese los datos del autor");
                return;
            }

            int idAutor = Integer.parseInt(tbAutor.getValueAt(filaSeleccionada, 0).toString());
            Autor autorActualizado = new Autor();
            autorActualizado.setId(idAutor);
            autorActualizado.setNombre(nombre);
            autorDAO.actualizarAutor(autorActualizado);
            autores = autorDAO.obtenerAutores();
            llenarTablaAutor();
            limpiar3();
        }
    }//GEN-LAST:event_btnModificarAutorActionPerformed

    //SENTENCIAS EDITORIALES

    private void btnRegistrarEditorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarEditorialActionPerformed
        String nombreEditorial = txtEditorial.getText();
        if (nombreEditorial.equals("")) {
            JOptionPane.showMessageDialog(null, "Ingrese los datos de la editorial");
            return;
        }
        // Verificar si la editorial ya existe en la base de datos
        boolean editorialExiste = editorialDAO.verificarEditorialExistente(nombreEditorial);
        if (editorialExiste) {
            JOptionPane.showMessageDialog(null, "La editorial '" + nombreEditorial + "' ya está registrada.");
            return;
        }
        Editorial nuevaEditorial = new Editorial();
        nuevaEditorial.setNombre(nombreEditorial);
        editorialDAO.crearEditorial(nuevaEditorial);
        JOptionPane.showMessageDialog(null, "Editorial registrada exitosamente");
        // Actualizar la tabla y el combo box
        editoriales = editorialDAO.obtenerEditoriales();
        llenarTablaEditorial();
        limpiar4();
        llenarComboBoxEditoriales();
    }//GEN-LAST:event_btnRegistrarEditorialActionPerformed

    private void btnEliminarEditorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarEditorialActionPerformed
        int filaSeleccionada = tbEditorial.getSelectedRow();
        if (filaSeleccionada != -1) {
            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar esta editorial?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                int idEditorial = Integer.parseInt(tbEditorial.getValueAt(filaSeleccionada, 0).toString());
                editorialDAO.eliminarEditorial(idEditorial);
                editoriales = editorialDAO.obtenerEditoriales();
                llenarTablaEditorial();
                limpiar4();
            }
        }
    }//GEN-LAST:event_btnEliminarEditorialActionPerformed

    private void tbEditorialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbEditorialMouseClicked
        int row = tbEditorial.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "No se Selecciono");
        } else {
            idEdi = Integer.parseInt((String) tbEditorial.getValueAt(row, 0).toString());
            String editor = (String) tbEditorial.getValueAt(row, 1);
            txtidEditorial.setText("" + idEdi);
            txtEditorial.setText(editor);
        }
    }//GEN-LAST:event_tbEditorialMouseClicked

    private void btnLimpiarEditorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarEditorialActionPerformed
        txtidEditorial.setText("");
        txtEditorial.setText("");
        txtEditorial.requestFocus();
    }//GEN-LAST:event_btnLimpiarEditorialActionPerformed

    private void btnModificarEditorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarEditorialActionPerformed
        int filaSeleccionada = tbEditorial.getSelectedRow();
        if (filaSeleccionada != -1) {
            String nombre = txtEditorial.getText();
            if (nombre.equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese los datos de la editorial");
                return;
            }
            int idEditorial = Integer.parseInt(tbEditorial.getValueAt(filaSeleccionada, 0).toString());
            Editorial editorialActualizada = new Editorial();
            editorialActualizada.setId(idEditorial);
            editorialActualizada.setNombre(nombre);
            editorialDAO.actualizarEditorial(editorialActualizada);
            editoriales = editorialDAO.obtenerEditoriales();
            llenarTablaEditorial();
            limpiar4();
        }
    }//GEN-LAST:event_btnModificarEditorialActionPerformed

    private void btnEliminarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarUsuarioActionPerformed
        int filaSeleccionada = tbUsuarios.getSelectedRow();
        if (filaSeleccionada != -1) {
            int idUsuario = Integer.parseInt(tbUsuarios.getValueAt(filaSeleccionada, 0).toString());
            // Verificar si el usuario tiene préstamos registrados
            boolean tienePrestamos = usuarioDAO.tienePrestamos(idUsuario);
            if (tienePrestamos) {
                // Mostrar mensaje de error
                JOptionPane.showMessageDialog(this, "No se puede eliminar el usuario porque tiene préstamos registrados.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Llamar al método eliminarUsuario para eliminar al usuario
                usuarioDAO.eliminarUsuario(idUsuario);
                // Actualizar la vista con los usuarios restantes
                usuarios = usuarioDAO.obtenerUsuarios();
                llenarTablaUsuarios();
                limpiar1();
            }
        } else {
            // Mostrar mensaje de error si no se seleccionó ninguna fila
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }//GEN-LAST:event_btnEliminarUsuarioActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiar1();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void txtUsuarioEstudianteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsuarioEstudianteFocusGained
        txtUsuarioEstudiante.setText("");
    }//GEN-LAST:event_txtUsuarioEstudianteFocusGained

    private void btnRegistrarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarLibroActionPerformed
        String titulo = txtTitulo.getText();
        String autorNombre = (String) cbAutor.getSelectedItem();
        String editorialNombre = (String) cbEditorial.getSelectedItem();
        int cantidad = Integer.parseInt(txtCantidad.getText());
        // Obtener el autor y la editorial por nombre
        Autor autor = cn.obtenerAutorPorNombre(autorNombre);
        Editorial editorial = edi.obtenerEditorialPorNombre(editorialNombre);
        // Validar si se encontró el autor y la editorial
        if (autor == null || editorial == null) {
            JOptionPane.showMessageDialog(this, "Autor o editorial no válido");
            return;
        }
        // Crear el objeto Libro con los datos ingresados
        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setAutor(Integer.toString(autor.getId()));
        libro.setEditorial(Integer.toString(editorial.getId()));
        libro.setCantidad(cantidad);
        // Llamar al método para crear el libro en la base de datos
        libroDAO.crearLibro(libro);
        JOptionPane.showMessageDialog(this, "Libro registrado exitosamente");
        llenarTablaLibros();
        limpiar2();
    }//GEN-LAST:event_btnRegistrarLibroActionPerformed

    private void btnLimpiarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarLibroActionPerformed
        limpiar2();
    }//GEN-LAST:event_btnLimpiarLibroActionPerformed

    private void tbLibrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbLibrosMouseClicked
        cargarDatosUsuarioSeleccionado2();
    }//GEN-LAST:event_tbLibrosMouseClicked

    private void btnEliminarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarLibroActionPerformed
        int filaSeleccionada = tbLibros.getSelectedRow();
        if (filaSeleccionada != -1) {
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar este libro?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                int idLibro = Integer.parseInt(tbLibros.getValueAt(filaSeleccionada, 0).toString());
                libroDAO.eliminarLibro(idLibro);
                libros = libroDAO.obtenerTodosLosLibros();
                llenarTablaLibros();
                limpiar2();
            }
        }
    }//GEN-LAST:event_btnEliminarLibroActionPerformed

    private void btnActualizarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarLibroActionPerformed
        int filaSeleccionada = tbLibros.getSelectedRow();
        if (filaSeleccionada != -1) {
            String titulo = txtTitulo.getText();
            String autorNombre = cbAutor.getSelectedItem().toString();
            String editorialNombre = cbEditorial.getSelectedItem().toString();
            int cantidad = Integer.parseInt(txtCantidad.getText());
            // Validar si se encontró el autor y la editorial
            int autorId = autorDAO.obtenerAutorIdPorNombre(autorNombre);
            int editorialId = editorialDAO.obtenerEditorialIdPorNombre(editorialNombre);
            if (autorId == 0 || editorialId == 0) {
                JOptionPane.showMessageDialog(this, "Autor o editorial no válido");
                return;
            }
            // Crear el objeto Libro con los datos actualizados
            Libro libroActualizado = new Libro();
            libroActualizado.setId(libros.get(filaSeleccionada).getId());
            libroActualizado.setTitulo(titulo);
            libroActualizado.setAutor(autorNombre);
            libroActualizado.setEditorial(editorialNombre);
            libroActualizado.setCantidad(cantidad);
            // Actualizar el libro en la base de datos
            libroDAO.actualizarLibro(libroActualizado);
            // Actualizar la vista con los datos modificados
            libros = libroDAO.obtenerTodosLosLibros();
            llenarTablaLibros();
            limpiar2();
        }

    }//GEN-LAST:event_btnActualizarLibroActionPerformed

    private void tbPrestamosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPrestamosMouseClicked
        cargarDatosUsuarioSeleccionado4();
    }//GEN-LAST:event_tbPrestamosMouseClicked

    private void txtBuscarEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarEstFocusGained
        txtBuscarEst.setText("");
    }//GEN-LAST:event_txtBuscarEstFocusGained

    private void btnBuscarLibro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarLibro1ActionPerformed
        String nombreLibro = txtBuscarEst.getText();
        buscarUsuarioPorNombreLibPres(nombreLibro);
    }//GEN-LAST:event_btnBuscarLibro1ActionPerformed

    private void btnGenerarReporteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGenerarReporteMouseClicked
        try {
            Conexion.MySQLConexion con = new Conexion.MySQLConexion();
            Connection cnn = con.getConnection();
            JasperReport reporte = null;
            String path = "src\\Reportes\\ReporteDeVencimientos.jasper";
            reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint jprint = JasperFillManager.fillReport(reporte, null, cnn);
            JasperViewer view = new JasperViewer(jprint, false);
            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            view.setVisible(true);

        } catch (JRException ex) {
            Logger.getLogger(Admin.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnGenerarReporteMouseClicked


    private void btnActualizaPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizaPrestamoActionPerformed
        int filaSeleccionada = tbPrestamos.getSelectedRow();
        if (filaSeleccionada != -1) {
            // Obtener el id del préstamo y la nueva fecha de devolución ingresada por el usuario
            int prestamoId = Integer.parseInt(tbPrestamos.getValueAt(filaSeleccionada, 0).toString());
            LocalDate nuevaFechaDevolucion = LocalDate.parse(txtFechaDevolucion.getText());
            // Actualizar la fecha de devolución en el modelo de datos de la tabla
            DefaultTableModel modelo = (DefaultTableModel) tbPrestamos.getModel();
            modelo.setValueAt(nuevaFechaDevolucion, filaSeleccionada, 4); // Actualizar la columna 4 (fecha de devolución)
            String comboB = cbEstado.getSelectedItem().toString();
            int estado = 0;
            if (comboB.equals("pendiente")) {
                estado = 1;
            } else {
                estado = 2;
            }
            // Actualizar la vista de la tabla
            tbPrestamos.setModel(modelo);
            // Guardar los cambios en la base de datos
            prestamoDAO.actualizarFechasDevolucion(prestamoId, nuevaFechaDevolucion, estado);
            llenarTablaPrestamos();
            JOptionPane.showMessageDialog(null, "Los cambios se han guardado correctamente");
        }
    }//GEN-LAST:event_btnActualizaPrestamoActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas salir?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        } else {
            setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        }
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        Login loginFrame = new Login();
        loginFrame.setVisible(true);
        dispose();
    }//GEN-LAST:event_formWindowClosed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel barraDeOpciones;
    private javax.swing.JButton btnActualizaPrestamo;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnActualizarLibro;
    private javax.swing.JButton btnAgregarAutor;
    private javax.swing.JLabel btnAutor;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscarAutor;
    private javax.swing.JButton btnBuscarEditorial;
    private javax.swing.JButton btnBuscarEst;
    private javax.swing.JButton btnBuscarLibro;
    private javax.swing.JButton btnBuscarLibro1;
    private javax.swing.JButton btnBuscarPrestamo;
    private javax.swing.JLabel btnEditorial;
    private javax.swing.JButton btnEliminarAutor;
    private javax.swing.JButton btnEliminarEditorial;
    private javax.swing.JButton btnEliminarLibro;
    private javax.swing.JButton btnEliminarUsuario;
    private javax.swing.JLabel btnEstudiantes;
    private javax.swing.JLabel btnGenerarReporte;
    private javax.swing.JLabel btnLibros;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnLimpiarAutor;
    private javax.swing.JButton btnLimpiarEditorial;
    private javax.swing.JButton btnLimpiarLibro;
    private javax.swing.JButton btnModificarAutor;
    private javax.swing.JButton btnModificarEditorial;
    private javax.swing.JLabel btnPrestamos;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnRegistrarEditorial;
    private javax.swing.JButton btnRegistrarLibro;
    private javax.swing.JLabel btnUsuarios;
    private javax.swing.JComboBox<String> cbAutor;
    private javax.swing.JComboBox<String> cbEditorial;
    private javax.swing.JComboBox<String> cbEstado;
    private javax.swing.JComboBox<String> cbTipoUsuario;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel panelAutor;
    private javax.swing.JPanel panelEditorial;
    private javax.swing.JPanel panelEstudiantes;
    private javax.swing.JPanel panelLibros;
    private javax.swing.JPanel panelNuevoEstudiante;
    private javax.swing.JPanel panelPrestamos;
    private javax.swing.JPanel panelUs;
    private javax.swing.JPanel panelUsuarioAdmin;
    private javax.swing.JPanel panelUsuarioEstudiante;
    private javax.swing.JPanel panelUsuarios;
    private javax.swing.JTable tbAutor;
    private javax.swing.JTable tbEditorial;
    private javax.swing.JTable tbEstudiantes;
    private javax.swing.JTable tbLibros;
    private javax.swing.JTable tbPrestamos;
    private javax.swing.JTable tbUsuarios;
    private javax.swing.JTextField txtAutor;
    private javax.swing.JTextField txtBuscarAutor;
    private javax.swing.JTextField txtBuscarEditorial;
    private javax.swing.JTextField txtBuscarEst;
    private javax.swing.JTextField txtBuscarEstu;
    private javax.swing.JTextField txtBuscarEstudiante;
    private javax.swing.JTextField txtBuscarLibro;
    private javax.swing.JTextField txtBuscarUsuario;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCod;
    private javax.swing.JPasswordField txtContraseñaAdmin;
    private javax.swing.JPasswordField txtContraseñaEstudiante;
    private javax.swing.JTextField txtEditorial;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFechaDevolucion;
    private javax.swing.JTextField txtFechaPrestamo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtTitulo;
    private javax.swing.JTextField txtUsuarioAdmin;
    private javax.swing.JTextField txtUsuarioEstudiante;
    private javax.swing.JTextField txtUsuarioEstudianteCarrera;
    private javax.swing.JTextField txtidAutor;
    private javax.swing.JTextField txtidEditorial;
    // End of variables declaration//GEN-END:variables

}
