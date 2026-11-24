package com.gitb.juandaaguilera.cafeteria.modelo;

public class Plato extends ProductoBase {
    public Plato(String nombre, double precio) {
        super(nombre, precio, Categoria.PLATO);
    }
}
