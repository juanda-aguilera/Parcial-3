package com.gitb.juandaaguilera.cafeteria.pedido;

public class EstadoListo extends EstadoBase {
    @Override
    public String getNombre() {
        return LISTO;
    }

    @Override
    public void avanzar(Pedido pedido) {
        pedido.cambiarEstado(new EstadoEntregado());
    }
}
