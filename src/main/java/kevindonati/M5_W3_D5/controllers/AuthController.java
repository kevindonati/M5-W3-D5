package kevindonati.M5_W3_D5.controllers;

import kevindonati.M5_W3_D5.entities.Utente;
import kevindonati.M5_W3_D5.exceptions.ValidationException;
import kevindonati.M5_W3_D5.payloads.LoginDTO;
import kevindonati.M5_W3_D5.payloads.LoginResponseDTO;
import kevindonati.M5_W3_D5.payloads.UtenteDTO;
import kevindonati.M5_W3_D5.payloads.UtenteResponseDTO;
import kevindonati.M5_W3_D5.services.AuthService;
import kevindonati.M5_W3_D5.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UtenteService utenteService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UtenteResponseDTO saveUtente(@RequestBody @Validated UtenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new ValidationException(errorsList);
        }
        Utente utenteSalvato = this.utenteService.save(body);
        return new UtenteResponseDTO(utenteSalvato.getId());
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Validated LoginDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new ValidationException(errorsList);
        }
        return new LoginResponseDTO(authService.checkCredentialsAndGenerateToken(body));
    }
}
