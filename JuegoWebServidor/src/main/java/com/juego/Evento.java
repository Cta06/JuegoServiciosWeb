package com.juego;

public class Evento {

    private String mensaje;

    public Evento() {}

    public Evento(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}