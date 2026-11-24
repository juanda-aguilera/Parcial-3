package com.gitb.juandaaguilera.cafeteria.modelo;

public interface Producto {
    String getNombre();
    String getDescripcion();
    double getPrecio();
    Categoria getCategoria();
}
