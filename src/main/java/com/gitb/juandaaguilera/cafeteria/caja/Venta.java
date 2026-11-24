package com.gitb.juandaaguilera.cafeteria.caja;

import java.time.LocalDateTime;
import java.util.List;

public record Venta(int idPedido, String mesa, double subtotal, double descuento, double total,
        String metodoPago, String promocion, LocalDateTime fecha, List<String> productos) {
}
