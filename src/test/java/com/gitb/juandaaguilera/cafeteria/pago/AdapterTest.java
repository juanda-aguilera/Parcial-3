package com.gitb.juandaaguilera.cafeteria.pago;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.gitb.juandaaguilera.cafeteria.pago.externo.BilleteraDigitalApi;
import com.gitb.juandaaguilera.cafeteria.pago.externo.DatafonoExterno;

class AdapterTest {
    @Test
    void adaptadorDatafonoTraduceElPagoYDevuelveAutorizacion() {
        MetodoPago tarjeta = new AdaptadorDatafono(new DatafonoExterno(), "VISA");
        ResultadoPago r = tarjeta.pagar(25000);
        assertTrue(r.aprobado());
        assertTrue(r.referencia().startsWith("AUT"));
    }

    @Test
    void adaptadorDatafonoTraduceCodigosDeError() {
        ResultadoPago r = new AdaptadorDatafono(new DatafonoExterno(), "DINERS").pagar(25000);
        assertFalse(r.aprobado());
        assertTrue(r.mensaje().contains("Franquicia"));
        assertFalse(new AdaptadorDatafono(new DatafonoExterno(), "VISA").pagar(900000).aprobado());
    }

    @Test
    void adaptadorBilleteraAceptaCelularValido() {
        MetodoPago billetera = new AdaptadorBilleteraDigital(new BilleteraDigitalApi(), "3001234567");
        ResultadoPago r = billetera.pagar(15000);
        assertTrue(r.aprobado());
        assertEquals("BD-1", r.referencia());
    }

    @Test
    void adaptadorBilleteraRechazaCelularInvalido() {
        assertFalse(new AdaptadorBilleteraDigital(new BilleteraDigitalApi(), "123").pagar(15000).aprobado());
    }

    @Test
    void efectivoCalculaCambio() {
        ResultadoPago r = new PagoEfectivo(50000).pagar(42000);
        assertTrue(r.aprobado());
        assertEquals(8000, r.cambio(), 0.01);
        assertFalse(new PagoEfectivo(1000).pagar(42000).aprobado());
    }
}
