package pe.edu.upeu.PharmaBackend.service.impl;

import java.time.LocalDate;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.PharmaBackend.auditoria.EventoAuditoria;
import pe.edu.upeu.PharmaBackend.dto.AuditoriaEventoDTO;
import pe.edu.upeu.PharmaBackend.dto.PaginaResponseDTO;
import pe.edu.upeu.PharmaBackend.repository.AuditoriaEventoRepository;
import pe.edu.upeu.PharmaBackend.util.PaginacionUtil;

@Service
public class AuditoriaService {
    private static final Logger log = LoggerFactory.getLogger(AuditoriaService.class);
    private static final Set<String> CAMPOS = Set.of("fecha", "usuario", "accion");
    private final ApplicationEventPublisher eventos;
    private final AuditoriaTransaccionService escritor;
    private final AuditoriaEventoRepository repository;

    public AuditoriaService(ApplicationEventPublisher eventos,
                           AuditoriaTransaccionService escritor,
                           AuditoriaEventoRepository repository) {
        this.eventos = eventos;
        this.escritor = escritor;
        this.repository = repository;
    }

    /** La bitácora solo se persiste DESPUÉS de confirmar la transacción. */
    @Transactional(propagation = Propagation.MANDATORY)
    public void registrar(String accion, String entidad, Long id, String detalle) {
        eventos.publishEvent(EventoAuditoria.de(accion, entidad, id, detalle));
    }

    /** El login fallido no tiene transacción de negocio que confirmar. */
    public void registrarLogin(String accion, String username) {
        try {
            escritor.guardar(EventoAuditoria.login(accion, username));
        } catch (RuntimeException ex) {
            log.error("No se pudo guardar la auditoría de login", ex);
        }
    }

    @Transactional(readOnly = true)
    public PaginaResponseDTO<AuditoriaEventoDTO> buscar(String usuario, String accion,
            LocalDate desde, LocalDate hasta, int pagina, int tamanio) {
        Pageable pageable = PaginacionUtil.construir(pagina, tamanio,
                "fecha", "desc", CAMPOS, "fecha");
        return PaginaResponseDTO.de(repository.buscar(
                blancoANulo(usuario), blancoANulo(accion),
                desde == null ? null : desde.atStartOfDay(),
                hasta == null ? null : hasta.plusDays(1).atStartOfDay(),
                pageable).map(AuditoriaEventoDTO::de));
    }

    private static String blancoANulo(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
