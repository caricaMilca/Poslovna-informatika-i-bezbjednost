package poslovna.servisi;

import java.util.List;

import org.springframework.http.ResponseEntity;

import poslovna.model.NaseljenoMjesto;
import poslovna.model.Valuta;

public interface ValutaServis {

	Valuta preuzmiValutu(Long idvalute);

	ResponseEntity<Valuta> registracijaValuta(Valuta valuta);

	ResponseEntity<List<Valuta>> sveValute();

	ResponseEntity<List<Valuta>> pretraziValute(Valuta valuta);

	ResponseEntity<?> izbrisiV(Long id);
	
	ResponseEntity<Valuta> izmeniV(Valuta v);
}
