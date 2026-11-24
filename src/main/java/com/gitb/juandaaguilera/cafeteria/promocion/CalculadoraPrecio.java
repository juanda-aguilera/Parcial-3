package com.gitb.juandaaguilera.cafeteria.promocion;

import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;

public class CalculadoraPrecio {
    private EstrategiaPromocion estrategia = new SinPromocion();

    public void setEstrategia(EstrategiaPromocion estrategia) {
        this.estrategia = (estrategia == null) ? new SinPromocion() : estrategia;
    }

    public EstrategiaPromocion getEstrategia() {
        return estrategia;
    }

    public double calcularDescuento(Pedido pedido) {
        double descuento = estrategia.calcularDescuento(pedido);
        return Math.max(0, Math.min(descuento, pedido.getSubtotal()));
    }

    public double calcularTotal(Pedido pedido) {
        return pedido.getSubtotal() - calcularDescuento(pedido);
    }
}
