package com.gitb.juandaaguilera.cafeteria.fachada;

import com.gitb.juandaaguilera.cafeteria.builder.ComboBuilder;
import com.gitb.juandaaguilera.cafeteria.builder.DirectorCombos;
import com.gitb.juandaaguilera.cafeteria.caja.CajaRegistradora;
import com.gitb.juandaaguilera.cafeteria.caja.ReporteCierre;
import com.gitb.juandaaguilera.cafeteria.caja.Venta;
import com.gitb.juandaaguilera.cafeteria.catalogo.Menu;
import com.gitb.juandaaguilera.cafeteria.comando.AgregarProductoComando;
import com.gitb.juandaaguilera.cafeteria.comando.AplicarAdicionComando;
import com.gitb.juandaaguilera.cafeteria.comando.Comando;
import com.gitb.juandaaguilera.cafeteria.comando.HistorialComandos;
import com.gitb.juandaaguilera.cafeteria.comando.QuitarProductoComando;
import com.gitb.juandaaguilera.cafeteria.composite.Combo;
import com.gitb.juandaaguilera.cafeteria.decorador.CatalogoAdiciones;
import com.gitb.juandaaguilera.cafeteria.fabrica.FabricaLineaAlmuerzo;
import com.gitb.juandaaguilera.cafeteria.fabrica.FabricaLineaDesayuno;
import com.gitb.juandaaguilera.cafeteria.fabrica.FabricaLineaMenu;
import com.gitb.juandaaguilera.cafeteria.fabrica.ItemMenu;
import com.gitb.juandaaguilera.cafeteria.modelo.Formato;
import com.gitb.juandaaguilera.cafeteria.modelo.Producto;
import com.gitb.juandaaguilera.cafeteria.notificacion.ObservadorPedido;
import com.gitb.juandaaguilera.cafeteria.notificacion.PantallaCaja;
import com.gitb.juandaaguilera.cafeteria.notificacion.PantallaCocina;
import com.gitb.juandaaguilera.cafeteria.notificacion.PantallaMesero;
import com.gitb.juandaaguilera.cafeteria.notificacion.TableroNotificaciones;
import com.gitb.juandaaguilera.cafeteria.pago.AdaptadorBilleteraDigital;
import com.gitb.juandaaguilera.cafeteria.pago.AdaptadorDatafono;
import com.gitb.juandaaguilera.cafeteria.pago.MetodoPago;
import com.gitb.juandaaguilera.cafeteria.pago.PagoEfectivo;
import com.gitb.juandaaguilera.cafeteria.pago.ResultadoPago;
import com.gitb.juandaaguilera.cafeteria.pago.externo.BilleteraDigitalApi;
import com.gitb.juandaaguilera.cafeteria.pago.externo.DatafonoExterno;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;
import com.gitb.juandaaguilera.cafeteria.persistencia.FuentePersistencia;
import com.gitb.juandaaguilera.cafeteria.persistencia.PedidoDAO;
import com.gitb.juandaaguilera.cafeteria.persistencia.RegistroPedidoObserver;
import com.gitb.juandaaguilera.cafeteria.persistencia.memoria.PersistenciaMemoria;
import com.gitb.juandaaguilera.cafeteria.promocion.CalculadoraPrecio;
import com.gitb.juandaaguilera.cafeteria.promocion.EstrategiaPromocion;
import com.gitb.juandaaguilera.cafeteria.promocion.SelectorPromocion;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CafeteriaFacade {
    public static final List<String> METODOS_PAGO = List.of("EFECTIVO", "TARJETA", "BILLETERA");

    private final Menu menu = Menu.getInstancia();
    private final CajaRegistradora caja = CajaRegistradora.getInstancia();
    private final Map<Integer, Pedido> pedidos = new LinkedHashMap<>();
    private final Map<Integer, HistorialComandos> historiales = new HashMap<>();
    private final TableroNotificaciones tablero = new TableroNotificaciones();
    private final List<ObservadorPedido> pantallas;
    private final DirectorCombos director = new DirectorCombos(new ComboBuilder());
    private final Map<String, FabricaLineaMenu> lineas = new LinkedHashMap<>();
    private final SelectorPromocion selector = SelectorPromocion.conPromocionesPorDefecto();
    private final DatafonoExterno datafono = new DatafonoExterno();
    private final BilleteraDigitalApi billetera = new BilleteraDigitalApi();
    private final Clock reloj;
    private final FuentePersistencia persistencia;
    private final PedidoDAO pedidoDAO;
    private final ObservadorPedido registroPedidos;

    public CafeteriaFacade() {
        this(Clock.systemDefaultZone(), FuentePersistencia.detectar());
    }

    public CafeteriaFacade(Clock reloj) {
        this(reloj, new PersistenciaMemoria());
    }

    public CafeteriaFacade(Clock reloj, FuentePersistencia persistencia) {
        this.reloj = reloj;
        this.persistencia = persistencia;
        this.pedidoDAO = persistencia.crearPedidoDAO();
        this.registroPedidos = new RegistroPedidoObserver(pedidoDAO);
        menu.cargarItems(persistencia.crearMenuDAO().listar());
        caja.usarRepositorio(persistencia.crearVentaDAO());
        this.pantallas = List.of(new PantallaCocina(tablero), new PantallaMesero(tablero), new PantallaCaja(tablero));
        registrarLinea(new FabricaLineaDesayuno());
        registrarLinea(new FabricaLineaAlmuerzo());
    }

    public final void registrarLinea(FabricaLineaMenu fabrica) {
        lineas.put(fabrica.getNombreLinea().toUpperCase(), fabrica);
    }

    public String getNombrePersistencia() {
        return persistencia.getNombre();
    }

    public List<ItemMenu> verMenu() {
        return menu.getItems();
    }

    public Map<String, String> verAdiciones() {
        return CatalogoAdiciones.disponibles();
    }

    public List<String> verLineasComboDelDia() {
        return new ArrayList<>(lineas.keySet());
    }

    public List<String> verPromocionesVigentes() {
        List<String> lista = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now(reloj);
        for (EstrategiaPromocion p : selector.getPromociones()) {
            if (p.aplica(ahora)) {
                lista.add(p.getNombre());
            }
        }
        return lista;
    }

    public List<Pedido> listarPedidos() {
        return new ArrayList<>(pedidos.values());
    }

    public Pedido consultarPedido(int idPedido) {
        Pedido p = pedidos.get(idPedido);
        if (p == null) {
            throw new IllegalArgumentException("No existe el pedido #" + idPedido);
        }
        return p;
    }

    public List<String> getNotificaciones() {
        return tablero.getMensajes();
    }

    public int crearPedido(String mesa) {
        int id = Math.max(pedidoDAO.siguienteId(), pedidos.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1);
        Pedido pedido = new Pedido(id, mesa);
        pedidoDAO.guardar(pedido);
        pedido.agregarObservador(registroPedidos);
        for (ObservadorPedido o : pantallas) {
            pedido.agregarObservador(o);
        }
        pedidos.put(pedido.getId(), pedido);
        historiales.put(pedido.getId(), new HistorialComandos());
        return pedido.getId();
    }

    public void agregarProducto(int idPedido, String codigoMenu) {
        Pedido p = consultarPedido(idPedido);
        ejecutar(idPedido, new AgregarProductoComando(p, menu.crearProducto(codigoMenu)));
    }

    public void agregarAdicion(int idPedido, int indice, String codigoAdicion) {
        ejecutar(idPedido, new AplicarAdicionComando(consultarPedido(idPedido), indice, codigoAdicion));
    }

    public void quitarProducto(int idPedido, int indice) {
        ejecutar(idPedido, new QuitarProductoComando(consultarPedido(idPedido), indice));
    }

    public void agregarComboDelDia(int idPedido, String linea) {
        FabricaLineaMenu fabrica = lineas.get(linea == null ? "" : linea.trim().toUpperCase());
        if (fabrica == null) {
            throw new IllegalArgumentException("No hay combo del día para la línea " + linea);
        }
        Combo combo = director.construirComboDelDia(fabrica);
        ejecutar(idPedido, new AgregarProductoComando(consultarPedido(idPedido), combo));
    }

    public void agregarComboPersonalizado(int idPedido, String nombre, String codBebida, String codPlato, String codPostre) {
        Combo combo = director.construirComboPersonalizado(nombre, crearOpcional(codBebida),
                crearOpcional(codPlato), crearOpcional(codPostre));
        ejecutar(idPedido, new AgregarProductoComando(consultarPedido(idPedido), combo));
    }

    private Producto crearOpcional(String codigo) {
        return (codigo == null || codigo.isBlank()) ? null : menu.crearProducto(codigo);
    }

    private void ejecutar(int idPedido, Comando comando) {
        consultarPedido(idPedido);
        historiales.get(idPedido).ejecutar(comando);
    }

    public boolean deshacer(int idPedido) {
        consultarPedido(idPedido);
        return historiales.get(idPedido).deshacer();
    }

    public boolean rehacer(int idPedido) {
        consultarPedido(idPedido);
        return historiales.get(idPedido).rehacer();
    }

    public List<String> historialAcciones(int idPedido) {
        consultarPedido(idPedido);
        return historiales.get(idPedido).getAcciones();
    }

    public void avanzarEstado(int idPedido) {
        consultarPedido(idPedido).avanzar();
    }

    public void cancelarPedido(int idPedido) {
        consultarPedido(idPedido).cancelar();
    }

    private CalculadoraPrecio calculadoraPara(Pedido pedido) {
        CalculadoraPrecio calc = new CalculadoraPrecio();
        calc.setEstrategia(selector.mejorPara(pedido, LocalDateTime.now(reloj)));
        return calc;
    }

    public String promocionAplicada(int idPedido) {
        return calculadoraPara(consultarPedido(idPedido)).getEstrategia().getNombre();
    }

    public double calcularDescuento(int idPedido) {
        Pedido p = consultarPedido(idPedido);
        return calculadoraPara(p).calcularDescuento(p);
    }

    public double calcularTotal(int idPedido) {
        Pedido p = consultarPedido(idPedido);
        return calculadoraPara(p).calcularTotal(p);
    }

    public ResultadoPago pagar(int idPedido, String metodo, String dato) {
        Pedido pedido = consultarPedido(idPedido);
        if (!pedido.getEstado().permitePago()) {
            throw new IllegalStateException("Solo se cobra un pedido ENTREGADO (está " + pedido.getNombreEstado() + ")");
        }
        CalculadoraPrecio calc = calculadoraPara(pedido);
        double descuento = calc.calcularDescuento(pedido);
        double total = calc.calcularTotal(pedido);

        MetodoPago medio = crearMetodoPago(metodo, dato);
        ResultadoPago resultado = medio.pagar(total);
        if (resultado.aprobado()) {
            pedido.pagar();
            List<String> nombres = new ArrayList<>();
            for (Producto prod : pedido.getProductos()) {
                nombres.add(prod.getNombre());
            }
            caja.registrarVenta(new Venta(pedido.getId(), pedido.getMesa(), pedido.getSubtotal(), descuento, total,
                    medio.getNombre(), calc.getEstrategia().getNombre(), LocalDateTime.now(reloj), nombres));
        }
        return resultado;
    }

    private MetodoPago crearMetodoPago(String metodo, String dato) {
        String m = metodo == null ? "" : metodo.trim().toUpperCase();
        return switch (m) {
            case "EFECTIVO" -> new PagoEfectivo(leerValor(dato));
            case "TARJETA" -> new AdaptadorDatafono(datafono, dato == null ? "" : dato.trim());
            case "BILLETERA" -> new AdaptadorBilleteraDigital(billetera, dato == null ? "" : dato.trim());
            default -> throw new IllegalArgumentException("Medio de pago no soportado: " + metodo);
        };
    }

    private static double leerValor(String dato) {
        try {
            return Double.parseDouble(dato.replace(".", "").replace(",", "").replace("$", "").trim());
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Valor recibido inválido: " + dato);
        }
    }

    public ReporteCierre cerrarCaja() {
        return caja.cerrarCaja(LocalDateTime.now(reloj));
    }

    public String resumenPedido(int idPedido) {
        Pedido p = consultarPedido(idPedido);
        StringBuilder sb = new StringBuilder();
        sb.append(p).append('\n');
        List<Producto> productos = p.getProductos();
        for (int i = 0; i < productos.size(); i++) {
            sb.append("  ").append(i + 1).append(". ").append(productos.get(i).getDescripcion())
                    .append("  ").append(Formato.pesos(productos.get(i).getPrecio())).append('\n');
        }
        if (productos.isEmpty()) {
            sb.append("  (sin productos)\n");
        }
        sb.append("  Subtotal:  ").append(Formato.pesos(p.getSubtotal())).append('\n');
        sb.append("  Promoción: ").append(promocionAplicada(idPedido))
                .append(" -").append(Formato.pesos(calcularDescuento(idPedido))).append('\n');
        sb.append("  TOTAL:     ").append(Formato.pesos(calcularTotal(idPedido)));
        return sb.toString();
    }
}
