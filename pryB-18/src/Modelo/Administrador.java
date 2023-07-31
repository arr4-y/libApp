package Modelo;

public class Administrador extends Usuario {

    private String usuario;
    private String contraseña;
    private Usuario usu;

    public Administrador(int id, String nombre, String tipo, String email, String telefono, String usuario, String contraseña) {
        super(id, nombre, email, telefono, tipo);
        this.usuario = usuario;
        this.contraseña = contraseña;
    }

    public Administrador() {
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

    public Usuario getUsu() {
        return usu;
    }

    public void setUsu(Usuario usu) {
        this.usu = usu;
    }
    

}
