package com.municipalidad.upc.entidades;

public class Actividad {
    private int id;
    private String descripcion;

    public Actividad(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String toString() {
        return getDescripcion();
    }
}
