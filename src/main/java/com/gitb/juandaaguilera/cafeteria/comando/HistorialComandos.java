package com.gitb.juandaaguilera.cafeteria.comando;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class HistorialComandos {
    private final Deque<Comando> pilaDeshacer = new ArrayDeque<>();
    private final Deque<Comando> pilaRehacer = new ArrayDeque<>();

    public void ejecutar(Comando comando) {
        comando.ejecutar();
        pilaDeshacer.push(comando);
        pilaRehacer.clear();
    }

    public boolean deshacer() {
        if (pilaDeshacer.isEmpty()) {
            return false;
        }
        Comando c = pilaDeshacer.peek();
        c.deshacer();
        pilaRehacer.push(pilaDeshacer.pop());
        return true;
    }

    public boolean rehacer() {
        if (pilaRehacer.isEmpty()) {
            return false;
        }
        Comando c = pilaRehacer.peek();
        c.ejecutar();
        pilaDeshacer.push(pilaRehacer.pop());
        return true;
    }

    public boolean puedeDeshacer() {
        return !pilaDeshacer.isEmpty();
    }

    public boolean puedeRehacer() {
        return !pilaRehacer.isEmpty();
    }

    public List<String> getAcciones() {
        List<String> lista = new ArrayList<>();
        pilaDeshacer.descendingIterator().forEachRemaining(c -> lista.add(c.getDescripcion()));
        return lista;
    }
}
