package poslovna.kontroleri;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poslovna.model.Djelatnost;
import poslovna.model.Korisnik;
import poslovna.model.Privilege;
import poslovna.model.Role;
import poslovna.servisi.DjelatnostServis;

@RestController
@RequestMapping("/djelatnost")
public class DjelatnostKontroler {

	@Autowired
	DjelatnostServis djelatnostServis;

	@Autowired
	HttpSession sesija;

	@PostMapping(path = "/registracijaDjelatnosti")
	public ResponseEntity<Djelatnost> registracijaDjelatnosti(@Valid @RequestBody Djelatnost djelatnost) {
		if (authorize("registracijaDjelatnosti") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return djelatnostServis.registracijaDjelatnosti(djelatnost);

	}

	@GetMapping(path = "/sveDjelatnosti")
	public ResponseEntity<List<Djelatnost>> sveDjelatnosti() {
		if (authorize("sveDjelatnosti") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return djelatnostServis.sveDjelatnosti();
	}
	
	@PutMapping(path = "/pretraziDjelatnosti")
	public ResponseEntity<List<Djelatnost>> pretraziDjelatnosti(@RequestBody Djelatnost djelatnost) {
		if (authorize("pretraziDjelatnosti") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return djelatnostServis.pretraziDjelantosti(djelatnost);
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
