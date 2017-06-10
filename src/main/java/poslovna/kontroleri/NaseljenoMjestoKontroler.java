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
import poslovna.model.NaseljenoMjesto;
import poslovna.servisi.NaseljenoMjestoServis;

@RestController
@RequestMapping("/naseljenoMjesto")
public class NaseljenoMjestoKontroler {

	@Autowired
	NaseljenoMjestoServis naseljenoMjestoServis;

	@Autowired
	HttpSession sesija;

	@AutorizacijaAnnotation(imeMetode = "registracijaNaseljenogMjesta")
	@PostMapping(path = "/registracijaNaseljenogMjesta/{idDrzave}")
	public ResponseEntity<NaseljenoMjesto> registracijaNaseljenoMjesto(@Valid @RequestBody NaseljenoMjesto nm,
			@PathVariable("idDrzave") Long idDrzave) {
		return naseljenoMjestoServis.registracijaNaseljenogMjesta(nm, idDrzave);

	}

	@AutorizacijaAnnotation(imeMetode = "svaNaseljenaMjesta")
	@GetMapping(path = "/svaNaseljenaMjesta")
	public ResponseEntity<List<NaseljenoMjesto>> svaNaseljenaMjesta() {
		return naseljenoMjestoServis.svaNaseljenaMjesta();
	}

	@AutorizacijaAnnotation(imeMetode = "svaNaseljenaMjestaDrzave")
	@GetMapping(path = "/svaNaseljenaMjestaDrzave/{idDrzave}")
	public ResponseEntity<List<NaseljenoMjesto>> svaNaseljenaMjesta(@PathVariable("idDrzave") Long idDrzave) {
		System.out.println("aaaaaaaaaaaaa " + idDrzave);
		return naseljenoMjestoServis.svaNaseljenaMjestaDrzave(idDrzave);
	}

	@AutorizacijaAnnotation(imeMetode = "pretraziNaseljenaMjesta")
	@PutMapping(path = "/pretraziNaseljenaMjesta/{idDrzave}")
	public ResponseEntity<List<NaseljenoMjesto>> pretraziNaseljenaMjesta(
			@RequestBody(required = false) NaseljenoMjesto nm, @PathVariable("idDrzave") Long idDrzave) {
		return naseljenoMjestoServis.pretraziNaseljenaMjesta(nm, idDrzave);
	}
	
	@AutorizacijaAnnotation(imeMetode = "izbrisiNaseljenoMjesto")
	@PutMapping(path = "/izbrisiNaseljenoMjesto/{idNM}")
	public ResponseEntity<?> izbrisiNM(@PathVariable("idNM") Long idNM) {
		return naseljenoMjestoServis.izbrisiNM(idNM);
	}
	
	@AutorizacijaAnnotation(imeMetode = "izmjeniNaseljenoMjesto")
	@PutMapping(path = "/izmjeniNaseljenoMjesto/{idDrzave}")
	public ResponseEntity<NaseljenoMjesto> izmjeniNaseljenoMjesto(@RequestBody(required = false) NaseljenoMjesto nm, @PathVariable("idDrzave") Long idDrzave) {
		return naseljenoMjestoServis.izmjeniNM(nm, idDrzave);
	}
}
