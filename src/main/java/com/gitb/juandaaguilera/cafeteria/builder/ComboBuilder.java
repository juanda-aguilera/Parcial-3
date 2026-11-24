package com.gitb.juandaaguilera.cafeteria.builder;

import com.gitb.juandaaguilera.cafeteria.composite.Combo;
import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;
import java.util.ArrayList;
import java.util.List;

public class ComboBuilder {
    private String nombre;
    private Producto bebida;
    private Producto plato;
    private Producto postre;
    private final List<Producto> extras = new ArrayList<>();
    private double descuento;

    public ComboBuilder() {
        reiniciar();
    }

    public ComboBuilder reiniciar() {
        nombre = "Combo";
        bebida = null;
        plato = null;
        postre = null;
        extras.clear();
        descuento = 0;
        return this;
    }

    public ComboBuilder nombre(String nombre) {
        if (nombre != null && !nombre.isBlank()) {
            this.nombre = nombre;
        }
        return this;
    }

    public ComboBuilder conBebida(Producto bebida) {
        this.bebida = validar(bebida, Categoria.BEBIDA);
        return this;
    }

    public ComboBuilder conPlato(Producto plato) {
        this.plato = validar(plato, Categoria.PLATO);
        return this;
    }

    public ComboBuilder conPostre(Producto postre) {
        this.postre = validar(postre, Categoria.POSTRE);
        return this;
    }

    public ComboBuilder conExtra(Producto extra) {
        if (extra == null) {
            throw new IllegalArgumentException("Extra vacío");
        }
        extras.add(extra);
        return this;
    }

    public ComboBuilder conDescuento(double descuento) {
        if (descuento < 0 || descuento > 0.5) {
            throw new IllegalArgumentException("Descuento fuera de rango (0 a 50 %)");
        }
        this.descuento = descuento;
        return this;
    }

    public Combo build() {
        if (bebida == null && plato == null) {
            throw new IllegalStateException("Un combo debe tener por lo menos una bebida o un plato");
        }
        int cantidad = extras.size() + (bebida != null ? 1 : 0) + (plato != null ? 1 : 0) + (postre != null ? 1 : 0);
        if (cantidad < 2) {
            throw new IllegalStateException("Un combo necesita mínimo 2 productos");
        }
        Combo combo = new Combo(nombre, descuento);
        if (bebida != null) {
            combo.agregar(bebida);
        }
        if (plato != null) {
            combo.agregar(plato);
        }
        if (postre != null) {
            combo.agregar(postre);
        }
        for (Producto extra : extras) {
            combo.agregar(extra);
        }
        reiniciar();
        return combo;
    }

    private static Producto validar(Producto p, Categoria esperada) {
        if (p == null) {
            throw new IllegalArgumentException("Falta el producto de tipo " + esperada);
        }
        if (p.getCategoria() != esperada) {
            throw new IllegalArgumentException(p.getNombre() + " no es de tipo " + esperada);
        }
        return p;
    }
}
