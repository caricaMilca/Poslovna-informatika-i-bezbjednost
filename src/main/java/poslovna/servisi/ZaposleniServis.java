package poslovna.servisi;

import org.springframework.http.ResponseEntity;

import poslovna.model.Zaposleni;

public interface ZaposleniServis {

	ResponseEntity<Zaposleni> registracijaSalteruse(Zaposleni z);

	Zaposleni preuzmiZaposlenog(Long id);
	
}
