package poslovna.servisi;

import java.util.List;

import org.springframework.http.ResponseEntity;

import poslovna.model.Drzava;

public interface DrzavaServis {

	ResponseEntity<Drzava> registracijaDrzave(Drzava drzava);

	ResponseEntity<List<Drzava>> sveDrzave();

	ResponseEntity<List<Drzava>> sveDrzaveValute(Long idValute);

	ResponseEntity<List<Drzava>> pretraziDrzave(Drzava drzava, Long idValute);

}
