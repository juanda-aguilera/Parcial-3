package com.gitb.juandaaguilera.cafeteria.notificacion;

import java.util.ArrayList;
import java.util.List;

public class TableroNotificaciones {
    private final List<String> mensajes = new ArrayList<>();

    public void publicar(String mensaje) {
        mensajes.add(mensaje);
    }

    public List<String> getMensajes() {
        return new ArrayList<>(mensajes);
    }
}
