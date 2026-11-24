package com.gitb.juandaaguilera.cafeteria.persistencia.memoria;

import com.gitb.juandaaguilera.cafeteria.modelo.Producto;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;
import com.gitb.juandaaguilera.cafeteria.persistencia.PedidoDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidoDAOMemoria implements PedidoDAO {

    private final Map<Integer, String> estados = new HashMap<>();
    private final Map<Integer, List<String>> detalles = new HashMap<>();
    private int ultimoId = 0;

    @Override
    public int siguienteId() {
        return ultimoId + 1;
    }

    @Override
    public void guardar(Pedido pedido) {
        ultimoId = Math.max(ultimoId, pedido.getId());
        estados.put(pedido.getId(), pedido.getNombreEstado());
    }

    @Override
    public void actualizarEstado(Pedido pedido) {
        estados.put(pedido.getId(), pedido.getNombreEstado());
    }

    @Override
    public void guardarDetalle(Pedido pedido) {
        List<String> lineas = new ArrayList<>();
        for (Producto p : pedido.getProductos()) {
            lineas.add(p.getDescripcion());
        }
        detalles.put(pedido.getId(), lineas);
    }

    public String estadoGuardado(int id) {
        return estados.get(id);
    }

    public List<String> detalleGuardado(int id) {
        return detalles.getOrDefault(id, List.of());
    }
}
