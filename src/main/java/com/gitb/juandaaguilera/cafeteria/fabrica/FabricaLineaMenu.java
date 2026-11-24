package com.gitb.juandaaguilera.cafeteria.fabrica;

import com.gitb.juandaaguilera.cafeteria.modelo.Bebida;
import com.gitb.juandaaguilera.cafeteria.modelo.Plato;
import com.gitb.juandaaguilera.cafeteria.modelo.Postre;

public interface FabricaLineaMenu {
    String getNombreLinea();
    Bebida crearBebida();
    Plato crearPlato();
    Postre crearPostre();
}
