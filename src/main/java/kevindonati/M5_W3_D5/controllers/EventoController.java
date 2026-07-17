package kevindonati.M5_W3_D5.controllers;

import kevindonati.M5_W3_D5.entities.Evento;
import kevindonati.M5_W3_D5.entities.Utente;
import kevindonati.M5_W3_D5.exceptions.ValidationException;
import kevindonati.M5_W3_D5.payloads.EventoDTO;
import kevindonati.M5_W3_D5.payloads.EventoResponseDTO;
import kevindonati.M5_W3_D5.services.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/eventi")
public class EventoController {
    @Autowired
    private EventoService eventoService;

    @GetMapping
    public Page<Evento> getEventi(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String sortBy) {
        return eventoService.findAll(page, size, sortBy);
    }

    @GetMapping("/{id}")
    public Evento getEvento(@PathVariable UUID id) {
        return eventoService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public EventoResponseDTO createEvento(@AuthenticationPrincipal Utente utenteAutenticato, @RequestBody @Validated EventoDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        Evento nuovoEvento = eventoService.save(body, utenteAutenticato);
        return new EventoResponseDTO(nuovoEvento.getId());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Evento aggiornaEvento(@PathVariable UUID id, @RequestBody @Validated EventoDTO body, BindingResult validationResult, @AuthenticationPrincipal Utente utente) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return eventoService.findByIdAndUpdate(id, body, utente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public void eliminaEvento(@PathVariable UUID id, @AuthenticationPrincipal Utente utente) {
        eventoService.findByIdAndDelete(id, utente);
    }
}
