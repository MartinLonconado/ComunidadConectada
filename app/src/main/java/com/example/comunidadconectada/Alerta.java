package com.example.comunidadconectada;

public class Alerta {
    private final String titulo;
    private final String descripcion;
    private final String residente;
    private final String fechaHora;
    private final EstadoAlerta estado;

    public Alerta(String titulo, String descripcion, String residente, String fechaHora, EstadoAlerta estado) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.residente = residente;
        this.fechaHora = fechaHora;
        this.estado = estado;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getResidente() {
        return residente;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public EstadoAlerta getEstado() {
        return estado;
    }
}
