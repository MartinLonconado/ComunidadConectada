package com.example.comunidadconectada;

public class Anuncio {
    private String id;
    private String titulo;
    private String contenido;
    private String publicadoPor;
    private String fecha;

    public Anuncio() {
    }

    public Anuncio(String titulo, String contenido, String publicadoPor, String fecha) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.publicadoPor = publicadoPor;
        this.fecha = fecha;
    }

    public Anuncio(String id, String titulo, String contenido, String publicadoPor, String fecha) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.publicadoPor = publicadoPor;
        this.fecha = fecha;
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

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getPublicadoPor() {
        return publicadoPor;
    }

    public void setPublicadoPor(String publicadoPor) {
        this.publicadoPor = publicadoPor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
