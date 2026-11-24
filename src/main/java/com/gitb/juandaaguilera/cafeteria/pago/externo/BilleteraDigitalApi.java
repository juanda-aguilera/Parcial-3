package com.gitb.juandaaguilera.cafeteria.pago.externo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BilleteraDigitalApi {
    private int contador = 0;

    public Map<String, String> solicitarCobro(String celular, BigDecimal valor) {
        Map<String, String> respuesta = new HashMap<>();
        if (celular == null || !celular.matches("3\\d{9}")) {
            respuesta.put("estado", "ERROR");
            respuesta.put("detalle", "Celular no registrado en la billetera");
            return respuesta;
        }
        contador++;
        respuesta.put("estado", "OK");
        respuesta.put("idTransaccion", "BD-" + contador);
        respuesta.put("detalle", "Cobro de " + valor + " aceptado");
        return respuesta;
    }
}
