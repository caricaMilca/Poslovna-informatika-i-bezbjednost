package poslovna.servisiImplementacija;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.model.Banka;
import poslovna.model.Racun;
import poslovna.model.Zaposleni;
import poslovna.repozitorijumi.KlijentRepozitorijum;
import poslovna.repozitorijumi.RacunRepozitorijum;
import poslovna.repozitorijumi.ValutaRepozitorijum;
import poslovna.servisi.RacunServis;

@Service
@Transactional
public class RacunServisImpl implements RacunServis {

	@Autowired
	HttpSession sesija;

	@Autowired
	KlijentRepozitorijum klijentRepozitorijum;

	@Autowired
	ValutaRepozitorijum valutaRepozitorijum;

	@Autowired
	RacunRepozitorijum racunRepozitorijum;

	Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
	Banka b = z.banka;

	@Override
	public ResponseEntity<Racun> registracijaRacuna(Racun racun, Long idKlijenta, Long idValute) {
		racun.banka = b;
		racun.klijent = klijentRepozitorijum.findOne(idKlijenta);
		racun.valuta = valutaRepozitorijum.findOne(idValute);
		return new ResponseEntity<Racun>(racunRepozitorijum.save(racun), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<List<Racun>> sviRacuni() {
		return new ResponseEntity<List<Racun>>(racunRepozitorijum.findByBanka(b), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Racun>> sviRacuniValute(Long idValute) {
		return new ResponseEntity<List<Racun>>(
				racunRepozitorijum.findByBankaAndValuta(b, valutaRepozitorijum.findOne(idValute)), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Racun>> sviRacuniKlijenta(Long idKlijenta) {
		return new ResponseEntity<List<Racun>>(
				racunRepozitorijum.findByBankaAndKlijent(b, klijentRepozitorijum.findOne(idKlijenta)), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Racun>> pretraziRacune(Racun racun, Long idKlijenta, Long idValute) {
		List<Racun> lista = new ArrayList<Racun>();
		if (racun != null) {
			if (racun.vazeci != null)
				lista.addAll(racunRepozitorijum.findByBankaAndVazeci(b, racun.vazeci));
			if (racun.brojRacuna != null)
				lista.addAll(racunRepozitorijum.findByBankaAndBrojRacuna(b, racun.brojRacuna));
			if (racun.datumOtvaranja != null)
				lista.addAll(racunRepozitorijum.findByBankaAndDatumOtvaranja(b, racun.datumOtvaranja));
			if (racun.datumZatvaranja != null)
				lista.addAll(racunRepozitorijum.findByBankaAndDatumZatvaranja(b, racun.datumZatvaranja));
		}
		if (idKlijenta != null)
			lista.addAll(racunRepozitorijum.findByBankaAndKlijent(b, klijentRepozitorijum.findOne(idKlijenta)));
		if (idValute != null)
			lista.addAll(racunRepozitorijum.findByBankaAndValuta(b, valutaRepozitorijum.findOne(idValute)));
		Set<Racun> set = new HashSet<Racun>();
		set.addAll(lista);
		lista.clear();
		lista.addAll(set);
		return new ResponseEntity<List<Racun>>(lista, HttpStatus.OK);
	}

}
