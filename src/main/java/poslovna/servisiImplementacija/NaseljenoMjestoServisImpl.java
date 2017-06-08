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

import poslovna.model.NaseljenoMjesto;
import poslovna.model.NaseljenoMjesto;
import poslovna.model.Zaposleni;
import poslovna.repozitorijumi.DrzavaRepozitorijum;
import poslovna.repozitorijumi.NaseljenoMjestoRepozitorijum;
import poslovna.servisi.NaseljenoMjestoServis;

@Service
@Transactional
public class NaseljenoMjestoServisImpl implements NaseljenoMjestoServis {

	@Autowired
	NaseljenoMjestoRepozitorijum naseljenoMjestoRepozitorijum;

	@Autowired
	DrzavaRepozitorijum drzavaRepozitorijum;

	@Override
	public ResponseEntity<NaseljenoMjesto> registracijaNaseljenogMjesta(NaseljenoMjesto nm, Long idDrzave) {
		if (naseljenoMjestoRepozitorijum.findByPttOznaka(nm.pttOznaka).size() != 0)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		nm.drzava = drzavaRepozitorijum.findOne(idDrzave);
		return new ResponseEntity<NaseljenoMjesto>(naseljenoMjestoRepozitorijum.save(nm), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<NaseljenoMjesto>> svaNaseljenaMjesta() {
		return new ResponseEntity<List<NaseljenoMjesto>>(naseljenoMjestoRepozitorijum.findAll(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<NaseljenoMjesto>> svaNaseljenaMjestaDrzave(Long idDrzave) {
		return new ResponseEntity<List<NaseljenoMjesto>>(
				naseljenoMjestoRepozitorijum.findByDrzava(drzavaRepozitorijum.findOne(idDrzave)), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<NaseljenoMjesto>> pretraziNaseljenaMjesta(NaseljenoMjesto nm, Long idDrzave) {
		List<NaseljenoMjesto> k = naseljenoMjestoRepozitorijum.findAll();
		List<NaseljenoMjesto> lista = new ArrayList<NaseljenoMjesto>();
		List<NaseljenoMjesto> naziv = new ArrayList<NaseljenoMjesto>();
		List<NaseljenoMjesto> ptt = new ArrayList<NaseljenoMjesto>();
		List<NaseljenoMjesto> drzava = new ArrayList<NaseljenoMjesto>();
		if (nm == null && idDrzave == -1)
			return new ResponseEntity<List<NaseljenoMjesto>>(lista, HttpStatus.OK);
		if (nm != null) {
			if (nm.naziv != null) {
				naziv = naseljenoMjestoRepozitorijum.findByNaziv(nm.naziv);
				k.retainAll(naziv);
			}
			if (nm.pttOznaka != null) {
				ptt = naseljenoMjestoRepozitorijum.findByPttOznaka(nm.pttOznaka);
				k.retainAll(ptt);
			}
		}
		if (idDrzave != -1) {
			drzava = naseljenoMjestoRepozitorijum.findByDrzava(drzavaRepozitorijum.findOne(idDrzave));
			k.retainAll(drzava);
		}
		Set<NaseljenoMjesto> set = new HashSet<NaseljenoMjesto>();
		set.addAll(k);
		k.clear();
		k.addAll(set);
		return new ResponseEntity<List<NaseljenoMjesto>>(k, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<NaseljenoMjesto> izmjeniNM(NaseljenoMjesto nm, Long idDrzave) {
		// TODO Auto-generated method stub
		NaseljenoMjesto naseljenoMesto = naseljenoMjestoRepozitorijum.findOne(nm.id);

		if (nm.naziv != null)
			naseljenoMesto.naziv = nm.naziv;
		if (nm.pttOznaka != null)
			naseljenoMesto.pttOznaka = nm.pttOznaka;
		if (idDrzave != -1)
			naseljenoMesto.drzava = drzavaRepozitorijum.findOne(idDrzave);
		return new ResponseEntity<NaseljenoMjesto>(naseljenoMjestoRepozitorijum.save(naseljenoMesto), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> izbrisiNM(Long idNM) {
		// TODO Auto-generated method stub
		naseljenoMjestoRepozitorijum.delete(idNM);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
