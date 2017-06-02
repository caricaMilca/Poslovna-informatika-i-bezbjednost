package poslovna.kontroleri;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poslovna.servisi.BankaServis;

@RestController
@RequestMapping("/banka")
public class BankaKontroler {

	@Autowired
	BankaServis bankaServis;

	/*@AutorizacijaAnnotation(imeMetode = "registracijaBanke")
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
	public ResponseEntity<List<KursUValuti>> pretraziKursUValuti(@RequestBody KursUValuti kursUValuti,
			@PathVariable("idValuteOsnovni") Long idValuteOsnovni, @PathVariable("idValutePrema") Long idValutePrema,
			@PathVariable("idKursneListe") Long idKursneListe) {
		return kursUValutiServis.pretraziKursUValuti(kursUValuti, idValuteOsnovni, idValutePrema, idKursneListe);

	}*/

}
