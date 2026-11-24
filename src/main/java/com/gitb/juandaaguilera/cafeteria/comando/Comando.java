package com.gitb.juandaaguilera.cafeteria.comando;

public interface Comando {
    void ejecutar();
    void deshacer();
    String getDescripcion();
}
