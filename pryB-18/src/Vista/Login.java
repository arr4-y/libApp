package Vista;

import Vista.*;
import Modelo.Estudiante;
import Modelo.Administrador;
import Conexion.MySQLConexion;
import Negocio.UsuarioDAO;
import java.sql.Connection;
import Modelo.Usuario;
import javax.swing.JOptionPane;

public class Login extends javax.swing.JFrame {

    private Connection connection;
    private UsuarioDAO usuarioDAO;
    public static int idUsuario;

    Usuario us;

    public Login() {

        usuarioDAO = new UsuarioDAO();
        initComponents();
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        panelEstudiante.setVisible(true);
        panelAdmin.setVisible(false);

        MySQLConexion mysqlConexion = new MySQLConexion();
        connection = mysqlConexion.getConnection();

    }

    public int obtenerIdUsuarioSession() {
        String username = txtEstudiante.getText();
        String password = new String(txtPassEstudiante.getPassword());

        int idUsuario = 0; // Valor predeterminado o código de error

        // Llamar al método autenticarEstudiante en tu DAO de usuario
        Estudiante estudiante = usuarioDAO.autenticarEstudiante(username, password);

        if (estudiante != null) {
            idUsuario = estudiante.getId();
            Login.idUsuario = idUsuario; // Asignar el valor a la variable estática
        }

        return idUsuario;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnEntrarComoEstudiante = new javax.swing.JButton();
        btnEntrarComoAdmin = new javax.swing.JButton();
        btnEntrar = new javax.swing.JButton();
        panelCentralLogin = new javax.swing.JPanel();
        panelEstudiante = new javax.swing.JPanel();
        txtEstudiante = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        txtPassEstudiante = new javax.swing.JPasswordField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        panelAdmin = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtAdmin = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        txtPassAdmin = new javax.swing.JPasswordField();
        jSeparator4 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/library.png"))); // NOI18N
        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(186, 79, 84));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Dubai Light", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Bienvenido");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 180, 40));

        btnEntrarComoEstudiante.setBackground(new java.awt.Color(255, 255, 255));
        btnEntrarComoEstudiante.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnEntrarComoEstudiante.setForeground(new java.awt.Color(0, 0, 0));
        btnEntrarComoEstudiante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/student.png"))); // NOI18N
        btnEntrarComoEstudiante.setText("Estudiante");
        btnEntrarComoEstudiante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntrarComoEstudianteActionPerformed(evt);
            }
        });
        jPanel3.add(btnEntrarComoEstudiante, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 120, 30));

        btnEntrarComoAdmin.setBackground(new java.awt.Color(255, 255, 255));
        btnEntrarComoAdmin.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnEntrarComoAdmin.setForeground(new java.awt.Color(0, 0, 0));
        btnEntrarComoAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/admin.png"))); // NOI18N
        btnEntrarComoAdmin.setText("Admin");
        btnEntrarComoAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntrarComoAdminActionPerformed(evt);
            }
        });
        jPanel3.add(btnEntrarComoAdmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 120, 30));

        btnEntrar.setBackground(new java.awt.Color(255, 255, 255));
        btnEntrar.setFont(new java.awt.Font("Dubai Light", 0, 12)); // NOI18N
        btnEntrar.setForeground(new java.awt.Color(0, 0, 0));
        btnEntrar.setText("Entrar");
        btnEntrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntrarActionPerformed(evt);
            }
        });
        jPanel3.add(btnEntrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, 80, 30));

        panelCentralLogin.setBackground(new java.awt.Color(186, 79, 84));

        panelEstudiante.setBackground(new java.awt.Color(186, 79, 84));
        panelEstudiante.setForeground(new java.awt.Color(60, 63, 65));

        txtEstudiante.setBackground(new java.awt.Color(186, 79, 84));
        txtEstudiante.setFont(new java.awt.Font("Dubai Light", 0, 14)); // NOI18N
        txtEstudiante.setForeground(new java.awt.Color(255, 255, 255));
        txtEstudiante.setText("Username");
        txtEstudiante.setBorder(null);
        txtEstudiante.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEstudianteFocusGained(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        txtPassEstudiante.setBackground(new java.awt.Color(186, 79, 84));
        txtPassEstudiante.setFont(new java.awt.Font("Dubai Light", 0, 14)); // NOI18N
        txtPassEstudiante.setForeground(new java.awt.Color(255, 255, 255));
        txtPassEstudiante.setText("Password");
        txtPassEstudiante.setBorder(null);
        txtPassEstudiante.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPassEstudianteFocusGained(evt);
            }
        });

        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/user.png"))); // NOI18N

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lock.png"))); // NOI18N

        javax.swing.GroupLayout panelEstudianteLayout = new javax.swing.GroupLayout(panelEstudiante);
        panelEstudiante.setLayout(panelEstudianteLayout);
        panelEstudianteLayout.setHorizontalGroup(
            panelEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEstudianteLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(panelEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator1)
                    .addComponent(txtPassEstudiante)
                    .addComponent(txtEstudiante, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                    .addComponent(jSeparator2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelEstudianteLayout.setVerticalGroup(
            panelEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEstudianteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelEstudianteLayout.createSequentialGroup()
                        .addComponent(txtEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPassEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelEstudianteLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        panelAdmin.setBackground(new java.awt.Color(186, 79, 84));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/user.png"))); // NOI18N

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lock.png"))); // NOI18N

        txtAdmin.setBackground(new java.awt.Color(186, 79, 84));
        txtAdmin.setFont(new java.awt.Font("Dubai Light", 0, 14)); // NOI18N
        txtAdmin.setForeground(new java.awt.Color(255, 255, 255));
        txtAdmin.setText("Username");
        txtAdmin.setBorder(null);
        txtAdmin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAdminFocusGained(evt);
            }
        });

        jSeparator3.setForeground(new java.awt.Color(255, 255, 255));

        txtPassAdmin.setBackground(new java.awt.Color(186, 79, 84));
        txtPassAdmin.setFont(new java.awt.Font("Dubai Light", 0, 14)); // NOI18N
        txtPassAdmin.setForeground(new java.awt.Color(255, 255, 255));
        txtPassAdmin.setText("Password");
        txtPassAdmin.setBorder(null);
        txtPassAdmin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPassAdminFocusGained(evt);
            }
        });

        jSeparator4.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelAdminLayout = new javax.swing.GroupLayout(panelAdmin);
        panelAdmin.setLayout(panelAdminLayout);
        panelAdminLayout.setHorizontalGroup(
            panelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAdminLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPassAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        panelAdminLayout.setVerticalGroup(
            panelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAdminLayout.createSequentialGroup()
                .addGap(0, 8, Short.MAX_VALUE)
                .addGroup(panelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAdminLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel7)
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAdminLayout.createSequentialGroup()
                        .addComponent(txtAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addGroup(panelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(txtPassAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelCentralLoginLayout = new javax.swing.GroupLayout(panelCentralLogin);
        panelCentralLogin.setLayout(panelCentralLoginLayout);
        panelCentralLoginLayout.setHorizontalGroup(
            panelCentralLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCentralLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCentralLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelEstudiante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelCentralLoginLayout.setVerticalGroup(
            panelCentralLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCentralLoginLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(panelAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.add(panelCentralLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 210, 110));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void btnEntrarComoEstudianteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntrarComoEstudianteActionPerformed
        panelEstudiante.setVisible(true);
        panelAdmin.setVisible(false);
    }//GEN-LAST:event_btnEntrarComoEstudianteActionPerformed

    private void txtEstudianteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEstudianteFocusGained
        txtEstudiante.setText("");
    }//GEN-LAST:event_txtEstudianteFocusGained

    private void txtPassEstudianteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPassEstudianteFocusGained
        txtPassEstudiante.setText("");
    }//GEN-LAST:event_txtPassEstudianteFocusGained

    private void txtPassAdminFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPassAdminFocusGained
        txtPassAdmin.setText("");
    }//GEN-LAST:event_txtPassAdminFocusGained

    private void txtAdminFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAdminFocusGained
        txtAdmin.setText("");
    }//GEN-LAST:event_txtAdminFocusGained

    private void btnEntrarComoAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntrarComoAdminActionPerformed
        panelAdmin.setVisible(true);
        panelEstudiante.setVisible(false);
    }//GEN-LAST:event_btnEntrarComoAdminActionPerformed

    private void btnEntrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntrarActionPerformed
        String usuario;
        String contrasena;
        if (panelEstudiante.isVisible()) {
            // Obtener el usuario y la contraseña del estudiante
            usuario = txtEstudiante.getText();
            contrasena = String.valueOf(txtPassEstudiante.getPassword());
            // Lógica de autenticación del estudiante utilizando el método autenticar de EstudianteDAO
            Estudiante estudiante = usuarioDAO.autenticarEstudiante(usuario, contrasena);
            if (estudiante != null) {
                JOptionPane.showMessageDialog(this, "Acceso concedido al estudiante", "Acceso Exitoso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                //Asignar valor a variable estática de formulario
                Student.usu = usuario;
                Student.pss = contrasena;
                Student.nombres = estudiante.getNombre();
                Student.tel = estudiante.getTelefono();
                Student.em = estudiante.getEmail();
                Student.carr = estudiante.getCarrera();
                Student.nombres = estudiante.getNombre().toUpperCase();
                Student estudianteFrame = new Student();
                estudianteFrame.Mostraruser();
                estudianteFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            }
        } else if (panelAdmin.isVisible()) {
            // Obtener el usuario y la contraseña del administrador
            usuario = txtAdmin.getText();
            contrasena = String.valueOf(txtPassAdmin.getPassword());
            // Lógica de autenticación del administrador utilizando el método autenticar de AdministradorDAO
            Administrador administrador = usuarioDAO.autenticarAdministrador(usuario, contrasena);
            if (administrador != null) {
                JOptionPane.showMessageDialog(this, "Acceso concedido al administrador", "Acceso Exitoso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                Admin.nombres = administrador.getNombre().toUpperCase();
                Admin adminFrame = new Admin();
                adminFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEntrarActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntrar;
    private javax.swing.JButton btnEntrarComoAdmin;
    private javax.swing.JButton btnEntrarComoEstudiante;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JPanel panelAdmin;
    private javax.swing.JPanel panelCentralLogin;
    private javax.swing.JPanel panelEstudiante;
    private javax.swing.JTextField txtAdmin;
    private javax.swing.JTextField txtEstudiante;
    private javax.swing.JPasswordField txtPassAdmin;
    private javax.swing.JPasswordField txtPassEstudiante;
    // End of variables declaration//GEN-END:variables
}
