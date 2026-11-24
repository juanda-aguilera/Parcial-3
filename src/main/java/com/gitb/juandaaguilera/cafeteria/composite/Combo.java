package com.gitb.juandaaguilera.cafeteria.composite;

import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Formato;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Combo implements Producto {
    private final String nombre;
    private final double descuento;
    private final List<Producto> items = new ArrayList<>();

    public Combo(String nombre, double descuento) {
        if (descuento < 0 || descuento > 0.5) {
            throw new IllegalArgumentException("El descuento del combo debe estar entre 0 % y 50 %");
        }
        this.nombre = nombre;
        this.descuento = descuento;
    }

    public void agregar(Producto producto) {
        if (producto == null || producto == this) {
            throw new IllegalArgumentException("Producto inválido para el combo");
        }
        items.add(producto);
    }

    public void quitar(Producto producto) {
        items.remove(producto);
    }

    public List<Producto> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double getDescuento() {
        return descuento;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public double getPrecio() {
        double suma = 0;
        for (Producto p : items) {
            suma += p.getPrecio();
        }
        return suma * (1 - descuento);
    }

    @Override
    public String getDescripcion() {
        List<String> partes = new ArrayList<>();
        for (Producto p : items) {
            partes.add(p.getDescripcion());
        }
        String texto = nombre + " [" + String.join(", ", partes) + "]";
        if (descuento > 0) {
            texto += " (-" + Math.round(descuento * 100) + "%)";
        }
        return texto;
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.COMBO;
    }

    @Override
    public String toString() {
        return getDescripcion() + " " + Formato.pesos(getPrecio());
    }
}
