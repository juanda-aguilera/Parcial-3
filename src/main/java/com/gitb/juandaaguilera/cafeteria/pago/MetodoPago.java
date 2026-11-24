package com.gitb.juandaaguilera.cafeteria.pago;

public interface MetodoPago {
    String getNombre();
    ResultadoPago pagar(double monto);
}
