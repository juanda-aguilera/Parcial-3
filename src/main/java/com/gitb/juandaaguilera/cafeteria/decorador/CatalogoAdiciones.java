package com.gitb.juandaaguilera.cafeteria.decorador;

import com.gitb.juandaaguilera.cafeteria.modelo.Producto;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class CatalogoAdiciones {
    private static final Map<String, String> DISPONIBLES = new LinkedHashMap<>();

    static {
        DISPONIBLES.put("A1", "Leche de almendras +$2.000 (bebidas)");
        DISPONIBLES.put("A2", "Shot de expreso +$2.500 (bebidas)");
        DISPONIBLES.put("A3", "Crema chantilly +$1.500 (bebidas y postres)");
        DISPONIBLES.put("A4", "Queso extra +$3.000 (platos)");
        DISPONIBLES.put("A5", "Tocineta +$3.500 (platos)");
    }

    private CatalogoAdiciones() {
    }

    public static Map<String, String> disponibles() {
        return Collections.unmodifiableMap(DISPONIBLES);
    }

    public static Producto aplicar(String codigo, Producto producto) {
        return switch (codigo.trim().toUpperCase()) {
            case "A1" -> new LecheAlmendras(producto);
            case "A2" -> new ShotExpreso(producto);
            case "A3" -> new CremaChantilly(producto);
            case "A4" -> new QuesoExtra(producto);
            case "A5" -> new Tocineta(producto);
            default -> throw new IllegalArgumentException("No existe la adición " + codigo);
        };
    }
}
