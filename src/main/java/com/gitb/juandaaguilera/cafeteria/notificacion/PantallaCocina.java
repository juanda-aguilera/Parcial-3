package com.gitb.juandaaguilera.cafeteria.notificacion;

import com.gitb.juandaaguilera.cafeteria.modelo.Producto;
import com.gitb.juandaaguilera.cafeteria.pedido.EstadoPedido;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;

public class PantallaCocina extends PantallaArea {
    public PantallaCocina(TableroNotificaciones tablero) {
        super("COCINA", tablero);
    }

    @Override
    public void actualizar(Pedido pedido, String anterior, String nuevo) {
        if (EstadoPedido.EN_PREPARACION.equals(nuevo)) {
            StringBuilder sb = new StringBuilder("Preparar pedido #" + pedido.getId() + ": ");
            for (Producto p : pedido.getProductos()) {
                sb.append(p.getDescripcion()).append("; ");
            }
            avisar(sb.toString());
        } else if (EstadoPedido.CANCELADO.equals(nuevo) && EstadoPedido.EN_PREPARACION.equals(anterior)) {
            avisar("Detener pedido #" + pedido.getId() + ", fue cancelado");
        }
    }
}
