package com.gitb.juandaaguilera.cafeteria.promocion;

import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class PromoMadrugador implements EstrategiaPromocion {
    @Override
    public String getNombre() {
        return "Madrugador (10% en todo, L-V 6-9 a. m.)";
    }

    @Override
    public boolean aplica(LocalDateTime momento) {
        DayOfWeek dia = momento.getDayOfWeek();
        boolean habil = dia != DayOfWeek.SATURDAY && dia != DayOfWeek.SUNDAY;
        int hora = momento.getHour();
        return habil && hora >= 6 && hora < 9;
    }

    @Override
    public double calcularDescuento(Pedido pedido) {
        return pedido.getSubtotal() * 0.10;
    }
}
