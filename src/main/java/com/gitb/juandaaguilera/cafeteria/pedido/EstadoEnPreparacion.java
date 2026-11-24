package com.gitb.juandaaguilera.cafeteria.pedido;

public class EstadoEnPreparacion extends EstadoBase {
    @Override
    public String getNombre() {
        return EN_PREPARACION;
    }

    @Override
    public void avanzar(Pedido pedido) {
        pedido.cambiarEstado(new EstadoListo());
    }

    @Override
    public void cancelar(Pedido pedido) {
        pedido.cambiarEstado(new EstadoCancelado());
    }
}
