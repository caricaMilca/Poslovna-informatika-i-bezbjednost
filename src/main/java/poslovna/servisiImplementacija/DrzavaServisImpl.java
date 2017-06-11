package poslovna.servisiImplementacija;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	public ResponseEntity<Drzava> registracijaDrzave(Drzava drzava, Long idValute) {
		if (drzavaRepozitorijum.findBySifra(drzava.sifra).size() != 0)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		drzava.valuta = valutaRepozitorijum.findOne(idValute);
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
		List<Drzava> d = drzavaRepozitorijum.findAll();
		List<Drzava> lista = new ArrayList<Drzava>();
		List<Drzava> naziv = new ArrayList<Drzava>();
		List<Drzava> oznaka = new ArrayList<Drzava>();
		List<Drzava> valuta = new ArrayList<Drzava>();
		if (drzava == null && idValute == -1)
			return new ResponseEntity<List<Drzava>>(lista, HttpStatus.OK);
		if (drzava != null) {
			if (drzava.naziv != null) {
				naziv = drzavaRepozitorijum.findByNaziv(drzava.naziv);
				d.retainAll(naziv);
			}
			if (drzava.sifra != null) {
				oznaka = drzavaRepozitorijum.findBySifra(drzava.sifra);
				d.retainAll(oznaka);
			}
		}
		if (idValute != -1) {
			valuta = drzavaRepozitorijum.findByValuta(valutaRepozitorijum.findOne(idValute));
			d.retainAll(valuta);
		}
		Set<Drzava> set = new HashSet<Drzava>();
		set.addAll(d);
		d.clear();
		d.addAll(set);
		return new ResponseEntity<List<Drzava>>(d, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> izbrisiDrzavu(Long idDrzavu) {
		drzavaRepozitorijum.delete(idDrzavu);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Drzava> izmeniDrzavu(Drzava drzava, Long idValute) {
		// TODO Auto-generated method stub
		Drzava d = drzavaRepozitorijum.findOne(drzava.id);

		if (drzava.naziv != null)
			d.naziv = drzava.naziv;
		if (drzava.sifra != null)
			d.sifra = drzava.sifra;
		if (idValute != -1)
			d.valuta = valutaRepozitorijum.findOne(idValute);
		return new ResponseEntity<Drzava>(drzavaRepozitorijum.save(d), HttpStatus.OK);
}
	
}
