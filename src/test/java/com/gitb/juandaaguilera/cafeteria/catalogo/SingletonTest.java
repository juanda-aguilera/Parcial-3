package com.gitb.juandaaguilera.cafeteria.catalogo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.gitb.juandaaguilera.cafeteria.caja.CajaRegistradora;
import com.gitb.juandaaguilera.cafeteria.caja.ReporteCierre;
import com.gitb.juandaaguilera.cafeteria.caja.Venta;
import java.time.LocalDateTime;
import java.util.List;

class SingletonTest {
    @Test
    void menuSiempreEsLaMismaInstancia() {
        assertSame(Menu.getInstancia(), Menu.getInstancia());
    }

    @Test
    void cajaSiempreEsLaMismaInstancia() {
        assertSame(CajaRegistradora.getInstancia(), CajaRegistradora.getInstancia());
    }

    @Test
    void ventasRegistradasDesdeCualquierLugarLlegamALaMismaCaja() {
        CajaRegistradora.getInstancia().cerrarCaja(LocalDateTime.now());
        CajaRegistradora.getInstancia().registrarVenta(new Venta(1, "Mesa 1", 10000, 0, 10000, "EFECTIVO",
                "Sin promoción", LocalDateTime.now(), List.of("Capuchino")));
        CajaRegistradora otraReferencia = CajaRegistradora.getInstancia();
        assertEquals(10000, otraReferencia.getTotalVendido(), 0.01);
        ReporteCierre r = otraReferencia.cerrarCaja(LocalDateTime.now());
        assertEquals(1, r.getCantidadVentas());
        assertEquals(0, CajaRegistradora.getInstancia().getVentas().size());
    }

    @Test
    void constructorDeCajaEsPrivado() throws Exception {
        assertTrue(java.lang.reflect.Modifier.isPrivate(
                CajaRegistradora.class.getDeclaredConstructor().getModifiers()));
    }
}
