package com.gitb.juandaaguilera.cafeteria.decorador;

import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Formato;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;

public abstract class AdicionDecorator implements Producto {
    protected final Producto producto;
    private final String nombreAdicion;
    private final double precioAdicion;

    protected AdicionDecorator(Producto producto, String nombreAdicion, double precioAdicion) {
        if (producto == null) {
            throw new IllegalArgumentException("No hay producto para decorar");
        }
        this.producto = producto;
        this.nombreAdicion = nombreAdicion;
        this.precioAdicion = precioAdicion;
    }

    protected static Producto exigir(Producto p, String adicion, Categoria... permitidas) {
        if (p == null) {
            throw new IllegalArgumentException("No hay producto para decorar");
        }
        for (Categoria c : permitidas) {
            if (p.getCategoria() == c) {
                return p;
            }
        }
        throw new IllegalArgumentException("La adición '" + adicion + "' no aplica a " + p.getCategoria());
    }

    @Override
    public String getNombre() {
        return producto.getNombre();
    }

    @Override
    public String getDescripcion() {
        return producto.getDescripcion() + " + " + nombreAdicion;
    }

    @Override
    public double getPrecio() {
        return producto.getPrecio() + precioAdicion;
    }

    @Override
    public Categoria getCategoria() {
        return producto.getCategoria();
    }

    public Producto getEnvuelto() {
        return producto;
    }

    @Override
    public String toString() {
        return getDescripcion() + " " + Formato.pesos(getPrecio());
    }
}
