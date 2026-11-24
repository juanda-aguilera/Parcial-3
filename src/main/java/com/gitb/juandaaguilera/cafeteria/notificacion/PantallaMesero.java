package com.gitb.juandaaguilera.cafeteria.notificacion;

import com.gitb.juandaaguilera.cafeteria.pedido.EstadoPedido;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;

public class PantallaMesero extends PantallaArea {
    public PantallaMesero(TableroNotificaciones tablero) {
        super("MESERO", tablero);
    }

    @Override
    public void actualizar(Pedido pedido, String anterior, String nuevo) {
        if (EstadoPedido.LISTO.equals(nuevo)) {
            avisar("Recoger pedido #" + pedido.getId() + " en barra y llevar a " + pedido.getMesa());
        } else if (EstadoPedido.CANCELADO.equals(nuevo)) {
            avisar("Informar a " + pedido.getMesa() + " que el pedido #" + pedido.getId() + " se canceló");
        }
    }
}
