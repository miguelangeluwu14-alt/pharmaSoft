# Checklist de demostración

Marcar casillas solamente después de comprobar cada punto.

- [ ] `oracle-local` healthy; comprobar conectividad y respaldo de datos de prueba.
- [ ] `pharma-redis` funcionando; revisar caché Redis.
- [ ] `pharma-backend` iniciado con perfil previsto; revisar `docker logs`.
- [ ] `pharma-frontend` accesible en `http://localhost:8082`.
- [ ] Usuario vendedor y administrador probados; contraseñas seguras NO almacenadas aquí.
- [ ] Cliente y producto de demostración con stock suficiente; registrar stock inicial.
- [ ] Ruta `/auditoria` visible para admin y restringida para vendedor.
- [ ] Informe Playwright HTML accesible (`npx playwright show-report`).
- [ ] Postman: casos 200/201, 400, 401, 403, 404 y 409 comprobados y documentados.
- [ ] Pestañas preparadas: Login, Ventas, Productos, Auditoría, Postman, reporte de pruebas.
- [ ] Captura del caso de venta: Oracle `creado_por`, bitácora y log con el mismo `requestId`.
- [ ] Ensayo cronometrado 1 registrado.
- [ ] Ensayo cronometrado 2 registrado.
- [ ] Flujo principal manual desde base preparada, corrida 1.
- [ ] Flujo principal manual desde base preparada, corrida 2.
- [ ] Flujo principal manual desde base preparada, corrida 3.
- [ ] Video de respaldo grabado (máximo 10 minutos) y enlace probado.
- [ ] Plan B: capturas, reporte HTML y PDF disponibles sin red.
- [ ] Repositorios Git limpios, commits documentados y etiqueta `v1.0-demo` en la versión correcta.
