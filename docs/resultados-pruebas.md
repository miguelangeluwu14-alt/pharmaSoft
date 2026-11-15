# Resultados de pruebas | SesiÃ³n 14 autÃ³noma

**Fecha:** 15/11/2026. **AplicaciÃ³n:** PharmaSoft. **Navegador:** Chromium. **Entorno comprobado:** frontend Docker localhost:8082 y backend Docker localhost:8081.

## EjecuciÃ³n E2E verificada

Comando: `npx playwright test --repeat-each=3 --reporter=list,html`.

| Indicador | Resultado observado |
|---|---:|
| Pruebas diferentes | 12 |
| Repeticiones por prueba | 3 |
| Ejecuciones | 36 |
| Aprobadas | 36 |
| Fallidas | 0 |
| Inestables | 0 |
| DuraciÃ³n total | 18,6 s |

**Evidencia**: `evidencias/s14-estabilizacion/playwright-36-pruebas-resumen.png` y `playwright-36-pruebas-detalle.png` (capturas facilitadas en la sesiÃ³n). El HTML completo se abre en el equipo con `npx playwright show-report`; conviene conservar la carpeta local `playwright-report/` o exportar sus capturas.

**Pruebas cubiertas:** autenticaciÃ³n (3), flujo de venta y descuento de stock (1), paginaciÃ³n de Productos (1), permisos (2), AuditorÃ­a como administrador y vendedor (2), cantidad mayor que stock (1), venta sin productos (1) y filtros de Ventas con retorno a pÃ¡gina 1 (1).

**LÃ­mite de la evidencia:** E2E-08 comprobÃ³ bloqueo desde la SPA antes de confirmar, no que una solicitud directa a la API devuelva 409; requiere validaciÃ³n separada de API. El flujo E2E de venta registra una venta y reduce stock; sus tres repeticiones alteraron existencias de datos de prueba.

**Commit exacto de esta ejecuciÃ³n:** PENDIENTE. Registrar con `git rev-parse --short HEAD` en el frontend, comprobando ademÃ¡s si habÃ­a archivos sin confirmar (`git status --short`). El hash solo representa fielmente la versiÃ³n probada cuando todos los archivos implicados estÃ¡n versionados.

## Postman

**Fecha:** 15/11/2026.

| Indicador | Resultado |
|---|---|
| Solicitudes ejecutadas | 10 |
| Validaciones aprobadas | 12/12 |
| Fallidas | 0 |
| Errores | 0 |
| Duracion | 975 ms |

Se verificaron los codigos HTTP 200, 400, 401, 403, 404 y 409 mediante Postman.

Las capturas de Collection Runner constituyen la evidencia de esta ejecucion.
## Reproducibilidad

Credenciales, JWT y secretos **no** deben incluirse en capturas ni en el repositorio. Para repetir localmente, definir las variables `E2E_*_PASSWORD` en la terminal. No usar `waitForTimeout` como sustituto de esperas por condiciones de interfaz.
