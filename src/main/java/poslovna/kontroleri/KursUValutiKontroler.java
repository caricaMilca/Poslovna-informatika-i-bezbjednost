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
import poslovna.model.KursUValuti;
import poslovna.servisi.KursUValutiServis;

@RestController
@RequestMapping("/kursUValuti")
public class KursUValutiKontroler {

	@Autowired
	HttpSession sesija;
	
	@Autowired
	KursUValutiServis kursUValutiServis;

	@AutorizacijaAnnotation(imeMetode = "registracijaKursaUValuti")
	@PostMapping(path = "/registracijaKursaUValuti/{idValuteOsnovni}/{idValutePrema}/{idKursneListe}")
	public ResponseEntity<KursUValuti> registracijaKursaUValuti(@Valid @RequestBody KursUValuti kursUValuti,
			@PathVariable("idValuteOsnovni") Long idValuteOsnovni, @PathVariable("idValutePrema") Long idValutePrema,
			@PathVariable("idKursneListe") Long idKursneListe) {
		return kursUValutiServis.registracijaKursaUValuti(kursUValuti, idValuteOsnovni, idValutePrema, idKursneListe);

	}

	@AutorizacijaAnnotation(imeMetode = "sviKurseviUValuti")
	@GetMapping(path = "/sviKurseviUValuti")
	public ResponseEntity<List<KursUValuti>> sviKurseviUValuti() {
		return kursUValutiServis.sviKurseviUValuti();
	}

	@AutorizacijaAnnotation(imeMetode = "sviKurseviUValutiValute")
	@GetMapping(path = "/sviKurseviUValutiValute/{idValute}")
	public ResponseEntity<List<KursUValuti>> sviKurseviUValutiValute(@PathVariable("idValute") Long idValute) {
		return kursUValutiServis.sviKurseviUValutiValute(idValute);
	}
	
	@AutorizacijaAnnotation(imeMetode = "sviKurseviUValutiKursneListe")
	@GetMapping(path = "/sviKurseviUValutiKursneListe/{idKursneListe}")
	public ResponseEntity<List<KursUValuti>> sviKurseviUValutiKursneListe(@PathVariable("idKursneListe") Long idKursneListe) {
		return kursUValutiServis.sviKurseviUValutiKursneListe(idKursneListe);
	}


	@AutorizacijaAnnotation(imeMetode = "pretraziKursUValuti")
	@PutMapping(path = "/pretraziKursUValuti/{idValuteOsnovni}/{idValutePrema}/{idKursneListe}")
	public ResponseEntity<List<KursUValuti>> pretraziKursUValuti(@RequestBody(required = false) KursUValuti kursUValuti,
			@PathVariable("idValuteOsnovni") Long idValuteOsnovni, @PathVariable("idValutePrema") Long idValutePrema,
			@PathVariable("idKursneListe") Long idKursneListe) {
		return kursUValutiServis.pretraziKursUValuti(kursUValuti, idValuteOsnovni, idValutePrema, idKursneListe);

	}

}
