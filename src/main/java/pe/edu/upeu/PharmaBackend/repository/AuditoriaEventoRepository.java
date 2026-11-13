package pe.edu.upeu.PharmaBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.PharmaBackend.entity.AuditoriaEvento;

public interface AuditoriaEventoRepository
        extends JpaRepository<AuditoriaEvento, Long> {
}