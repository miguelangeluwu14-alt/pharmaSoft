-- V3: verificar datos iniciales de seguridad.
-- Los registros fueron incorporados previamente en V2.

DECLARE
    v_empleados NUMBER;
    v_usuarios NUMBER;
    v_roles NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_empleados FROM empleados;
    SELECT COUNT(*) INTO v_usuarios FROM usuarios;
    SELECT COUNT(*) INTO v_roles FROM roles;

    IF v_empleados = 0 OR v_usuarios = 0 OR v_roles = 0 THEN
        RAISE_APPLICATION_ERROR(
            -20003,
            'Faltan datos iniciales de seguridad'
        );
    END IF;
END;
/