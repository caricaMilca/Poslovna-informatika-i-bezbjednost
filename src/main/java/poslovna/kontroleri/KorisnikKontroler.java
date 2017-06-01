package poslovna.kontroleri;

import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poslovna.model.Korisnik;
import poslovna.model.Privilege;
import poslovna.model.Role;
import poslovna.servisi.KorisnikServis;

@RestController
@ControllerAdvice
@RequestMapping("/korisnik")
public class KorisnikKontroler {

	@Autowired
	KorisnikServis korisnikServis;
	@Autowired
	HttpSession sesija;

	@GetMapping(path = "/logovanje/{korisnickoIme}/{lozinka}")
	public ResponseEntity<Korisnik> logovanje(@PathVariable("korisnickoIme") String korisnickoIme,
			@PathVariable("lozinka") String lozinka) {
		Korisnik k = korisnikServis.logovanje(korisnickoIme, lozinka);
		if (k != null) {
			sesija.setAttribute("korisnik", k);
			return new ResponseEntity<Korisnik>(k, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	@GetMapping(path = "/logout")
	public ResponseEntity<?> logout() {
		if ((Korisnik) sesija.getAttribute("korisnik") == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		sesija.invalidate();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping(path = "/promenaLozinke/{lozinka}")
	public ResponseEntity<?> promenaLozinke(@PathVariable("lozinka") String lozinka) {
		if (authorize("promenaLozinke") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		korisnikServis.promenaLozinke(lozinka);
		return new ResponseEntity<>(HttpStatus.OK);
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
