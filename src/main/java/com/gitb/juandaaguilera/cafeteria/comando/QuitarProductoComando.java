package com.gitb.juandaaguilera.cafeteria.comando;

import com.gitb.juandaaguilera.cafeteria.modelo.Producto;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;

public class QuitarProductoComando implements Comando {
    private final Pedido pedido;
    private final int indice;
    private Producto quitado;

    public QuitarProductoComando(Pedido pedido, int indice) {
        this.pedido = pedido;
        this.indice = indice;
    }

    @Override
    public void ejecutar() {
        quitado = pedido.quitarProducto(indice);
    }

    @Override
    public void deshacer() {
        pedido.agregarProducto(indice, quitado);
    }

    @Override
    public String getDescripcion() {
        return "Quitar " + (quitado == null ? "producto #" + (indice + 1) : quitado.getDescripcion());
    }
}
