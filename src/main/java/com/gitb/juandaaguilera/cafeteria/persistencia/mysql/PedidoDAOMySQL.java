package com.gitb.juandaaguilera.cafeteria.persistencia.mysql;

import com.gitb.juandaaguilera.cafeteria.modelo.Producto;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;
import com.gitb.juandaaguilera.cafeteria.persistencia.PedidoDAO;
import com.gitb.juandaaguilera.cafeteria.persistencia.PersistenciaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PedidoDAOMySQL implements PedidoDAO {

    private static final String SQL_MAX_ID = "SELECT COALESCE(MAX(id), 0) + 1 FROM pedido";
    private static final String SQL_INSERTAR =
            "INSERT INTO pedido (id, mesa, estado, fecha_creacion, fecha_actualizacion) VALUES (?, ?, ?, NOW(), NOW())";
    private static final String SQL_ESTADO =
            "UPDATE pedido SET estado = ?, fecha_actualizacion = NOW() WHERE id = ?";
    private static final String SQL_BORRAR_DETALLE = "DELETE FROM detalle_pedido WHERE pedido_id = ?";
    private static final String SQL_INSERTAR_DETALLE =
            "INSERT INTO detalle_pedido (pedido_id, posicion, nombre, descripcion, categoria, precio) VALUES (?, ?, ?, ?, ?, ?)";

    private final ConexionMySQL conexion;

    public PedidoDAOMySQL(ConexionMySQL conexion) {
        this.conexion = conexion;
    }

    @Override
    public int siguienteId() {
        try (Connection c = conexion.abrir();
                PreparedStatement ps = c.prepareStatement(SQL_MAX_ID);
                ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new PersistenciaException("Error consultando el consecutivo de pedidos: " + e.getMessage(), e);
        }
    }

    @Override
    public void guardar(Pedido pedido) {
        try (Connection c = conexion.abrir();
                PreparedStatement ps = c.prepareStatement(SQL_INSERTAR)) {
            ps.setInt(1, pedido.getId());
            ps.setString(2, pedido.getMesa());
            ps.setString(3, pedido.getNombreEstado());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error guardando el pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarEstado(Pedido pedido) {
        try (Connection c = conexion.abrir();
                PreparedStatement ps = c.prepareStatement(SQL_ESTADO)) {
            ps.setString(1, pedido.getNombreEstado());
            ps.setInt(2, pedido.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error actualizando el estado: " + e.getMessage(), e);
        }
    }

    @Override
    public void guardarDetalle(Pedido pedido) {
        try (Connection c = conexion.abrir()) {
            c.setAutoCommit(false);
            try (PreparedStatement borrar = c.prepareStatement(SQL_BORRAR_DETALLE);
                    PreparedStatement insertar = c.prepareStatement(SQL_INSERTAR_DETALLE)) {
                borrar.setInt(1, pedido.getId());
                borrar.executeUpdate();
                List<Producto> productos = pedido.getProductos();
                for (int i = 0; i < productos.size(); i++) {
                    Producto p = productos.get(i);
                    insertar.setInt(1, pedido.getId());
                    insertar.setInt(2, i + 1);
                    insertar.setString(3, p.getNombre());
                    insertar.setString(4, p.getDescripcion());
                    insertar.setString(5, p.getCategoria().name());
                    insertar.setDouble(6, p.getPrecio());
                    insertar.addBatch();
                }
                insertar.executeBatch();
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error guardando el detalle del pedido: " + e.getMessage(), e);
        }
    }
}
