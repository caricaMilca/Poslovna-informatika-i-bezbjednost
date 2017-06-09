package poslovna.servisi;

import java.util.List;

import org.springframework.http.ResponseEntity;

import poslovna.model.Drzava;

public interface DrzavaServis {

	ResponseEntity<List<Drzava>> sveDrzave();

	ResponseEntity<List<Drzava>> sveDrzaveValute(Long idValute);

	ResponseEntity<List<Drzava>> pretraziDrzave(Drzava drzava, Long idValute);

	ResponseEntity<Drzava> registracijaDrzave(Drzava drzava, Long idValute);

	ResponseEntity<?> izbrisiDrzavu(Long idDrzave);

	ResponseEntity<Drzava> izmeniDrzavu(Drzava drzava, Long idValute);

}
