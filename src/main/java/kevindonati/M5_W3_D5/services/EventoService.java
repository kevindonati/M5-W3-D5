package kevindonati.M5_W3_D5.services;

import kevindonati.M5_W3_D5.entities.Evento;
import kevindonati.M5_W3_D5.entities.Utente;
import kevindonati.M5_W3_D5.exceptions.BadRequestException;
import kevindonati.M5_W3_D5.exceptions.NotFoundException;
import kevindonati.M5_W3_D5.exceptions.UnauthorizedException;
import kevindonati.M5_W3_D5.payloads.EventoDTO;
import kevindonati.M5_W3_D5.repositories.EventoRepository;
import kevindonati.M5_W3_D5.repositories.PrenotazioneRepository;
import kevindonati.M5_W3_D5.repositories.UtenteRepository;
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
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    public Evento save(EventoDTO payload, Utente organizzatore) {
        if (payload.dataEvento().isBefore(LocalDate.now())) {
            throw new BadRequestException("La data non può essere nel passato");
        }

        if (eventoRepository.existsByTitoloAndDataEventoAndLuogo(payload.titolo(), payload.dataEvento(), payload.luogo())) {
            throw new BadRequestException("Esiste già un evento con questo titolo, questa data e questo luogo");
        }
        Evento nuovoEvento = new Evento(payload.titolo(), payload.descrizione(), payload.dataEvento(), payload.luogo(), payload.postiDisponibili(), organizzatore);
        return eventoRepository.save(nuovoEvento);
    }

    public Page<Evento> findAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 1) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return eventoRepository.findAll(pageable);
    }

    public Evento findById(UUID id) {
        return eventoRepository.findById(id).orElseThrow(() -> new NotFoundException("Evento con id " + id + " non trovato"));
    }

    public List<Evento> findByOrganizzatore(Utente organizzatore) {
        return eventoRepository.findByOrganizzatore(organizzatore);
    }

    public Evento findByIdAndUpdate(UUID id, EventoDTO payload, Utente utenteAutenticato) {
        Evento eventoTrovato = this.findById(id);
        if (!eventoTrovato.getOrganizzatore().getId().equals(utenteAutenticato.getId())) {
            throw new UnauthorizedException("Non puoi modificare un evento creato da un altro organizzatore");
        }

        eventoTrovato.setTitolo(payload.titolo());
        eventoTrovato.setDescrizione(payload.descrizione());
        eventoTrovato.setDataEvento(payload.dataEvento());
        eventoTrovato.setLuogo(payload.luogo());
        eventoTrovato.setPostiDisponibili(payload.postiDisponibili());

        return eventoRepository.save(eventoTrovato);
    }

    public void findByIdAndDelete(UUID id, Utente utenteAutenticato) {
        Evento eventoTrovato = this.findById(id);
        if (!eventoTrovato.getOrganizzatore().getId().equals(utenteAutenticato.getId())) {
            throw new UnauthorizedException("Non puoi eliminare un evento creato da un altro organizzatore");
        }
        if (prenotazioneRepository.existsByEvento(eventoTrovato)) {
            throw new BadRequestException("Non puoi eliminare un evento che ha delle prenotazioni attive");
        }
        eventoRepository.delete(eventoTrovato);
    }
}
