package com.example.comunidadconectada;

public class Anuncio {
    private final String titulo;
    private final String contenido;
    private final String publicadoPor;
    private final String fecha;

    public Anuncio(String titulo, String contenido, String publicadoPor, String fecha) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.publicadoPor = publicadoPor;
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public String getPublicadoPor() {
        return publicadoPor;
    }

    public String getFecha() {
        return fecha;
    }
}
