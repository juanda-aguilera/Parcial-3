package com.gitb.juandaaguilera.cafeteria.pedido;

import com.gitb.juandaaguilera.cafeteria.modelo.Producto;
import com.gitb.juandaaguilera.cafeteria.notificacion.ObservadorPedido;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {
    private final int id;
    private final String mesa;
    private final List<Producto> productos = new ArrayList<>();
    private final List<ObservadorPedido> observadores = new ArrayList<>();
    private EstadoPedido estado = new EstadoRegistrado();

    public Pedido(int id, String mesa) {
        this.id = id;
        this.mesa = (mesa == null || mesa.isBlank()) ? "Para llevar" : mesa;
    }

    public void agregarObservador(ObservadorPedido observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void quitarObservador(ObservadorPedido observador) {
        observadores.remove(observador);
    }

    private void notificar(String estadoAnterior, String estadoNuevo) {
        for (ObservadorPedido o : new ArrayList<>(observadores)) {
            o.actualizar(this, estadoAnterior, estadoNuevo);
        }
    }

    void cambiarEstado(EstadoPedido nuevo) {
        String anterior = estado.getNombre();
        estado = nuevo;
        notificar(anterior, nuevo.getNombre());
    }

    public void avanzar() {
        estado.avanzar(this);
    }

    public void cancelar() {
        estado.cancelar(this);
    }

    public void pagar() {
        estado.pagar(this);
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public String getNombreEstado() {
        return estado.getNombre();
    }

    public void agregarProducto(Producto producto) {
        validarModificable();
        productos.add(producto);
    }

    public void agregarProducto(int indice, Producto producto) {
        validarModificable();
        productos.add(indice, producto);
    }

    public Producto quitarProducto(int indice) {
        validarModificable();
        validarIndice(indice);
        return productos.remove(indice);
    }

    public Producto reemplazarProducto(int indice, Producto nuevo) {
        validarModificable();
        validarIndice(indice);
        return productos.set(indice, nuevo);
    }

    public Producto getProducto(int indice) {
        validarIndice(indice);
        return productos.get(indice);
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= productos.size()) {
            throw new IllegalArgumentException("No existe el producto #" + (indice + 1) + " en el pedido");
        }
    }

    private void validarModificable() {
        if (!estado.permiteModificar()) {
            throw new IllegalStateException("El pedido está " + estado.getNombre() + " y ya no se puede modificar");
        }
    }

    public List<Producto> getProductos() {
        return Collections.unmodifiableList(productos);
    }

    public boolean estaVacio() {
        return productos.isEmpty();
    }

    public double getSubtotal() {
        double total = 0;
        for (Producto p : productos) {
            total += p.getPrecio();
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public String getMesa() {
        return mesa;
    }

    @Override
    public String toString() {
        return "Pedido #" + id + " (" + mesa + ") - " + estado.getNombre();
    }
}
