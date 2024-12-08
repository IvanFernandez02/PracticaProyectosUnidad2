package com.practica.controller.model;

public class Proyecto {
    private Integer id;
    private String nombre;
    private Double inversion;
    private String fechaInicio;
    private String fechaFin;
    private Integer tiempoDeVida;
    private Integer capacidad;

    public Proyecto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getInversion() {
        return inversion;
    }

    public void setInversion(Double inversion) {
        this.inversion = inversion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getTiempoDeVida() {
        return tiempoDeVida;
    }

    public void setTiempoDeVida(Integer tiempoDeVida) {
        this.tiempoDeVida = tiempoDeVida;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public String toString() {
        return "Proyecto{" + "id=" + id + ", nombre=" + nombre + ", inversion=" + inversion + ", fechaInicio="
                + fechaInicio + ", fechaFin=" + fechaFin + ", tiempoDeVida=" + tiempoDeVida + ", capacidad=" + capacidad
                + '}';
    }

}
