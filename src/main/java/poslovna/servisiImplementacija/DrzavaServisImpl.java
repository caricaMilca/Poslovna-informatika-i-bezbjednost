package poslovna.servisiImplementacija;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.model.Drzava;
import poslovna.repozitorijumi.DrzavaRepozitorijum;
import poslovna.repozitorijumi.ValutaRepozitorijum;
import poslovna.servisi.DrzavaServis;

@Service
@Transactional
public class DrzavaServisImpl implements DrzavaServis {

	@Autowired
	DrzavaRepozitorijum drzavaRepozitorijum;

	@Autowired
	ValutaRepozitorijum valutaRepozitorijum;

	@Override
	public ResponseEntity<Drzava> registracijaDrzave(Drzava drzava) {
		if (drzavaRepozitorijum.findBySifra(drzava.sifra) != null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<Drzava>(drzavaRepozitorijum.save(drzava), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<List<Drzava>> sveDrzave() {
		return new ResponseEntity<List<Drzava>>(drzavaRepozitorijum.findAll(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Drzava>> sveDrzaveValute(Long idValute) {
		return new ResponseEntity<List<Drzava>>(drzavaRepozitorijum.findByValuta(valutaRepozitorijum.findOne(idValute)),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Drzava>> pretraziDrzave(Drzava drzava, Long idValute) {
		if (drzava == null)
			return new ResponseEntity<List<Drzava>>(
					drzavaRepozitorijum.findByValuta(valutaRepozitorijum.findOne(idValute)), HttpStatus.OK);
		else {
			if (drzava.naziv == null)
				drzava.naziv = "%";
			else
				drzava.naziv = "%" + drzava.naziv + "%";
			if (drzava.sifra == null)
				drzava.sifra = "%";
			else
				drzava.sifra = "%" + drzava.sifra + "%";
			if (idValute == -1)
				return new ResponseEntity<List<Drzava>>(
						drzavaRepozitorijum.findByNazivLikeOrSifraLike(drzava.naziv, drzava.sifra), HttpStatus.OK);
			else
				return new ResponseEntity<List<Drzava>>(drzavaRepozitorijum.findByNazivLikeOrSifraLikeOrValuta(
						drzava.naziv, drzava.sifra, valutaRepozitorijum.findOne(idValute)), HttpStatus.OK);
		}
	}

}
