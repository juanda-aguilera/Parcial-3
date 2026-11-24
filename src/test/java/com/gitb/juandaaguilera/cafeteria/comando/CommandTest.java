package com.gitb.juandaaguilera.cafeteria.comando;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.gitb.juandaaguilera.cafeteria.modelo.Bebida;
import com.gitb.juandaaguilera.cafeteria.modelo.Plato;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;

class CommandTest {
    @Test
    void agregarDeshacerYRehacer() {
        Pedido p = new Pedido(1, "Mesa 1");
        HistorialComandos h = new HistorialComandos();
        h.ejecutar(new AgregarProductoComando(p, new Plato("Arepa", 5000)));
        h.ejecutar(new AgregarProductoComando(p, new Bebida("Tinto", 2000, true)));
        assertEquals(2, p.getProductos().size());
        assertTrue(h.deshacer());
        assertEquals(1, p.getProductos().size());
        assertTrue(h.rehacer());
        assertEquals(2, p.getProductos().size());
        assertFalse(h.rehacer());
    }

    @Test
    void quitarYDeshacerDevuelveElProductoASuPosicion() {
        Pedido p = new Pedido(1, "Mesa 1");
        p.agregarProducto(new Plato("A", 1000));
        p.agregarProducto(new Plato("B", 1000));
        p.agregarProducto(new Plato("C", 1000));
        HistorialComandos h = new HistorialComandos();
        h.ejecutar(new QuitarProductoComando(p, 1));
        assertEquals("C", p.getProducto(1).getNombre());
        h.deshacer();
        assertEquals("B", p.getProducto(1).getNombre());
    }

    @Test
    void deshacerAdicionRecuperaElProductoOriginal() {
        Pedido p = new Pedido(1, "Mesa 1");
        p.agregarProducto(new Bebida("Capuchino", 7000, true));
        HistorialComandos h = new HistorialComandos();
        h.ejecutar(new AplicarAdicionComando(p, 0, "A1"));
        assertEquals(9000, p.getSubtotal(), 0.01);
        h.deshacer();
        assertEquals(7000, p.getSubtotal(), 0.01);
    }

    @Test
    void comandoFallidoNoQuedaEnElHistorial() {
        Pedido p = new Pedido(1, "Mesa 1");
        p.agregarProducto(new Plato("Arepa", 5000));
        HistorialComandos h = new HistorialComandos();
        assertThrows(IllegalArgumentException.class, () -> h.ejecutar(new AplicarAdicionComando(p, 0, "A1")));
        assertFalse(h.puedeDeshacer());
    }

    @Test
    void ejecutarNuevoComandoBorraLoQueHabiaParaRehacer() {
        Pedido p = new Pedido(1, "Mesa 1");
        HistorialComandos h = new HistorialComandos();
        h.ejecutar(new AgregarProductoComando(p, new Plato("A", 1000)));
        h.deshacer();
        h.ejecutar(new AgregarProductoComando(p, new Plato("B", 1000)));
        assertFalse(h.puedeRehacer());
    }
}
