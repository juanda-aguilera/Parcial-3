package com.gitb.juandaaguilera.cafeteria.caja;

import com.gitb.juandaaguilera.cafeteria.modelo.Formato;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReporteCierre {
    private final List<Venta> ventas;
    private final LocalDateTime fecha;

    public ReporteCierre(List<Venta> ventas, LocalDateTime fecha) {
        this.ventas = ventas;
        this.fecha = fecha;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public int getCantidadVentas() {
        return ventas.size();
    }

    public double getTotalBruto() {
        return ventas.stream().mapToDouble(Venta::subtotal).sum();
    }

    public double getTotalDescuentos() {
        return ventas.stream().mapToDouble(Venta::descuento).sum();
    }

    public double getTotalNeto() {
        return ventas.stream().mapToDouble(Venta::total).sum();
    }

    public Map<String, Double> getTotalPorMetodo() {
        Map<String, Double> m = new TreeMap<>();
        for (Venta v : ventas) {
            m.merge(v.metodoPago(), v.total(), Double::sum);
        }
        return m;
    }

    public Map<String, Integer> getMasVendidos() {
        Map<String, Integer> conteo = new LinkedHashMap<>();
        for (Venta v : ventas) {
            for (String p : v.productos()) {
                conteo.merge(p, 1, Integer::sum);
            }
        }
        List<Map.Entry<String, Integer>> lista = new ArrayList<>(conteo.entrySet());
        lista.sort((a, b) -> b.getValue() - a.getValue());
        Map<String, Integer> top = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(5, lista.size()); i++) {
            top.put(lista.get(i).getKey(), lista.get(i).getValue());
        }
        return top;
    }

    public String toTexto() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== CIERRE DE CAJA =====\n");
        sb.append("Fecha: ").append(fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append('\n');
        sb.append("Ventas: ").append(getCantidadVentas()).append('\n');
        sb.append("Total bruto: ").append(Formato.pesos(getTotalBruto())).append('\n');
        sb.append("Descuentos:  ").append(Formato.pesos(getTotalDescuentos())).append('\n');
        sb.append("Total neto:  ").append(Formato.pesos(getTotalNeto())).append('\n');
        sb.append("-- Por medio de pago --\n");
        getTotalPorMetodo().forEach((k, v) -> sb.append("  ").append(k).append(": ").append(Formato.pesos(v)).append('\n'));
        sb.append("-- Más vendidos --\n");
        getMasVendidos().forEach((k, v) -> sb.append("  ").append(k).append(" x").append(v).append('\n'));
        return sb.toString();
    }
}
