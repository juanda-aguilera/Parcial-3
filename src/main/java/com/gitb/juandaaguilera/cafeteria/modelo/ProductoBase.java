package com.gitb.juandaaguilera.cafeteria.modelo;

public abstract class ProductoBase implements Producto {
    private final String nombre;
    private final double precio;
    private final Categoria categoria;

    protected ProductoBase(String nombre, double precio, Categoria categoria) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El producto necesita un nombre");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDescripcion() {
        return nombre;
    }

    @Override
    public double getPrecio() {
        return precio;
    }

    @Override
    public Categoria getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return getDescripcion() + " " + Formato.pesos(getPrecio());
    }
}
