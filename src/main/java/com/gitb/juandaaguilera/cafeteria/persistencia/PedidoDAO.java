package com.gitb.juandaaguilera.cafeteria.persistencia;

import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;

public interface PedidoDAO {
    int siguienteId();
    void guardar(Pedido pedido);
    void actualizarEstado(Pedido pedido);
    void guardarDetalle(Pedido pedido);
}
