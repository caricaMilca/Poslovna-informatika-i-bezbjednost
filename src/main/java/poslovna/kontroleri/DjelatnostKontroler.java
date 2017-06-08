package poslovna.kontroleri;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poslovna.autorizacija.AutorizacijaAnnotation;
import poslovna.model.Djelatnost;
import poslovna.servisi.DjelatnostServis;

@RestController
@RequestMapping("/djelatnost")
public class DjelatnostKontroler {

	@Autowired
	DjelatnostServis djelatnostServis;

	@Autowired
	HttpSession sesija;

	@AutorizacijaAnnotation(imeMetode = "registracijaDjelatnosti")
	@PostMapping(path = "/registracijaDjelatnosti")
	public ResponseEntity<Djelatnost> registracijaDjelatnosti(@Valid @RequestBody Djelatnost djelatnost) {
		return djelatnostServis.registracijaDjelatnosti(djelatnost);

	}

	@AutorizacijaAnnotation(imeMetode = "sveDjelatnosti")
	@GetMapping(path = "/sveDjelatnosti")
	public ResponseEntity<List<Djelatnost>> sveDjelatnosti() {
		return djelatnostServis.sveDjelatnosti();
	}
	@AutorizacijaAnnotation(imeMetode = "izbrisiDjelatnost")
	@PutMapping(path = "/izbrisiDjelatnost/{idDjelatnosti}")
	public ResponseEntity<?> izbrisiDjelatnost(@PathVariable("idDjelatnosti") Long idDjelatnosti) {
		return djelatnostServis.izbrisiDjelatnost(idDjelatnosti);
	}
	
	@AutorizacijaAnnotation(imeMetode = "izmjeniDjelatnost")
	@PutMapping(path = "/izmjeniDjelatnost/{idDjelatnosti}")
	public ResponseEntity<Djelatnost> izmjeniDjelatnost(@RequestBody Djelatnost dje) {
		
		return djelatnostServis.izmjeniDjelatnost(dje);
	}
	
	@AutorizacijaAnnotation(imeMetode = "pretraziDjelatnosti")
	@PutMapping(path = "/pretraziDjelatnosti")
	public ResponseEntity<List<Djelatnost>> pretraziDjelatnosti(@RequestBody Djelatnost djelatnost) {
		return djelatnostServis.pretraziDjelantosti(djelatnost);
	}

}
