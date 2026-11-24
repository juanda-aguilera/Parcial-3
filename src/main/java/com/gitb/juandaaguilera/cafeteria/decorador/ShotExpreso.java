package com.gitb.juandaaguilera.cafeteria.decorador;

import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;

public class ShotExpreso extends AdicionDecorator {
    public static final double PRECIO = 2500;

    public ShotExpreso(Producto producto) {
        super(exigir(producto, "shot de expreso", Categoria.BEBIDA), "shot de expreso", PRECIO);
    }
}
