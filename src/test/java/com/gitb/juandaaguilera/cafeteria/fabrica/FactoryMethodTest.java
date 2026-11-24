package com.gitb.juandaaguilera.cafeteria.fabrica;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.gitb.juandaaguilera.cafeteria.catalogo.Menu;
import com.gitb.juandaaguilera.cafeteria.modelo.Bebida;
import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Plato;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;

class FactoryMethodTest {
    @Test
    void creadorBebidaCreaBebidaFriaOCaliente() {
        Producto p = new CreadorBebida().crear(new ItemMenu("X1", "Jugo", Categoria.BEBIDA, 5000, "fria"));
        assertTrue(p instanceof Bebida);
        assertFalse(((Bebida) p).isCaliente());
        assertEquals(5000, p.getPrecio(), 0.01);
    }

    @Test
    void creadorRechazaItemDeOtraCategoria() {
        ItemMenu postre = new ItemMenu("X2", "Flan", Categoria.POSTRE, 4000, "");
        assertThrows(IllegalArgumentException.class, () -> new CreadorPlato().crear(postre));
    }

    @Test
    void menuUsaElCreadorSegunElTipoDelCodigo() {
        Producto p = Menu.getInstancia().crearProducto("P01");
        assertTrue(p instanceof Plato);
        assertEquals(Categoria.PLATO, p.getCategoria());
    }

    @Test
    void cadaLlamadoCreaUnObjetoNuevo() {
        Menu menu = Menu.getInstancia();
        assertNotSame(menu.crearProducto("B01"), menu.crearProducto("B01"));
    }

    @Test
    void codigoInexistenteLanzaError() {
        assertThrows(IllegalArgumentException.class, () -> Menu.getInstancia().crearProducto("ZZZ"));
    }
}
