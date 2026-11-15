# Registro de incidencias | PharmaSoft sesiones 13 y 14

**Criterio:** se documentan situaciones reproducidas y corregidas según registros de terminal y pruebas. Una incidencia se considera **resuelta funcionalmente** cuando se verificó la corrección; el **enlace a commit** debe completarse antes de considerar cerrada su trazabilidad documental. No se inventan números de commit ni fechas de cambios.

| ID | Fecha | Módulo | Severidad estimada | Estado funcional | Commit de corrección |
|---|---|---|---|---|---|
| INC-01 | 13/11/2026 | Playwright/conectividad | Media | Resuelta | **Por identificar** |
| INC-02 | 13/11/2026 | Autenticación/datos de prueba | Alta | Resuelta en desarrollo | **Por identificar; corrección de datos, quizá sin commit** |
| INC-03 | 13/11/2026 | E2E venta | Media | Resuelta | **Por identificar** |
| INC-04 | 13/11/2026 | Docker/despliegue | Media | Resuelta | **No aplica a código; documentar instrucción** |
| INC-05 | 15/11/2026 | Docker Desktop | Alta | Resuelta | **No aplica a código; incidencia de entorno** |
| INC-06 | 15/11/2026 | Playwright/credenciales | Media | Resuelta | **No aplica a código; configuración local** |
| INC-07 | 15/11/2026 | Maven/JDK | Alta | Resuelta | **No aplica a código; configuración local** |

## Fichas de incidencia

### INC-01. Entorno E2E no disponible
- **Reproducción:** ejecutar inicialmente la suite Playwright contra un frontend local distinto del desplegado; seis de siete pruebas presentaron fallos de conexión.
- **Esperado / obtenido:** apertura del frontend y ejecución E2E / error de conexión.
- **Causa:** URL de prueba no apuntaba al frontend activo.
- **Corrección:** `E2E_BASE_URL=http://localhost:8082`.
- **Prueba de regresión:** ejecución completa de 12 pruebas, 36/36 aprobadas el 15/11.
- **Commit:** por localizar en historial si la configuración fue versionada; el valor de variable se define en el entorno.

### INC-02. Credenciales de usuarios de prueba
- **Reproducción:** login de perfiles vendedor/almacenero devolvía HTTP 401 durante la práctica.
- **Esperado / obtenido:** autenticación de usuarios de demostración / denegación.
- **Causa identificada en prueba:** los hashes BCrypt de la base de desarrollo no correspondían a las credenciales de prueba.
- **Corrección:** restablecimiento controlado de hashes de prueba en base de desarrollo.
- **Prueba:** inicio de sesión y pruebas de permisos aprobadas.
- **Commit:** no existe evidencia de cambio de código; documentar script de datos solo si está saneado y es reproducible. **Nunca publicar las contraseñas.**

### INC-03. Selección de cliente en venta E2E
- **Reproducción:** test buscaba `data-testid=opcion-cliente` con un nombre fijo inexistente.
- **Esperado / obtenido:** cliente seleccionable / elemento no encontrado.
- **Causa:** dato fijo sin coincidencia en la base.
- **Corrección:** utilizar cliente activo y producto con stock a través de variables `E2E_CLIENTE_BUSQUEDA` y `E2E_PRODUCTO_BUSQUEDA`.
- **Prueba de regresión:** `e2e/flujo-venta.spec.ts` dentro de las 36 ejecuciones aprobadas.
- **Commit:** localizar el que modificó `e2e/flujo-venta.spec.ts`.

### INC-04. Docker Compose ejecutado desde directorio incorrecto
- **Reproducción:** `docker compose up` en carpeta frontend mostraba `no configuration file provided`.
- **Esperado / obtenido:** recreación del contenedor / no se encontró Compose.
- **Causa:** `docker-compose.yml` pertenecía al proyecto backend.
- **Corrección:** ejecutar Compose desde `C:\Users\Administrator\Desktop\pharmaSoft`.
- **Prueba de regresión:** frontend actualizado, contenedor levantado y Angular compilado correctamente.
- **Commit:** no aplica; procedimiento de operación.

### INC-05. Motor Linux de Docker no disponible
- **Reproducción:** `docker info` y `docker build` reportaron ausencia de `dockerDesktopLinuxEngine`.
- **Esperado / obtenido:** acceso al motor / pipe inexistente.
- **Causa operativa:** Docker Desktop no había inicializado el motor Linux.
- **Corrección:** iniciar Docker Desktop; posteriormente `docker build` finalizó y `pharma-frontend` se recreó.
- **Prueba de regresión:** `docker build` exitoso y 36 pruebas Playwright con el frontend desplegado.
- **Commit:** no aplica; incidencia del entorno Windows/Docker.

### INC-06. Faltaban variables E2E de contraseñas
- **Reproducción:** `e2e/auditoria.spec.ts` falló por ausencia de `E2E_ADMIN_PASSWORD` y `E2E_VENDEDOR_PASSWORD`.
- **Esperado / obtenido:** autenticación automatizada / error explícito de configuración en `e2e/apoyo.ts`.
- **Causa:** variables de entorno no definidas para esa terminal.
- **Corrección:** definir las variables en PowerShell sin exponerlas en el repositorio.
- **Prueba de regresión:** 2/2 auditoría aprobadas y 36/36 suite completa.
- **Commit:** no aplica; credenciales de ejecución externas al código.

### INC-07. Maven no detectaba JDK
- **Reproducción:** `mvnw.cmd -DskipTests compile` devolvió `JAVA_HOME environment variable is not defined correctly`.
- **Esperado / obtenido:** compilación del backend / fallo previo al compilador.
- **Causa:** Java 21 instalado pero `JAVA_HOME` y `PATH` no apuntaban a él.
- **Corrección:** definir `JAVA_HOME` al JDK Microsoft 21 local y anteponer su carpeta `bin` a `PATH`.
- **Prueba de regresión:** 94 archivos Java compilados, `BUILD SUCCESS` y despliegue backend con Spring Boot 4.0.7.
- **Commit:** no aplica; configuración del equipo.

## Resumen verificable

**Cantidad:** 7 incidencias documentadas. **Por severidad propuesta:** 3 altas, 4 medias; 0 críticas, 0 bajas. **Por módulo:** E2E (2), autenticación/datos (1), Docker/despliegue (2), Playwright/configuración (1), Java/Maven (1). **Estado:** 7 corregidas funcionalmente; 0 altas/críticas abiertas en los casos documentados. **Pendiente documental:** identificar commits reales cuando corresponda y registrar evidencias de causas, correcciones y regresiones; estas siete no garantizan ausencia de otras incidencias en el sistema.

**Nota de integridad:** los problemas de configuración del equipo no tienen necesariamente un commit de código. No inventar uno: explicar por qué no corresponde y, si se decide, enlazar el commit de documentación que describe su solución.
