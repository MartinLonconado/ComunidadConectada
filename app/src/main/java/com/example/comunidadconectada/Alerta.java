package com.example.comunidadconectada;

public class Alerta {
    private String id;
    private String titulo;
    private String descripcion;
    private String residente;
    private String fechaHora;
    private EstadoAlerta estado;

    public Alerta() {
    }

    public Alerta(String titulo, String descripcion, String residente, String fechaHora, EstadoAlerta estado) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.residente = residente;
        this.fechaHora = fechaHora;
        this.estado = estado;
    }

    public Alerta(String id, String titulo, String descripcion, String residente, String fechaHora, EstadoAlerta estado) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.residente = residente;
        this.fechaHora = fechaHora;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getResidente() {
        return residente;
    }

    public void setResidente(String residente) {
        this.residente = residente;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public EstadoAlerta getEstado() {
        return estado;
    }

    public void setEstado(EstadoAlerta estado) {
        this.estado = estado;
    }
}
