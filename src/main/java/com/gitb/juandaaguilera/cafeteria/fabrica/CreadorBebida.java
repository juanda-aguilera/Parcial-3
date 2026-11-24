package com.gitb.juandaaguilera.cafeteria.fabrica;

import com.gitb.juandaaguilera.cafeteria.modelo.Bebida;
import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;

public class CreadorBebida extends CreadorProducto {
    @Override
    public Categoria getCategoria() {
        return Categoria.BEBIDA;
    }

    @Override
    protected Producto crearProducto(ItemMenu item) {
        boolean caliente = !"fria".equalsIgnoreCase(item.atributo());
        return new Bebida(item.nombre(), item.precio(), caliente);
    }
}
