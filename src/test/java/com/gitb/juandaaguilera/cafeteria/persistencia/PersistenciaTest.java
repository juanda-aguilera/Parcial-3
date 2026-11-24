package com.gitb.juandaaguilera.cafeteria.persistencia;

import static org.junit.jupiter.api.Assertions.*;

import com.gitb.juandaaguilera.cafeteria.caja.CajaRegistradora;
import com.gitb.juandaaguilera.cafeteria.fachada.CafeteriaFacade;
import com.gitb.juandaaguilera.cafeteria.fabrica.ItemMenu;
import com.gitb.juandaaguilera.cafeteria.modelo.Categoria;
import com.gitb.juandaaguilera.cafeteria.modelo.Plato;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;
import com.gitb.juandaaguilera.cafeteria.persistencia.memoria.MenuDAOMemoria;
import com.gitb.juandaaguilera.cafeteria.persistencia.memoria.PedidoDAOMemoria;
import com.gitb.juandaaguilera.cafeteria.persistencia.memoria.PersistenciaMemoria;
import com.gitb.juandaaguilera.cafeteria.persistencia.memoria.VentaDAOMemoria;
import java.time.Clock;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class PersistenciaTest {

    @Test
    void observadorGuardaEstadoYDetalleDelPedido() {
        PedidoDAOMemoria dao = new PedidoDAOMemoria();
        Pedido p = new Pedido(5, "Mesa 5");
        dao.guardar(p);
        p.agregarObservador(new RegistroPedidoObserver(dao));
        p.agregarProducto(new Plato("Arepa", 5000));
        p.avanzar();
        assertEquals("EN_PREPARACION", dao.estadoGuardado(5));
        assertEquals(1, dao.detalleGuardado(5).size());
        p.avanzar();
        assertEquals("LISTO", dao.estadoGuardado(5));
        assertEquals(6, dao.siguienteId());
    }

    @Test
    void fachadaGuardaVentaYCierreEnLaFuenteDePersistencia() {
        PersistenciaMemoria fuente = new PersistenciaMemoria();
        CafeteriaFacade sistema = new CafeteriaFacade(Clock.systemDefaultZone(), fuente);
        int id = sistema.crearPedido("Mesa 2");
        sistema.agregarProducto(id, "P01");
        sistema.avanzarEstado(id);
        sistema.avanzarEstado(id);
        sistema.avanzarEstado(id);
        assertTrue(sistema.pagar(id, "EFECTIVO", "100000").aprobado());

        VentaDAOMemoria ventas = (VentaDAOMemoria) fuente.crearVentaDAO();
        PedidoDAOMemoria pedidos = (PedidoDAOMemoria) fuente.crearPedidoDAO();
        assertEquals(1, ventas.listarSinCierre().size());
        assertEquals("PAGADO", pedidos.estadoGuardado(id));

        sistema.cerrarCaja();
        assertEquals(0, ventas.listarSinCierre().size());
        assertEquals(1, ventas.getCierres().size());
    }

    @Test
    void cajaRecuperaVentasPendientesAlCambiarDeRepositorio() {
        VentaDAOMemoria dao = new VentaDAOMemoria();
        dao.guardar(new com.gitb.juandaaguilera.cafeteria.caja.Venta(1, "Mesa 1", 5000, 0, 5000, "EFECTIVO",
                "Sin promoción", LocalDateTime.now(), java.util.List.of("Arepa")));
        CajaRegistradora.getInstancia().usarRepositorio(dao);
        assertEquals(5000, CajaRegistradora.getInstancia().getTotalVendido(), 0.01);
        CajaRegistradora.getInstancia().usarRepositorio(new VentaDAOMemoria());
    }

    @Test
    void menuSeReemplazaConLosItemsDeLaBaseDeDatos() {
        MenuDAOMemoria dao = new MenuDAOMemoria();
        dao.agregar(new ItemMenu("Z01", "Producto de prueba", Categoria.POSTRE, 1000, ""));
        com.gitb.juandaaguilera.cafeteria.catalogo.Menu menu = com.gitb.juandaaguilera.cafeteria.catalogo.Menu.getInstancia();
        java.util.List<ItemMenu> originales = menu.getItems();
        menu.cargarItems(dao.listar());
        assertEquals(1, menu.getItems().size());
        menu.cargarItems(originales);
        assertEquals(originales.size(), menu.getItems().size());
    }
}
