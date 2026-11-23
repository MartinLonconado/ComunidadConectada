package com.example.comunidadconectada;

public class Usuario {
    private String uid;
    private String nombre;
    private String departamento;
    private String correo;
    private String rol;
    private String fcmToken;

    // Constructor vacío requerido por Firebase Realtime Database
    public Usuario() {
    }

    public Usuario(String uid, String nombre, String departamento, String correo, String rol) {
        this.uid = uid;
        this.nombre = nombre;
        this.departamento = departamento;
        this.correo = correo;
        this.rol = rol;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
