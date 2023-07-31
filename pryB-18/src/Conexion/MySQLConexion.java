package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConexion {

    private static Connection conn;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String user = "root";
    private static final String pass = "";
    private static final String db = "bdbiblioteca2";
    private static final String url = "jdbc:mysql://localhost/" + db + "";

    public MySQLConexion() {
        conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Connect?
            if (conn != null) {
                System.out.println("Conexión establecida exitosamente");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Conexión Fallida:\n\n" + ex);
        }
    }

    public Connection getConnection() {
        return conn;
    }
}
