package kevindonati.M5_W3_D5.repositories;

import kevindonati.M5_W3_D5.entities.Evento;
import kevindonati.M5_W3_D5.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EventoRepository extends JpaRepository<Evento, UUID> {
    List<Evento> findByOrganizzatore(Utente organizzatore);

    boolean existsByTitoloAndDataEventoAndLuogo(String titolo, LocalDate dataEvento, String Luogo);


}
