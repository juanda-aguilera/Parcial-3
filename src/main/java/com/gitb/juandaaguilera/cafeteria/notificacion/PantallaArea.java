package com.gitb.juandaaguilera.cafeteria.notificacion;

import java.util.ArrayList;
import java.util.List;

public abstract class PantallaArea implements ObservadorPedido {
    private final String area;
    private final TableroNotificaciones tablero;
    private final List<String> recibidos = new ArrayList<>();

    protected PantallaArea(String area, TableroNotificaciones tablero) {
        this.area = area;
        this.tablero = tablero;
    }

    protected void avisar(String mensaje) {
        String linea = "[" + area + "] " + mensaje;
        recibidos.add(linea);
        tablero.publicar(linea);
    }

    public List<String> getRecibidos() {
        return new ArrayList<>(recibidos);
    }
}
