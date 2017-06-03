package poslovna.servisi;

import java.util.List;

import org.springframework.http.ResponseEntity;

import poslovna.model.Racun;

public interface RacunServis {

	ResponseEntity<Racun> registracijaRacuna(Racun racun, Long idKlijenta, Long idValute);

	ResponseEntity<List<Racun>> sviRacuni();

	ResponseEntity<List<Racun>> sviRacuniValute(Long idValute);

	ResponseEntity<List<Racun>> sviRacuniKlijenta(Long idKlijenta);

	ResponseEntity<List<Racun>> pretraziRacune(Racun racun, Long idKlijenta, Long idValute);

}
