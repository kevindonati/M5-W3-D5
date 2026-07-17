package kevindonati.M5_W3_D5.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PrenotazioneDTO(
        @NotNull(message = "Id evento obbligatorio")
        UUID idEvento
) {
}
