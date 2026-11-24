package com.gitb.juandaaguilera.cafeteria.notificacion;

import com.gitb.juandaaguilera.cafeteria.modelo.Formato;
import com.gitb.juandaaguilera.cafeteria.pedido.EstadoPedido;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;

public class PantallaCaja extends PantallaArea {
    public PantallaCaja(TableroNotificaciones tablero) {
        super("CAJA", tablero);
    }

    @Override
    public void actualizar(Pedido pedido, String anterior, String nuevo) {
        switch (nuevo) {
            case EstadoPedido.ENTREGADO -> avisar("Pedido #" + pedido.getId() + " por cobrar, subtotal "
                    + Formato.pesos(pedido.getSubtotal()));
            case EstadoPedido.PAGADO -> avisar("Pedido #" + pedido.getId() + " pagado");
            case EstadoPedido.CANCELADO -> avisar("Pedido #" + pedido.getId() + " anulado, no cobrar");
            default -> {
            }
        }
    }
}
