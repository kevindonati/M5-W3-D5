package kevindonati.M5_W3_D5.repositories;

import kevindonati.M5_W3_D5.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UtenteRepository extends JpaRepository<Utente, UUID> {
    Optional<Utente> findByEmail(String email);

    Optional<Utente> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
