package com.gitb.juandaaguilera.cafeteria.promocion;

import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;

final class Promociones {
    private Promociones() {
    }

    static double sumarCategoria(Pedido pedido, Categoria categoria) {
        double suma = 0;
        for (Producto p : pedido.getProductos()) {
            if (p.getCategoria() == categoria) {
                suma += p.getPrecio();
            }
        }
        return suma;
    }
}
