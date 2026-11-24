package com.gitb.juandaaguilera.cafeteria.pedido;

public interface EstadoPedido {
    String REGISTRADO = "REGISTRADO";
    String EN_PREPARACION = "EN_PREPARACION";
    String LISTO = "LISTO";
    String ENTREGADO = "ENTREGADO";
    String PAGADO = "PAGADO";
    String CANCELADO = "CANCELADO";

    String getNombre();

    void avanzar(Pedido pedido);

    void cancelar(Pedido pedido);

    void pagar(Pedido pedido);

    boolean permiteModificar();

    boolean permitePago();
}
