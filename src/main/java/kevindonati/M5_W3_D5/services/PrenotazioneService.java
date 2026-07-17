package kevindonati.M5_W3_D5.services;

import kevindonati.M5_W3_D5.entities.Evento;
import kevindonati.M5_W3_D5.entities.Prenotazione;
import kevindonati.M5_W3_D5.entities.Utente;
import kevindonati.M5_W3_D5.exceptions.NotFoundException;
import kevindonati.M5_W3_D5.repositories.PrenotazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public void findByIdAndDelete(UUID id) {
        Prenotazione prenotazioneTrovata = this.findById(id);
        prenotazioneRepository.delete(prenotazioneTrovata);
    }

    public List<Prenotazione> findByUtente(Utente utente) {
        return prenotazioneRepository.findByUtente(utente);
    }

    public List<Prenotazione> findByEvento(Evento evento) {
        return prenotazioneRepository.findByEvento(evento);
    }
}
