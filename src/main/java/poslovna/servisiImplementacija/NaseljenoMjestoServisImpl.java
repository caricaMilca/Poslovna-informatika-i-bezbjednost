package poslovna.servisiImplementacija;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.model.NaseljenoMjesto;
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
		if (naseljenoMjestoRepozitorijum.findByPttOznaka(nm.pttOznaka) != null)
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
		if(nm == null) {
			return new ResponseEntity<List<NaseljenoMjesto>>(naseljenoMjestoRepozitorijum.findByDrzava(drzavaRepozitorijum.findOne(idDrzave)), HttpStatus.OK);
		} else {
		if (nm.naziv == null)
			nm.naziv = "%";
		else
			nm.naziv = "%" + nm.naziv + "%";
		if(nm.pttOznaka == null)
			nm.pttOznaka = "%";
		else
			nm.pttOznaka = "%" + nm.pttOznaka + "%";
		if(idDrzave == -1)
			return new ResponseEntity<List<NaseljenoMjesto>>(naseljenoMjestoRepozitorijum.findByNazivLikeOrPttOznakaLike(nm.naziv, nm.pttOznaka), HttpStatus.OK);
		else 
			return new ResponseEntity<List<NaseljenoMjesto>>(naseljenoMjestoRepozitorijum.findByNazivLikeOrPttOznakaLikeOrDrzava(nm.naziv, nm.pttOznaka, drzavaRepozitorijum.findOne(idDrzave)), HttpStatus.OK);

	}}

}
