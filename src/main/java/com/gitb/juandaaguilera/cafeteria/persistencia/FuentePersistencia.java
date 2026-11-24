package com.gitb.juandaaguilera.cafeteria.persistencia;

import com.gitb.juandaaguilera.cafeteria.persistencia.memoria.PersistenciaMemoria;
import com.gitb.juandaaguilera.cafeteria.persistencia.mysql.ConexionMySQL;
import com.gitb.juandaaguilera.cafeteria.persistencia.mysql.PersistenciaMySQL;

public interface FuentePersistencia {

    String getNombre();

    MenuDAO crearMenuDAO();

    PedidoDAO crearPedidoDAO();

    VentaDAO crearVentaDAO();

    static FuentePersistencia detectar() {
        try {
            ConexionMySQL conexion = ConexionMySQL.desdeConfiguracion();
            conexion.probar();
            return new PersistenciaMySQL(conexion);
        } catch (RuntimeException e) {
            System.err.println("No se pudo conectar a MySQL (" + e.getMessage() + "). Se trabajará en memoria.");
            return new PersistenciaMemoria();
        }
    }
}
