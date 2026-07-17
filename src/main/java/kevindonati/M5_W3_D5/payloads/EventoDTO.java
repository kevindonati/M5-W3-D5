package kevindonati.M5_W3_D5.payloads;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EventoDTO(
        @NotBlank(message = "Titolo obbligatorio")
        @Size(min = 2, message = "Il titolo deve avere un numero di caratteri superiore a 1")
        String titolo,

        @NotBlank(message = "Descrizione obbligatoria")
        @Size(min = 2, message = "Il titolo deve avere un numero di caratteri superiore a 1")
        String descrizione,

        @NotNull(message = "La data è obbligatoria")
        @Future(message = "La data deve essere futura")
        LocalDate dataEvento,

        @NotBlank(message = "Luogo obbligatorio")
        String luogo,

        @Min(value = 1, message = "I posti disponibili devono essere superiori a 0")
        int postiDisponibili
) {
}
