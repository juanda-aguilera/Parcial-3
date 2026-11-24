package com.gitb.juandaaguilera.cafeteria.ui.consola;

import com.gitb.juandaaguilera.cafeteria.fachada.CafeteriaFacade;
import com.gitb.juandaaguilera.cafeteria.fabrica.ItemMenu;
import com.gitb.juandaaguilera.cafeteria.modelo.Formato;
import com.gitb.juandaaguilera.cafeteria.pago.ResultadoPago;
import com.gitb.juandaaguilera.cafeteria.pedido.Pedido;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AppConsola {
    private final CafeteriaFacade sistema;
    private final Scanner in = new Scanner(System.in);
    private int notificacionesMostradas = 0;

    public AppConsola(CafeteriaFacade sistema) {
        this.sistema = sistema;
    }

    public static void main(String[] args) {
        new AppConsola(new CafeteriaFacade()).iniciar();
    }

    public void iniciar() {
        System.out.println("=== CAFETERÍA - SISTEMA DE PEDIDOS (consola) ===");
        System.out.println("Datos: " + sistema.getNombrePersistencia());
        boolean seguir = true;
        while (seguir) {
            System.out.println("""

                    1. Ver menú            2. Nuevo pedido          3. Agregar producto
                    4. Agregar adición     5. Combo del día         6. Armar combo
                    7. Quitar producto     8. Deshacer              9. Rehacer
                    10. Ver pedido         11. Avanzar estado       12. Cancelar pedido
                    13. Pagar              14. Listar pedidos       15. Cierre de caja
                    0. Salir""");
            String op = leer("Opción");
            try {
                switch (op) {
                    case "1" -> verMenu();
                    case "2" -> {
                        int id = sistema.crearPedido(leer("Mesa (ej: Mesa 4)"));
                        System.out.println("Pedido #" + id + " creado");
                    }
                    case "3" -> {
                        int id = leerId();
                        sistema.agregarProducto(id, leer("Código del menú"));
                        System.out.println(sistema.resumenPedido(id));
                    }
                    case "4" -> agregarAdicion();
                    case "5" -> {
                        int id = leerId();
                        System.out.println("Líneas: " + sistema.verLineasComboDelDia());
                        sistema.agregarComboDelDia(id, leer("Línea"));
                        System.out.println(sistema.resumenPedido(id));
                    }
                    case "6" -> {
                        int id = leerId();
                        System.out.println("Deje vacío lo que no quiera incluir.");
                        sistema.agregarComboPersonalizado(id, leer("Nombre del combo"), leer("Código bebida"),
                                leer("Código plato"), leer("Código postre"));
                        System.out.println(sistema.resumenPedido(id));
                    }
                    case "7" -> {
                        int id = leerId();
                        System.out.println(sistema.resumenPedido(id));
                        sistema.quitarProducto(id, leerEntero("Número de producto a quitar") - 1);
                        System.out.println(sistema.resumenPedido(id));
                    }
                    case "8" -> {
                        int id = leerId();
                        System.out.println(sistema.deshacer(id) ? "Acción deshecha" : "No hay nada para deshacer");
                        System.out.println(sistema.resumenPedido(id));
                    }
                    case "9" -> {
                        int id = leerId();
                        System.out.println(sistema.rehacer(id) ? "Acción rehecha" : "No hay nada para rehacer");
                        System.out.println(sistema.resumenPedido(id));
                    }
                    case "10" -> {
                        int id = leerId();
                        System.out.println(sistema.resumenPedido(id));
                        System.out.println("Historial: " + sistema.historialAcciones(id));
                    }
                    case "11" -> {
                        int id = leerId();
                        sistema.avanzarEstado(id);
                        System.out.println(sistema.consultarPedido(id));
                    }
                    case "12" -> {
                        int id = leerId();
                        sistema.cancelarPedido(id);
                        System.out.println(sistema.consultarPedido(id));
                    }
                    case "13" -> pagar();
                    case "14" -> {
                        for (Pedido p : sistema.listarPedidos()) {
                            System.out.println(p + "  " + Formato.pesos(sistema.calcularTotal(p.getId())));
                        }
                    }
                    case "15" -> System.out.println(sistema.cerrarCaja().toTexto());
                    case "0" -> seguir = false;
                    default -> System.out.println("Opción no válida");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
            mostrarNotificacionesNuevas();
        }
        System.out.println("Hasta luego");
    }

    private void verMenu() {
        System.out.println("--- MENÚ ---");
        for (ItemMenu item : sistema.verMenu()) {
            System.out.printf("%-4s %-28s %-7s %s%n", item.codigo(), item.nombre(), item.categoria(),
                    Formato.pesos(item.precio()));
        }
        System.out.println("--- ADICIONES ---");
        for (Map.Entry<String, String> a : sistema.verAdiciones().entrySet()) {
            System.out.println(a.getKey() + "  " + a.getValue());
        }
        System.out.println("Promociones vigentes ahora: " + sistema.verPromocionesVigentes());
    }

    private void agregarAdicion() {
        int id = leerId();
        System.out.println(sistema.resumenPedido(id));
        int n = leerEntero("Número de producto") - 1;
        sistema.verAdiciones().forEach((k, v) -> System.out.println(k + "  " + v));
        sistema.agregarAdicion(id, n, leer("Código de adición"));
        System.out.println(sistema.resumenPedido(id));
    }

    private void pagar() {
        int id = leerId();
        System.out.println(sistema.resumenPedido(id));
        String metodo = leer("Medio de pago " + CafeteriaFacade.METODOS_PAGO);
        String dato = switch (metodo.trim().toUpperCase()) {
            case "EFECTIVO" -> leer("Valor recibido");
            case "TARJETA" -> leer("Franquicia (VISA/MASTERCARD)");
            case "BILLETERA" -> leer("Celular");
            default -> "";
        };
        ResultadoPago r = sistema.pagar(id, metodo, dato);
        System.out.println((r.aprobado() ? "APROBADO " : "RECHAZADO ") + r.mensaje() + " ref: " + r.referencia());
    }

    private void mostrarNotificacionesNuevas() {
        List<String> todas = sistema.getNotificaciones();
        for (int i = notificacionesMostradas; i < todas.size(); i++) {
            System.out.println("  >> " + todas.get(i));
        }
        notificacionesMostradas = todas.size();
    }

    private String leer(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return in.hasNextLine() ? in.nextLine().trim() : "0";
    }

    private int leerEntero(String etiqueta) {
        try {
            return Integer.parseInt(leer(etiqueta));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Debe escribir un número");
        }
    }

    private int leerId() {
        return leerEntero("Número de pedido");
    }
}
