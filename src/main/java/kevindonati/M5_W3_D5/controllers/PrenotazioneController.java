package kevindonati.M5_W3_D5.controllers;

import kevindonati.M5_W3_D5.entities.Prenotazione;
import kevindonati.M5_W3_D5.entities.Utente;
import kevindonati.M5_W3_D5.payloads.PrenotazioneResponseDTO;
import kevindonati.M5_W3_D5.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {
    @Autowired
    private PrenotazioneService prenotazioneService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Prenotazione> getPrenotazioni(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id") String sortBy) {
        return prenotazioneService.findAll(page, size, sortBy);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Prenotazione getPrenotazione(@PathVariable UUID id) {
        return prenotazioneService.findById(id);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public PrenotazioneResponseDTO prenotaEvento(@PathVariable UUID id, @AuthenticationPrincipal Utente utenteAutenticato) {
        Prenotazione nuovaPrenotazione = prenotazioneService.save(id, utenteAutenticato);
        return new PrenotazioneResponseDTO(nuovaPrenotazione.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminaPrenotazione(@PathVariable UUID id, @AuthenticationPrincipal Utente utente) {
        prenotazioneService.findByIdAndDelete(id, utente);
    }

    @GetMapping("/me")
    public List<Prenotazione> getOwnPrenotazioni(@AuthenticationPrincipal Utente utente) {
        return prenotazioneService.findByUtente(utente);
    }

}
