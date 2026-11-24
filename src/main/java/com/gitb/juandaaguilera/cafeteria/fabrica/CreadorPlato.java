package com.gitb.juandaaguilera.cafeteria.fabrica;

import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Plato;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;

public class CreadorPlato extends CreadorProducto {
    @Override
    public Categoria getCategoria() {
        return Categoria.PLATO;
    }

    @Override
    protected Producto crearProducto(ItemMenu item) {
        return new Plato(item.nombre(), item.precio());
    }
}
