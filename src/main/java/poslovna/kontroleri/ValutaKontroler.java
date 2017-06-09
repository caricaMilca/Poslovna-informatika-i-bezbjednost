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
import poslovna.model.Valuta;
import poslovna.servisi.ValutaServis;

@RestController
@RequestMapping("/valuta")
public class ValutaKontroler {

	@Autowired
	HttpSession sesija;

	@Autowired
	ValutaServis valutaServis;
	
	@AutorizacijaAnnotation(imeMetode = "registracijaValute")
	@PostMapping(path = "/registracijaValute")
	public ResponseEntity<Valuta> registracijaValute(@Valid @RequestBody Valuta valuta) {
		return valutaServis.registracijaValuta(valuta);

	}

	@AutorizacijaAnnotation(imeMetode = "sveValute")
	@GetMapping(path = "/sveValute")
	public ResponseEntity<List<Valuta>> sveValute() {
		return valutaServis.sveValute();
	}
	
	@AutorizacijaAnnotation(imeMetode = "pretraziValute")
	@PutMapping(path = "/pretraziValute")
	public ResponseEntity<List<Valuta>> pretraziValute(@RequestBody Valuta valuta) {
		return valutaServis.pretraziValute(valuta);
	}
	
	@AutorizacijaAnnotation(imeMetode = "izbrisiValutu")
	@PutMapping(path = "/izbrisiValutu/{id}")
	public ResponseEntity<?> izbrisiV(@PathVariable("id") Long idNM) {
		return valutaServis.izbrisiV(idNM);
	}
	
	@AutorizacijaAnnotation(imeMetode = "izmeniValutu")
	@PutMapping(path = "/izmeniValutu")
	public ResponseEntity<Valuta> izmeniV(@RequestBody(required = false) Valuta v) {
		return valutaServis.izmeniV(v);
	}
}
