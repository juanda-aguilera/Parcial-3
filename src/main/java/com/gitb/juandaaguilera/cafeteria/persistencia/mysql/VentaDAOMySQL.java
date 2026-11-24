package com.gitb.juandaaguilera.cafeteria.persistencia.mysql;

import com.gitb.juandaaguilera.cafeteria.caja.ReporteCierre;
import com.gitb.juandaaguilera.cafeteria.caja.Venta;
import com.gitb.juandaaguilera.cafeteria.persistencia.PersistenciaException;
import com.gitb.juandaaguilera.cafeteria.persistencia.VentaDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class VentaDAOMySQL implements VentaDAO {

    private static final String SQL_INSERTAR =
            "INSERT INTO venta (pedido_id, subtotal, descuento, total, metodo_pago, promocion, fecha) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SIN_CIERRE =
            "SELECT v.pedido_id, p.mesa, v.subtotal, v.descuento, v.total, v.metodo_pago, v.promocion, v.fecha "
            + "FROM venta v JOIN pedido p ON p.id = v.pedido_id WHERE v.cierre_id IS NULL ORDER BY v.id";
    private static final String SQL_PRODUCTOS =
            "SELECT nombre FROM detalle_pedido WHERE pedido_id = ? ORDER BY posicion";
    private static final String SQL_INSERTAR_CIERRE =
            "INSERT INTO cierre_caja (fecha, cantidad_ventas, total_bruto, total_descuentos, total_neto) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_CERRAR_VENTAS = "UPDATE venta SET cierre_id = ? WHERE cierre_id IS NULL";

    private final ConexionMySQL conexion;

    public VentaDAOMySQL(ConexionMySQL conexion) {
        this.conexion = conexion;
    }

    @Override
    public void guardar(Venta venta) {
        try (Connection c = conexion.abrir();
                PreparedStatement ps = c.prepareStatement(SQL_INSERTAR)) {
            ps.setInt(1, venta.idPedido());
            ps.setDouble(2, venta.subtotal());
            ps.setDouble(3, venta.descuento());
            ps.setDouble(4, venta.total());
            ps.setString(5, venta.metodoPago());
            ps.setString(6, venta.promocion());
            ps.setTimestamp(7, Timestamp.valueOf(venta.fecha()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error guardando la venta: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Venta> listarSinCierre() {
        List<Venta> ventas = new ArrayList<>();
        try (Connection c = conexion.abrir();
                PreparedStatement ps = c.prepareStatement(SQL_SIN_CIERRE);
                PreparedStatement psProductos = c.prepareStatement(SQL_PRODUCTOS);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int idPedido = rs.getInt("pedido_id");
                List<String> productos = new ArrayList<>();
                psProductos.setInt(1, idPedido);
                try (ResultSet rp = psProductos.executeQuery()) {
                    while (rp.next()) {
                        productos.add(rp.getString("nombre"));
                    }
                }
                ventas.add(new Venta(idPedido, rs.getString("mesa"), rs.getDouble("subtotal"),
                        rs.getDouble("descuento"), rs.getDouble("total"), rs.getString("metodo_pago"),
                        rs.getString("promocion"), rs.getTimestamp("fecha").toLocalDateTime(), productos));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error leyendo las ventas: " + e.getMessage(), e);
        }
        return ventas;
    }

    @Override
    public void registrarCierre(ReporteCierre reporte) {
        try (Connection c = conexion.abrir()) {
            c.setAutoCommit(false);
            try (PreparedStatement cierre = c.prepareStatement(SQL_INSERTAR_CIERRE, Statement.RETURN_GENERATED_KEYS);
                    PreparedStatement ventas = c.prepareStatement(SQL_CERRAR_VENTAS)) {
                cierre.setTimestamp(1, Timestamp.valueOf(reporte.getFecha()));
                cierre.setInt(2, reporte.getCantidadVentas());
                cierre.setDouble(3, reporte.getTotalBruto());
                cierre.setDouble(4, reporte.getTotalDescuentos());
                cierre.setDouble(5, reporte.getTotalNeto());
                cierre.executeUpdate();
                int idCierre;
                try (ResultSet llaves = cierre.getGeneratedKeys()) {
                    llaves.next();
                    idCierre = llaves.getInt(1);
                }
                ventas.setInt(1, idCierre);
                ventas.executeUpdate();
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error registrando el cierre de caja: " + e.getMessage(), e);
        }
    }
}
