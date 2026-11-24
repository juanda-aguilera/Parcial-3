package com.gitb.juandaaguilera.cafeteria.caja;

import com.gitb.juandaaguilera.cafeteria.persistencia.VentaDAO;
import com.gitb.juandaaguilera.cafeteria.persistencia.memoria.VentaDAOMemoria;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class CajaRegistradora {
    private static volatile CajaRegistradora instancia;

    private final List<Venta> ventas = new ArrayList<>();
    private VentaDAO repositorio = new VentaDAOMemoria();

    private CajaRegistradora() {
    }

    public static CajaRegistradora getInstancia() {
        if (instancia == null) {
            synchronized (CajaRegistradora.class) {
                if (instancia == null) {
                    instancia = new CajaRegistradora();
                }
            }
        }
        return instancia;
    }

    public synchronized void usarRepositorio(VentaDAO repositorio) {
        this.repositorio = repositorio;
        ventas.clear();
        ventas.addAll(repositorio.listarSinCierre());
    }

    public synchronized void registrarVenta(Venta venta) {
        repositorio.guardar(venta);
        ventas.add(venta);
    }

    public synchronized List<Venta> getVentas() {
        return new ArrayList<>(ventas);
    }

    public synchronized double getTotalVendido() {
        double total = 0;
        for (Venta v : ventas) {
            total += v.total();
        }
        return total;
    }

    public synchronized ReporteCierre cerrarCaja(LocalDateTime momento) {
        ReporteCierre reporte = new ReporteCierre(new ArrayList<>(ventas), momento);
        repositorio.registrarCierre(reporte);
        ventas.clear();
        return reporte;
    }
}
