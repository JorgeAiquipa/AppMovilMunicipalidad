package com.municipalidad.upc.entidades;


public class Dependiente {
    private String dniDependiente;
    private String dniCiudadano;
    private String nombre;
    private int edad;
    private String sexo;
    private String estado;

    public Dependiente(){}

    public String getDniDependiente() {
        return dniDependiente;
    }

    public void setDniDependiente(String dniDependiente) {
        this.dniDependiente = dniDependiente;
    }

    public String getDniCiudadano() {
        return dniCiudadano;
    }

    public void setDniCiudadano(String dniCiudadano) {
        this.dniCiudadano = dniCiudadano;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String toString() {
        return getNombre();
    }
}
