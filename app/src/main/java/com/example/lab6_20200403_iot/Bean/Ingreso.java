package com.example.lab6_20200403_iot.Bean;

public class Ingreso {
    private String titulo;
    private double monto;
    private String descripcion;
    private String fecha;
    private String userId;


    public Ingreso(){

    }
    public Ingreso( String titulo, double monto, String descripcion, String fecha, String userId) {
        this.titulo = titulo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
