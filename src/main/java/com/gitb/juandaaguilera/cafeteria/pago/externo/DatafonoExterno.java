package com.gitb.juandaaguilera.cafeteria.pago.externo;

public class DatafonoExterno {
    public static final int APROBADA = 0;
    public static final int FONDOS_INSUFICIENTES = 51;
    public static final int FRANQUICIA_INVALIDA = 14;

    private String ultimaAutorizacion = "";
    private int consecutivo = 1000;

    public int iniciarTransaccion(long valorEnCentavos, String franquicia) {
        if (franquicia == null || !(franquicia.equalsIgnoreCase("VISA") || franquicia.equalsIgnoreCase("MASTERCARD"))) {
            return FRANQUICIA_INVALIDA;
        }
        if (valorEnCentavos > 50_000_000L) {
            return FONDOS_INSUFICIENTES;
        }
        consecutivo++;
        ultimaAutorizacion = "AUT" + consecutivo;
        return APROBADA;
    }

    public String obtenerAutorizacion() {
        return ultimaAutorizacion;
    }
}
