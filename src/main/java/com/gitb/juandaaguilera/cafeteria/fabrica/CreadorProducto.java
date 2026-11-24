package com.gitb.juandaaguilera.cafeteria.fabrica;

import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;

public abstract class CreadorProducto {
    public final Producto crear(ItemMenu item) {
        if (item.categoria() != getCategoria()) {
            throw new IllegalArgumentException("El creador de " + getCategoria()
                    + " no puede crear un producto de tipo " + item.categoria());
        }
        if (item.precio() <= 0) {
            throw new IllegalArgumentException("El ítem " + item.codigo() + " no tiene precio válido");
        }
        return crearProducto(item);
    }

    public abstract Categoria getCategoria();

    protected abstract Producto crearProducto(ItemMenu item);
}
