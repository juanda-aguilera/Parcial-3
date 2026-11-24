package com.gitb.juandaaguilera.cafeteria.persistencia;

public class PersistenciaException extends IllegalStateException {
    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
