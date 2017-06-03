package poslovna.servisi;

import java.util.List;

import org.springframework.http.ResponseEntity;

import poslovna.model.DnevnoStanjeRacuna;

public interface DnevnoStanjeRacunaServis {

	ResponseEntity<DnevnoStanjeRacuna> registracijaDnevnogStanjaRacuna(DnevnoStanjeRacuna dnevnoStanjeRacuna,
			Long idRacuna);

	ResponseEntity<List<DnevnoStanjeRacuna>> svaDnevnaStanjeRacuna();

	ResponseEntity<List<DnevnoStanjeRacuna>> svaDnevnaStanjeRacunaDatog(Long idRacuna);

	ResponseEntity<List<DnevnoStanjeRacuna>> pretraziDnevnaStanjeRacuna(DnevnoStanjeRacuna dnevnoStanjeRacuna,
			Long idRacuna);

}
