package com.gitb.juandaaguilera.cafeteria.decorador;

import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;

public class QuesoExtra extends AdicionDecorator {
    public static final double PRECIO = 3000;

    public QuesoExtra(Producto producto) {
        super(exigir(producto, "queso extra", Categoria.PLATO), "queso extra", PRECIO);
    }
}
