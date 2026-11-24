package com.gitb.juandaaguilera.cafeteria.decorador;

import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;

public class LecheAlmendras extends AdicionDecorator {
    public static final double PRECIO = 2000;

    public LecheAlmendras(Producto producto) {
        super(exigir(producto, "leche de almendras", Categoria.BEBIDA), "leche de almendras", PRECIO);
    }
}
