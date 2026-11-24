package com.gitb.juandaaguilera.cafeteria.fabrica;

import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Postre;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;

public class CreadorPostre extends CreadorProducto {
    @Override
    public Categoria getCategoria() {
        return Categoria.POSTRE;
    }

    @Override
    protected Producto crearProducto(ItemMenu item) {
        return new Postre(item.nombre(), item.precio());
    }
}
