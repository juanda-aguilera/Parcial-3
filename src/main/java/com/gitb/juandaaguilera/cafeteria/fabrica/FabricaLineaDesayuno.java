package com.gitb.juandaaguilera.cafeteria.fabrica;

import com.gitb.juandaaguilera.cafeteria.modelo.Bebida;
import com.gitb.juandaaguilera.cafeteria.modelo.Plato;
import com.gitb.juandaaguilera.cafeteria.modelo.Postre;

public class FabricaLineaDesayuno implements FabricaLineaMenu {
    @Override
    public String getNombreLinea() {
        return "Desayuno";
    }

    @Override
    public Bebida crearBebida() {
        return new Bebida("Chocolate santafereño", 5000, true);
    }

    @Override
    public Plato crearPlato() {
        return new Plato("Calentado con huevo", 14000);
    }

    @Override
    public Postre crearPostre() {
        return new Postre("Almojábana", 3000);
    }
}
