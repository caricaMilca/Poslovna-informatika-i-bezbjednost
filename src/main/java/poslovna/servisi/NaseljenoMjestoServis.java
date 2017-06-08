package poslovna.servisi;

import java.util.List;

import org.springframework.http.ResponseEntity;

import poslovna.model.NaseljenoMjesto;

public interface NaseljenoMjestoServis {

	ResponseEntity<NaseljenoMjesto> registracijaNaseljenogMjesta(NaseljenoMjesto nm, Long idDrzave);

	ResponseEntity<List<NaseljenoMjesto>> svaNaseljenaMjesta();

	ResponseEntity<List<NaseljenoMjesto>> svaNaseljenaMjestaDrzave(Long idDrzave);

	ResponseEntity<List<NaseljenoMjesto>> pretraziNaseljenaMjesta(NaseljenoMjesto nm, Long idDrzave);

	ResponseEntity<NaseljenoMjesto> izmjeniNM(NaseljenoMjesto nm, Long idNM);

	ResponseEntity<?> izbrisiNM(Long idNM);
	
}
