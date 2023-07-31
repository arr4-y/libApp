/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Conexion.MySQLConexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import Modelo.Autor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class AutorDAO {

    private Connection connection;

    public AutorDAO() {
        MySQLConexion conexion = new MySQLConexion();
        connection = conexion.getConnection();
    }

    public void crearAutor(Autor autor) {
        String query = "INSERT INTO autores (nombre) VALUES (?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, autor.getNombre());
            statement.executeUpdate();
            statement.close();
            System.out.println("Autor creado exitosamente");
        } catch (SQLException e) {
            System.out.println("Error al crear el autor: " + e.getMessage());
        }
    }

    public Autor obtenerAutorPorNombre(String nombre) {
        String sql = "SELECT * FROM autores WHERE nombre = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nombre);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("autor_id");
                return new Autor(id, nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Autor> obtenerAutores() {
        ArrayList<Autor> autores = new ArrayList<>();
        String query = "SELECT * FROM autores";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Autor autor = new Autor();
                autor.setId(rs.getInt("autor_id"));
                autor.setNombre(rs.getString("nombre"));
                autores.add(autor);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return autores;
    }

    public void eliminarAutor(int idAutor) {
        String sql = "DELETE FROM autores WHERE autor_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idAutor);
            statement.executeUpdate();
            statement.close();
            System.out.println("Autor eliminado exitosamente");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el autor. Esto ocurre porque esta asociado a un libro ", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarAutor(Autor autor) {
        String query = "UPDATE autores SET nombre = ? WHERE autor_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, autor.getNombre());
            statement.setInt(2, autor.getId());
            statement.executeUpdate();
            statement.close();
            System.out.println("Autor actualizado exitosamente");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el autor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ArrayList<Autor> buscarAutorPorNombre(String nombreAutor) {
        ArrayList<Autor> autoresEncontrados = new ArrayList<>();
        String query = "SELECT * FROM autores WHERE nombre LIKE ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + nombreAutor + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Autor autor = new Autor();
                autor.setId(resultSet.getInt("autor_id"));
                autor.setNombre(resultSet.getString("nombre"));
                autoresEncontrados.add(autor);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return autoresEncontrados;
    }

    public Autor obtenerAutorPorId(int autorID) {
        String sql = "SELECT * FROM autores WHERE autor_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, autorID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                return new Autor(autorID, nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int obtenerAutorIdPorNombre(String nombreAutor) {
        int autorId = 0;
        try {
            String sql = "SELECT autor_id FROM autores WHERE nombre = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nombreAutor);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                autorId = resultSet.getInt("autor_id");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return autorId;
    }

    public boolean existeAutor(String nombreAutor) {
        boolean existe = false;
        String query = "SELECT COUNT(*) FROM autores WHERE nombre = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nombreAutor);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    existe = count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existe;
    }

}
