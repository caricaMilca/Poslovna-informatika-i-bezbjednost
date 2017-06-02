package poslovna.servisi;

import java.util.List;

import org.springframework.http.ResponseEntity;

import poslovna.model.KursnaLista;

public interface KursnaListaServis {

	ResponseEntity<KursnaLista> registracijaKursneListe(KursnaLista kursnaLista, Long idBanke);

	ResponseEntity<List<KursnaLista>> sveKursneListe();

	ResponseEntity<List<KursnaLista>> sveKursneListeBanke(Long idBanke);

	ResponseEntity<List<KursnaLista>> pretraziKursneListe(KursnaLista kursnaLista, Long idBanke);

}
