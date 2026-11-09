# PharmaSoft - Backend

## Descripcion
API REST para gestionar categorias, productos y ventas.
Tecnologias: Spring Boot, Java 21, Oracle y Redis.

## Arquitectura
Navegador -> Nginx/Angular -> Spring Boot -> Oracle
                                    |
                                    +-> Redis (cache)

## Puertos locales
- Frontend y Nginx: 8082
- Backend: 8081
- Redis: 6379
- Oracle: segun configuracion de Docker

## Variables de entorno
- DB_URL: conexion a Oracle.
- DB_USERNAME: usuario de Oracle.
- DB_PASSWORD: contrasena de Oracle.
- JWT_SECRET: clave de firma JWT.
- FRONTEND_ORIGINS: origenes autorizados.

No publicar contrasenas, tokens ni archivos .env.

## Despliegue
Desde la carpeta del backend ejecutar:

docker compose up -d --build
docker compose ps

Verificar disponibilidad:

curl.exe http://localhost:8081/actuator/health

## Recuperacion y rollback
1. Identificar una version estable en Git.
2. Respaldar la base de datos Oracle.
3. Restaurar el codigo o imagen Docker estable.
4. Reconstruir los servicios afectados.
5. Comprobar Health, autenticacion y operaciones criticas.

El rollback del codigo no revierte automaticamente la base de datos.

## Lista de verificacion
- Compilacion satisfactoria.
- Credenciales protegidas.
- Autenticacion y autorizacion verificadas.
- Redis y su invalidacion comprobados.
- Logs revisados para evitar datos sensibles.
- Trazabilidad mediante X-Request-Id.
- Health y metricas disponibles.
- Procedimiento de recuperacion documentado.

## Prioridades antes de produccion
1. Configurar HTTPS.
2. Automatizar respaldos y restauracion de Oracle.
3. Implementar alertas de disponibilidad y errores.

El entorno actual corresponde a pruebas locales.
