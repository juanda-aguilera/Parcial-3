package com.gitb.juandaaguilera.cafeteria.comando;

import com.gitb.juandaaguilera.cafeteria.decorador.CatalogoAdiciones;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;

public class AplicarAdicionComando implements Comando {
    private final Pedido pedido;
    private final int indice;
    private final String codigoAdicion;
    private Producto original;

    public AplicarAdicionComando(Pedido pedido, int indice, String codigoAdicion) {
        this.pedido = pedido;
        this.indice = indice;
        this.codigoAdicion = codigoAdicion;
    }

    @Override
    public void ejecutar() {
        original = pedido.getProducto(indice);
        Producto decorado = CatalogoAdiciones.aplicar(codigoAdicion, original);
        pedido.reemplazarProducto(indice, decorado);
    }

    @Override
    public void deshacer() {
        pedido.reemplazarProducto(indice, original);
    }

    @Override
    public String getDescripcion() {
        return "Adición " + codigoAdicion + " al producto #" + (indice + 1);
    }
}
