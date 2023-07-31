/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
 

 
public class devolucionDeta {

    private Prestamo prestamo;
    private String tit;
    private int nomA;
    private String fechPre;
    private String fechDev;
    private String estado;
    private double multa;
    private String descp;

    public devolucionDeta() {
    }

    public devolucionDeta(String tit, String fechPre, String fechDev, String estado, double multa) {
        this.tit = tit;
        this.fechPre = fechPre;
        this.fechDev = fechDev;
        this.estado = estado;
        this.multa = multa;
    }

    public devolucionDeta(Prestamo prestamo, double multa, String estado) {
        this.prestamo = prestamo;
        this.multa = multa;
        this.estado = estado;
    }

    public String getTit() {
        return tit;
    }

    public void setTit(String tit) {
        this.tit = tit;
    }

    public int getNomA() {
        return nomA;
    }

    public void setNomA(int nomA) {
        this.nomA = nomA;
    }

    public String getFechPre() {
        return fechPre;
    }

    public void setFechPre(String fechPre) {
        this.fechPre = fechPre;
    }

    public String getFechDev() {
        return fechDev;
    }

    public void setFechDev(String fechDev) {
        this.fechDev = fechDev;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getMulta() {
        return multa;
    }

    public void setMulta(double multa) {
        this.multa = multa;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }
}
