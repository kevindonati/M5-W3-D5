package kevindonati.M5_W3_D5.services;

import kevindonati.M5_W3_D5.entities.Evento;
import kevindonati.M5_W3_D5.entities.Utente;
import kevindonati.M5_W3_D5.exceptions.NotFoundException;
import kevindonati.M5_W3_D5.payloads.EventoDTO;
import kevindonati.M5_W3_D5.repositories.EventoRepository;
import kevindonati.M5_W3_D5.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private UtenteService utenteService;

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

    public Evento findByIdAndUpdate(UUID id, EventoDTO payload) {
        Evento eventoTrovato = this.findById(id);

        eventoTrovato.setTitolo(payload.titolo());
        eventoTrovato.setDescrizione(payload.descrizione());
        eventoTrovato.setDataEvento(payload.dataEvento());
        eventoTrovato.setLuogo(payload.luogo());
        eventoTrovato.setPostiDisponibili(payload.postiDisponibili());

        return eventoRepository.save(eventoTrovato);
    }

    public void findByIdAndDelete(UUID id) {
        Evento eventoTrovato = this.findById(id);
        eventoRepository.delete(eventoTrovato);
    }
}
