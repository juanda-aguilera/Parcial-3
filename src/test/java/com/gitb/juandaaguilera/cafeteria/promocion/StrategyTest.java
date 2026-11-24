package com.gitb.juandaaguilera.cafeteria.promocion;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.gitb.juandaaguilera.cafeteria.modelo.Bebida;
import com.gitb.juandaaguilera.cafeteria.modelo.Plato;
import com.gitb.juandaaguilera.cafeteria.modelo.Postre;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;
import java.time.LocalDateTime;

class StrategyTest {
    private static final LocalDateTime LUNES_4PM = LocalDateTime.of(2026, 11, 23, 16, 0);
    private static final LocalDateTime MARTES_12M = LocalDateTime.of(2026, 11, 24, 12, 0);
    private static final LocalDateTime LUNES_8PM = LocalDateTime.of(2026, 11, 23, 20, 0);

    private Pedido pedidoDePrueba() {
        Pedido p = new Pedido(1, "Mesa 1");
        p.agregarProducto(new Bebida("Capuchino", 10000, true));
        p.agregarProducto(new Plato("Arepa", 10000));
        p.agregarProducto(new Postre("Brownie", 6000));
        return p;
    }

    @Test
    void happyHourDescuentaSoloBebidas() {
        EstrategiaPromocion e = new PromoHappyHour();
        assertTrue(e.aplica(LUNES_4PM));
        assertFalse(e.aplica(LUNES_8PM));
        assertEquals(2000, e.calcularDescuento(pedidoDePrueba()), 0.01);
    }

    @Test
    void martesDulceDescuentaMitadDePostres() {
        EstrategiaPromocion e = new PromoMartesDulce();
        assertTrue(e.aplica(MARTES_12M));
        assertEquals(3000, e.calcularDescuento(pedidoDePrueba()), 0.01);
    }

    @Test
    void contextoCambiaDeEstrategiaSinCambiarSuCodigo() {
        CalculadoraPrecio calc = new CalculadoraPrecio();
        Pedido p = pedidoDePrueba();
        assertEquals(26000, calc.calcularTotal(p), 0.01);
        calc.setEstrategia(new PromoHappyHour());
        assertEquals(24000, calc.calcularTotal(p), 0.01);
        calc.setEstrategia(new PromoMartesDulce());
        assertEquals(23000, calc.calcularTotal(p), 0.01);
    }

    @Test
    void selectorEscogeLaPromocionVigenteMasConveniente() {
        SelectorPromocion s = SelectorPromocion.conPromocionesPorDefecto();
        assertTrue(s.mejorPara(pedidoDePrueba(), LUNES_4PM) instanceof PromoHappyHour);
        assertTrue(s.mejorPara(pedidoDePrueba(), MARTES_12M) instanceof PromoMartesDulce);
        assertTrue(s.mejorPara(pedidoDePrueba(), LUNES_8PM) instanceof SinPromocion);
    }
}
