package com.gitb.juandaaguilera.cafeteria.persistencia;

import com.gitb.juandaaguilera.cafeteria.caja.ReporteCierre;
import com.gitb.juandaaguilera.cafeteria.caja.Venta;
import java.util.List;

public interface VentaDAO {
    void guardar(Venta venta);
    List<Venta> listarSinCierre();
    void registrarCierre(ReporteCierre reporte);
}
