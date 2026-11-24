package com.gitb.juandaaguilera.cafeteria.persistencia;

import com.gitb.juandaaguilera.cafeteria.fabrica.ItemMenu;
import java.util.List;

public interface MenuDAO {
    List<ItemMenu> listar();
}
