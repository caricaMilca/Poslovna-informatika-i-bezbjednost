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
import poslovna.model.AnalitikaIzvoda;
import poslovna.servisi.AnalitikaIzvodaServis;

@RestController
@RequestMapping("/analitikaIzvoda")
public class AnalitikaIzvodaKontroler {

	@Autowired
	HttpSession sesija;

	@Autowired
	AnalitikaIzvodaServis analitikaIzvodaServis;

	@AutorizacijaAnnotation(imeMetode = "registracijaAnalitikeIzvoda")
	@PostMapping(path = "/registracijaAnalitikeIzvoda/{idDnevnogStanjaRacuna}/{idValute}/{idTipaPlacanja}")
	public ResponseEntity<AnalitikaIzvoda> registracijaAnalitikeIzvoda(
			@Valid @RequestBody AnalitikaIzvoda analitikaIzvoda,
			@PathVariable("idDnevnogStanjaRacuna") Long idDnevnogStanjaRacuna, @PathVariable("idValute") Long idValute,
			@PathVariable("idTipaPlacanja") Long idTipaPlacanja) {
		return analitikaIzvodaServis.registracijaAnalitikeIzvoda(analitikaIzvoda, idDnevnogStanjaRacuna, idValute,
				idTipaPlacanja);

	}

	@AutorizacijaAnnotation(imeMetode = "sveAnalitikeIzvoda")
	@GetMapping(path = "/sveAnalitikeIzvoda")
	public ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvoda() {
		return analitikaIzvodaServis.sveAnalitikeIzvoda();
	}

	@AutorizacijaAnnotation(imeMetode = "sveAnalitikeIzvodaDnevnog")
	@GetMapping(path = "/sveAnalitikeIzvodaDnevnog/{idRacuna}")
	public ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvodaDnevnog(
			@PathVariable("idDnevnogStanjaRacuna") Long idDnevnogStanjaRacuna) {
		return analitikaIzvodaServis.sveAnalitikeIzvodaDnevnog(idDnevnogStanjaRacuna);
	}
	
	@AutorizacijaAnnotation(imeMetode = "sveAnalitikeIzvodaValute")
	@GetMapping(path = "/sveAnalitikeIzvodaValute/{idValute}")
	public ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvodaValute(
			@PathVariable("idValute") Long idValute) {
		return analitikaIzvodaServis.sveAnalitikeIzvodaValute(idValute);
	}

	@AutorizacijaAnnotation(imeMetode = "sveAnalitikeIzvodaTipaPlacanja")
	@GetMapping(path = "/sveAnalitikeIzvodaTipaPlacanja/{idTipaPlacanja}")
	public ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvodaTipaPlacanja(
			@PathVariable("idTipaPlacanja") Long idTipaPlacanja) {
		return analitikaIzvodaServis.sveAnalitikeIzvodaTipaPlacanja(idTipaPlacanja);
	}

	
	@AutorizacijaAnnotation(imeMetode = "pretraziAnalitikeIzvoda")
	@PutMapping(path = "/pretraziAnalitikeIzvoda/{idDnevnogStanjaRacuna}/{idValute}/{idTipaPlacanja}")
	public ResponseEntity<List<AnalitikaIzvoda>> pretraziAnalitikeIzvoda(
			@Valid @RequestBody (required = false) AnalitikaIzvoda analitikaIzvoda,
			@PathVariable("idDnevnogStanjaRacuna") Long idDnevnogStanjaRacuna, @PathVariable("idValute") Long idValute,
			@PathVariable("idTipaPlacanja") Long idTipaPlacanja) {
		return analitikaIzvodaServis.pretraziAnalitikeIzvoda(analitikaIzvoda, idDnevnogStanjaRacuna, idValute,
				idTipaPlacanja);

	}
}
