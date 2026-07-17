package kevindonati.M5_W3_D5.services;

import kevindonati.M5_W3_D5.entities.Evento;
import kevindonati.M5_W3_D5.entities.Prenotazione;
import kevindonati.M5_W3_D5.entities.Utente;
import kevindonati.M5_W3_D5.exceptions.BadRequestException;
import kevindonati.M5_W3_D5.exceptions.NotFoundException;
import kevindonati.M5_W3_D5.exceptions.UnauthorizedException;
import kevindonati.M5_W3_D5.repositories.PrenotazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PrenotazioneService {
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;
    @Autowired
    private EventoService eventoService;
    @Autowired
    private UtenteService utenteService;

    public Prenotazione save(UUID idEvento, Utente utente) {
        Evento eventoTrovato = eventoService.findById(idEvento);

        if (eventoTrovato.getPostiDisponibili() <= 0) {
            throw new BadRequestException("Non ci sono più posti disponibili");
        }

        Prenotazione nuovaPrenotazione = new Prenotazione(LocalDate.now(), utente, eventoTrovato);
        return prenotazioneRepository.save(nuovaPrenotazione);
    }

    public Page<Prenotazione> findAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 1) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione findById(UUID id) {
        return prenotazioneRepository.findById(id).orElseThrow(() -> new NotFoundException("Prenotazione con id " + id + " non trovata"));
    }

    public void findByIdAndDelete(UUID id, Utente utente) {
        Prenotazione prenotazioneTrovata = this.findById(id);
        if (!prenotazioneTrovata.getUtente().getId().equals(utente.getId())) {
            throw new UnauthorizedException("Non puoi eliminare una prenotazione che non è tua");
        }
        prenotazioneRepository.delete(prenotazioneTrovata);
    }

    public List<Prenotazione> findByUtente(Utente utente) {
        return prenotazioneRepository.findByUtente(utente);
    }

    public List<Prenotazione> findByEvento(Evento evento) {
        return prenotazioneRepository.findByEvento(evento);
    }
}
