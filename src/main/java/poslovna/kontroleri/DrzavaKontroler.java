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
import poslovna.model.Drzava;
import poslovna.servisi.DrzavaServis;

@RestController
@RequestMapping("/drzava")
public class DrzavaKontroler {

	@Autowired
	HttpSession sesija;

	@Autowired
	DrzavaServis drzavaServis;

	@AutorizacijaAnnotation(imeMetode = "registracijaDrzave")
	@PostMapping(path = "/registracijaDrzave/{idValute}")
	public ResponseEntity<Drzava> registracijaDrzave(@Valid @RequestBody Drzava drzava,
			@PathVariable("idValute") Long idValute) {
		return drzavaServis.registracijaDrzave(drzava, idValute);

	}

	@AutorizacijaAnnotation(imeMetode = "sveDrzave")
	@GetMapping(path = "/sveDrzave")
	public ResponseEntity<List<Drzava>> sveDrzave() {
		return drzavaServis.sveDrzave();
	}

	@AutorizacijaAnnotation(imeMetode = "sveDrzaveValute")
	@GetMapping(path = "/sveDrzaveValute/{idValute}")
	public ResponseEntity<List<Drzava>> sveDrzaveValute(@PathVariable("idValute") Long idValute) {
		return drzavaServis.sveDrzaveValute(idValute);
	}

	@AutorizacijaAnnotation(imeMetode = "pretraziDrzave")
	@PutMapping(path = "/pretraziDrzave/{idValute}")
	public ResponseEntity<List<Drzava>> pretraziDrzave(@RequestBody(required = false) Drzava drzava,
			@PathVariable("idValute") Long idValute) {
		return drzavaServis.pretraziDrzave(drzava, idValute);
	}
	
	@AutorizacijaAnnotation(imeMetode = "izbrisiDrzavu")
	@PutMapping(path = "/izbrisiDrzavu/{idDrzave}")
	public ResponseEntity<?> izbrisiDrzavu(@PathVariable("idDrzave") Long idDrzave) {
		return drzavaServis.izbrisiDrzavu(idDrzave);
	}

	@AutorizacijaAnnotation(imeMetode = "izmeniDrzavu")
	@PutMapping(path = "/izmeniDrzavu/{idValute}")
	public ResponseEntity<Drzava> izmeniDrzavu(@RequestBody(required = false) Drzava drzava, @PathVariable("idValute") Long idValute) {
		
		return drzavaServis.izmeniDrzavu(drzava, idValute);
	}
	
}
