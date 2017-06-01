package poslovna.kontroleri;

import java.util.Iterator;

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

import poslovna.model.Klijent;
import poslovna.model.Korisnik;
import poslovna.model.Privilege;
import poslovna.model.Role;
import poslovna.model.Zaposleni;
import poslovna.servisi.DjelatnostServis;
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
	DjelatnostServis djelatnostServis;
	@Autowired
	HttpSession sesija;

	@PostMapping(path = "/registracijaKlijentaPravno/{idDjelatnosti}")
	public ResponseEntity<Klijent> registracijaKlijentaPravno(@Valid @RequestBody Klijent klijent, @PathVariable("idDjelatnosti") Long idDjelatnosti) {
		if (authorize("registracijaKlijentaPravno") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		klijent.djelatnost = djelatnostServis.preuzmiDjelatnost(idDjelatnosti);
		return klijentServis.registracijaKlijenta(klijent);

	}

	@PostMapping(path = "/registracijaKlijentaFizicko")
	public ResponseEntity<Klijent> registracijaKlijentaFizicko(@Valid @RequestBody Klijent klijent) {
		if (authorize("registracijaKlijentaFizicko") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return klijentServis.registracijaKlijenta(klijent);

	}

	@PostMapping(path = "/registracijaSalteruse")
	public ResponseEntity<Zaposleni> registracijaSalteruse(@Valid @RequestBody Zaposleni z) {
		if (authorize("registracijaSalteruse") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return zaposleniServis.registracijaSalteruse(z);

	}

	@GetMapping(path = "/preuzmiZaposlenog")
	public ResponseEntity<Zaposleni> preuzmiZaposlenog() {
		if (authorize("preuzmiZaposlenog") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		return new ResponseEntity<Zaposleni>(zaposleniServis.preuzmiZaposlenog(k.id), HttpStatus.OK);
	}

	public String authorize(String imeOperacije) {

		if ((Korisnik) sesija.getAttribute("korisnik") == null)
			return "Ne";
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		Iterator<Role> iterator = k.roles.iterator();

		while (iterator.hasNext()) {
			Iterator<Privilege> it = iterator.next().privileges.iterator();
			while (it.hasNext())
				if (it.next().name.equals(imeOperacije))
					return "Da";
		}
		return "Ne";
	}
}
