package com.gitb.juandaaguilera.cafeteria.pago;

import com.gitb.juandaaguilera.cafeteria.pago.externo.DatafonoExterno;

public class AdaptadorDatafono implements MetodoPago {
    private final DatafonoExterno datafono;
    private final String franquicia;

    public AdaptadorDatafono(DatafonoExterno datafono, String franquicia) {
        this.datafono = datafono;
        this.franquicia = franquicia;
    }

    @Override
    public String getNombre() {
        return "TARJETA";
    }

    @Override
    public ResultadoPago pagar(double monto) {
        long centavos = Math.round(monto * 100);
        int codigo = datafono.iniciarTransaccion(centavos, franquicia);
        return switch (codigo) {
            case DatafonoExterno.APROBADA -> ResultadoPago.aprobado(datafono.obtenerAutorizacion(),
                    "Tarjeta " + franquicia.toUpperCase() + " aprobada", 0);
            case DatafonoExterno.FONDOS_INSUFICIENTES -> ResultadoPago.rechazado("Tarjeta rechazada: fondos insuficientes");
            case DatafonoExterno.FRANQUICIA_INVALIDA -> ResultadoPago.rechazado("Franquicia no aceptada (use VISA o MASTERCARD)");
            default -> ResultadoPago.rechazado("Error del datáfono, código " + codigo);
        };
    }
}
