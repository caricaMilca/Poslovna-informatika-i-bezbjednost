package poslovna.kontroleri;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poslovna.model.Drzava;
import poslovna.model.Korisnik;
import poslovna.model.Privilege;
import poslovna.model.Role;
import poslovna.servisi.DrzavaServis;
import poslovna.servisi.ValutaServis;

@RestController
@RequestMapping("/drzava")
public class DrzavaKontroler {

	@Autowired
	HttpSession sesija;

	@Autowired
	DrzavaServis drzavaServis;
	
	@Autowired
	ValutaServis valutaServis;
	
	@PostMapping(path = "/registracijaDrzave/{idValute}")
	public ResponseEntity<Drzava> registracijaDrzave(@Valid @RequestBody Drzava drzava, @PathVariable("idValute") Long idValute) {
		if (authorize("registracijaDrzave") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		drzava.valuta = valutaServis.preuzmiValutu(idValute);
		return drzavaServis.registracijaDrzave(drzava);

	}

	@GetMapping(path = "/sveDrzave")
	public ResponseEntity<List<Drzava>> sveDrzave() {
		if (authorize("sveDrzave") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return drzavaServis.sveDrzave();
	}
	
	@GetMapping(path = "/sveDrzaveValute/{idValute}")
	public ResponseEntity<List<Drzava>> sveDrzaveValute( @PathVariable("idValute") Long idValute) {
		if (authorize("sveDrzaveValute") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return drzavaServis.sveDrzaveValute(idValute);
	}
	
	@PutMapping(path = "/pretraziDrzave/{idValute}")
	public ResponseEntity<List<Drzava>> pretraziDrzave(@RequestBody (required=false) Drzava drzava, @PathVariable("idValute") Long idValute) {
		if (authorize("pretraziDrzave") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return drzavaServis.pretraziDrzave(drzava, idValute);
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
