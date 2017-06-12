package poslovna.servisi;

import java.util.List;

import org.springframework.http.ResponseEntity;

import poslovna.model.AnalitikaIzvoda;

public interface AnalitikaIzvodaServis {

	ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvoda();

	ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvodaDnevnog(Long idDnevnogStanjaRacuna);

	ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvodaValute(Long idValute);

	ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvodaTipaPlacanja(Long idTipaPlacanja);

	ResponseEntity<List<AnalitikaIzvoda>> pretraziAnalitikeIzvoda(AnalitikaIzvoda analitikaIzvoda, Long idDnevnogStanjaRacuna,
			Long idValute, Long idTipaPlacanja);

	ResponseEntity<AnalitikaIzvoda> uplataNaRacun(AnalitikaIzvoda analitikaIzvoda, String sifraValute,
			Long idTipaPlacanja);

	ResponseEntity<AnalitikaIzvoda> isplataSaRacuna(AnalitikaIzvoda analitikaIzvoda, String sifraValute,
			Long idTipaPlacanja);

	ResponseEntity<AnalitikaIzvoda> transferSredstava(AnalitikaIzvoda analitikaIzvoda, String sifraValute,
			Long idTipaPlacanja);

}
