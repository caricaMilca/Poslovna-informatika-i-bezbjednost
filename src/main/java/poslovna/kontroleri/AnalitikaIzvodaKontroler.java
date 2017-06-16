package poslovna.kontroleri;

import java.util.Calendar;
import java.util.Date;
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

import poslovna.autorizacija.AutorizacijaAnnotation;
import poslovna.model.AnalitikaIzvoda;
import poslovna.model.TipTransakcije;
import poslovna.servisi.AnalitikaIzvodaServis;

@RestController
@RequestMapping("/analitikaIzvoda")
public class AnalitikaIzvodaKontroler {

	@Autowired
	HttpSession sesija;

	@Autowired
	AnalitikaIzvodaServis analitikaIzvodaServis;

	@AutorizacijaAnnotation(imeMetode = "transakcija")
	@PostMapping(path = "/transakcija/{sifraValute}/{idTipaPlacanja}")
	public ResponseEntity<AnalitikaIzvoda> transakcija(@Valid @RequestBody AnalitikaIzvoda analitikaIzvoda,
			@PathVariable("sifraValute") String sifraValute, @PathVariable("idTipaPlacanja") Long idTipaPlacanja) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date today = c.getTime();
		if (analitikaIzvoda.datumPrimanja.before(new Date()) && !(analitikaIzvoda.datumPrimanja.equals(today))) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if (analitikaIzvoda.tipTransakcije == TipTransakcije.UPLATA)
			return analitikaIzvodaServis.uplataNaRacun(analitikaIzvoda, sifraValute, idTipaPlacanja);
		else if (analitikaIzvoda.tipTransakcije == TipTransakcije.ISPLATA)
			return analitikaIzvodaServis.isplataSaRacuna(analitikaIzvoda, sifraValute, idTipaPlacanja);
		else
			return analitikaIzvodaServis.transferSredstava(analitikaIzvoda, sifraValute, idTipaPlacanja);
	}

	@AutorizacijaAnnotation(imeMetode = "sveAnalitikeIzvoda")
	@GetMapping(path = "/sveAnalitikeIzvoda")
	public ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvoda() {
		return analitikaIzvodaServis.sveAnalitikeIzvoda();
	}
	
	@AutorizacijaAnnotation(imeMetode = "sveAnalitikePrenosa")
	@GetMapping(path = "/sveAnalitikePrenosa/{id}")
	public ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikePrenosa(@PathVariable("id") Long id) {
		return analitikaIzvodaServis.sveAnalitikePrenosa(id);
	}

	@AutorizacijaAnnotation(imeMetode = "sveAnalitikeIzvodaDnevnog")
	@GetMapping(path = "/sveAnalitikeIzvodaDnevnog/{idRacuna}")
	public ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvodaDnevnog(
			@PathVariable("idDnevnogStanjaRacuna") Long idDnevnogStanjaRacuna) {
		return analitikaIzvodaServis.sveAnalitikeIzvodaDnevnog(idDnevnogStanjaRacuna);
	}

	@AutorizacijaAnnotation(imeMetode = "sveAnalitikeIzvodaValute")
	@GetMapping(path = "/sveAnalitikeIzvodaValute/{idValute}")
	public ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvodaValute(@PathVariable("idValute") Long idValute) {
		return analitikaIzvodaServis.sveAnalitikeIzvodaValute(idValute);
	}

	@AutorizacijaAnnotation(imeMetode = "sveAnalitikeIzvodaTipaPlacanja")
	@GetMapping(path = "/sveAnalitikeIzvodaTipaPlacanja/{idTipaPlacanja}")
	public ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvodaTipaPlacanja(
			@PathVariable("idTipaPlacanja") Long idTipaPlacanja) {
		return analitikaIzvodaServis.sveAnalitikeIzvodaTipaPlacanja(idTipaPlacanja);
	}

	@AutorizacijaAnnotation(imeMetode = "pretraziAnalitikeIzvoda")
	@PutMapping(path = "/pretraziAnalitikeIzvoda")
	public ResponseEntity<List<AnalitikaIzvoda>> pretraziAnalitikeIzvoda(
			@Valid @RequestBody(required = false) AnalitikaIzvoda analitikaIzvoda) {
		return analitikaIzvodaServis.pretraziAnalitikeIzvoda(analitikaIzvoda);
	}
}
