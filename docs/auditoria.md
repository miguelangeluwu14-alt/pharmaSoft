# Auditoría de PharmaSoft

**Actividad autónoma — Sesión 14 · 15 de noviembre de 2026**

## 1. Finalidad y alcance

La auditoría permite identificar qué usuario realizó una operación, cuándo ocurrió, sobre qué entidad se efectuó y cuál fue su resultado registrado. En PharmaSoft se distinguen tres mecanismos complementarios: columnas de auditoría en las entidades, bitácora de eventos de negocio y logs técnicos. Se evita incluir información sensible en los detalles.

La página `/auditoria` del frontend permite consultar la bitácora, paginar resultados y aplicar filtros por usuario, acción y fechas, con estado conservado en la URL. Las pruebas E2E-12 y E2E-13 confirmaron el acceso administrativo y la restricción para el vendedor. Se verificó además que el endpoint protegido exige autenticación.

## 2. Eventos de negocio

Los siguientes registros se identificaron en el código o se comprobaron mediante la API:

| Acción | Cuándo se publica | Entidad | Detalle previsto | Evidencia |
|---|---|---|---|---|
| `CLIENTE_REGISTRADO` | Tras guardar un cliente nuevo correctamente | `CLIENTES` | `Cliente registrado` | **Verificado** en bitácora con ID de entidad **9** (15/11/2026) |
| `CATEGORIA_ELIMINADA` | Tras eliminar una categoría sin productos asociados | `CATEGORIAS` | `Categoria eliminada: <nombre>` | **Verificado** en bitácora con ID de entidad **102** (15/11/2026) |
| `ACTUALIZAR` | Tras modificar un cliente o una categoría | `CLIENTES` / `CATEGORIAS` | `Cliente actualizado` / `Categoria actualizada: <nombre>` | Implementado en los servicios; verificación específica pendiente |
| `DESACTIVAR` | Tras dar de baja lógica a un cliente activo | `CLIENTES` | `Cliente dado de baja` | Implementado en `ClienteServiceImpl`; verificación específica pendiente |
| `CREAR` | Tras crear una categoría | `CATEGORIAS` | `Categoria creada: <nombre>` | Implementado en `CategoriaServiceImpl`; verificación específica pendiente |

La acción `VENTA_REGISTRADA` fue comprobada en el código de `VentaServiceImpl.java` (línea 171) y en la consulta protegida a `/api/v1/auditoria` para las ventas 94 a 98. La acción `LOGIN_FALLIDO` se consultó en la práctica; su trazabilidad específica requiere consultar la bitácora y guardar capturas.

## 3. Cómo funciona el registro

1. El servicio de negocio efectúa los cambios dentro de una transacción `@Transactional`.
2. En el método de negocio se llama a `AuditoriaService.registrar(accion, entidad, id, detalle)`.
3. Este método exige una transacción activa (`Propagation.MANDATORY`) y publica un `EventoAuditoria` mediante `ApplicationEventPublisher`.
4. `AuditoriaListener` procesa el evento al confirmar la operación. Así, si la transacción de negocio se revierte, no debe persistirse un evento falso de éxito.
5. `AuditoriaTransaccionService` es el componente que guarda la entrada en la bitácora.

**Excepción: login fallido.** Como la autenticación rechazada no confirma una transacción de negocio, `AuditoriaService.registrarLogin` escribe directamente el evento de autenticación mediante el servicio de escritura y captura los errores de ese registro. El intento rechazado debe poder auditarse aunque el inicio de sesión no tenga éxito.

**Archivos de referencia:**

- `src/main/java/pe/edu/upeu/PharmaBackend/service/impl/ClienteServiceImpl.java`
- `src/main/java/pe/edu/upeu/PharmaBackend/service/impl/CategoriaServiceImpl.java`
- `src/main/java/pe/edu/upeu/PharmaBackend/service/impl/AuditoriaService.java`
- `src/main/java/pe/edu/upeu/PharmaBackend/auditoria/EventoAuditoria.java`
- `src/main/java/pe/edu/upeu/PharmaBackend/auditoria/AuditoriaListener.java`
- `src/main/java/pe/edu/upeu/PharmaBackend/service/impl/AuditoriaTransaccionService.java`

## 4. Diferencias entre mecanismos

| Mecanismo | Qué permite responder | Ejemplo |
|---|---|---|
| Columnas de auditoría | Quién creó o modificó una fila, y los datos de control disponibles en la entidad | `creado_por`, `modificado_por` |
| Bitácora de eventos | Qué operación de negocio ocurrió y sobre qué registro | `CLIENTE_REGISTRADO` con entidad ID 9 |
| Logs técnicos | Qué sucedió durante la ejecución y cómo diagnosticar una solicitud | Mensajes técnicos asociados a un `requestId` |

Las columnas muestran el estado de autoría de un registro; la bitácora documenta acciones; los logs ayudan a investigar la secuencia técnica. No son intercambiables.

## 5. Datos que no deben registrarse

No deben incluirse contraseñas, tokens JWT, encabezados de autorización, secretos, credenciales de base de datos ni información personal innecesaria en `detalle` o en los logs. El campo `detalle` debe limitarse al tipo de operación y al identificador necesario para su rastreo. Cualquier captura destinada al repositorio debe ocultar los secretos.

## 6. Evidencia ejecutada el 15/11/2026

Se inició sesión como administrador y se ejecutó una prueba controlada de la API:

| Verificación | Resultado |
|---|---|
| `POST /api/v1/auth/login` | Inicio de sesión correcto |
| `POST /api/v1/clientes` | Cliente de prueba creado, ID **9** |
| `POST /api/v1/categorias` | Categoría temporal creada |
| `DELETE /api/v1/categorias/102` | Categoría temporal eliminada |
| Consulta `GET /api/v1/auditoria?accion=CLIENTE_REGISTRADO` | Evento **verificado** con ID 9 |
| Consulta `GET /api/v1/auditoria?accion=CATEGORIA_ELIMINADA` | Evento **verificado** con ID 102 |

La categoría temporal se eliminó; el cliente de prueba quedó registrado. Este resultado verifica la presencia de ambos eventos, pero no sustituye las capturas completas de trazabilidad requeridas para una venta.

## 7. Trazabilidad integral de una venta: evidencia pendiente

La actividad autónoma solicita seleccionar una venta real de demostración y enlazar tres evidencias del **mismo caso**:

1. La fila de `ventas` que muestre `creado_por` y el identificador de venta.
2. El evento correspondiente en `GET /api/v1/auditoria`, con usuario, entidad, ID y `requestId`.
3. Las líneas del log del backend con el **mismo `requestId`**.

**Caso seleccionado:** venta **98**, usuario **vendedor**, acción **VENTA_REGISTRADA**, fecha reportada en API **2026-11-15T16:03:18.268504**, `requestId` **d3173997**. La consulta de la bitácora sí está verificada. **Pendiente:** captura de `ventas.creado_por` para ID 98, y log que contenga exactamente `d3173997`. El backend fue recreado después de la venta; el log histórico podría no estar disponible. No atribuir correlación comprobada a un log que no se recuperó.

## 8. Referencias técnicas

- Spring Framework. *Transaction-bound Events*. https://docs.spring.io/spring-framework/reference/data-access/transaction/event.html
- Spring Data JPA. *Auditing*. https://docs.spring.io/spring-data/jpa/reference/auditing.html
- OWASP. *Logging Cheat Sheet*. https://cheatsheetseries.owasp.org/cheatsheets/Logging_Cheat_Sheet.html
