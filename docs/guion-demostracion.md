# Guion de demostración | PharmaSoft

**Duración objetivo:** 10 minutos. **Ensayos cronometrados exigidos:** mínimo 2; **todavía no acreditados**.

| Tiempo | Bloque | Narración y acción |
|---|---|---|
| 0:00-1:00 | Contexto | Presentar el problema operativo de botica, roles y diagrama Angular -> Spring Boot -> Oracle, con Redis como caché |
| 1:00-4:30 | Flujo principal | Iniciar como vendedor, localizar cliente activo, buscar producto con stock, registrar venta, abrir comprobante y demostrar disminución de existencias |
| 4:30-6:00 | Seguridad | Mostrar permisos por rol, impedir acceso del almacenero a Ventas y del vendedor a Auditoría; diferenciar respuesta 401 (sin autenticación) de 403 (sin autorización) |
| 6:00-7:30 | Volumen y rendimiento | Mostrar paginación del servidor, URL con página/orden, botón F5, Redis y Actuator; **solo afirmar 5 000 productos si el conteo real los confirma** |
| 7:30-8:30 | Auditoría | Como administrador mostrar VENTA_REGISTRADA y `requestId`; exhibir log con mismo identificador **únicamente si existe evidencia** |
| 8:30-10:00 | Calidad | Presentar HTML Playwright con 36/36, explicar 7 incidencias identificadas y correcciones; distinguir verificaciones ejecutadas de pendientes |

## Ensayos cronometrados

| Ensayo | Fecha y hora | Duración real | Desvíos / correcciones |
|---|---|---|---|
| 1 | **Pendiente** | **Pendiente** | Registrar después de ensayar |
| 2 | **Pendiente** | **Pendiente** | Registrar después de ensayar |

## Tres verificaciones manuales del flujo

La guía pide partir de una base recién preparada. No realizar restauraciones destructivas sin respaldo. Usar un conjunto de datos de prueba conocido y verificar stock inicial/final en cada ejecución.

| Corrida | Fecha | Duración | ID venta | Stock antes/después | Resultado / desvíos |
|---|---|---|---|---|---|
| 1 | Pendiente | Pendiente | Pendiente | Pendiente | Pendiente |
| 2 | Pendiente | Pendiente | Pendiente | Pendiente | Pendiente |
| 3 | Pendiente | Pendiente | Pendiente | Pendiente | Pendiente |

## Respaldo en video

Grabar la ejecución completa (máximo 10 minutos), ocultando credenciales/JWT. **Enlace pendiente:** añadirlo a este documento y al informe de entrega. Si falla la demostración, utilizar capturas y reporte HTML. La guía no considera el video reemplazado por las capturas.
