package com.gitb.juandaaguilera.cafeteria.pago;

import com.gitb.juandaaguilera.cafeteria.pago.externo.BilleteraDigitalApi;
import java.math.BigDecimal;
import java.util.Map;

public class AdaptadorBilleteraDigital implements MetodoPago {
    private final BilleteraDigitalApi api;
    private final String celular;

    public AdaptadorBilleteraDigital(BilleteraDigitalApi api, String celular) {
        this.api = api;
        this.celular = celular;
    }

    @Override
    public String getNombre() {
        return "BILLETERA";
    }

    @Override
    public ResultadoPago pagar(double monto) {
        Map<String, String> r = api.solicitarCobro(celular, BigDecimal.valueOf(Math.round(monto)));
        if ("OK".equals(r.get("estado"))) {
            return ResultadoPago.aprobado(r.get("idTransaccion"), "Pago con billetera digital aprobado", 0);
        }
        return ResultadoPago.rechazado(r.get("detalle"));
    }
}
