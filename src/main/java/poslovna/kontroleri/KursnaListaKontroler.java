/**
 * 
 */
package poslovna.kontroleri;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poslovna.autorizacija.AutorizacijaAnnotation;
import poslovna.model.KursnaLista;
import poslovna.servisi.KursnaListaServis;

/**
 * @author FixMe
 *
 */

@RestController
@RequestMapping("/kursnaLista")
public class KursnaListaKontroler {

	@Autowired
	KursnaListaServis kursnaListaServis;

	@AutorizacijaAnnotation(imeMetode = "registracijaKursneListe")
	@PostMapping(path = "/registracijaKursneListe")
	public ResponseEntity<KursnaLista> registracijaKursneListe(@Valid @RequestBody KursnaLista kursnaLista) {
		return kursnaListaServis.registracijaKursneListe(kursnaLista);

	}

	@AutorizacijaAnnotation(imeMetode = "sveKursneListe")
	@GetMapping(path = "/sveKursneListe")
	public ResponseEntity<List<KursnaLista>> sveKursneListe() {
		return kursnaListaServis.sveKursneListe();
	}

	@AutorizacijaAnnotation(imeMetode = "pretraziKursneListe")
	@PutMapping(path = "/pretraziKursneListe")
	public ResponseEntity<List<KursnaLista>> pretraziKursneListe(@RequestBody(required = false) KursnaLista kursnaLista) {
		return kursnaListaServis.pretraziKursneListe(kursnaLista);

	}

}
