package com.gitb.juandaaguilera.cafeteria.pago;

public record ResultadoPago(boolean aprobado, String referencia, String mensaje, double cambio) {
    public static ResultadoPago aprobado(String referencia, String mensaje, double cambio) {
        return new ResultadoPago(true, referencia, mensaje, cambio);
    }

    public static ResultadoPago rechazado(String mensaje) {
        return new ResultadoPago(false, "-", mensaje, 0);
    }
}
