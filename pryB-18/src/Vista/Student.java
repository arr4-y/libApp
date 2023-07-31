/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import Negocio.UsuarioDAO;
import Modelo.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Negocio.LibroDAO;
import Negocio.DevolucionesDAO;
import Negocio.PrestamoDAO;

//Fechas
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class Student extends javax.swing.JFrame {

    /*Variable estatica-->colocar datos desde cualquier otro formulario 
    en el mismo paquete*/
    public static String usu;
    public static String pss;

    public static String nom;
    public static String em;
    public static String tel;
    public static String carr;

    Login frmlg;
    public static String nombres;

    private DefaultTableModel tablaLibrosModelo;
    private DefaultTableModel tablaSolicModelo;

    private LibroDAO libroDAO;
    private UsuarioDAO estudianteDAO; // Instancia del DAO
    private DevolucionesDAO devolucionesDAO;
    private PrestamoDAO prestaDAO;

    private ArrayList<Libro> libros;
    private ArrayList<devolucionDeta> presta;

    private Estudiante codE;

    public Student() {
        initComponents();
        setLocationRelativeTo(null);
        int idUsuario = Login.idUsuario;

        this.setTitle(nombres);

        // Inicializar el DAO
        estudianteDAO = new UsuarioDAO();
        prestaDAO = new PrestamoDAO();
        libroDAO = new LibroDAO();
        devolucionesDAO = new DevolucionesDAO();

        codE = estudianteDAO.autenticarEstudiante(usu, pss);
        presta = devolucionesDAO.obtenerTodosLosPrestamos(codE.getId());
        libros = libroDAO.obtenerTodosLosLibros();
        txtID_usu.setText(codE.getId() + "");
        tablaLibrosModelo = new DefaultTableModel();
        tablaSolicModelo = new DefaultTableModel();
        //PANELES
        panelPerfil.setVisible(true);
        panelSolicitarPrestamos.setVisible(false);
        panelLibros.setVisible(false);
        panelHistorial.setVisible(false);
        //POR DEFAULT
        Mostraruser();

    }

    public void Mostraruser() {
        String usua = usu;
        String pass = pss;
        Estudiante codE = estudianteDAO.autenticarEstudiante(usua, pass);
        lbNom.setText(codE.getNombre());
        lbFono.setText(codE.getTelefono());
        lbCor.setText(codE.getEmail());
        lbCar.setText(codE.getCarrera());
    }

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

    public void llenarTablaLibros2() {
        tablaLibrosModelo = (DefaultTableModel) tbLib.getModel();
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

    public void llenarTablaHistorial() {
        tablaSolicModelo = (DefaultTableModel) tbHistorial.getModel();
        tablaSolicModelo.setRowCount(0);
        Estudiante codE = estudianteDAO.autenticarEstudiante(usu, pss);
        presta = (ArrayList<devolucionDeta>) devolucionesDAO.obtenerTodosLosPrestamos2(codE.getId());
        for (devolucionDeta d : presta) {
            Object[] fila = {
                d.getTit(),
                d.getFechPre(),
                d.getFechDev(),
                d.getEstado(),
                d.getMulta()
            };
            tablaSolicModelo.addRow(fila);
        }
    }

    //buscara libro
    private void buscarLibroPorNombre(String nombreLibro) {
        DefaultTableModel modelo = (DefaultTableModel) tbLib.getModel();
        modelo.setRowCount(0);
        ArrayList<Libro> librosEncontrados = libroDAO.buscarLibroPorTitulo(nombreLibro);
        if (librosEncontrados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron libros con ese nombre");
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

    private void buscarLibroPorNombre2(String nombreLibro) {
        DefaultTableModel modelo = (DefaultTableModel) tbLibros.getModel();
        modelo.setRowCount(0);
        ArrayList<Libro> librosEncontrados = libroDAO.buscarLibroPorTitulo(nombreLibro);
        if (librosEncontrados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron libros con ese nombre");
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

    public void obtenerPrestamoPorID(int idPrestamo) {
        tablaSolicModelo = (DefaultTableModel) tbHistorial.getModel();
        tablaSolicModelo.setRowCount(0);
        int idUsuario = codE.getId();
        ArrayList<devolucionDeta> prestamos = devolucionesDAO.obtenerPrestamoPorId(idPrestamo, idUsuario);
        if (!prestamos.isEmpty()) {
            // Se encontraron préstamos, mostrarlos en la tabla
            for (devolucionDeta prestamo : prestamos) {
                Object[] fila = {
                    prestamo.getTit(),
                    prestamo.getFechPre(),
                    prestamo.getFechDev(),
                    prestamo.getEstado(),
                    prestamo.getMulta()
                };
                tablaSolicModelo.addRow(fila);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontraron préstamos con el ID especificado", "Error de búsqueda", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static String Devolucion() {
        Date t = new Date();
        t.setDate(t.getDate() + 7);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return sd.format(t);
    }

    public static String hoy() {
        Date t = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return sd.format(t);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        barraDeOpciones = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnPerfil = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnPrestamos = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        btnHistorial = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        btnLibros = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        panelPerfil = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lbimg = new javax.swing.JLabel();
        lbNom = new javax.swing.JLabel();
        lbCor = new javax.swing.JLabel();
        lbCar = new javax.swing.JLabel();
        lbFono = new javax.swing.JLabel();
        panelSolicitarPrestamos = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        btnPrestamo = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        txtIDL = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtID_usu = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        tbLib = new javax.swing.JTable();
        txtBuscarLibro = new javax.swing.JTextField();
        btnBuscarLibro = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        panelHistorial = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbHistorial = new javax.swing.JTable();
        txtBuscarPrestamo = new javax.swing.JTextField();
        btnBuscarPrestamo = new javax.swing.JButton();
        panelLibros = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtBuscarLibro2 = new javax.swing.JTextField();
        btnBuscarLibro2 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbLibros = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();

        jLabel8.setText("jLabel6");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(186, 79, 84));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        barraDeOpciones.setBackground(new java.awt.Color(255, 255, 255));
        barraDeOpciones.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        barraDeOpciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel5.setForeground(new java.awt.Color(0, 0, 0));

        btnPerfil.setFont(new java.awt.Font("Dubai Light", 1, 18)); // NOI18N
        btnPerfil.setForeground(new java.awt.Color(0, 0, 0));
        btnPerfil.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/usuarios.png"))); // NOI18N
        btnPerfil.setText("Perfil");
        btnPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPerfilMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(btnPerfil, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnPerfil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        barraDeOpciones.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 180, 40));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel4.setForeground(new java.awt.Color(0, 0, 0));

        btnPrestamos.setFont(new java.awt.Font("Dubai Light", 1, 18)); // NOI18N
        btnPrestamos.setForeground(new java.awt.Color(0, 0, 0));
        btnPrestamos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnPrestamos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/give.png"))); // NOI18N
        btnPrestamos.setText("Préstamo");
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

        barraDeOpciones.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 180, 40));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel10.setForeground(new java.awt.Color(0, 0, 0));

        btnHistorial.setFont(new java.awt.Font("Dubai Light", 1, 18)); // NOI18N
        btnHistorial.setForeground(new java.awt.Color(0, 0, 0));
        btnHistorial.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnHistorial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/historial.png"))); // NOI18N
        btnHistorial.setText("Historial");
        btnHistorial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHistorialMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(btnHistorial, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnHistorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        barraDeOpciones.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 180, 40));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel9.setForeground(new java.awt.Color(0, 0, 0));

        btnLibros.setFont(new java.awt.Font("Dubai Light", 1, 18)); // NOI18N
        btnLibros.setForeground(new java.awt.Color(0, 0, 0));
        btnLibros.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnLibros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/books.png"))); // NOI18N
        btnLibros.setText("Libros");
        btnLibros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLibrosMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(btnLibros, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnLibros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        barraDeOpciones.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 180, 40));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logo_colegio.png"))); // NOI18N
        jLabel5.setText("jLabel5");

        jLabel6.setFont(new java.awt.Font("Eras Bold ITC", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("I.E. Emblemática ");

        jLabel7.setFont(new java.awt.Font("Eras Bold ITC", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Jose María Eguren");

        btnSalir.setBackground(new java.awt.Color(255, 51, 51));
        btnSalir.setFont(new java.awt.Font("Dubai Light", 1, 12)); // NOI18N
        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(14, 14, 14)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(barraDeOpciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(barraDeOpciones, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(221, 221, 221)));

        panelPerfil.setBackground(new java.awt.Color(255, 255, 255));
        panelPerfil.setPreferredSize(new java.awt.Dimension(408, 322));

        jLabel9.setFont(new java.awt.Font("Dubai Light", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("BIENVENIDO ESTUDIANTE");

        jLabel1.setFont(new java.awt.Font("Dubai Light", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Nombre:");

        jLabel2.setFont(new java.awt.Font("Dubai Light", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Teléfono");

        jLabel3.setFont(new java.awt.Font("Dubai Light", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Correo Electrónico:");

        jLabel4.setFont(new java.awt.Font("Dubai Light", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Carrera:");

        lbimg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/perfil.png"))); // NOI18N

        lbNom.setFont(new java.awt.Font("Dubai Light", 0, 18)); // NOI18N
        lbNom.setForeground(new java.awt.Color(51, 51, 255));

        lbCor.setFont(new java.awt.Font("Dubai Light", 0, 18)); // NOI18N
        lbCor.setForeground(new java.awt.Color(51, 51, 255));
        lbCor.setText(" ");

        lbCar.setFont(new java.awt.Font("Dubai Light", 0, 18)); // NOI18N
        lbCar.setForeground(new java.awt.Color(51, 51, 255));
        lbCar.setText(" ");

        lbFono.setFont(new java.awt.Font("Dubai Light", 0, 18)); // NOI18N
        lbFono.setForeground(new java.awt.Color(51, 51, 255));
        lbFono.setText(" ");

        javax.swing.GroupLayout panelPerfilLayout = new javax.swing.GroupLayout(panelPerfil);
        panelPerfil.setLayout(panelPerfilLayout);
        panelPerfilLayout.setHorizontalGroup(
            panelPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPerfilLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(panelPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelPerfilLayout.createSequentialGroup()
                        .addGroup(panelPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lbCor, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                            .addComponent(lbFono, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbNom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbCar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbimg, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelPerfilLayout.setVerticalGroup(
            panelPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPerfilLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addGroup(panelPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(lbNom, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lbFono))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lbCor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lbCar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelPerfilLayout.createSequentialGroup()
                .addComponent(lbimg)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        panelSolicitarPrestamos.setBackground(new java.awt.Color(255, 255, 255));
        panelSolicitarPrestamos.setForeground(new java.awt.Color(255, 255, 255));
        panelSolicitarPrestamos.setPreferredSize(new java.awt.Dimension(417, 303));

        jLabel11.setFont(new java.awt.Font("Dubai Light", 0, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("SOLICITAR ");

        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel16.setFont(new java.awt.Font("Dubai Light", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Libro");

        btnPrestamo.setBackground(new java.awt.Color(0, 0, 0));
        btnPrestamo.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnPrestamo.setForeground(new java.awt.Color(255, 255, 255));
        btnPrestamo.setText("Solicitar");
        btnPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrestamoActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(0, 0, 0));
        jButton5.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Erase.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        txtIDL.setEditable(false);
        txtIDL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDLActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Dubai Light", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Usuario");

        txtID_usu.setEditable(false);
        txtID_usu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtID_usuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtID_usu, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtIDL, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtIDL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtID_usu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(btnPrestamo))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        tbLib.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        tbLib.setModel(new javax.swing.table.DefaultTableModel(
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
        tbLib.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbLibMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tbLib);

        txtBuscarLibro.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        txtBuscarLibro.setText("Ingrese título");
        txtBuscarLibro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarLibroFocusGained(evt);
            }
        });

        btnBuscarLibro.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarLibro.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnBuscarLibro.setForeground(new java.awt.Color(0, 0, 0));
        btnBuscarLibro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        btnBuscarLibro.setText("Buscar");
        btnBuscarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarLibroActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Dubai Light", 0, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("PRÉSTAMOS");

        javax.swing.GroupLayout panelSolicitarPrestamosLayout = new javax.swing.GroupLayout(panelSolicitarPrestamos);
        panelSolicitarPrestamos.setLayout(panelSolicitarPrestamosLayout);
        panelSolicitarPrestamosLayout.setHorizontalGroup(
            panelSolicitarPrestamosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSolicitarPrestamosLayout.createSequentialGroup()
                .addGroup(panelSolicitarPrestamosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSolicitarPrestamosLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel11)
                        .addGap(74, 74, 74))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSolicitarPrestamosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelSolicitarPrestamosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)))
                .addGroup(panelSolicitarPrestamosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSolicitarPrestamosLayout.createSequentialGroup()
                        .addComponent(txtBuscarLibro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSolicitarPrestamosLayout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        panelSolicitarPrestamosLayout.setVerticalGroup(
            panelSolicitarPrestamosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSolicitarPrestamosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSolicitarPrestamosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSolicitarPrestamosLayout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addGroup(panelSolicitarPrestamosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscarLibro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscarLibro)))
                    .addGroup(panelSolicitarPrestamosLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        panelHistorial.setBackground(new java.awt.Color(255, 255, 255));
        panelHistorial.setPreferredSize(new java.awt.Dimension(417, 303));

        jLabel12.setFont(new java.awt.Font("Dubai Light", 0, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("HISTORIAL DE PRÉSTAMOS");

        tbHistorial.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        tbHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Ttitulo", "F Préstamo", "F Devolución", "Estado", "Multa"
            }
        ));
        jScrollPane5.setViewportView(tbHistorial);

        txtBuscarPrestamo.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        txtBuscarPrestamo.setText("Ingrese título");
        txtBuscarPrestamo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarPrestamoFocusGained(evt);
            }
        });
        txtBuscarPrestamo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarPrestamoKeyTyped(evt);
            }
        });

        btnBuscarPrestamo.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarPrestamo.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnBuscarPrestamo.setForeground(new java.awt.Color(0, 0, 0));
        btnBuscarPrestamo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        btnBuscarPrestamo.setText("Buscar");
        btnBuscarPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPrestamoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelHistorialLayout = new javax.swing.GroupLayout(panelHistorial);
        panelHistorial.setLayout(panelHistorialLayout);
        panelHistorialLayout.setHorizontalGroup(
            panelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHistorialLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelHistorialLayout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscarPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 618, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelHistorialLayout.setVerticalGroup(
            panelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHistorialLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtBuscarPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarPrestamo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        panelLibros.setBackground(new java.awt.Color(255, 255, 255));
        panelLibros.setPreferredSize(new java.awt.Dimension(417, 303));

        jLabel13.setFont(new java.awt.Font("Dubai Light", 0, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("LIBROS DISPONIBLES");

        txtBuscarLibro2.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        txtBuscarLibro2.setText("Ingrese nombre del libro");
        txtBuscarLibro2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarLibro2FocusGained(evt);
            }
        });

        btnBuscarLibro2.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarLibro2.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnBuscarLibro2.setForeground(new java.awt.Color(0, 0, 0));
        btnBuscarLibro2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        btnBuscarLibro2.setText("Buscar");
        btnBuscarLibro2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarLibro2ActionPerformed(evt);
            }
        });

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
        jScrollPane6.setViewportView(tbLibros);

        javax.swing.GroupLayout panelLibrosLayout = new javax.swing.GroupLayout(panelLibros);
        panelLibros.setLayout(panelLibrosLayout);
        panelLibrosLayout.setHorizontalGroup(
            panelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLibrosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtBuscarLibro2, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscarLibro2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelLibrosLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelLibrosLayout.setVerticalGroup(
            panelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLibrosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtBuscarLibro2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarLibro2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSolicitarPrestamos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
                    .addComponent(panelHistorial, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
                    .addComponent(panelLibros, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
                    .addComponent(panelPerfil, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(panelPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelSolicitarPrestamos, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelLibros, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 660, 280));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/portadaEstudiante.jpg"))); // NOI18N
        jLabel18.setText("jLabel18");
        jPanel7.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 220));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPerfilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPerfilMouseClicked
        panelPerfil.setVisible(true);
        panelSolicitarPrestamos.setVisible(false);
        panelLibros.setVisible(false);
        panelHistorial.setVisible(false);
    }//GEN-LAST:event_btnPerfilMouseClicked

    private void btnPrestamosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrestamosMouseClicked
        panelPerfil.setVisible(false);
        panelSolicitarPrestamos.setVisible(true);
        panelLibros.setVisible(false);
        panelHistorial.setVisible(false);
        llenarTablaLibros2();
    }//GEN-LAST:event_btnPrestamosMouseClicked

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnLibrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLibrosMouseClicked
        panelPerfil.setVisible(false);
        panelSolicitarPrestamos.setVisible(false);
        panelLibros.setVisible(true);
        panelHistorial.setVisible(false);
        llenarTablaLibros();
    }//GEN-LAST:event_btnLibrosMouseClicked

    private void btnHistorialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHistorialMouseClicked
        panelPerfil.setVisible(false);
        panelSolicitarPrestamos.setVisible(false);
        panelLibros.setVisible(false);
        panelHistorial.setVisible(true);
        llenarTablaHistorial();
    }//GEN-LAST:event_btnHistorialMouseClicked

    private void txtBuscarPrestamoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarPrestamoFocusGained
        txtBuscarPrestamo.setText("");
    }//GEN-LAST:event_txtBuscarPrestamoFocusGained

    private void btnBuscarPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPrestamoActionPerformed
        int idPrestamo = Integer.parseInt(txtBuscarPrestamo.getText());
        obtenerPrestamoPorID(idPrestamo);
    }//GEN-LAST:event_btnBuscarPrestamoActionPerformed

    private void txtBuscarLibro2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarLibro2FocusGained
        txtBuscarLibro2.setText("");
    }//GEN-LAST:event_txtBuscarLibro2FocusGained

    private void btnBuscarLibro2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarLibro2ActionPerformed
        String nombreLibro = txtBuscarLibro2.getText();
        buscarLibroPorNombre2(nombreLibro);
    }//GEN-LAST:event_btnBuscarLibro2ActionPerformed

    private void txtIDLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDLActionPerformed

    private void txtID_usuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtID_usuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtID_usuActionPerformed

    private void btnPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrestamoActionPerformed
        prestaDAO.RegistraPrestamo_Devolucion(Integer.parseInt(txtIDL.getText()), Integer.parseInt(txtID_usu.getText()), hoy(), Devolucion());
    }//GEN-LAST:event_btnPrestamoActionPerformed

    private void tbLibMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbLibMouseClicked
        int filaSeleccionada = tbLib.getSelectedRow();
        String cod = tbLib.getValueAt(filaSeleccionada, 0).toString();
        txtIDL.setText(cod);
    }//GEN-LAST:event_tbLibMouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        txtIDL.setText("");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txtBuscarLibroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarLibroFocusGained
        txtBuscarLibro.setText("");
    }//GEN-LAST:event_txtBuscarLibroFocusGained

    private void btnBuscarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarLibroActionPerformed
        String nombreLibro = txtBuscarLibro.getText();
        buscarLibroPorNombre(nombreLibro);
    }//GEN-LAST:event_btnBuscarLibroActionPerformed

    private void txtBuscarPrestamoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarPrestamoKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume(); // Evita que el carácter no numérico sea ingresado
            JOptionPane.showMessageDialog(this, "Por favor, ingrese solo el ID del prestamo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txtBuscarPrestamoKeyTyped

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Student().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel barraDeOpciones;
    private javax.swing.JButton btnBuscarLibro;
    private javax.swing.JButton btnBuscarLibro2;
    private javax.swing.JButton btnBuscarPrestamo;
    private javax.swing.JLabel btnHistorial;
    private javax.swing.JLabel btnLibros;
    private javax.swing.JLabel btnPerfil;
    private javax.swing.JButton btnPrestamo;
    private javax.swing.JLabel btnPrestamos;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel lbCar;
    private javax.swing.JLabel lbCor;
    private javax.swing.JLabel lbFono;
    private javax.swing.JLabel lbNom;
    private javax.swing.JLabel lbimg;
    private javax.swing.JPanel panelHistorial;
    private javax.swing.JPanel panelLibros;
    private javax.swing.JPanel panelPerfil;
    private javax.swing.JPanel panelSolicitarPrestamos;
    private javax.swing.JTable tbHistorial;
    private javax.swing.JTable tbLib;
    private javax.swing.JTable tbLibros;
    private javax.swing.JTextField txtBuscarLibro;
    private javax.swing.JTextField txtBuscarLibro2;
    private javax.swing.JTextField txtBuscarPrestamo;
    private javax.swing.JTextField txtIDL;
    private javax.swing.JTextField txtID_usu;
    // End of variables declaration//GEN-END:variables

}
