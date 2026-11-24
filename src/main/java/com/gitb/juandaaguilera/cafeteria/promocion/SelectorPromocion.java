package com.gitb.juandaaguilera.cafeteria.promocion;

import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SelectorPromocion {
    private final List<EstrategiaPromocion> promociones = new ArrayList<>();

    public static SelectorPromocion conPromocionesPorDefecto() {
        SelectorPromocion s = new SelectorPromocion();
        s.registrar(new PromoHappyHour());
        s.registrar(new PromoMartesDulce());
        s.registrar(new PromoMadrugador());
        s.registrar(new PromoFinDeSemanaCombos());
        return s;
    }

    public void registrar(EstrategiaPromocion promocion) {
        promociones.add(promocion);
    }

    public List<EstrategiaPromocion> getPromociones() {
        return new ArrayList<>(promociones);
    }

    public EstrategiaPromocion mejorPara(Pedido pedido, LocalDateTime momento) {
        EstrategiaPromocion mejor = new SinPromocion();
        double mayor = 0;
        for (EstrategiaPromocion p : promociones) {
            if (p.aplica(momento)) {
                double d = p.calcularDescuento(pedido);
                if (d > mayor) {
                    mayor = d;
                    mejor = p;
                }
            }
        }
        return mejor;
    }
}
