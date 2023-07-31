/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Conexion.MySQLConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Modelo.*; 
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList; 

/**
 *
 * @author Miguel Millan
 */
public class DevolucionesDAO {

    private Connection connection;

    private AutorDAO autorDAO;
    private EditorialDAO editorialDAO;

    public DevolucionesDAO() {
        MySQLConexion conexion = new MySQLConexion();
        connection = conexion.getConnection();

        autorDAO = new AutorDAO();
        editorialDAO = new EditorialDAO();
    }

    public ArrayList<devolucionDeta> obtenerTodosLosPrestamos(int id) {
        ArrayList<devolucionDeta> de = new ArrayList<>();
        String query = "SELECT l.titulo,l.autor_id,p.fecha_prestamo,p.fecha_vencimiento, e.nombre,e.descripcion,d.multa FROM "
                + "prestamos p join libros l on l.libro_id=p.libro_id JOIN devoluciones d on d.prestamo_id=p.prestamo_id "
                + "JOIN estado e on e.estados_id=d.estados_id WHERE usuario_id=?;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                devolucionDeta d = new devolucionDeta();
                d.setTit(resultSet.getString("titulo"));
                d.setNomA(resultSet.getInt("autor_id"));
                d.setFechPre(resultSet.getString("fecha_prestamo"));
                d.setFechDev(resultSet.getString("fecha_vencimiento"));
                d.setDescp(resultSet.getString("descripcion"));
                d.setMulta(resultSet.getDouble("multa"));
                de.add(d);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
        }

        return de;
    }

    
    public ArrayList<devolucionDeta> obtenerTodosLosPrestamos2(int id) {
        ArrayList<devolucionDeta> de = new ArrayList<>();
        String query = "SELECT l.titulo, p.fecha_prestamo, p.fecha_vencimiento, e.nombre, d.multa"
                + " FROM libros l"
                + " JOIN prestamos p ON l.libro_id = p.libro_id"
                + " JOIN devoluciones d ON d.prestamo_id = p.prestamo_id"
                + " JOIN estado e ON e.estados_id = d.estados_id"
                + " WHERE p.usuario_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                devolucionDeta d = new devolucionDeta();
                d.setTit(resultSet.getString(1));
                d.setFechPre(resultSet.getString(2));
                d.setFechDev(resultSet.getString(3));
                d.setEstado(resultSet.getString(4));
                d.setMulta(resultSet.getDouble(5));
                de.add(d);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return de;
    }

    public ArrayList<devolucionDeta> obtenerPrestamoPorId(int prestamoId, int usuarioId) {
        ArrayList<devolucionDeta> prestamos = new ArrayList<>();
        try {
            String query = "SELECT p.prestamo_id, l.titulo, p.fecha_prestamo, p.fecha_vencimiento, d.estados_id, d.multa "
                    + "FROM prestamos p "
                    + "INNER JOIN libros l ON p.libro_id = l.libro_id "
                    + "INNER JOIN devoluciones d ON p.prestamo_id = d.prestamo_id "
                    + "WHERE p.prestamo_id = ? AND p.usuario_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, prestamoId);
            statement.setInt(2, usuarioId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String libroTitulo = resultSet.getString("titulo");
                LocalDate fechaPrestamo = resultSet.getDate("fecha_prestamo").toLocalDate();
                LocalDate fechaDevolucion = resultSet.getDate("fecha_vencimiento").toLocalDate();
                String estadoId = resultSet.getString("estados_id");
                double multa = resultSet.getDouble("multa");
                devolucionDeta prestamo = new devolucionDeta(libroTitulo, fechaPrestamo.toString(), fechaDevolucion.toString(), estadoId, multa);
                prestamos.add(prestamo);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prestamos;
    }

    public ArrayList<devolucionDeta> obtenerTodosLosPrestamosEstudiantes() {
        ArrayList<devolucionDeta> prestamos = new ArrayList<>();
        try {
            String query = "SELECT p.prestamo_id, u.nombre AS estudiante, l.titulo AS libro, p.fecha_prestamo, p.fecha_vencimiento, p.cantidad, d.multa "
                    + "FROM prestamos p "
                    + "INNER JOIN libros l ON p.libro_id = l.libro_id "
                    + "INNER JOIN usuarios u ON p.usuario_id = u.usuario_id "
                    + "INNER JOIN devoluciones d ON p.prestamo_id = d.prestamo_id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int prestamoId = resultSet.getInt("prestamo_id");
                String estudianteNombre = resultSet.getString("estudiante");
                String libroTitulo = resultSet.getString("libro");
                LocalDate fechaPrestamo = resultSet.getDate("fecha_prestamo").toLocalDate();
                LocalDate fechaDevolucion = resultSet.getDate("fecha_vencimiento").toLocalDate();
                int cantidad = resultSet.getInt("cantidad");
                double multa = resultSet.getDouble("multa");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prestamos;
    }

    public ArrayList<String> obtenerEstadosPrestamo(int devolucionId) {
        ArrayList<String> estadosPrestamo = new ArrayList<>();
        try {
            String query = "SELECT e.nombre FROM devoluciones d INNER JOIN estado e ON d.estados_id = e.estados_id WHERE d.devolucion_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, devolucionId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String nombreEstado = resultSet.getString("nombre");
                estadosPrestamo.add(nombreEstado);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estadosPrestamo;
    }

    public ArrayList<String> obtenerEstadoPrestamo() {
        ArrayList<String> estadosPrestamo = new ArrayList<>();
        try {
            String query = "SELECT nombre FROM estado";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String nombreEstado = resultSet.getString("nombre");
                estadosPrestamo.add(nombreEstado);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estadosPrestamo;
    }

    public void realizarDevolucion(int devolucionId, int estadoId) {
        try {
            String query = "UPDATE devoluciones SET estados_id = ? WHERE devolucion_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, estadoId);
            statement.setInt(2, devolucionId);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de excepciones
        }
    }

    public int ObtenerEstadoId(String estadoSeleccionado) {
        int estadoId = 0; // Valor predeterminado
        try {
            String query = "SELECT estados_id FROM estado WHERE nombre = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, estadoSeleccionado);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                estadoId = resultSet.getInt("estados_id");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estadoId;
    }


}
