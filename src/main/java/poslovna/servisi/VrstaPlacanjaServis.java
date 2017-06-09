package poslovna.servisi;

import java.util.List;

import org.springframework.http.ResponseEntity;

import poslovna.model.VrstaPlacanja;

public interface VrstaPlacanjaServis {

	ResponseEntity<VrstaPlacanja> registracijaVrstePlacanja(VrstaPlacanja vrstaPlacanja);

	ResponseEntity<List<VrstaPlacanja>> sveVrstePlacanja();

	ResponseEntity<?> izbrisiVrstuPlacanja(Long id);

	ResponseEntity<VrstaPlacanja> izmjeniVrstuPlacanja(VrstaPlacanja vrstaPlacanja);

}
