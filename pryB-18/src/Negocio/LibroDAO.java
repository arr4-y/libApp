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
public class LibroDAO {

    private Connection connection;
    private AutorDAO autorDAO;
    private EditorialDAO editorialDAO;
    
    public LibroDAO() {
        MySQLConexion conexion = new MySQLConexion();
        connection = conexion.getConnection();
        autorDAO = new AutorDAO();
        editorialDAO = new EditorialDAO();
    }

    public void crearLibro(Libro libro) {
        String query = "INSERT INTO libros (titulo, autor_id, editorial_id, cantidad) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, libro.getTitulo());
            statement.setString(2, libro.getAutor());
            statement.setString(3, libro.getEditorial());
            statement.setInt(4, libro.getCantidad());
            statement.executeUpdate();
            statement.close();
            System.out.println("Libro creado exitosamente");
        } catch (SQLException e) {
            System.out.println("Error al crear el libro: " + e.getMessage());
        }
    }

    public ArrayList<Libro> obtenerTodosLosLibros() {
        ArrayList<Libro> libros = new ArrayList<>();

        String query = "SELECT l.libro_id, l.titulo, a.nombre AS autor, e.nombre AS editorial, l.cantidad "
                + "FROM libros l "
                + "JOIN autores a ON l.autor_id = a.autor_id "
                + "JOIN editoriales e ON l.editorial_id = e.editorial_id";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Libro libro = new Libro();
                libro.setId(resultSet.getInt("libro_id"));
                libro.setTitulo(resultSet.getString("titulo"));
                libro.setAutor(resultSet.getString("autor"));
                libro.setEditorial(resultSet.getString("editorial"));
                libro.setCantidad(resultSet.getInt("cantidad"));
                libros.add(libro);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
        }
        return libros;
    }

    public void eliminarLibro(int idLibro) {
        String sql = "DELETE FROM libros WHERE libro_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idLibro);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarLibro(Libro libroActualizado) {
        String sql = "UPDATE libros SET titulo = ?, autor_id = ?, editorial_id = ?, cantidad = ? WHERE libro_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, libroActualizado.getTitulo());
            statement.setInt(2, autorDAO.obtenerAutorIdPorNombre(libroActualizado.getAutor()));
            statement.setInt(3, editorialDAO.obtenerEditorialIdPorNombre(libroActualizado.getEditorial()));
            statement.setInt(4, libroActualizado.getCantidad());
            statement.setInt(5, libroActualizado.getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Libro> buscarLibroPorTitulo(String tituloLibro) {
        ArrayList<Libro> librosEncontrados = new ArrayList<>();
        String query = "SELECT * FROM libros WHERE titulo LIKE ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + tituloLibro + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Libro libro = new Libro();
                libro.setId(resultSet.getInt("libro_id"));
                libro.setTitulo(resultSet.getString("titulo"));
                int autorId = resultSet.getInt("autor_id");
                Autor nombreAutor = autorDAO.obtenerAutorPorId(autorId);  // Obtener nombre del autor por su ID
                libro.setAutor(nombreAutor);
                int ediId = resultSet.getInt("editorial_id");
                Editorial nombreEditorial = editorialDAO.obtenerEditorialPorId(ediId);
                libro.setEditorial(nombreEditorial);
                libro.setCantidad(resultSet.getInt("cantidad"));
                librosEncontrados.add(libro);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return librosEncontrados;
    }

    //lo va usar la clase prestamoDAO
    public Libro obtenerLibroPorId(String libroId) {
        Libro libro = null;
        try {
            String query = "SELECT * FROM libros WHERE libro_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, libroId); // Convertir a int
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("libro_id");
                String titulo = resultSet.getString("titulo");
                libro = new Libro();
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libro;
    }
    
    public ArrayList<devolucionDeta> obtenerTodosLosPrestamosLibroPorNom(String nomlibro) {
        ArrayList<devolucionDeta> prestamos = new ArrayList<>();
        try {
            String query = "SELECT p.prestamo_id,u.nombre,l.titulo, p.fecha_prestamo, p.fecha_vencimiento, d.multa, e.nombre FROM libros l JOIN prestamos p ON l.libro_id = p.libro_id\n" +
                            "JOIN usuarios u on p.usuario_id=u.usuario_id\n" +
                            "JOIN devoluciones d ON d.prestamo_id = p.prestamo_id\n" +
                            " JOIN estado e ON e.estados_id = d.estados_id\n" +
                            "WHERE l.titulo LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" +nomlibro+ "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int prestamoId = resultSet.getInt(1);
                String estudianteNombre = resultSet.getString(2);
                String libroTitulo = resultSet.getString(3);
                LocalDate fechaPrestamo = resultSet.getDate(4).toLocalDate();
                LocalDate fechaDevolucion = resultSet.getDate(5).toLocalDate();
                double multa = resultSet.getDouble(6);
                String estado = resultSet.getString(7);
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
}
