package poslovna.kontroleri;

import java.util.List;

import javax.servlet.http.HttpSession;

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
import poslovna.model.Racun;
import poslovna.servisi.RacunServis;

@RestController
@RequestMapping("/racun")
public class RacunKontroler {

	@Autowired
	HttpSession sesija;
	
	@Autowired
	RacunServis racunServis;
	
	@AutorizacijaAnnotation(imeMetode = "registracijaRacuna")
	@PostMapping(path = "/registracijaRacuna/{idKlijenta}/{idValute}")
	public ResponseEntity<Racun> registracijaRacuna(
			@PathVariable("idKlijenta") Long idKlijenta, @PathVariable("idValute") Long idValute) {
		return racunServis.registracijaRacuna(idKlijenta, idValute);
	}

	@AutorizacijaAnnotation(imeMetode = "sviRacuni")
	@GetMapping(path = "/sviRacuni")
	public ResponseEntity<List<Racun>> sviRacuni() {
		return racunServis.sviRacuni();
	}

	@AutorizacijaAnnotation(imeMetode = "sviRacuniValute")
	@GetMapping(path = "/sviRacuniValute/{idValute}")
	public ResponseEntity<List<Racun>> sviRacuniValute(@PathVariable("idValute") Long idValute) {
		return racunServis.sviRacuniValute(idValute);
	}
	
	@AutorizacijaAnnotation(imeMetode = "sviRacuniKlijenta")
	@GetMapping(path = "/sviRacuniKlijenta/{idKlijenta}")
	public ResponseEntity<List<Racun>> sviRacuniKlijenta(@PathVariable("idKlijenta") Long idKlijenta) {
		return racunServis.sviRacuniKlijenta(idKlijenta);
	}
	
	@AutorizacijaAnnotation(imeMetode = "sveAnalitikeRacuna")
	@GetMapping(path = "/sveAnalitikeRacuna/{idRacuna}")
	public ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeRacuna(@PathVariable("idRacuna") Long idRacuna) {
			System.out.println("safasfasfasfas");
		return racunServis.sveAnalitikeRacuna(idRacuna);
	}

	@AutorizacijaAnnotation(imeMetode = "pretraziRacune")
	@PutMapping(path = "/pretraziRacune/{idKlijenta}/{idValute}")
	public ResponseEntity<List<Racun>> pretraziRacune(
			@RequestBody(required = false) Racun racun, @PathVariable("idKlijenta") Long idKlijenta, @PathVariable("idValute") Long idValute) {
		return racunServis.pretraziRacune(racun, idKlijenta, idValute);
	}
	
	@AutorizacijaAnnotation(imeMetode = "zatvoriRacun")
	@PutMapping(path = "/zatvoriRacun")
	public ResponseEntity<Racun> zatvoriRacun(
			@RequestBody Racun racun) {
		return racunServis.zatvoriRacun(racun);
	}

}
