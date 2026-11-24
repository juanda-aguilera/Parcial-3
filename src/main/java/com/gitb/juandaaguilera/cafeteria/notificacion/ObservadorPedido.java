package com.gitb.juandaaguilera.cafeteria.notificacion;

import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;

public interface ObservadorPedido {
    void actualizar(Pedido pedido, String estadoAnterior, String estadoNuevo);
}
