package com.gitb.juandaaguilera.cafeteria.catalogo;

import com.gitb.juandaaguilera.cafeteria.fabrica.CreadorBebida;
import com.gitb.juandaaguilera.cafeteria.fabrica.CreadorPlato;
import com.gitb.juandaaguilera.cafeteria.fabrica.CreadorPostre;
import com.gitb.juandaaguilera.cafeteria.fabrica.CreadorProducto;
import com.gitb.juandaaguilera.cafeteria.fabrica.ItemMenu;
import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Menu {
    private static Menu instancia;

    private final Map<String, ItemMenu> items = new LinkedHashMap<>();
    private final Map<Categoria, CreadorProducto> creadores = new EnumMap<>(Categoria.class);

    private Menu() {
        registrarCreador(new CreadorBebida());
        registrarCreador(new CreadorPlato());
        registrarCreador(new CreadorPostre());
        cargarConfiguracion();
    }

    public static synchronized Menu getInstancia() {
        if (instancia == null) {
            instancia = new Menu();
        }
        return instancia;
    }

    public void registrarCreador(CreadorProducto creador) {
        creadores.put(creador.getCategoria(), creador);
    }

    public void agregarItem(ItemMenu item) {
        if (!creadores.containsKey(item.categoria())) {
            throw new IllegalArgumentException("No hay creador registrado para " + item.categoria());
        }
        items.put(item.codigo().toUpperCase(), item);
    }

    public void cargarItems(List<ItemMenu> nuevos) {
        if (nuevos == null || nuevos.isEmpty()) {
            return;
        }
        items.clear();
        for (ItemMenu item : nuevos) {
            agregarItem(item);
        }
    }

    public List<ItemMenu> getItems() {
        return new ArrayList<>(items.values());
    }

    public ItemMenu buscar(String codigo) {
        ItemMenu item = items.get(codigo == null ? "" : codigo.trim().toUpperCase());
        if (item == null) {
            throw new IllegalArgumentException("No existe el código " + codigo + " en el menú");
        }
        return item;
    }

    public Producto crearProducto(String codigo) {
        ItemMenu item = buscar(codigo);
        return creadores.get(item.categoria()).crear(item);
    }

    private void cargarConfiguracion() {
        try (InputStream in = Menu.class.getResourceAsStream("/menu.txt")) {
            if (in == null) {
                cargarPorDefecto();
                return;
            }
            BufferedReader lector = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String linea;
            while ((linea = lector.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty() && !linea.startsWith("#")) {
                    agregarItem(parsear(linea));
                }
            }
        } catch (IOException e) {
            cargarPorDefecto();
        }
    }

    public static ItemMenu parsear(String linea) {
        String[] p = linea.split(";");
        if (p.length < 4) {
            throw new IllegalArgumentException("Línea de menú inválida: " + linea);
        }
        String atributo = p.length > 4 ? p[4].trim() : "";
        return new ItemMenu(p[0].trim(), p[1].trim(), Categoria.valueOf(p[2].trim().toUpperCase()),
                Double.parseDouble(p[3].trim()), atributo);
    }

    private void cargarPorDefecto() {
        agregarItem(new ItemMenu("B01", "Café americano", Categoria.BEBIDA, 4500, "caliente"));
        agregarItem(new ItemMenu("P01", "Sándwich de pollo", Categoria.PLATO, 16000, ""));
        agregarItem(new ItemMenu("R01", "Brownie", Categoria.POSTRE, 6000, ""));
    }
}
