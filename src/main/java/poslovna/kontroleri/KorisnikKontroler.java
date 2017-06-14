package poslovna.kontroleri;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poslovna.autorizacija.AutorizacijaAnnotation;
import poslovna.model.Korisnik;
import poslovna.servisi.KorisnikServis;

@RestController
@RequestMapping("/korisnik")
public class KorisnikKontroler {

	@Autowired
	KorisnikServis korisnikServis;
	@Autowired
	HttpSession sesija;

	final static Logger logger = Logger.getLogger(KorisnikKontroler.class);
	
	
	@AutorizacijaAnnotation(imeMetode = "logovanje")
	@GetMapping(path = "/logovanje/{korisnickoIme}/{lozinka}")
	public ResponseEntity<Korisnik> logovanje(@PathVariable("korisnickoIme") String korisnickoIme,
			@PathVariable("lozinka") String lozinka) {
		Korisnik k = korisnikServis.logovanje(korisnickoIme, lozinka);
		if (k != null) {
			sesija.setAttribute("korisnik", k);
			return new ResponseEntity<Korisnik>(k, HttpStatus.OK);
		} else{
			logger.info("Korisnik " + korisnickoIme + " je pokusao logovanja. Neuspesno.");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@AutorizacijaAnnotation(imeMetode = "logout")
	@GetMapping(path = "/logout")
	public ResponseEntity<?> logout() {
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		if (k == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		sesija.invalidate();
		logger.info("Korisnik " + k.korisnickoIme + " se uspesno izlogovao.");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@AutorizacijaAnnotation(imeMetode = "promenaLozinke")
	@PutMapping(path = "/promenaLozinke/{lozinka}")
	public ResponseEntity<?> promenaLozinke(@PathVariable("lozinka") String lozinka) {
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		korisnikServis.promenaLozinke(lozinka);
		logger.info("Korisnik " + k.korisnickoIme + " je uspesno promenio lozinku.");
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

}
