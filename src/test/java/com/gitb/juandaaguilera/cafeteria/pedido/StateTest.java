package com.gitb.juandaaguilera.cafeteria.pedido;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.gitb.juandaaguilera.cafeteria.modelo.Plato;

class StateTest {
    private Pedido pedidoConProducto() {
        Pedido p = new Pedido(1, "Mesa 2");
        p.agregarProducto(new Plato("Arepa", 5000));
        return p;
    }

    @Test
    void recorreTodosLosEstadosHastaPagado() {
        Pedido p = pedidoConProducto();
        assertEquals(EstadoPedido.REGISTRADO, p.getNombreEstado());
        p.avanzar();
        assertEquals(EstadoPedido.EN_PREPARACION, p.getNombreEstado());
        p.avanzar();
        assertEquals(EstadoPedido.LISTO, p.getNombreEstado());
        p.avanzar();
        assertEquals(EstadoPedido.ENTREGADO, p.getNombreEstado());
        assertTrue(p.getEstado().permitePago());
        p.pagar();
        assertEquals(EstadoPedido.PAGADO, p.getNombreEstado());
    }

    @Test
    void pedidoVacioNoSeEnviaACocina() {
        assertThrows(IllegalStateException.class, () -> new Pedido(2, "Mesa 3").avanzar());
    }

    @Test
    void enPreparacionYaNoSePuedeModificar() {
        Pedido p = pedidoConProducto();
        p.avanzar();
        assertThrows(IllegalStateException.class, () -> p.agregarProducto(new Plato("Otra", 1000)));
    }

    @Test
    void listoNoSePuedeCancelar() {
        Pedido p = pedidoConProducto();
        p.avanzar();
        p.avanzar();
        assertThrows(IllegalStateException.class, p::cancelar);
    }

    @Test
    void soloSePagaCuandoEstaEntregado() {
        Pedido p = pedidoConProducto();
        assertThrows(IllegalStateException.class, p::pagar);
        p.cancelar();
        assertEquals(EstadoPedido.CANCELADO, p.getNombreEstado());
        assertThrows(IllegalStateException.class, p::avanzar);
    }
}
