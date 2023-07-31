/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Conexion.MySQLConexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import Modelo.Editorial;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EditorialDAO {

    private Connection connection;

    public EditorialDAO() {
        MySQLConexion conexion = new MySQLConexion();
        connection = conexion.getConnection();
    }

    public void crearEditorial(Editorial editorial) {
        String query = "INSERT INTO editoriales (nombre) VALUES (?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, editorial.getNombre());
            statement.executeUpdate();
            statement.close();
            System.out.println("Editorial creada exitosamente");
        } catch (SQLException e) {
            System.out.println("Error al crear la editorial: " + e.getMessage());
        }
    }

    public ArrayList<Editorial> obtenerTodosLasEditoriales() {
        ArrayList<Editorial> editoriales = new ArrayList<>();
        String query = "SELECT editorial_id, nombre FROM editoriales";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("editorial_id");
                String nombre = rs.getString("nombre");
                Editorial editorial = new Editorial(id, nombre);
                editoriales.add(editorial);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return editoriales;
    }

    public ArrayList<Editorial> obtenerEditoriales() {
        ArrayList<Editorial> editoriales = new ArrayList<>();
        String query = "SELECT * FROM editoriales";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("editorial_id");
                String nombre = resultSet.getString("nombre");

                Editorial editorial = new Editorial(id, nombre);
                editoriales.add(editorial);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener las editoriales: " + e.getMessage());
        }
        return editoriales;
    }

    public void eliminarEditorial(int idEditorial) {
        String query = "DELETE FROM editoriales WHERE editorial_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idEditorial);
            statement.executeUpdate();
            statement.close();
            System.out.println("Editorial eliminada exitosamente");
        } catch (SQLException e) {
            System.out.println("Error al eliminar la editorial: " + e.getMessage());
        }
    }

    public void actualizarEditorial(Editorial editorialActualizada) {
        String query = "UPDATE editoriales SET nombre = ? WHERE editorial_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, editorialActualizada.getNombre());
            statement.setInt(2, editorialActualizada.getId());
            statement.executeUpdate();
            statement.close();
            System.out.println("Editorial actualizada exitosamente");
        } catch (SQLException e) {
            System.out.println("Error al actualizar la editorial: " + e.getMessage());
        }
    }

    public ArrayList<Editorial> buscarEditorialPorNombre(String nombreEditorial) {
        ArrayList<Editorial> editorialesEncontradas = new ArrayList<>();
        String query = "SELECT * FROM editoriales WHERE nombre LIKE ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + nombreEditorial + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Editorial editorial = new Editorial();
                editorial.setId(resultSet.getInt("editorial_id"));
                editorial.setNombre(resultSet.getString("nombre"));
                editorialesEncontradas.add(editorial);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return editorialesEncontradas;
    }

    public Editorial obtenerEditorialPorNombre(String nombre) {
        String sql = "SELECT * FROM editoriales WHERE nombre = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nombre);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("editorial_id");
                return new Editorial(id, nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Editorial> obtenerTodasLasEditoriales() {
        ArrayList<Editorial> editoriales = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM editoriales");
            while (rs.next()) {
                int id = rs.getInt("editorial_id");
                String nombre = rs.getString("nombre");
                Editorial editorial = new Editorial(id, nombre);
                editoriales.add(editorial);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return editoriales;
    }

    public Editorial obtenerEditorialPorId(int ediID) {
        String sql = "SELECT * FROM editoriales WHERE editorial_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, ediID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                return new Editorial(ediID, nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int obtenerEditorialIdPorNombre(String nombreEditorial) {
        int editorialId = 0;
        try {
            String sql = "SELECT editorial_id FROM editoriales WHERE nombre = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nombreEditorial);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                editorialId = resultSet.getInt("editorial_id");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return editorialId;
    }

    public boolean verificarEditorialExistente(String nombreEditorial) {
        try {
            String query = "SELECT COUNT(*) FROM editoriales WHERE nombre = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nombreEditorial);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    

}
