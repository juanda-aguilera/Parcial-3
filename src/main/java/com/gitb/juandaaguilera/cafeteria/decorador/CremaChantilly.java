package com.gitb.juandaaguilera.cafeteria.decorador;

import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;

public class CremaChantilly extends AdicionDecorator {
    public static final double PRECIO = 1500;

    public CremaChantilly(Producto producto) {
        super(exigir(producto, "crema chantilly", Categoria.BEBIDA, Categoria.POSTRE), "crema chantilly", PRECIO);
    }
}
