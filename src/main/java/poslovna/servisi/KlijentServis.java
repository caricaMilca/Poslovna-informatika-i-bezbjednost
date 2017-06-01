package poslovna.servisi;

import java.util.List;

import org.springframework.http.ResponseEntity;

import poslovna.model.Klijent;


public interface KlijentServis {

	ResponseEntity<Klijent> registracijaKlijenta(Klijent k);
	
	ResponseEntity<Klijent> registracijaKlijentaF(Klijent k);

	Klijent preuzmiKlijenta(Long id);

	ResponseEntity<List<Klijent>> sviKlijenti();

	ResponseEntity<List<Klijent>> sviKlijentiDjelatnosti(Long idDjelatnosti);

	ResponseEntity<List<Klijent>> pretraziKlijente(Klijent klijent, Long idDjelatnosti);


}
