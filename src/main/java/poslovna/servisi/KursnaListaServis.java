package poslovna.servisi;

import java.util.List;

import org.springframework.http.ResponseEntity;

import poslovna.model.KursnaLista;


public interface KursnaListaServis {

	ResponseEntity<KursnaLista> registracijaKursneListe(KursnaLista kursnaLista);

	ResponseEntity<List<KursnaLista>> sveKursneListe();

	ResponseEntity<List<KursnaLista>> pretraziKursneListe(KursnaLista kursnaLista);

	ResponseEntity<KursnaLista> izmeniKL(KursnaLista kl);

	ResponseEntity<?> izbrisiKL(Long id);
}
