package com.gitb.juandaaguilera.cafeteria.fabrica;

import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;

public record ItemMenu(String codigo, String nombre, Categoria categoria, double precio, String atributo) {
}
