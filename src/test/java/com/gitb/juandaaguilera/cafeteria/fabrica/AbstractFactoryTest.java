package com.gitb.juandaaguilera.cafeteria.fabrica;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;

class AbstractFactoryTest {
    @Test
    void familiaDesayunoCreaTresProductosDeLaMismaLinea() {
        FabricaLineaMenu f = new FabricaLineaDesayuno();
        assertEquals("Desayuno", f.getNombreLinea());
        assertEquals(Categoria.BEBIDA, f.crearBebida().getCategoria());
        assertEquals(Categoria.PLATO, f.crearPlato().getCategoria());
        assertEquals(Categoria.POSTRE, f.crearPostre().getCategoria());
        assertTrue(f.crearBebida().isCaliente());
    }

    @Test
    void familiasDiferentesCreanProductosDiferentes() {
        FabricaLineaMenu desayuno = new FabricaLineaDesayuno();
        FabricaLineaMenu almuerzo = new FabricaLineaAlmuerzo();
        assertNotEquals(desayuno.crearPlato().getNombre(), almuerzo.crearPlato().getNombre());
        assertFalse(almuerzo.crearBebida().isCaliente());
    }
}
