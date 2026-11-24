package com.gitb.juandaaguilera.cafeteria.persistencia.memoria;

import com.gitb.juandaaguilera.cafeteria.persistencia.FuentePersistencia;
import com.gitb.juandaaguilera.cafeteria.persistencia.MenuDAO;
import com.gitb.juandaaguilera.cafeteria.persistencia.PedidoDAO;
import com.gitb.juandaaguilera.cafeteria.persistencia.VentaDAO;

public class PersistenciaMemoria implements FuentePersistencia {

    private final MenuDAOMemoria menu = new MenuDAOMemoria();
    private final PedidoDAOMemoria pedidos = new PedidoDAOMemoria();
    private final VentaDAOMemoria ventas = new VentaDAOMemoria();

    @Override
    public String getNombre() {
        return "Memoria (sin base de datos)";
    }

    @Override
    public MenuDAO crearMenuDAO() {
        return menu;
    }

    @Override
    public PedidoDAO crearPedidoDAO() {
        return pedidos;
    }

    @Override
    public VentaDAO crearVentaDAO() {
        return ventas;
    }
}
