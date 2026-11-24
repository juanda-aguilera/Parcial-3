package com.gitb.juandaaguilera.cafeteria.builder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.gitb.juandaaguilera.cafeteria.composite.Combo;
import com.gitb.juandaaguilera.cafeteria.fabrica.FabricaLineaAlmuerzo;
import com.gitb.juandaaguilera.cafeteria.modelo.Bebida;
import com.gitb.juandaaguilera.cafeteria.modelo.Plato;
import com.gitb.juandaaguilera.cafeteria.modelo.Postre;

class BuilderTest {
    @Test
    void armaComboPasoAPaso() {
        Combo c = new ComboBuilder().nombre("Combo test")
                .conBebida(new Bebida("Tinto", 2000, true))
                .conPlato(new Plato("Empanada", 3000))
                .conDescuento(0.10)
                .build();
        assertEquals(2, c.getItems().size());
        assertEquals(4500, c.getPrecio(), 0.01);
    }

    @Test
    void noPermiteComboSoloDePostres() {
        ComboBuilder b = new ComboBuilder().conPostre(new Postre("Flan", 4000)).conExtra(new Postre("Bocadillo", 1000));
        assertThrows(IllegalStateException.class, b::build);
    }

    @Test
    void noPermiteComboDeUnSoloProducto() {
        ComboBuilder b = new ComboBuilder().conPlato(new Plato("Arepa", 5000));
        assertThrows(IllegalStateException.class, b::build);
    }

    @Test
    void validaQueCadaPasoRecibaElTipoCorrecto() {
        assertThrows(IllegalArgumentException.class, () -> new ComboBuilder().conBebida(new Plato("Arepa", 5000)));
    }

    @Test
    void directorArmaComboDelDiaConDescuentoFijo() {
        Combo c = new DirectorCombos(new ComboBuilder()).construirComboDelDia(new FabricaLineaAlmuerzo());
        assertEquals(3, c.getItems().size());
        assertEquals(DirectorCombos.DESCUENTO_COMBO_DIA, c.getDescuento(), 0.0001);
        assertEquals((7000 + 28000 + 5000) * 0.85, c.getPrecio(), 0.01);
    }

    @Test
    void directorDaMasDescuentoSiElComboEstaCompleto() {
        DirectorCombos d = new DirectorCombos(new ComboBuilder());
        Combo completo = d.construirComboPersonalizado("A", new Bebida("Tinto", 2000, true),
                new Plato("Arepa", 5000), new Postre("Flan", 3000));
        Combo dos = d.construirComboPersonalizado("B", new Bebida("Tinto", 2000, true), new Plato("Arepa", 5000), null);
        assertEquals(0.10, completo.getDescuento(), 0.0001);
        assertEquals(0.05, dos.getDescuento(), 0.0001);
    }
}
