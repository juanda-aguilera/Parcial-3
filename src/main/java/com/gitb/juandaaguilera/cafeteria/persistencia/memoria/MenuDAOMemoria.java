package com.gitb.juandaaguilera.cafeteria.persistencia.memoria;

import com.gitb.juandaaguilera.cafeteria.fabrica.ItemMenu;
import com.gitb.juandaaguilera.cafeteria.persistencia.MenuDAO;
import java.util.ArrayList;
import java.util.List;

public class MenuDAOMemoria implements MenuDAO {

    private final List<ItemMenu> items = new ArrayList<>();

    public void agregar(ItemMenu item) {
        items.add(item);
    }

    @Override
    public List<ItemMenu> listar() {
        return new ArrayList<>(items);
    }
}
