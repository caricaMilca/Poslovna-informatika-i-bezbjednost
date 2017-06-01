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

import poslovna.model.Korisnik;
import poslovna.model.NaseljenoMjesto;
import poslovna.model.Privilege;
import poslovna.model.Role;
import poslovna.servisi.NaseljenoMjestoServis;

@RestController
@RequestMapping("/naseljenoMjesto")
public class NaseljenoMjestoKontroler {

	@Autowired
	NaseljenoMjestoServis naseljenoMjestoServis;

	@Autowired
	HttpSession sesija;
	
	@PostMapping(path = "/registracijaNaseljenogMjesta/{idDrzave}")
	public ResponseEntity<NaseljenoMjesto> registracijaNaseljenoMjesto(@Valid @RequestBody NaseljenoMjesto nm, @PathVariable("idDrzave") Long idDrzave) {
		if (authorize("registracijaNaseljenogMjesta") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return naseljenoMjestoServis.registracijaNaseljenogMjesta(nm, idDrzave);

	}

	@GetMapping(path = "/svaNaseljenaMjesta")
	public ResponseEntity<List<NaseljenoMjesto>> svaNaseljenaMjesta() {
		if (authorize("svaNaseljenaMjesta") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return naseljenoMjestoServis.svaNaseljenaMjesta();
	}
	
	@GetMapping(path = "/svaNaseljenaMjestaDrzave/{idDrzave}")
	public ResponseEntity<List<NaseljenoMjesto>> svaNaseljenaMjesta(@PathVariable("idDrzave") Long idDrzave) {
		if (authorize("svaNaseljenaMjestaDrzave") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return naseljenoMjestoServis.svaNaseljenaMjestaDrzave(idDrzave);
	}
	
	@PutMapping(path = "/pretraziNaseljenaMjesta/{idDrzave}")
	public ResponseEntity<List<NaseljenoMjesto>> pretraziNaseljenaMjesta(@RequestBody(required=false) NaseljenoMjesto nm, @PathVariable("idDrzave") Long idDrzave) {
		if (authorize("pretraziNaseljenaMjesta") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return naseljenoMjestoServis.pretraziNaseljenaMjesta(nm, idDrzave);
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
