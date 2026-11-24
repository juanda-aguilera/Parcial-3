package com.gitb.juandaaguilera.cafeteria.persistencia.memoria;

import com.gitb.juandaaguilera.cafeteria.caja.ReporteCierre;
import com.gitb.juandaaguilera.cafeteria.caja.Venta;
import com.gitb.juandaaguilera.cafeteria.persistencia.VentaDAO;
import java.util.ArrayList;
import java.util.List;

public class VentaDAOMemoria implements VentaDAO {

    private final List<Venta> abiertas = new ArrayList<>();
    private final List<ReporteCierre> cierres = new ArrayList<>();

    @Override
    public void guardar(Venta venta) {
        abiertas.add(venta);
    }

    @Override
    public List<Venta> listarSinCierre() {
        return new ArrayList<>(abiertas);
    }

    @Override
    public void registrarCierre(ReporteCierre reporte) {
        cierres.add(reporte);
        abiertas.clear();
    }

    public List<ReporteCierre> getCierres() {
        return new ArrayList<>(cierres);
    }
}
