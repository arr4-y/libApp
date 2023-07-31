package Negocio;

import Conexion.MySQLConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import Modelo.*;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class PrestamoDAO {
    private Connection connection;
    private UsuarioDAO usuarioDAO;
    private LibroDAO libroDAO;
    private UsuarioDAO estudianteDAO;

    public PrestamoDAO() {
        MySQLConexion conexion = new MySQLConexion();
        connection = conexion.getConnection();
        usuarioDAO = new UsuarioDAO();
        libroDAO = new LibroDAO();
        estudianteDAO = new UsuarioDAO();
    }

    public ArrayList<Prestamo> obtenerTodosLosPrestamos() {
        ArrayList<Prestamo> prestamos = new ArrayList<>();
        try {
            String query = "SELECT p.prestamo_id, l.titulo, u.nombre, p.fecha_prestamo, p.fecha_vencimiento FROM prestamos p "
                    + "INNER JOIN libros l ON p.libro_id = l.libro_id "
                    + "INNER JOIN usuarios u ON p.usuario_id = u.usuario_id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int prestamoId = resultSet.getInt("prestamo_id");
                String libroTitulo = resultSet.getString("titulo");
                String estudianteNombre = resultSet.getString("nombre");
                LocalDate fechaPrestamo = resultSet.getDate("fecha_prestamo").toLocalDate();
                LocalDate fechaVencimiento = resultSet.getDate("fecha_vencimiento").toLocalDate();
                Prestamo prestamo = new Prestamo(estudianteNombre, libroTitulo, fechaPrestamo, fechaVencimiento);
                prestamos.add(prestamo);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prestamos;
    }

    public void registrarPrestamo(Prestamo prestamo) {
        try {
            String query = "INSERT INTO prestamos (libro_id, estudiante_id, fecha_prestamo, fecha_vencimiento) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(prestamo.getLibro()));
            statement.setInt(2, Integer.parseInt(prestamo.getEstudiante()));
            statement.setDate(3, java.sql.Date.valueOf(prestamo.getFechaPrestamo()));
            statement.setDate(4, java.sql.Date.valueOf(prestamo.getFechaDevolucion()));
            statement.executeUpdate();
            statement.close();
            System.out.println("El préstamo ha sido registrado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void RegistraPrestamo_Devolucion(int idL, int idU, String fechP, String fechD) {
        try {
            String query = "CALL spRegistra_prestamo(?, ?, ?, ?)";
            CallableStatement statement = connection.prepareCall(query);
            statement.setInt(1, idL);
            statement.setInt(2, idU);
            statement.setString(3, fechP);
            statement.setString(4, fechD);
            statement.execute();
            statement.close();
            JOptionPane.showMessageDialog(null, "El préstamo ha sido registrado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Prestamo obtenerPrestamoPorId(int prestamoId) {
        Prestamo prestamo = null;
        try {
            String query = "SELECT p.prestamo_id, l.titulo, p.fecha_prestamo, p.fecha_vencimiento, d.estados_id, d.multa "
                    + "FROM prestamos p "
                    + "INNER JOIN libros l ON p.libro_id = l.libro_id "
                    + "INNER JOIN devoluciones d ON p.prestamo_id = d.prestamo_id "
                    + "WHERE p.prestamo_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, prestamoId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String libroTitulo = resultSet.getString("titulo");
                LocalDate fechaPrestamo = resultSet.getDate("fecha_prestamo").toLocalDate();
                LocalDate fechaDevolucion = resultSet.getDate("fecha_vencimiento").toLocalDate();
                int estadoId = resultSet.getInt("estados_id");
                double multa = resultSet.getDouble("multa");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prestamo;
    }

    public ArrayList<devolucionDeta> obtenerTodosLosPrestamosEstudiantes() {
        ArrayList<devolucionDeta> prestamos = new ArrayList<>();
        try {
            String query = "SELECT * FROM vista_prestamos_all_admin2";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int prestamoId = resultSet.getInt("prestamo_id");
                String estudianteNombre = resultSet.getString("estudiante");
                String libroTitulo = resultSet.getString("libro");
                LocalDate fechaPrestamo = resultSet.getDate("fecha_prestamo").toLocalDate();
                LocalDate fechaDevolucion = resultSet.getDate("fecha_vencimiento").toLocalDate();
                double multa = resultSet.getDouble("multa");
                String estado = resultSet.getString("estado");
                Prestamo prestamo = new Prestamo(prestamoId, estudianteNombre, libroTitulo, fechaPrestamo, fechaDevolucion);
                devolucionDeta devolucion = new devolucionDeta(prestamo, multa, estado);
                prestamos.add(devolucion);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de excepciones
        }

        return prestamos;
    }
    
    
     public void actualizarFechasDevolucion(int prestamoId, LocalDate nuevaFechaDevolucion, int estado) {
        try {
            String query = "CALL actualizarFechasDevolucion(?, ?, ?)";
            CallableStatement statement = connection.prepareCall(query);
            statement.setInt(1, prestamoId);
            statement.setDate(2, java.sql.Date.valueOf(nuevaFechaDevolucion));
            statement.setInt(3, estado);
            statement.execute();
            statement.close();
            System.out.println("Se realizaron los cambios");
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de excepciones
        }
    }
    

     
}
