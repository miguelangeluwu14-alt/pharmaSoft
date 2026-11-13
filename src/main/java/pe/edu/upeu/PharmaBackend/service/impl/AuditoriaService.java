package pe.edu.upeu.PharmaBackend.service.impl;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.upeu.PharmaBackend.entity.AuditoriaEvento;
import pe.edu.upeu.PharmaBackend.repository.AuditoriaEventoRepository;

@Service
public class AuditoriaService {

    private final AuditoriaEventoRepository repository;

    public AuditoriaService(AuditoriaEventoRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void registrar(
            String accion,
            String entidad,
            Long entidadId,
            String detalle) {

        Authentication autenticacion =
                SecurityContextHolder.getContext().getAuthentication();

        String usuario = "SISTEMA";

        if (autenticacion != null
                && autenticacion.isAuthenticated()
                && !("anonymousUser".equals(autenticacion.getPrincipal()))) {
            usuario = autenticacion.getName();
        }

        AuditoriaEvento evento = new AuditoriaEvento();
        evento.setFecha(LocalDateTime.now());
        evento.setUsuario(usuario);
        evento.setAccion(accion);
        evento.setEntidad(entidad);
        evento.setEntidadId(entidadId);
        evento.setDetalle(detalle);

        repository.save(evento);
    }
}