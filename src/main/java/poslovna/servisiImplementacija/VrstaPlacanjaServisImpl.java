package poslovna.servisiImplementacija;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.model.VrstaPlacanja;
import poslovna.repozitorijumi.VrstaPlacanjaRepozitorijum;
import poslovna.servisi.VrstaPlacanjaServis;

@Service
@Transactional
public class VrstaPlacanjaServisImpl implements VrstaPlacanjaServis {

	@Autowired
	VrstaPlacanjaRepozitorijum vrstaPlacanjaRepozitorijum;

	@Override
	public ResponseEntity<VrstaPlacanja> registracijaVrstePlacanja(VrstaPlacanja vrstaPlacanja) {
		if(vrstaPlacanjaRepozitorijum.findByNaziv(vrstaPlacanja.naziv) != null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<VrstaPlacanja>(vrstaPlacanjaRepozitorijum.save(vrstaPlacanja), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<List<VrstaPlacanja>> sveVrstePlacanja() {
		return new ResponseEntity<List<VrstaPlacanja>>(vrstaPlacanjaRepozitorijum.findAll(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> izbrisiVrstuPlacanja(Long id) {
		vrstaPlacanjaRepozitorijum.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<VrstaPlacanja> izmjeniVrstuPlacanja(VrstaPlacanja vrstaPlacanja) {
		VrstaPlacanja v = vrstaPlacanjaRepozitorijum.findOne(vrstaPlacanja.id);
		if(vrstaPlacanjaRepozitorijum.findByNaziv(vrstaPlacanja.naziv) != null && v.naziv.equals(vrstaPlacanja.naziv))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		v.naziv = vrstaPlacanja.naziv;
		return new ResponseEntity<VrstaPlacanja>(vrstaPlacanjaRepozitorijum.save(v), HttpStatus.OK);
	}

}
