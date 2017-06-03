package poslovna.servisiImplementacija;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.model.Djelatnost;
import poslovna.repozitorijumi.DjelatnostRepozitorijum;
import poslovna.servisi.DjelatnostServis;

@Service
@Transactional
public class DjelatnostServisImpl implements DjelatnostServis {

	@Autowired
	DjelatnostRepozitorijum djelatnostRepozitorijum;
	
	@Override
	public ResponseEntity<Djelatnost> registracijaDjelatnosti(Djelatnost djelatnost) {
		if(djelatnostRepozitorijum.findBySifra(djelatnost.sifra) != null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		else 
			return new ResponseEntity<Djelatnost>(djelatnostRepozitorijum.save(djelatnost), HttpStatus.CREATED);
			
	}

	@Override
	public ResponseEntity<List<Djelatnost>> sveDjelatnosti() {
		return new ResponseEntity<List<Djelatnost>>(djelatnostRepozitorijum.findAll(), HttpStatus.OK);
	}

	@Override
	public Djelatnost preuzmiDjelatnost(Long idDjelatnosti) {
		return djelatnostRepozitorijum.findOne(idDjelatnosti);
	}

	@Override
	public ResponseEntity<List<Djelatnost>> pretraziDjelantosti(Djelatnost djelatnost) {
		if(djelatnost.naziv == null)
			djelatnost.naziv = "%";
		else 
			djelatnost.naziv = "%" + djelatnost.naziv + "%";
		if(djelatnost.sifra == null)
			djelatnost.sifra = "%";
		else 
			djelatnost.sifra = "%" + djelatnost.sifra + "%";
		return new ResponseEntity<List<Djelatnost>>(djelatnostRepozitorijum.findByNazivLikeOrSifraLike(djelatnost.naziv, djelatnost.sifra), HttpStatus.OK);
	}

}
