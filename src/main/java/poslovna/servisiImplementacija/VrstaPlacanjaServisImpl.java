package poslovna.servisiImplementacija;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.model.Korisnik;
import poslovna.model.VrstaPlacanja;
import poslovna.repozitorijumi.VrstaPlacanjaRepozitorijum;
import poslovna.servisi.VrstaPlacanjaServis;

@Service
@Transactional
public class VrstaPlacanjaServisImpl implements VrstaPlacanjaServis {

	@Autowired
	VrstaPlacanjaRepozitorijum vrstaPlacanjaRepozitorijum;

	@Autowired
	HttpSession sesija;
	
	final static Logger logger = Logger.getLogger(VrstaPlacanjaServisImpl.class);
	
	@Override
	public ResponseEntity<VrstaPlacanja> registracijaVrstePlacanja(VrstaPlacanja vrstaPlacanja) {
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		if(vrstaPlacanjaRepozitorijum.findByNaziv(vrstaPlacanja.naziv) != null){
			logger.info("Korisnik " + k.korisnickoIme + " neuspesno pokusao da registruje vrstu placanja.");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}
		logger.info("Korisnik " + k.korisnickoIme + " uspesno registrovao vrstu placanja " + vrstaPlacanja.naziv+ ".");
		return new ResponseEntity<VrstaPlacanja>(vrstaPlacanjaRepozitorijum.save(vrstaPlacanja), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<List<VrstaPlacanja>> sveVrstePlacanja() {
		return new ResponseEntity<List<VrstaPlacanja>>(vrstaPlacanjaRepozitorijum.findAll(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> izbrisiVrstuPlacanja(Long id) {
		vrstaPlacanjaRepozitorijum.delete(id);
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		logger.info("Korisnik " + k.korisnickoIme + " uspesno izbrisao vrstu placanja " + vrstaPlacanjaRepozitorijum.findOne(id).naziv + ".");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<VrstaPlacanja> izmjeniVrstuPlacanja(VrstaPlacanja vrstaPlacanja) {
		VrstaPlacanja v = vrstaPlacanjaRepozitorijum.findOne(vrstaPlacanja.id);
		if(vrstaPlacanjaRepozitorijum.findByNaziv(vrstaPlacanja.naziv) != null && v.naziv.equals(vrstaPlacanja.naziv))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		v.naziv = vrstaPlacanja.naziv;
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		logger.info("Korisnik " + k.korisnickoIme + " uspesno izmenio polja vrste placanja" + vrstaPlacanja.naziv + ".");
		return new ResponseEntity<VrstaPlacanja>(vrstaPlacanjaRepozitorijum.save(v), HttpStatus.OK);
	}

}
