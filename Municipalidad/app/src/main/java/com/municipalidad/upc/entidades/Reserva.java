package com.municipalidad.upc.entidades;


public class Reserva {

    private int idHorario;
    private String dia;
    private String disciplina;
    private String sede;
    private String horaInicio;
    private String horaFin;
    private String edadDesde;
    private String edadHasta;
    private int disponibles;
    private double precio;
    private int idActividad;
    private int idMatricula;

    public Reserva(){}

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getEdadDesde() {
        return edadDesde;
    }

    public void setEdadDesde(String edadDesde) {
        this.edadDesde = edadDesde;
    }

    public String getEdadHasta() {
        return edadHasta;
    }

    public void setEdadHasta(String edadHasta) {
        this.edadHasta = edadHasta;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public int getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(int idMatricula) {
        this.idMatricula = idMatricula;
    }
}
