package com.gitb.juandaaguilera.cafeteria.comando;

import com.gitb.juandaaguilera.cafeteria.modelo.Producto;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;

public class AgregarProductoComando implements Comando {
    private final Pedido pedido;
    private final Producto producto;
    private int posicion = -1;

    public AgregarProductoComando(Pedido pedido, Producto producto) {
        this.pedido = pedido;
        this.producto = producto;
    }

    @Override
    public void ejecutar() {
        pedido.agregarProducto(producto);
        posicion = pedido.getProductos().size() - 1;
    }

    @Override
    public void deshacer() {
        pedido.quitarProducto(posicion);
    }

    @Override
    public String getDescripcion() {
        return "Agregar " + producto.getDescripcion();
    }
}
