package pe.edu.upeu.PharmaBackend.auditoria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import pe.edu.upeu.PharmaBackend.service.impl.AuditoriaTransaccionService;

@Component
public class AuditoriaListener {
    private static final Logger log = LoggerFactory.getLogger(AuditoriaListener.class);
    private final AuditoriaTransaccionService escritor;

    public AuditoriaListener(AuditoriaTransaccionService escritor) {
        this.escritor = escritor;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void alConfirmar(EventoAuditoria evento) {
        try {
            escritor.guardar(evento);
        } catch (RuntimeException ex) {
            log.error("Fallo al auditar {} {}", evento.accion(), evento.entidadId(), ex);
        }
    }
}
