package pe.edu.upeu.PharmaBackend.service.impl;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.PharmaBackend.auditoria.EventoAuditoria;
import pe.edu.upeu.PharmaBackend.entity.AuditoriaEvento;
import pe.edu.upeu.PharmaBackend.repository.AuditoriaEventoRepository;

@Service
public class AuditoriaTransaccionService {
    private final AuditoriaEventoRepository repository;

    public AuditoriaTransaccionService(AuditoriaEventoRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void guardar(EventoAuditoria evento) {
        AuditoriaEvento a = new AuditoriaEvento();
        a.setFecha(LocalDateTime.now());
        a.setUsuario(evento.usuario());
        a.setAccion(evento.accion());
        a.setEntidad(evento.entidad());
        a.setEntidadId(evento.entidadId());
        a.setDetalle(evento.detalle());
        a.setRequestId(evento.requestId());
        repository.save(a);
    }
}
