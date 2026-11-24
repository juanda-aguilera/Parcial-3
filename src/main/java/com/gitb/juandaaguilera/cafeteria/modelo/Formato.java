package com.gitb.juandaaguilera.cafeteria.modelo;

import java.util.Locale;

public final class Formato {
    private static final Locale CO = Locale.forLanguageTag("es-CO");

    private Formato() {
    }

    public static String pesos(double valor) {
        return String.format(CO, "$%,.0f", valor);
    }
}
