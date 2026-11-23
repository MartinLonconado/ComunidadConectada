package com.example.comunidadconectada;

public class ChatMessage {
    private final String nombre;
    private final String vivienda;
    private final String mensaje;
    private final String fechaHora;
    private final boolean esMio;

    public ChatMessage(String nombre, String vivienda, String mensaje, String fechaHora, boolean esMio) {
        this.nombre = nombre;
        this.vivienda = vivienda;
        this.mensaje = mensaje;
        this.fechaHora = fechaHora;
        this.esMio = esMio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getVivienda() {
        return vivienda;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public boolean isEsMio() {
        return esMio;
    }
}
