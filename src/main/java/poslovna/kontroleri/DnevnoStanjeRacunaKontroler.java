package poslovna.kontroleri;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poslovna.autorizacija.AutorizacijaAnnotation;
import poslovna.model.DnevnoStanjeRacuna;
import poslovna.servisi.DnevnoStanjeRacunaServis;

@RestController
@RequestMapping("/dnevnoStanjeRacuna")
public class DnevnoStanjeRacunaKontroler {

	@Autowired
	HttpSession sesija;

	@Autowired
	DnevnoStanjeRacunaServis dnevnoStanjeRacunaServis;

	@AutorizacijaAnnotation(imeMetode = "registracijaDnevnogStanjaRacuna")
	@PostMapping(path = "/registracijaDnevnogStanjaRacuna/{idRacuna}")
	public ResponseEntity<DnevnoStanjeRacuna> registracijaDnevnogStanjaRacuna(
			@Valid @RequestBody DnevnoStanjeRacuna dnevnoStanjeRacuna, @PathVariable("idRacuna") Long idRacuna) {
		return dnevnoStanjeRacunaServis.registracijaDnevnogStanjaRacuna(dnevnoStanjeRacuna, idRacuna);

	}

	@AutorizacijaAnnotation(imeMetode = "svaDnevnaStanjeRacuna")
	@GetMapping(path = "/svaDnevnaStanjeRacuna")
	public ResponseEntity<List<DnevnoStanjeRacuna>> svaDnevnaStanjeRacuna() {
		return dnevnoStanjeRacunaServis.svaDnevnaStanjeRacuna();
	}

	@AutorizacijaAnnotation(imeMetode = "svaDnevnaStanjeRacunaDatog")
	@GetMapping(path = "/svaDnevnaStanjeRacunaDatog/{idRacuna}")
	public ResponseEntity<List<DnevnoStanjeRacuna>> svaDnevnaStanjaRacunaDatog(
			@PathVariable("idRacuna") Long idRacuna) {
		return dnevnoStanjeRacunaServis.svaDnevnaStanjeRacunaDatog(idRacuna);
	}

	@AutorizacijaAnnotation(imeMetode = "pretraziDnevnaStanjeRacuna")
	@GetMapping(path = "/pretraziDnevnaStanjeRacuna/{idRacuna}")
	public ResponseEntity<List<DnevnoStanjeRacuna>> pretraziDnevnaStanjeRacuna(
			@RequestBody(required = false) DnevnoStanjeRacuna dnevnoStanjeRacuna,
			@PathVariable("idRacuna") Long idRacuna) {
		return dnevnoStanjeRacunaServis.pretraziDnevnaStanjeRacuna(dnevnoStanjeRacuna, idRacuna);
	}
}
