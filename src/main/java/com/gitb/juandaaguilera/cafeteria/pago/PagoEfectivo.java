package com.gitb.juandaaguilera.cafeteria.pago;

import com.gitb.juandaaguilera.cafeteria.modelo.Formato;

public class PagoEfectivo implements MetodoPago {
    private final double recibido;

    public PagoEfectivo(double recibido) {
        this.recibido = recibido;
    }

    @Override
    public String getNombre() {
        return "EFECTIVO";
    }

    @Override
    public ResultadoPago pagar(double monto) {
        if (recibido < monto) {
            return ResultadoPago.rechazado("Dinero insuficiente: faltan " + Formato.pesos(monto - recibido));
        }
        double cambio = recibido - monto;
        return ResultadoPago.aprobado("EF-" + System.currentTimeMillis() % 100000,
                "Pago en efectivo, cambio " + Formato.pesos(cambio), cambio);
    }
}
