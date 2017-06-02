package poslovna.kontroleri;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poslovna.autorizacija.AutorizacijaAnnotation;
import poslovna.model.Klijent;
import poslovna.model.Korisnik;
import poslovna.servisi.KlijentServis;

@RestController
@RequestMapping("/klijent")
public class KlijentKontroler {

	@Autowired
	HttpSession sesija;

	@Autowired
	KlijentServis klijentServis;

	@AutorizacijaAnnotation(imeMetode = "preuzmiKlijenta")
	@GetMapping(path = "/preuzmiKlijenta")
	public ResponseEntity<Klijent> preuzmiKlijenta() {
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		return new ResponseEntity<Klijent>(klijentServis.preuzmiKlijenta(k.id), HttpStatus.OK);
	}

	@AutorizacijaAnnotation(imeMetode = "sviKlijenti")
	@GetMapping(path = "/sviKlijenti")
	public ResponseEntity<List<Klijent>> sviKlijenti() {
		return klijentServis.sviKlijenti();
	}

	@AutorizacijaAnnotation(imeMetode = "sviKlijentiDjelatnosti")
	@GetMapping(path = "/sviKlijentiDjelatnosti/{idDjelatnosti}")
	public ResponseEntity<List<Klijent>> sviKlijentiDjelatnosti(@PathVariable("idDjelatnosti") Long idDjelatnosti) {
		return klijentServis.sviKlijentiDjelatnosti(idDjelatnosti);
	}

	@AutorizacijaAnnotation(imeMetode = "pretraziKlijente")
	@PutMapping(path = "/pretraziKlijente/{idDjelatnosti}")
	public ResponseEntity<List<Klijent>> pretraziKlijente(@RequestBody(required = false) Klijent klijent,
			@PathVariable("idDjelatnosti") Long idDjelatnosti) {
		return klijentServis.pretraziKlijente(klijent, idDjelatnosti);
	}
}
