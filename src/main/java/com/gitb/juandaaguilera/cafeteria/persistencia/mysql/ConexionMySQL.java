package com.gitb.juandaaguilera.cafeteria.persistencia.mysql;

import com.gitb.juandaaguilera.cafeteria.persistencia.PersistenciaException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionMySQL {

    private final String url;
    private final String usuario;
    private final String clave;

    public ConexionMySQL(String url, String usuario, String clave) {
        this.url = url;
        this.usuario = usuario;
        this.clave = clave;
    }

    public static ConexionMySQL desdeConfiguracion() {
        Properties p = new Properties();
        try (InputStream in = ConexionMySQL.class.getResourceAsStream("/db.properties")) {
            if (in != null) {
                p.load(in);
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo leer db.properties", e);
        }
        String url = System.getProperty("db.url", p.getProperty("url",
                "jdbc:mysql://localhost:3306/cafeteria_patrones"));
        String usuario = System.getProperty("db.usuario", p.getProperty("usuario", "root"));
        String clave = System.getProperty("db.clave", p.getProperty("clave", ""));
        return new ConexionMySQL(url, usuario, clave);
    }

    public Connection abrir() {
        try {
            return DriverManager.getConnection(url, usuario, clave);
        } catch (SQLException e) {
            throw new PersistenciaException("Error conectando a la base de datos: " + e.getMessage(), e);
        }
    }

    public void probar() {
        try (Connection c = abrir()) {
            if (!c.isValid(3)) {
                throw new PersistenciaException("La conexión no es válida", null);
            }
        } catch (SQLException e) {
            throw new PersistenciaException(e.getMessage(), e);
        }
    }

    public String getUrl() {
        return url;
    }
}
