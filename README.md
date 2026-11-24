# CafeteriaPatrones – Sistema de pedidos para cafetería

Parcial 3 de Ingeniería de Software 2. Propuesta 3: **Cafetería o restaurante**.
Proyecto Java 21 + Maven, con versión por **consola** y versión **gráfica (Swing)**, que usan el mismo núcleo a través de la fachada `CafeteriaFacade`.

## Base de datos (XAMPP / MySQL)

1. Abrir XAMPP y encender **MySQL** (y Apache si se va a usar phpMyAdmin).
2. En phpMyAdmin → pestaña **Importar** → elegir `database/cafeteria_patrones.sql` → Continuar.
   (También: `mysql -u root < database/cafeteria_patrones.sql`). Crea la base `cafeteria_patrones` con 5 tablas, una vista y el menú inicial.
3. Si el usuario o la clave de MySQL no son `root` / vacía, cambiarlos en `src/main/resources/db.properties`.

Al iniciar, el programa intenta conectarse. Si lo logra, la consola muestra `Datos: MySQL (cafeteria_patrones)` y la ventana lo dice en el título. Si MySQL está apagado, sigue funcionando en memoria (con `menu.txt`) y avisa en la salida.

Qué se guarda: el menú se lee de `producto_menu`; cada pedido se guarda en `pedido` y su estado se actualiza en cada cambio; los productos quedan en `detalle_pedido` al enviarse a cocina; cada pago crea una fila en `venta`; el cierre de caja crea una fila en `cierre_caja` y marca las ventas con su `cierre_id`. Si se cierra el programa sin hacer el cierre, las ventas pendientes se recuperan al volver a abrirlo.

## Cómo compilar y ejecutar

**NetBeans (recomendado):** `File > Open Project` → seleccionar la carpeta `CafeteriaPatrones` (tiene `pom.xml`).
- Interfaz gráfica: clic derecho en `ui/gui/AppGrafica.java` → *Run File* (también es la clase principal del proyecto, F6).
- Consola: clic derecho en `ui/consola/AppConsola.java` → *Run File*.
- Pruebas: clic derecho en el proyecto → *Test* (Alt+F6).

**Maven por terminal:**
```bash
mvn clean test                     # compila y corre las 54 pruebas JUnit 5 (no necesitan MySQL)
mvn exec:java -Dexec.mainClass=com.gitb.juandaaguilera.cafeteria.ui.gui.AppGrafica
mvn exec:java -Dexec.mainClass=com.gitb.juandaaguilera.cafeteria.ui.consola.AppConsola
```
Requisitos: JDK 21 o superior, Maven 3.8+, XAMPP (MySQL/MariaDB). El driver `mysql-connector-j` lo descarga Maven solo.

## Funcionalidades implementadas

| Funcionalidad de la propuesta | Implementada | Dónde |
|---|---|---|
| Bebidas y platos con adiciones que cambian precio y descripción | Sí | `decorador` |
| Combos armados paso a paso con muchas opciones | Sí (combo del día y combo personalizado; un combo puede contener otro combo) | `builder`, `composite` |
| Tipos de producto creados desde un menú configurable | Sí (`src/main/resources/menu.txt`) | `fabrica`, `catalogo` |
| Aviso automático a cocina, caja y mesero al cambiar el estado | Sí | `notificacion`, `pedido` |
| Promociones por horario o por día | Sí (happy hour, martes dulce, madrugador, fin de semana) | `promocion` |
| Cierre de caja y reporte de ventas | Sí (totales, descuentos, por medio de pago, más vendidos) | `caja` |
| Extra: deshacer / rehacer acciones del pedido | Sí | `comando` |
| Extra: pagos con efectivo, datáfono y billetera digital simulados | Sí | `pago` |
| Extra: base de datos MySQL (menú, pedidos, detalle, ventas, cierres) | Sí | `persistencia`, `database/` |

Fuera de alcance: inventario de insumos, usuarios/roles con login (ver documento de diseño).

## Patrones aplicados (12, de las tres categorías)

| Categoría | Patrón | Clases del proyecto |
|---|---|---|
| Creacional | Factory Method | `CreadorProducto` (Creator), `CreadorBebida/Plato/Postre`, cliente `Menu` |
| Creacional | Abstract Factory | `FabricaLineaMenu`, `FabricaLineaDesayuno`, `FabricaLineaAlmuerzo`; y en persistencia `FuentePersistencia` → `PersistenciaMySQL` / `PersistenciaMemoria` |
| Creacional | Singleton | `CajaRegistradora`, `Menu` |
| Creacional | Builder | `ComboBuilder` (Builder), `DirectorCombos` (Director), `Combo` (Product) |
| Estructural | Adapter | `MetodoPago` (Target), `AdaptadorDatafono`/`AdaptadorBilleteraDigital` (Adapter), `DatafonoExterno`/`BilleteraDigitalApi` (Adaptee) |
| Estructural | Facade | `CafeteriaFacade` |
| Estructural | Decorator | `AdicionDecorator` y `LecheAlmendras`, `ShotExpreso`, `CremaChantilly`, `QuesoExtra`, `Tocineta` |
| Estructural | Composite | `Producto` (Component), `Combo` (Composite), `Bebida/Plato/Postre` (Leaf) |
| Comportamiento | Strategy | `EstrategiaPromocion`, promociones concretas, `CalculadoraPrecio` (Context) |
| Comportamiento | Observer | `Pedido` (Subject), `ObservadorPedido`, `PantallaCocina/Mesero/Caja`, `RegistroPedidoObserver` (guarda en BD) |
| Comportamiento | State | `EstadoPedido`, `EstadoRegistrado` … `EstadoCancelado`, `Pedido` (Context) |
| Comportamiento | Command | `Comando`, `AgregarProductoComando`, `QuitarProductoComando`, `AplicarAdicionComando`, `HistorialComandos` (Invoker) |

## Mapa del proyecto

```
src/main/java/com/gitb/juandaaguilera/cafeteria/
├── modelo/         Producto, ProductoBase, Bebida, Plato, Postre, Categoria, Formato
├── decorador/      AdicionDecorator + 5 adiciones, CatalogoAdiciones           (Decorator)
├── composite/      Combo                                                       (Composite)
├── builder/        ComboBuilder, DirectorCombos                                (Builder)
├── fabrica/        CreadorProducto + 3 creadores, ItemMenu                     (Factory Method)
│                   FabricaLineaMenu + Desayuno/Almuerzo                        (Abstract Factory)
├── catalogo/       Menu                                                        (Singleton + cliente Factory Method)
├── pedido/         Pedido, EstadoPedido, EstadoBase + 6 estados                (State, Subject del Observer)
├── notificacion/   ObservadorPedido, PantallaCocina/Mesero/Caja, Tablero       (Observer)
├── promocion/      EstrategiaPromocion + 5 promociones, CalculadoraPrecio, Selector (Strategy)
├── pago/           MetodoPago, PagoEfectivo, adaptadores, externo/*            (Adapter)
├── caja/           CajaRegistradora, Venta, ReporteCierre                      (Singleton)
├── comando/        Comando + 3 comandos, HistorialComandos                     (Command)
├── persistencia/   FuentePersistencia, MenuDAO, PedidoDAO, VentaDAO, RegistroPedidoObserver
│   ├── mysql/      ConexionMySQL, PersistenciaMySQL, *DAOMySQL                 (DAO + Abstract Factory)
│   └── memoria/    PersistenciaMemoria, *DAOMemoria                            (para pruebas o sin BD)
├── fachada/        CafeteriaFacade                                             (Facade)
└── ui/
    ├── consola/    AppConsola
    └── gui/        AppGrafica, VentanaPrincipal
src/main/resources/menu.txt      menú de respaldo cuando no hay BD
src/main/resources/db.properties datos de conexión a MySQL
database/cafeteria_patrones.sql  script de la base de datos
src/test/java/...                una clase de prueba por patrón + persistencia (54 pruebas)
docs/diagramas/                  .puml (fuente) y .png de todos los diagramas
docs/PROMPTS_DIAGRAMAS.md        descripción detallada de cada diagrama
```

## Cómo extender el sistema (cambio de requisito en vivo)

| Cambio pedido | Qué se hace | Clases existentes que NO se tocan |
|---|---|---|
| Nueva adición (ej. "sirope de caramelo") | Crear `SiropeCaramelo extends AdicionDecorator` y agregar el código en `CatalogoAdiciones` | Producto, Pedido, Combo |
| Nueva promoción (ej. "2x1 en tinto los viernes") | Crear clase que implemente `EstrategiaPromocion` y registrarla en `SelectorPromocion` | CalculadoraPrecio, Fachada, UIs |
| Nuevo combo del día (ej. línea "Onces") | Crear `FabricaLineaOnces implements FabricaLineaMenu` y `registrarLinea(...)` | DirectorCombos, ComboBuilder |
| Nuevo producto del menú | `INSERT INTO producto_menu ...` (o una línea en `menu.txt` sin BD) | Todo el código |
| Cambiar de motor de base de datos | Nueva familia que implemente `FuentePersistencia` y los 3 DAO | Fachada, caja, pedido |
| Nuevo tipo de producto (ej. "Snack") | Nuevo valor en `Categoria`, clase `Snack`, `CreadorSnack` y `menu.registrarCreador(...)` | Menu.crearProducto, Pedido |
| Nuevo medio de pago externo | Crear su adaptador que implemente `MetodoPago` | Pedido, Caja |
| Nueva área que reciba avisos (ej. "Domicilios") | Crear `PantallaDomicilios extends PantallaArea` y registrarla | Pedido, estados |
