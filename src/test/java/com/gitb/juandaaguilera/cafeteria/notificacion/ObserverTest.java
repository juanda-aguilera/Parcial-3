package com.gitb.juandaaguilera.cafeteria.notificacion;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.gitb.juandaaguilera.cafeteria.modelo.Plato;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;
import java.util.ArrayList;
import java.util.List;

class ObserverTest {
    @Test
    void todosLosObservadoresRecibenCadaCambio() {
        List<String> cambios = new ArrayList<>();
        Pedido p = new Pedido(7, "Mesa 7");
        p.agregarObservador((pedido, ant, nuevo) -> cambios.add(ant + "->" + nuevo));
        p.agregarProducto(new Plato("Arepa", 5000));
        p.avanzar();
        p.avanzar();
        assertEquals(List.of("REGISTRADO->EN_PREPARACION", "EN_PREPARACION->LISTO"), cambios);
    }

    @Test
    void cadaAreaReaccionaSoloALoQueLeInteresa() {
        TableroNotificaciones tablero = new TableroNotificaciones();
        PantallaCocina cocina = new PantallaCocina(tablero);
        PantallaMesero mesero = new PantallaMesero(tablero);
        PantallaCaja caja = new PantallaCaja(tablero);
        Pedido p = new Pedido(8, "Mesa 8");
        p.agregarObservador(cocina);
        p.agregarObservador(mesero);
        p.agregarObservador(caja);
        p.agregarProducto(new Plato("Arepa", 5000));

        p.avanzar();
        assertEquals(1, cocina.getRecibidos().size());
        assertEquals(0, mesero.getRecibidos().size());
        p.avanzar();
        assertEquals(1, mesero.getRecibidos().size());
        p.avanzar();
        assertEquals(1, caja.getRecibidos().size());
        assertEquals(3, tablero.getMensajes().size());
    }

    @Test
    void observadorQuitadoNoRecibeAvisos() {
        TableroNotificaciones tablero = new TableroNotificaciones();
        PantallaCocina cocina = new PantallaCocina(tablero);
        Pedido p = new Pedido(9, "Mesa 9");
        p.agregarObservador(cocina);
        p.quitarObservador(cocina);
        p.agregarProducto(new Plato("Arepa", 5000));
        p.avanzar();
        assertTrue(cocina.getRecibidos().isEmpty());
    }
}
