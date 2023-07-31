package Modelo;

 
public class Libro {

    private int id;
    private String titulo;
    private String autor;
    private String editorial;
    private int cantidad;

    // Constructor
    public Libro(String titulo, String autor, String editorial, int cantidad) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.cantidad = cantidad;
    }

    public Libro() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setAutorId(String autorId) {
        this.autor = autorId;
    }

    public void setEditorialId(String editorialId) {
        this.editorial = editorialId;
    }

    public void setAutor(Autor nombreAutor) {
        this.autor = nombreAutor.getNombre();

    }

    public void setEditorial(Editorial nombreEditorial) {
        this.editorial = nombreEditorial.getNombre();

    }
    
    
    

}
