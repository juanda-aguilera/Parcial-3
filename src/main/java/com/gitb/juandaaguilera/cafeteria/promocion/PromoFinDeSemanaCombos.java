package com.gitb.juandaaguilera.cafeteria.promocion;

import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class PromoFinDeSemanaCombos implements EstrategiaPromocion {
    @Override
    public String getNombre() {
        return "Fin de semana (10% extra en combos)";
    }

    @Override
    public boolean aplica(LocalDateTime momento) {
        DayOfWeek dia = momento.getDayOfWeek();
        return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;
    }

    @Override
    public double calcularDescuento(Pedido pedido) {
        return Promociones.sumarCategoria(pedido, Categoria.COMBO) * 0.10;
    }
}
