package com.gitb.juandaaguilera.cafeteria.decorador;

import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;

public class Tocineta extends AdicionDecorator {
    public static final double PRECIO = 3500;

    public Tocineta(Producto producto) {
        super(exigir(producto, "tocineta", Categoria.PLATO), "tocineta", PRECIO);
    }
}
