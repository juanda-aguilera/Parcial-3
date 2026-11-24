package com.gitb.juandaaguilera.cafeteria.fabrica;

import com.gitb.juandaaguilera.cafeteria.modelo.Bebida;
import com.gitb.juandaaguilera.cafeteria.modelo.Plato;
import com.gitb.juandaaguilera.cafeteria.modelo.Postre;

public class FabricaLineaAlmuerzo implements FabricaLineaMenu {
    @Override
    public String getNombreLinea() {
        return "Almuerzo";
    }

    @Override
    public Bebida crearBebida() {
        return new Bebida("Limonada de coco", 7000, false);
    }

    @Override
    public Plato crearPlato() {
        return new Plato("Bandeja paisa", 28000);
    }

    @Override
    public Postre crearPostre() {
        return new Postre("Arroz con leche", 5000);
    }
}
