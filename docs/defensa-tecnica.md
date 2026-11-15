# Defensa técnica: diez preguntas y respuestas

Estas respuestas son una **base de estudio**, deben comprenderse y reformularse con palabras propias antes de sustentar. Las rutas se sustentan en el código aportado o en las pruebas verificadas. La ruta exacta de algún método de seguridad/caché debe revisarse antes de la presentación.

### 1. ¿Por qué paginar en el servidor y qué consulta ejecuta Oracle?

Porque la SPA solo necesita los registros de la página visible y no debe cargar miles de filas en memoria. Spring Data JPA crea una consulta limitada por página y otra de conteo del total cuando corresponde al cálculo de páginas. El filtrado y orden permitido se aplican en el backend, usando `PaginacionUtil` y repositorios Spring Data. **Ver:** `service/impl/ClienteServiceImpl.java` (`listar`) y `util/PaginacionUtil.java`; comprobar consultas SQL reales con el perfil `dev` antes de afirmar su cantidad exacta.

### 2. ¿Cómo evitas una venta a medias y un falso evento?

La operación de venta debe ejecutarse en una transacción: si falla una regla y se hace rollback, no debe quedar una venta parcialmente confirmada. `AuditoriaService.registrar` publica el evento dentro de la transacción (`MANDATORY`) y el listener lo procesa después del commit. Así, `VENTA_REGISTRADA` no debería persistir cuando la transacción se revierte. **Ver:** `service/impl/VentaServiceImpl.java`, `service/impl/AuditoriaService.java` y `auditoria/AuditoriaListener.java`.

### 3. ¿Qué ocurre si vence el token durante una venta?

El backend debe rechazar una solicitud sin autenticación válida con 401; la SPA debe eliminar la sesión inválida o dirigir al login, sin presentar la venta como guardada. Es indispensable comprobar la reacción exacta del interceptor y del guard de Angular para describir qué mensaje muestra. **Verificar:** archivos de `src/app/core/auth/` y servicios HTTP del frontend; pruebas E2E de autenticación. **Pendiente:** prueba específica de token vencido en plena venta.

### 4. ¿Por qué invalidar la caché cuando se registra una venta?

Porque la venta modifica el stock y una copia anterior de Productos podría mostrar cantidades equivocadas. La invalidación permite consultar el estado actualizado en el siguiente acceso. **Ver:** `service/impl/VentaServiceImpl.java`, `config/CacheConfig.java` y anotaciones `@CacheEvict` del backend. **Verificar** el punto exacto de invalidación en el código desplegado.

### 5. ¿En qué se diferencian columnas de auditoría, bitácora y logs?

`creado_por` y `modificado_por` identifican autoría de una fila; la bitácora registra acciones de negocio como `CLIENTE_REGISTRADO` y `VENTA_REGISTRADA`; los logs permiten investigar pasos técnicos y relacionarlos con un `requestId`. Se complementan, pero no sustituyen unas a otras. **Ver:** entidades auditadas, `AuditoriaEvento.java`, `EventoAuditoria.java` y `config/RequestIdFilter.java`.

### 6. ¿Cómo sabes que una prueba E2E es estable?

Se ejecuta repetidamente sin esperas fijas, con condiciones observables y datos consistentes. El 15/11/2026 las 12 pruebas se ejecutaron tres veces y las 36 aprobaron en 18,6 s. Hubo incidencias iniciales de URL y datos de prueba que se corrigieron antes de esa corrida. **Ver:** frontend `e2e/`, `docs/resultados-pruebas.md` y capturas del HTML.

### 7. ¿Por qué limitar los campos que puede usar el usuario para ordenar?

Porque los nombres de columnas enviados por el cliente no deben aceptarse arbitrariamente. `PaginacionUtil` valida contra listas permitidas. En la sesión práctica los campos no permitidos y tamaños de página fuera de rango devolvieron 409. **Ver:** `util/PaginacionUtil.java` y `ClienteServiceImpl.CAMPOS_ORDENABLES`.

### 8. ¿Cómo diferencias 401 y 403?

401 significa que la solicitud no trae autenticación válida; 403, que el usuario está autenticado pero carece de permiso. En la práctica se comprobó 401 sin token. Las pruebas E2E confirmaron restricciones de vistas; falta documentar una respuesta API 403 para esta actividad. **Ver:** `config/SecurityConfig.java`, guard de permisos de Angular y `docs/resultados-pruebas.md`.

### 9. ¿Cómo compruebas que un evento de auditoría corresponde a una venta concreta?

Con el ID de venta, usuario, hora, acción y `requestId` idénticos entre el registro de Oracle, la bitácora y logs técnicos. Se verificó en la API la venta 98 con `requestId=d3173997`; faltan evidencia de `creado_por` y log histórico correlacionado. **Ver:** `VentaServiceImpl.java`, `AuditoriaController.java`, `RequestIdFilter.java`, `docs/auditoria.md`.

### 10. ¿Qué aporta repetir las pruebas y registrar incidencias?

Demuestra que la misma funcionalidad puede repetirse bajo condiciones controladas y permite rastrear problemas hasta su causa, corrección y regresión. No sustituye pruebas de carga, seguridad exhaustiva o restauración de base de datos. **Ver:** `docs/incidencias.md`, `docs/resultados-pruebas.md` y reportes de Playwright.
