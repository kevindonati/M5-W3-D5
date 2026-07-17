package kevindonati.M5_W3_D5.services;

import kevindonati.M5_W3_D5.entities.Utente;
import kevindonati.M5_W3_D5.enums.Ruolo;
import kevindonati.M5_W3_D5.exceptions.BadRequestException;
import kevindonati.M5_W3_D5.exceptions.NotFoundException;
import kevindonati.M5_W3_D5.payloads.UtenteDTO;
import kevindonati.M5_W3_D5.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private PasswordEncoder bcrypt;

    public Utente save(UtenteDTO payload) {
        if (utenteRepository.existsByEmail(payload.email())) {
            throw new BadRequestException("L'email " + payload.email() + " risulta già registrata");
        }

        if (utenteRepository.existsByUsername(payload.username())) {
            throw new BadRequestException("Lo username " + payload.username() + " è già in uso");
        }

        Utente nuovoUtente = new Utente(payload.nome(), payload.cognome(), payload.username(), payload.email(), bcrypt.encode(payload.password()), Ruolo.UTENTE);
        return utenteRepository.save(nuovoUtente);
    }

    public Page<Utente> findAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 1) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return utenteRepository.findAll(pageable);
    }

    public Utente findById(UUID id) {
        return utenteRepository.findById(id).orElseThrow(() -> new NotFoundException("Utente con id " + id + " non trovato"));
    }

    public Utente findByIdAndUpdate(UUID id, UtenteDTO payload) {
        Utente utenteTrovato = this.findById(id);
        if (!utenteTrovato.getEmail().equals(payload.email()) && utenteRepository.existsByEmail(payload.email())) {
            throw new BadRequestException("L'email è già registrata");
        }

        if (!utenteTrovato.getUsername().equals(payload.username()) && utenteRepository.existsByUsername(payload.username())) {
            throw new BadRequestException("Lo username è già in uso");
        }

        utenteTrovato.setNome(payload.nome());
        utenteTrovato.setCognome(payload.cognome());
        utenteTrovato.setUsername(payload.username());
        utenteTrovato.setEmail(payload.email());
        utenteTrovato.setPassword(bcrypt.encode(payload.password()));
        return utenteRepository.save(utenteTrovato);
    }

    public void findByIdAndDelete(UUID id) {
        Utente utenteTrovato = this.findById(id);
        utenteRepository.delete(utenteTrovato);
    }
}
