package kevindonati.M5_W3_D5.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kevindonati.M5_W3_D5.enums.Ruolo;

public record UtenteDTO(
        @NotBlank(message = "Nome obbligatorio")
        @Size(min = 2, max = 25, message = "Il nome deve avere un numero di caratteri compreso tra 2 e 25")
        String nome,

        @NotBlank(message = "Cognome obbligatorio")
        @Size(min = 2, max = 25, message = "Il cognome deve avere un numero di caratteri compreso tra 2 e 25")
        String cognome,

        @NotBlank(message = "Username obbligatorio")
        @Size(min = 2, max = 25, message = "Lo username deve avere un numero di caratteri compreso tra 2 e 25")
        String username,

        @Email(message = "Email non valida")
        @NotBlank(message = "Email obbligatoria")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 6, message = "La password deve contenere almeno 6 caratteri")
        String password
) {
}
