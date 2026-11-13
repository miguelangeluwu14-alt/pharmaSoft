package pe.edu.upeu.PharmaBackend.auditoria;

import org.slf4j.MDC;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public record EventoAuditoria(String usuario, String accion, String entidad,
                              Long entidadId, String detalle, String requestId) {

    public static EventoAuditoria de(String accion, String entidad, Long id, String detalle) {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        String usuario = (a == null || !a.isAuthenticated() || a instanceof AnonymousAuthenticationToken)
                ? "sistema" : a.getName();
        return new EventoAuditoria(recortar(usuario, 50), recortar(accion, 40),
                recortar(entidad, 40), id, recortar(detalle, 500), MDC.get("requestId"));
    }

    public static EventoAuditoria login(String accion, String username) {
        return new EventoAuditoria(recortar(username, 50), accion, "USUARIOS",
                null, null, MDC.get("requestId"));
    }

    private static String recortar(String valor, int maximo) {
        return valor == null ? null : valor.substring(0, Math.min(valor.length(), maximo));
    }
}
