package kevindonati.M5_W3_D5.repositories;

import kevindonati.M5_W3_D5.entities.Evento;
import kevindonati.M5_W3_D5.entities.Prenotazione;
import kevindonati.M5_W3_D5.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, UUID> {
    List<Prenotazione> findByUtente(Utente utente);

    List<Prenotazione> findByEvento(Evento evento);
    
    boolean existsByEvento(Evento evento);
}
