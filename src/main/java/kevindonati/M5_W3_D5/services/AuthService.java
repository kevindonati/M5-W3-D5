package kevindonati.M5_W3_D5.services;

import kevindonati.M5_W3_D5.entities.Utente;
import kevindonati.M5_W3_D5.exceptions.UnauthorizedException;
import kevindonati.M5_W3_D5.payloads.LoginDTO;
import kevindonati.M5_W3_D5.repositories.UtenteRepository;
import kevindonati.M5_W3_D5.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private JWTTools jwtTools;

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        Utente found = this.utenteRepository.findByEmail(body.email()).orElseThrow(() -> new UnauthorizedException("Credenziali non valide"));

        if (bcrypt.matches(body.password(), found.getPassword())) {
            return this.jwtTools.generateToken(found);
        } else {
            throw new UnauthorizedException("Credenziali sbagliate");
        }
    }
}
