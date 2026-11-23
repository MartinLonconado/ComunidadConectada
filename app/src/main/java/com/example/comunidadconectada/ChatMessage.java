package com.example.comunidadconectada;

public class ChatMessage {
    private String senderUid;
    private String nombre;
    private String vivienda;
    private String mensaje;
    private String fechaHora;
    private boolean esMio;

    public ChatMessage() {
    }

    public ChatMessage(String nombre, String vivienda, String mensaje, String fechaHora, boolean esMio) {
        this.nombre = nombre;
        this.vivienda = vivienda;
        this.mensaje = mensaje;
        this.fechaHora = fechaHora;
        this.esMio = esMio;
    }

    public ChatMessage(String senderUid, String nombre, String vivienda, String mensaje, String fechaHora) {
        this.senderUid = senderUid;
        this.nombre = nombre;
        this.vivienda = vivienda;
        this.mensaje = mensaje;
        this.fechaHora = fechaHora;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getVivienda() {
        return vivienda;
    }

    public void setVivienda(String vivienda) {
        this.vivienda = vivienda;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public boolean isEsMio() {
        return esMio;
    }

    public void setEsMio(boolean esMio) {
        this.esMio = esMio;
    }
}
