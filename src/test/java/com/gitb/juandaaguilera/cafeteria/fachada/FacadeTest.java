package com.gitb.juandaaguilera.cafeteria.fachada;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.gitb.juandaaguilera.cafeteria.caja.CajaRegistradora;
import com.gitb.juandaaguilera.cafeteria.caja.ReporteCierre;
import com.gitb.juandaaguilera.cafeteria.pago.ResultadoPago;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;

class FacadeTest {
    private CafeteriaFacade sistema;

    @BeforeEach
    void preparar() {
        ZoneId zona = ZoneId.of("America/Bogota");

        Clock reloj = Clock.fixed(LocalDateTime.of(2026, 11, 23, 16, 0).atZone(zona).toInstant(), zona);
        sistema = new CafeteriaFacade(reloj);
        CajaRegistradora.getInstancia().cerrarCaja(LocalDateTime.now());
    }

    @Test
    void flujoCompletoDesdeElPedidoHastaElCierre() {
        int id = sistema.crearPedido("Mesa 5");
        sistema.agregarProducto(id, "B02");
        sistema.agregarAdicion(id, 0, "A1");
        sistema.agregarComboDelDia(id, "desayuno");
        assertEquals(27700, sistema.consultarPedido(id).getSubtotal(), 0.01);
        assertEquals(25900, sistema.calcularTotal(id), 0.01);

        sistema.avanzarEstado(id);
        sistema.avanzarEstado(id);
        sistema.avanzarEstado(id);
        ResultadoPago r = sistema.pagar(id, "TARJETA", "MASTERCARD");
        assertTrue(r.aprobado());
        assertEquals("PAGADO", sistema.consultarPedido(id).getNombreEstado());
        assertFalse(sistema.getNotificaciones().isEmpty());

        ReporteCierre cierre = sistema.cerrarCaja();
        assertEquals(1, cierre.getCantidadVentas());
        assertEquals(25900, cierre.getTotalNeto(), 0.01);
        assertEquals(1800, cierre.getTotalDescuentos(), 0.01);
    }

    @Test
    void noCobraUnPedidoQueNoSeHaEntregado() {
        int id = sistema.crearPedido("Mesa 6");
        sistema.agregarProducto(id, "P01");
        assertThrows(IllegalStateException.class, () -> sistema.pagar(id, "EFECTIVO", "50000"));
    }

    @Test
    void pagoRechazadoNoRegistraVenta() {
        int id = sistema.crearPedido("Mesa 6");
        sistema.agregarProducto(id, "P01");
        sistema.avanzarEstado(id);
        sistema.avanzarEstado(id);
        sistema.avanzarEstado(id);
        assertFalse(sistema.pagar(id, "EFECTIVO", "1000").aprobado());
        assertEquals("ENTREGADO", sistema.consultarPedido(id).getNombreEstado());
        assertEquals(0, sistema.cerrarCaja().getCantidadVentas());
    }

    @Test
    void deshacerDesdeLaFachada() {
        int id = sistema.crearPedido("Mesa 1");
        sistema.agregarComboPersonalizado(id, "Mio", "B01", "P02", null);
        assertEquals(1, sistema.consultarPedido(id).getProductos().size());
        assertTrue(sistema.deshacer(id));
        assertTrue(sistema.consultarPedido(id).estaVacio());
    }
}
