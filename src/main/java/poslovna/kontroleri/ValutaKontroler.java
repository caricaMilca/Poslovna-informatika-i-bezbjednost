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

import poslovna.model.Korisnik;
import poslovna.model.Privilege;
import poslovna.model.Role;
import poslovna.model.Valuta;
import poslovna.servisi.ValutaServis;

@RestController
@RequestMapping("/valuta")
public class ValutaKontroler {

	@Autowired
	HttpSession sesija;

	@Autowired
	ValutaServis valutaServis;
	
	@PostMapping(path = "/registracijaValute")
	public ResponseEntity<Valuta> registracijaValute(@Valid @RequestBody Valuta valuta) {
		if (authorize("registracijaValute") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return valutaServis.registracijaValuta(valuta);

	}

	@GetMapping(path = "/sveValute")
	public ResponseEntity<List<Valuta>> sveValute() {
		if (authorize("sveValute") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return valutaServis.sveValute();
	}
	
	@PutMapping(path = "/pretraziValute")
	public ResponseEntity<List<Valuta>> pretraziValute(@RequestBody Valuta valuta) {
		if (authorize("pretraziValute") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return valutaServis.pretraziValute(valuta);
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
