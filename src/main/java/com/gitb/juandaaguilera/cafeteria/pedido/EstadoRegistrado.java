package com.gitb.juandaaguilera.cafeteria.pedido;

public class EstadoRegistrado extends EstadoBase {
    @Override
    public String getNombre() {
        return REGISTRADO;
    }

    @Override
    public void avanzar(Pedido pedido) {
        if (pedido.estaVacio()) {
            throw new IllegalStateException("No se puede enviar a cocina un pedido vacío");
        }
        pedido.cambiarEstado(new EstadoEnPreparacion());
    }

    @Override
    public void cancelar(Pedido pedido) {
        pedido.cambiarEstado(new EstadoCancelado());
    }

    @Override
    public boolean permiteModificar() {
        return true;
    }
}
