package com.gitb.juandaaguilera.cafeteria.composite;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.gitb.juandaaguilera.cafeteria.decorador.QuesoExtra;
import com.gitb.juandaaguilera.cafeteria.modelo.Bebida;
import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Plato;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;

class CompositeTest {
    @Test
    void precioDelComboEsLaSumaDeSusHijosMenosDescuento() {
        Combo c = new Combo("Combo", 0.10);
        c.agregar(new Bebida("Tinto", 2000, true));
        c.agregar(new QuesoExtra(new Plato("Arepa", 5000)));
        assertEquals((2000 + 8000) * 0.9, c.getPrecio(), 0.01);
        assertEquals(Categoria.COMBO, c.getCategoria());
    }

    @Test
    void unComboPuedeContenerOtroCombo() {
        Combo interno = new Combo("Interno", 0);
        interno.agregar(new Bebida("Tinto", 2000, true));
        interno.agregar(new Plato("Arepa", 5000));
        Combo familiar = new Combo("Familiar", 0);
        familiar.agregar(interno);
        familiar.agregar(new Plato("Arepa", 5000));
        Producto comoProducto = familiar;
        assertEquals(12000, comoProducto.getPrecio(), 0.01);
        assertTrue(comoProducto.getDescripcion().contains("Interno ["));
    }

    @Test
    void unComboNoSePuedeContenerASiMismo() {
        Combo c = new Combo("C", 0);
        assertThrows(IllegalArgumentException.class, () -> c.agregar(c));
    }
}
