package com.gitb.juandaaguilera.cafeteria.promocion;

import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;
import java.time.LocalDateTime;

public interface EstrategiaPromocion {
    String getNombre();

    boolean aplica(LocalDateTime momento);

    double calcularDescuento(Pedido pedido);
}
