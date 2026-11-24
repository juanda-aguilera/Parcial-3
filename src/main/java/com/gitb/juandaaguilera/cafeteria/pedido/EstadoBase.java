package com.gitb.juandaaguilera.cafeteria.pedido;

public abstract class EstadoBase implements EstadoPedido {
    @Override
    public void avanzar(Pedido pedido) {
        throw new IllegalStateException("Un pedido " + getNombre() + " no puede avanzar");
    }

    @Override
    public void cancelar(Pedido pedido) {
        throw new IllegalStateException("Un pedido " + getNombre() + " ya no se puede cancelar");
    }

    @Override
    public void pagar(Pedido pedido) {
        throw new IllegalStateException("Solo se puede pagar un pedido ENTREGADO (está " + getNombre() + ")");
    }

    @Override
    public boolean permiteModificar() {
        return false;
    }

    @Override
    public boolean permitePago() {
        return false;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}
