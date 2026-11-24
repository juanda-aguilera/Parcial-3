package com.gitb.juandaaguilera.cafeteria.promocion;

import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class PromoMartesDulce implements EstrategiaPromocion {
    @Override
    public String getNombre() {
        return "Martes dulce (50% en postres)";
    }

    @Override
    public boolean aplica(LocalDateTime momento) {
        return momento.getDayOfWeek() == DayOfWeek.TUESDAY;
    }

    @Override
    public double calcularDescuento(Pedido pedido) {
        return Promociones.sumarCategoria(pedido, Categoria.POSTRE) * 0.50;
    }
}
