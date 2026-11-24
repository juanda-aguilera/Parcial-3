package com.gitb.juandaaguilera.cafeteria.modelo;

public class Bebida extends ProductoBase {
    private final boolean caliente;

    public Bebida(String nombre, double precio, boolean caliente) {
        super(nombre, precio, Categoria.BEBIDA);
        this.caliente = caliente;
    }

    public boolean isCaliente() {
        return caliente;
    }
}
