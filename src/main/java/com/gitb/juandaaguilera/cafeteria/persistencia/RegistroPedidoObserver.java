package com.gitb.juandaaguilera.cafeteria.persistencia;

import com.gitb.juandaaguilera.cafeteria.notificacion.ObservadorPedido;
import com.gitb.juandaaguilera.cafeteria.pedido.EstadoPedido;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;

public class RegistroPedidoObserver implements ObservadorPedido {

    private final PedidoDAO dao;

    public RegistroPedidoObserver(PedidoDAO dao) {
        this.dao = dao;
    }

    @Override
    public void actualizar(Pedido pedido, String estadoAnterior, String estadoNuevo) {
        if (EstadoPedido.EN_PREPARACION.equals(estadoNuevo)) {
            dao.guardarDetalle(pedido);
        }
        dao.actualizarEstado(pedido);
    }
}
