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
import poslovna.model.VrstaPlacanja;
import poslovna.servisi.VrstaPlacanjaServis;

@RestController
@RequestMapping("/vrstaPlacanja")
public class VrstaPlacanjaKontroler {

	@Autowired
	HttpSession sesija;

	@Autowired
	VrstaPlacanjaServis vrstaPlacanjaServis;

	@AutorizacijaAnnotation(imeMetode = "registracijaVrstePlacanja")
	@PostMapping(path = "/registracijaVrstePlacanja")
	public ResponseEntity<VrstaPlacanja> registracijaVrstePlacanja(@Valid @RequestBody VrstaPlacanja vrstaPlacanja) {
		return vrstaPlacanjaServis.registracijaVrstePlacanja(vrstaPlacanja);

	}

	@AutorizacijaAnnotation(imeMetode = "izbrisiVrstuPlacanja")
	@PutMapping(path = "/izbrisiVrstuPlacanja/{id}")
	public ResponseEntity<?> izbrisiVrstuPlacanja(@PathVariable("id") Long id) {
		return vrstaPlacanjaServis.izbrisiVrstuPlacanja(id);
	}

	@AutorizacijaAnnotation(imeMetode = "izmjeniVrstuPlacanja")
	@PutMapping(path = "/izmjeniVrstuPlacanja")
	public ResponseEntity<VrstaPlacanja> izmjeniVrstuPlacanja(@RequestBody VrstaPlacanja vrstaPlacanja) {
		return vrstaPlacanjaServis.izmjeniVrstuPlacanja(vrstaPlacanja);
	}

	@AutorizacijaAnnotation(imeMetode = "sveVrstePlacanja")
	@GetMapping(path = "/sveVrstePlacanja")
	public ResponseEntity<List<VrstaPlacanja>> sveVrstePlacanja() {
		return vrstaPlacanjaServis.sveVrstePlacanja();
	}

}
