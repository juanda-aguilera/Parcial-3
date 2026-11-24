package com.gitb.juandaaguilera.cafeteria.ui.gui;

import com.gitb.juandaaguilera.cafeteria.fachada.CafeteriaFacade;
import com.gitb.juandaaguilera.cafeteria.fabrica.ItemMenu;
import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Formato;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;
import com.gitb.juandaaguilera.cafeteria.pago.ResultadoPago;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class VentanaPrincipal extends JFrame {
    private final CafeteriaFacade sistema;

    private final DefaultTableModel modeloMenu = new DefaultTableModel(new Object[]{"Código", "Producto", "Tipo", "Precio"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tablaMenu = new JTable(modeloMenu);
    private final JComboBox<Pedido> comboPedidos = new JComboBox<>();
    private final JTextField txtMesa = new JTextField("Mesa 1", 8);
    private final DefaultListModel<String> modeloProductos = new DefaultListModel<>();
    private final JList<String> listaProductos = new JList<>(modeloProductos);
    private final JComboBox<String> comboAdiciones = new JComboBox<>();
    private final JLabel lblEstado = new JLabel("Estado: -");
    private final JLabel lblTotal = new JLabel("Total: -");
    private final JLabel lblPromo = new JLabel("Promoción: -");
    private final JComboBox<String> comboPago = new JComboBox<>(CafeteriaFacade.METODOS_PAGO.toArray(new String[0]));
    private final JTextField txtDatoPago = new JTextField(10);
    private final JTextArea areaAvisos = new JTextArea(8, 60);

    public VentanaPrincipal(CafeteriaFacade sistema) {
        super("Cafetería - Sistema de pedidos");
        this.sistema = sistema;
        setTitle("Cafetería - Sistema de pedidos  |  Datos: " + sistema.getNombrePersistencia());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        add(crearPanelMenu(), BorderLayout.WEST);
        add(crearPanelPedido(), BorderLayout.CENTER);
        add(crearPanelAvisos(), BorderLayout.SOUTH);
        cargarMenu();
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearPanelMenu() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Menú"));
        tablaMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaMenu.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaMenu.getColumnModel().getColumn(1).setPreferredWidth(190);
        JScrollPane scroll = new JScrollPane(tablaMenu);
        scroll.setPreferredSize(new java.awt.Dimension(440, 300));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridLayout(0, 1, 4, 4));
        botones.add(boton("Agregar producto seleccionado", this::agregarProducto));
        botones.add(boton("Combo del día...", this::comboDelDia));
        botones.add(boton("Armar combo personalizado...", this::comboPersonalizado));
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelPedido() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Pedido"));

        JPanel arriba = new JPanel();
        arriba.add(new JLabel("Mesa:"));
        arriba.add(txtMesa);
        arriba.add(boton("Nuevo pedido", this::nuevoPedido));
        arriba.add(new JLabel("Pedido:"));
        comboPedidos.addActionListener(e -> refrescarPedido());
        arriba.add(comboPedidos);
        panel.add(arriba, BorderLayout.NORTH);

        listaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(listaProductos), BorderLayout.CENTER);

        JPanel abajo = new JPanel(new GridLayout(0, 1, 4, 4));
        JPanel filaAdicion = new JPanel();
        sistema.verAdiciones().forEach((k, v) -> comboAdiciones.addItem(k + " - " + v));
        filaAdicion.add(comboAdiciones);
        filaAdicion.add(boton("Agregar adición", this::agregarAdicion));
        filaAdicion.add(boton("Quitar", this::quitarProducto));
        abajo.add(filaAdicion);

        JPanel filaDeshacer = new JPanel();
        filaDeshacer.add(boton("Deshacer", () -> {
            if (!sistema.deshacer(idActual())) {
                aviso("No hay nada para deshacer");
            }
        }));
        filaDeshacer.add(boton("Rehacer", () -> {
            if (!sistema.rehacer(idActual())) {
                aviso("No hay nada para rehacer");
            }
        }));
        filaDeshacer.add(boton("Avanzar estado", () -> sistema.avanzarEstado(idActual())));
        filaDeshacer.add(boton("Cancelar pedido", () -> sistema.cancelarPedido(idActual())));
        abajo.add(filaDeshacer);

        JPanel filaTotales = new JPanel(new GridLayout(1, 3));
        lblEstado.setFont(lblEstado.getFont().deriveFont(Font.BOLD));
        filaTotales.add(lblEstado);
        filaTotales.add(lblPromo);
        filaTotales.add(lblTotal);
        abajo.add(filaTotales);

        JPanel filaPago = new JPanel();
        filaPago.add(new JLabel("Pago:"));
        filaPago.add(comboPago);
        filaPago.add(new JLabel("Dato (valor / VISA / celular):"));
        filaPago.add(txtDatoPago);
        filaPago.add(boton("Pagar", this::pagar));
        filaPago.add(boton("Cierre de caja", this::cierreCaja));
        abajo.add(filaPago);

        panel.add(abajo, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelAvisos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Avisos a cocina, caja y mesero"));
        areaAvisos.setEditable(false);
        areaAvisos.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        panel.add(new JScrollPane(areaAvisos), BorderLayout.CENTER);
        return panel;
    }

    private void cargarMenu() {
        for (ItemMenu item : sistema.verMenu()) {
            modeloMenu.addRow(new Object[]{item.codigo(), item.nombre(), item.categoria(), Formato.pesos(item.precio())});
        }
    }

    private void nuevoPedido() {
        int id = sistema.crearPedido(txtMesa.getText());
        comboPedidos.addItem(sistema.consultarPedido(id));
        comboPedidos.setSelectedIndex(comboPedidos.getItemCount() - 1);
    }

    private void agregarProducto() {
        int fila = tablaMenu.getSelectedRow();
        if (fila < 0) {
            throw new IllegalArgumentException("Seleccione un producto del menú");
        }
        sistema.agregarProducto(idActual(), (String) modeloMenu.getValueAt(fila, 0));
    }

    private void agregarAdicion() {
        String codigo = ((String) comboAdiciones.getSelectedItem()).substring(0, 2);
        sistema.agregarAdicion(idActual(), indiceSeleccionado(), codigo);
    }

    private void quitarProducto() {
        sistema.quitarProducto(idActual(), indiceSeleccionado());
    }

    private void comboDelDia() {
        int id = idActual();
        Object linea = JOptionPane.showInputDialog(this, "Línea del combo del día", "Combo del día",
                JOptionPane.QUESTION_MESSAGE, null, sistema.verLineasComboDelDia().toArray(), null);
        if (linea != null) {
            sistema.agregarComboDelDia(id, linea.toString());
        }
    }

    private void comboPersonalizado() {
        int id = idActual();
        JComboBox<String> bebidas = opciones(Categoria.BEBIDA);
        JComboBox<String> platos = opciones(Categoria.PLATO);
        JComboBox<String> postres = opciones(Categoria.POSTRE);
        JTextField nombre = new JTextField("Mi combo");
        JPanel p = new JPanel(new GridLayout(0, 2, 4, 4));
        p.add(new JLabel("Nombre:"));
        p.add(nombre);
        p.add(new JLabel("Bebida:"));
        p.add(bebidas);
        p.add(new JLabel("Plato:"));
        p.add(platos);
        p.add(new JLabel("Postre:"));
        p.add(postres);
        int r = JOptionPane.showConfirmDialog(this, p, "Armar combo paso a paso", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            sistema.agregarComboPersonalizado(id, nombre.getText(), codigoDe(bebidas), codigoDe(platos), codigoDe(postres));
        }
    }

    private JComboBox<String> opciones(Categoria categoria) {
        List<String> lista = new ArrayList<>();
        lista.add("(ninguno)");
        for (ItemMenu item : sistema.verMenu()) {
            if (item.categoria() == categoria) {
                lista.add(item.codigo() + " - " + item.nombre());
            }
        }
        return new JComboBox<>(lista.toArray(new String[0]));
    }

    private static String codigoDe(JComboBox<String> combo) {
        String s = (String) combo.getSelectedItem();
        return (s == null || s.startsWith("(")) ? null : s.substring(0, s.indexOf(' '));
    }

    private void pagar() {
        int id = idActual();
        ResultadoPago r = sistema.pagar(id, (String) comboPago.getSelectedItem(), txtDatoPago.getText());
        JOptionPane.showMessageDialog(this, r.mensaje() + "\nReferencia: " + r.referencia(),
                r.aprobado() ? "Pago aprobado" : "Pago rechazado",
                r.aprobado() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    private void cierreCaja() {
        JTextArea texto = new JTextArea(sistema.cerrarCaja().toTexto());
        texto.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        texto.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(texto), "Cierre de caja", JOptionPane.PLAIN_MESSAGE);
    }

    private int idActual() {
        Pedido p = (Pedido) comboPedidos.getSelectedItem();
        if (p == null) {
            throw new IllegalStateException("Primero cree un pedido");
        }
        return p.getId();
    }

    private int indiceSeleccionado() {
        int i = listaProductos.getSelectedIndex();
        if (i < 0) {
            throw new IllegalArgumentException("Seleccione un producto del pedido");
        }
        return i;
    }

    private JButton boton(String texto, Runnable accion) {
        JButton b = new JButton(texto);
        b.addActionListener(e -> {
            try {
                accion.run();
            } catch (IllegalArgumentException | IllegalStateException ex) {
                aviso(ex.getMessage());
            }
            refrescarPedido();
        });
        return b;
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atención", JOptionPane.WARNING_MESSAGE);
    }

    private void refrescarPedido() {
        modeloProductos.clear();
        Pedido p = (Pedido) comboPedidos.getSelectedItem();
        if (p != null) {
            for (Producto prod : p.getProductos()) {
                modeloProductos.addElement(prod.getDescripcion() + "   " + Formato.pesos(prod.getPrecio()));
            }
            lblEstado.setText("Estado: " + p.getNombreEstado());
            lblPromo.setText("Promo: " + sistema.promocionAplicada(p.getId()));
            lblTotal.setText("Total: " + Formato.pesos(sistema.calcularTotal(p.getId())));
            comboPedidos.repaint();
        }
        areaAvisos.setText(String.join("\n", sistema.getNotificaciones()));
    }
}
