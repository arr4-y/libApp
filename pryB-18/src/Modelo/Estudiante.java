package Modelo;

public class Estudiante extends Usuario {

    private String usuario;
    private String contraseña;
    private String carrera;
    private Usuario usu;

    public Estudiante(int id, String nombre, String email, String tipo, String telefono, String usuario, String contraseña, String carrera) {
        super(id, nombre, email, telefono, tipo);
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.carrera = carrera;
    }

    public Estudiante() {
        super(null, null, null, null);
    }
    public String getUsuario() {
        return usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public Usuario getUsu() {
        return usu;
    }
  
    public void setUsu(Usuario usu) {
        this.usu = usu;
    }
    
    

}
