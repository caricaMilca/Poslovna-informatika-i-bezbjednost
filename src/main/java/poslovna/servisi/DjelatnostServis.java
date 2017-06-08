package poslovna.servisi;

import java.util.List;

import org.springframework.http.ResponseEntity;

import poslovna.model.Djelatnost;

public interface DjelatnostServis {

	ResponseEntity<Djelatnost> registracijaDjelatnosti(Djelatnost djelatnost);

	ResponseEntity<List<Djelatnost>> sveDjelatnosti();

	Djelatnost preuzmiDjelatnost(Long idDjelatnosti);

	ResponseEntity<List<Djelatnost>> pretraziDjelantosti(Djelatnost djelatnost);

	ResponseEntity<?> izbrisiDjelatnost(Long idDjelatnosti);

	ResponseEntity<Djelatnost> izmjeniDjelatnost(Djelatnost dje);
}
