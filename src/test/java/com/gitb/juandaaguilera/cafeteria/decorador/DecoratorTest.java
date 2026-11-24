package com.gitb.juandaaguilera.cafeteria.decorador;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.gitb.juandaaguilera.cafeteria.modelo.Bebida;
import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Plato;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;

class DecoratorTest {
    @Test
    void adicionSumaPrecioYDescripcion() {
        Producto cafe = new LecheAlmendras(new Bebida("Capuchino", 7000, true));
        assertEquals(9000, cafe.getPrecio(), 0.01);
        assertEquals("Capuchino + leche de almendras", cafe.getDescripcion());
        assertEquals(Categoria.BEBIDA, cafe.getCategoria());
    }

    @Test
    void sePuedenApilarVariasAdiciones() {
        Producto cafe = new CremaChantilly(new ShotExpreso(new LecheAlmendras(new Bebida("Capuchino", 7000, true))));
        assertEquals(7000 + 2000 + 2500 + 1500, cafe.getPrecio(), 0.01);
        assertTrue(cafe.getDescripcion().endsWith("+ crema chantilly"));
    }

    @Test
    void noSePuedeEcharLecheAUnPlato() {
        assertThrows(IllegalArgumentException.class, () -> new LecheAlmendras(new Plato("Arepa", 5000)));
    }

    @Test
    void catalogoAplicaLaAdicionPorCodigo() {
        Producto p = CatalogoAdiciones.aplicar("A5", new Plato("Hamburguesa", 24000));
        assertTrue(p instanceof Tocineta);
        assertEquals(27500, p.getPrecio(), 0.01);
    }
}
