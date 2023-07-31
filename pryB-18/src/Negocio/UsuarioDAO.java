package Negocio;

import Conexion.MySQLConexion;
import java.sql.Connection;
import Modelo.*;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private Connection connection;

    public UsuarioDAO() {
        MySQLConexion conexion = new MySQLConexion();
        connection = conexion.getConnection();
    }

    public Administrador autenticarAdministrador(String usuario, String contraseña) {
        try {
            String query = "SELECT  c.nombre, x.usuario,x.contraseña  FROM administradores x JOIN usuarios c on x.usuario_id=c.usuario_id WHERE x.usuario = ? AND x.contraseña = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, usuario);
            statement.setString(2, contraseña);
            ResultSet resultSet = statement.executeQuery();
            Administrador administrador = null;
            if (resultSet.next()) {
                administrador = new Administrador();
                administrador.setNombre(resultSet.getString("nombre"));
                administrador.setUsuario(resultSet.getString("usuario"));
                administrador.setContraseña(resultSet.getString("contraseña"));
            }
            resultSet.close();
            statement.close();
            return administrador;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Estudiante autenticarEstudiante(String usuario, String contraseña) {
        try {
            String query = "SELECT c.usuario_id,c.nombre,c.email, c.tipo,c.telefono,x.usuario,x.contraseña,x.carrera FROM estudiantes x JOIN usuarios c on x.usuario_id=c.usuario_id WHERE x.usuario = ? AND x.contraseña = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, usuario);
            statement.setString(2, contraseña);
            ResultSet resultSet = statement.executeQuery();
            Estudiante estudiante = null;
            if (resultSet.next()) {
                estudiante = new Estudiante();
                estudiante.setId(resultSet.getInt(1));
                estudiante.setUsuario(resultSet.getString("usuario"));
                estudiante.setContraseña(resultSet.getString("contraseña"));
                estudiante.setNombre(resultSet.getString("nombre"));
                estudiante.setEmail(resultSet.getString("email"));
                estudiante.setTelefono(resultSet.getString("telefono"));
                estudiante.setCarrera(resultSet.getString("carrera"));
            }
            resultSet.close();
            statement.close();
            ;
            return estudiante;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //ss
    public void solicitarPrestamoLibro(Estudiante estudiante, Libro libro) {
        try {
            LocalDate fechaSolicitud = LocalDate.now();
            String query = "INSERT INTO prestamos (id_estudiante, id_libro, fecha_solicitud) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, estudiante.getId());
            statement.setInt(2, libro.getId());
            statement.setDate(3, java.sql.Date.valueOf(fechaSolicitud));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean agregarUsuario(String nombre, String email, String usuario, String contraseña, String tipo, String telefono, String carrera) {
        try {
            String query = "INSERT INTO usuarios (nombre, email, tipo, telefono) VALUES (?,?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, nombre);
            statement.setString(2, email);
            statement.setString(3, tipo);
            statement.setString(4, telefono);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            int userId = 0;
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            }
            // Agregar lógica adicional según el tipo de usuario
            if (tipo.equals("Estudiante")) {
                String estudianteQuery = "INSERT INTO estudiantes (usuario_id,usuario, contraseña, carrera) VALUES (?, ?, ?,?)";
                PreparedStatement estudianteStatement = connection.prepareStatement(estudianteQuery);
                estudianteStatement.setInt(1, userId);
                estudianteStatement.setString(2, usuario);
                estudianteStatement.setString(3, contraseña);
                estudianteStatement.setString(4, carrera);
                estudianteStatement.executeUpdate();
                estudianteStatement.close();
            } else if (tipo.equals("Administrador")) {
                String administradorQuery = "INSERT INTO administradores (usuario_id,usuario, contraseña) VALUES (?, ?, ?)";
                PreparedStatement administradorStatement = connection.prepareStatement(administradorQuery);
                administradorStatement.setInt(1, userId);
                administradorStatement.setString(2, usuario);
                administradorStatement.setString(3, contraseña);
                administradorStatement.executeUpdate();
                administradorStatement.close();
            }
            generatedKeys.close();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Usuario> obtenerUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try {
            String query = "SELECT * FROM usuarios";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int userId = resultSet.getInt("usuario_id");
                String nombre = resultSet.getString("nombre");
                String email = resultSet.getString("email");
                String telefono = resultSet.getString("telefono");
                String tipo = resultSet.getString("tipo");
                Usuario usuario = null;
                if (tipo.equals("Estudiante")) {
                    String usuarioQuery = "SELECT * FROM estudiantes WHERE usuario_id = ?";
                    PreparedStatement usuarioStatement = connection.prepareStatement(usuarioQuery);
                    usuarioStatement.setInt(1, userId);
                    ResultSet usuarioResult = usuarioStatement.executeQuery();
                    if (usuarioResult.next()) {
                        String usuarioEstudiante = usuarioResult.getString("usuario");
                        String contraseñaEstudiante = usuarioResult.getString("contraseña");
                        String carreraEstudiante = usuarioResult.getString("carrera");
                        usuario = new Estudiante(userId, nombre, email, tipo, telefono, usuarioEstudiante, contraseñaEstudiante, carreraEstudiante);
                    }
                } else if (tipo.equals("Administrador")) {
                    String usuarioQuery = "SELECT * FROM administradores WHERE usuario_id = ?";
                    PreparedStatement usuarioStatement = connection.prepareStatement(usuarioQuery);
                    usuarioStatement.setInt(1, userId);
                    ResultSet usuarioResult = usuarioStatement.executeQuery();

                    if (usuarioResult.next()) {
                        String usuarioAdmin = usuarioResult.getString("usuario");
                        String contraseñaAdmin = usuarioResult.getString("contraseña");

                        usuario = new Administrador(userId, nombre, tipo, email, telefono, usuarioAdmin, contraseñaAdmin);
                    }
                }
                if (usuario != null) {
                    usuarios.add(usuario);
                }
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public ArrayList<Usuario> buscarUsuarioPorNombre(String nombreUsuario) {
        ArrayList<Usuario> usuariosEncontrados = new ArrayList<>();
        try {
            String query = "SELECT * FROM usuarios WHERE nombre LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + nombreUsuario + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int userId = resultSet.getInt("usuario_id");
                String nombre = resultSet.getString("nombre");
                String email = resultSet.getString("email");
                String telefono = resultSet.getString("telefono");
                String tipo = resultSet.getString("tipo");
                Usuario usuario = null;
                if (tipo.equals("Estudiante")) {
                    String usuarioQuery = "SELECT * FROM estudiantes WHERE usuario_id = ?";
                    PreparedStatement usuarioStatement = connection.prepareStatement(usuarioQuery);
                    usuarioStatement.setInt(1, userId);
                    ResultSet usuarioResult = usuarioStatement.executeQuery();
                    if (usuarioResult.next()) {
                        String usuarioEstudiante = usuarioResult.getString("usuario");
                        String contraseñaEstudiante = usuarioResult.getString("contraseña");
                        String carreraEstudiante = usuarioResult.getString("carrera");
                        usuario = new Estudiante(userId, nombre, email, tipo, telefono, usuarioEstudiante, contraseñaEstudiante, carreraEstudiante);
                    }
                } else if (tipo.equals("Administrador")) {
                    String usuarioQuery = "SELECT * FROM administradores WHERE usuario_id = ?";
                    PreparedStatement usuarioStatement = connection.prepareStatement(usuarioQuery);
                    usuarioStatement.setInt(1, userId);
                    ResultSet usuarioResult = usuarioStatement.executeQuery();
                    if (usuarioResult.next()) {
                        String usuarioAdmin = usuarioResult.getString("usuario");
                        String contraseñaAdmin = usuarioResult.getString("contraseña");
                        usuario = new Administrador(userId, nombre, tipo, email, telefono, usuarioAdmin, contraseñaAdmin);
                    }
                }
                if (usuario != null) {
                    usuariosEncontrados.add(usuario);
                }
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuariosEncontrados;
    }

    public void actualizarUsuario(Usuario usuario) {
        try {
            String query = "CALL actualizar_tipo_usuario(?, ?, ?, ?, ?, ?, ?, ?)";
            CallableStatement statement = connection.prepareCall(query);
            statement.setInt(1, usuario.getId());
            statement.setString(2, usuario.getNombre());
            statement.setString(3, usuario.getEmail());
            statement.setString(4, usuario.getTipo());
            statement.setString(7, usuario.getTelefono());
            // Establecer un valor vacío para el parámetro 8
            statement.setString(8, "");
            if (usuario instanceof Estudiante) {
                Estudiante estudiante = (Estudiante) usuario;
                statement.setString(5, estudiante.getUsuario());
                statement.setString(6, estudiante.getContraseña());
                statement.setString(8, estudiante.getCarrera());
            } else if (usuario instanceof Administrador) {
                Administrador administrador = (Administrador) usuario;
                statement.setString(5, administrador.getUsuario());
                statement.setString(6, administrador.getContraseña());
            }
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Obtener el tipo anterior del usuario
    public String obtenerTipoAnterior(int usuarioId) throws SQLException {
        String tipoAnterior = "";
        String query = "SELECT tipo FROM usuarios WHERE usuario_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, usuarioId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            tipoAnterior = resultSet.getString("tipo");
        }
        resultSet.close();
        statement.close();
        return tipoAnterior;
    }

    public void eliminarUsuario(int userId) {
        try {
            String query = "CALL eliminar_usuario_con_validacion(?)";
            CallableStatement statement = connection.prepareCall(query);
            statement.setInt(1, userId);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean tienePrestamos(int usuarioId) {
        boolean tienePrestamos = false;
        try {
            String query = "SELECT COUNT(*) FROM prestamos WHERE usuario_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, usuarioId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                tienePrestamos = (count > 0);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tienePrestamos;
    }

    //SEGUNDO PANEL
    public List<Estudiante> obtenerEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();
        try {
            String query = "SELECT u.usuario_id, u.nombre, e.usuario, u.telefono, u.email, e.carrera "
                    + "FROM usuarios u "
                    + "JOIN estudiantes e ON u.usuario_id = e.usuario_id";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("usuario_id");
                String nombre = resultSet.getString("nombre");
                String usuario = resultSet.getString("usuario");
                String telefono = resultSet.getString("telefono");
                String email = resultSet.getString("email");
                String carrera = resultSet.getString("carrera");

                Estudiante estudiante = new Estudiante(id, nombre, email, "Estudiante", telefono, usuario, "", carrera);
                estudiantes.add(estudiante);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return estudiantes;
    }


// Verificar si el usuario ya está en uso
    public boolean existeUsuario(String usuario) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM usuarios WHERE nombre = ?")) {
            stmt.setString(1, usuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

// Verificar si se está intentando registrar el mismo usuario con diferentes contraseñas
    public boolean existeContraseñaDiferente(String usuario, String contraseña) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM vistausuarioscontraseñas WHERE usuario = ? AND contraseña != ?")) {
            stmt.setString(1, usuario);
            stmt.setString(2, contraseña);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    public ArrayList<devolucionDeta> obtenerTodosLosPrestamosEstudiantesPorNom(String nomEstudiante) {
        ArrayList<devolucionDeta> prestamos = new ArrayList<>();
        try {
            String query = "SELECT p.prestamo_id,u.nombre,l.titulo, p.fecha_prestamo, p.fecha_vencimiento, d.multa, e.nombre FROM libros l JOIN prestamos p ON l.libro_id = p.libro_id\n" +
                            "JOIN usuarios u on p.usuario_id=u.usuario_id\n" +
                            "JOIN devoluciones d ON d.prestamo_id = p.prestamo_id\n" +
                            " JOIN estado e ON e.estados_id = d.estados_id\n" +
                            "WHERE u.nombre LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" +nomEstudiante+ "%");
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
