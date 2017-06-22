package poslovna.kontroleri;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poslovna.autorizacija.AutorizacijaAnnotation;
import poslovna.model.Klijent;
import poslovna.model.Korisnik;
import poslovna.model.Zaposleni;
import poslovna.servisi.KlijentServis;
import poslovna.servisi.KorisnikServis;
import poslovna.servisi.ZaposleniServis;

@RestController
@RequestMapping("/zaposleni")
public class ZaposleniKontroler {

	@Autowired
	private KlijentServis klijentServis;
	@Autowired
	private ZaposleniServis zaposleniServis;
	@Autowired
	KorisnikServis korisnikServis;
	@Autowired
	HttpSession sesija;

	@AutorizacijaAnnotation(imeMetode = "registracijaKlijenta")
	@PostMapping(path = "/registracijaKlijenta/{idDjelatnosti}")
	public ResponseEntity<Klijent> registracijaKlijenta(@Valid @RequestBody Klijent klijent,
			@PathVariable("idDjelatnosti") Long idDjelatnosti) {
		return klijentServis.registracijaKlijenta(klijent, idDjelatnosti);
	}

	@AutorizacijaAnnotation(imeMetode = "registracijaSalteruse")
	@PostMapping(path = "/registracijaSalteruse")
	public ResponseEntity<Zaposleni> registracijaSalteruse(@Valid @RequestBody Zaposleni z) {
		return zaposleniServis.registracijaSalteruse(z);

	}

	@AutorizacijaAnnotation(imeMetode = "preuzmiZaposlenog")
	@GetMapping(path = "/preuzmiZaposlenog")
	public ResponseEntity<Zaposleni> preuzmiZaposlenog() {
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		if (k == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<Zaposleni>(zaposleniServis.preuzmiZaposlenog(k.id), HttpStatus.OK);
	}
}
