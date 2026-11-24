package com.gitb.juandaaguilera.cafeteria.promocion;

import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;
import java.time.LocalDateTime;

public class SinPromocion implements EstrategiaPromocion {
    @Override
    public String getNombre() {
        return "Sin promoción";
    }

    @Override
    public boolean aplica(LocalDateTime momento) {
        return true;
    }

    @Override
    public double calcularDescuento(Pedido pedido) {
        return 0;
    }
}
