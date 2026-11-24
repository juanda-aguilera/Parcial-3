package com.gitb.juandaaguilera.cafeteria.builder;

import com.gitb.juandaaguilera.cafeteria.composite.Combo;
import com.gitb.juandaaguilera.cafeteria.fabrica.FabricaLineaMenu;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;

public class DirectorCombos {
    public static final double DESCUENTO_COMBO_DIA = 0.15;

    private final ComboBuilder builder;

    public DirectorCombos(ComboBuilder builder) {
        this.builder = builder;
    }

    public Combo construirComboDelDia(FabricaLineaMenu fabrica) {
        return builder.reiniciar()
                .nombre("Combo " + fabrica.getNombreLinea())
                .conBebida(fabrica.crearBebida())
                .conPlato(fabrica.crearPlato())
                .conPostre(fabrica.crearPostre())
                .conDescuento(DESCUENTO_COMBO_DIA)
                .build();
    }

    public Combo construirComboPersonalizado(String nombre, Producto bebida, Producto plato, Producto postre) {
        builder.reiniciar().nombre(nombre);
        int partes = 0;
        if (bebida != null) {
            builder.conBebida(bebida);
            partes++;
        }
        if (plato != null) {
            builder.conPlato(plato);
            partes++;
        }
        if (postre != null) {
            builder.conPostre(postre);
            partes++;
        }
        return builder.conDescuento(partes == 3 ? 0.10 : 0.05).build();
    }
}
