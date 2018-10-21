package com.example.mislugares;

public class Usuario {
    private String nombre;
    private String correo;
    private long inicioSesion;

    public Usuario() {
    }

    public Usuario(String nombre, String correo, long inicioSesion) {
        this.nombre = nombre;
        this.correo = correo;
        this.inicioSesion = inicioSesion;
    }

    public Usuario(String nombre, String correo) {
        this(nombre, correo, System.currentTimeMillis());
    }
}
