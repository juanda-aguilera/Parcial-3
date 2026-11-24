package com.gitb.juandaaguilera.cafeteria.persistencia.mysql;

import com.gitb.juandaaguilera.cafeteria.fabrica.ItemMenu;
import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.persistencia.MenuDAO;
import com.gitb.juandaaguilera.cafeteria.persistencia.PersistenciaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuDAOMySQL implements MenuDAO {

    private static final String SQL_LISTAR =
            "SELECT codigo, nombre, categoria, precio, atributo FROM producto_menu WHERE activo = 1 ORDER BY codigo";

    private final ConexionMySQL conexion;

    public MenuDAOMySQL(ConexionMySQL conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<ItemMenu> listar() {
        List<ItemMenu> items = new ArrayList<>();
        try (Connection c = conexion.abrir();
                PreparedStatement ps = c.prepareStatement(SQL_LISTAR);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String atributo = rs.getString("atributo");
                items.add(new ItemMenu(rs.getString("codigo"), rs.getString("nombre"),
                        Categoria.valueOf(rs.getString("categoria")), rs.getDouble("precio"),
                        atributo == null ? "" : atributo));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error leyendo el menú: " + e.getMessage(), e);
        }
        return items;
    }
}
