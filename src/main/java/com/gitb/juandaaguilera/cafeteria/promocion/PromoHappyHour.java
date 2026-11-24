package com.gitb.juandaaguilera.cafeteria.promocion;

import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;
import java.time.LocalDateTime;

public class PromoHappyHour implements EstrategiaPromocion {
    @Override
    public String getNombre() {
        return "Happy hour (20% en bebidas, 3-6 p. m.)";
    }

    @Override
    public boolean aplica(LocalDateTime momento) {
        int hora = momento.getHour();
        return hora >= 15 && hora < 18;
    }

    @Override
    public double calcularDescuento(Pedido pedido) {
        return Promociones.sumarCategoria(pedido, Categoria.BEBIDA) * 0.20;
    }
}
