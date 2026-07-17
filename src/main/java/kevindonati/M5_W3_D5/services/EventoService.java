package kevindonati.M5_W3_D5.services;

import kevindonati.M5_W3_D5.repositories.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;
}
