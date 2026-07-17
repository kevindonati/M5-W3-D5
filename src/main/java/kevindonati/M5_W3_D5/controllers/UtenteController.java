package kevindonati.M5_W3_D5.controllers;

import kevindonati.M5_W3_D5.entities.Utente;
import kevindonati.M5_W3_D5.payloads.UtenteUpdateDTO;
import kevindonati.M5_W3_D5.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/utenti")
public class UtenteController {
    @Autowired
    private UtenteService utenteService;

    @GetMapping("/me")
    public Utente getProfile(@AuthenticationPrincipal Utente utente) {
        return utente;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Utente> getUtenti(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String sortBy) {
        return utenteService.findAll(page, size, sortBy);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utente getUtente(@PathVariable UUID id) {
        return utenteService.findById(id);
    }

    @PutMapping("/me")
    public Utente updateOwnProfile(@AuthenticationPrincipal Utente utenteAutenticato, @RequestBody UtenteUpdateDTO body) {
        return this.utenteService.findByIdAndUpdate(utenteAutenticato.getId(), body);
    }

    @DeleteMapping("/me")
    public void deleteOwnProfile(@AuthenticationPrincipal Utente utenteAutenticato) {
        this.utenteService.findByIdAndDelete(utenteAutenticato.getId());
    }
}
