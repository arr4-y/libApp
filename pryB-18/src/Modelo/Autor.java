package Modelo;

public class Autor {

    private int id;
    private String nombre;

    // Constructor
    public Autor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Autor() {
    }
  
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
