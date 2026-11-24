package com.gitb.juandaaguilera.cafeteria.persistencia.mysql;

import com.gitb.juandaaguilera.cafeteria.persistencia.FuentePersistencia;
import com.gitb.juandaaguilera.cafeteria.persistencia.MenuDAO;
import com.gitb.juandaaguilera.cafeteria.persistencia.PedidoDAO;
import com.gitb.juandaaguilera.cafeteria.persistencia.VentaDAO;

public class PersistenciaMySQL implements FuentePersistencia {

    private final ConexionMySQL conexion;

    public PersistenciaMySQL(ConexionMySQL conexion) {
        this.conexion = conexion;
    }

    @Override
    public String getNombre() {
        return "MySQL (cafeteria_patrones)";
    }

    @Override
    public MenuDAO crearMenuDAO() {
        return new MenuDAOMySQL(conexion);
    }

    @Override
    public PedidoDAO crearPedidoDAO() {
        return new PedidoDAOMySQL(conexion);
    }

    @Override
    public VentaDAO crearVentaDAO() {
        return new VentaDAOMySQL(conexion);
    }
}
