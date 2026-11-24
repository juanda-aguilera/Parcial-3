package com.gitb.juandaaguilera.cafeteria.pedido;

public class EstadoEntregado extends EstadoBase {
    @Override
    public String getNombre() {
        return ENTREGADO;
    }

    @Override
    public void avanzar(Pedido pedido) {
        throw new IllegalStateException("El pedido entregado pasa a PAGADO solo al registrar el pago en caja");
    }

    @Override
    public void pagar(Pedido pedido) {
        pedido.cambiarEstado(new EstadoPagado());
    }

    @Override
    public boolean permitePago() {
        return true;
    }
}
