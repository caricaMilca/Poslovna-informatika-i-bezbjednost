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
import poslovna.model.DnevnoStanjeRacuna;
import poslovna.model.Zaposleni;
import poslovna.repozitorijumi.DnevnoStanjeRacunaRepozitorijum;
import poslovna.repozitorijumi.RacunRepozitorijum;
import poslovna.servisi.DnevnoStanjeRacunaServis;

@Service
@Transactional
public class DnevnoStanjeRacunaServisImpl implements DnevnoStanjeRacunaServis {

	@Autowired
	RacunRepozitorijum racunRepozitorijum;

	@Autowired
	HttpSession sesija;
	
	@Autowired
	DnevnoStanjeRacunaRepozitorijum dnevnoStanjeRacunaRepozitorijum;
	
	@Override
	public ResponseEntity<DnevnoStanjeRacuna> registracijaDnevnogStanjaRacuna(DnevnoStanjeRacuna dnevnoStanjeRacuna,
			Long idRacuna) {
		dnevnoStanjeRacuna.racun = racunRepozitorijum.findOne(idRacuna);
		return new ResponseEntity<DnevnoStanjeRacuna>(dnevnoStanjeRacunaRepozitorijum.save(dnevnoStanjeRacuna), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<List<DnevnoStanjeRacuna>> svaDnevnaStanjeRacuna() {
		Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
		Banka b = z.banka;
		return new ResponseEntity<List<DnevnoStanjeRacuna>>(dnevnoStanjeRacunaRepozitorijum.dnevnaStanjaUBanci(b), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<DnevnoStanjeRacuna>> svaDnevnaStanjeRacunaDatog(Long idRacuna) {
		Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
		Banka b = z.banka;
		List<DnevnoStanjeRacuna> d = dnevnoStanjeRacunaRepozitorijum.dnevnaStanjaUBanci(b);
		List<DnevnoStanjeRacuna> lista = dnevnoStanjeRacunaRepozitorijum.findByRacun(racunRepozitorijum.findOne(idRacuna));
		lista.retainAll(d);
		Set<DnevnoStanjeRacuna> set = new HashSet<DnevnoStanjeRacuna>();
		set.addAll(lista);
		lista.clear();
		lista.addAll(set);
		return new ResponseEntity<List<DnevnoStanjeRacuna>>(lista, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<DnevnoStanjeRacuna>> pretraziDnevnaStanjeRacuna(DnevnoStanjeRacuna dnevnoStanjeRacuna,
			Long idRacuna) {
		Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
		Banka b = z.banka;
		List<DnevnoStanjeRacuna> d = dnevnoStanjeRacunaRepozitorijum.dnevnaStanjaUBanci(b);
		List<DnevnoStanjeRacuna> lista = new ArrayList<DnevnoStanjeRacuna>();
		if(dnevnoStanjeRacuna != null){
			if(dnevnoStanjeRacuna.datumPrometa != null)
				lista.addAll(dnevnoStanjeRacunaRepozitorijum.findByDatumPrometa(dnevnoStanjeRacuna.datumPrometa));
			if(dnevnoStanjeRacuna.novoStanje != null)
				lista.addAll(dnevnoStanjeRacunaRepozitorijum.findByNovoStanje(dnevnoStanjeRacuna.novoStanje));
			if(dnevnoStanjeRacuna.prethodnoStanje != null)
				lista.addAll(dnevnoStanjeRacunaRepozitorijum.findByPrethodnoStanje(dnevnoStanjeRacuna.prethodnoStanje));
			if(dnevnoStanjeRacuna.prometNaKorist != null)
				lista.addAll(dnevnoStanjeRacunaRepozitorijum.findByPrometNaKorist(dnevnoStanjeRacuna.prometNaKorist));
			if(dnevnoStanjeRacuna.prometNaTeret != null)
				lista.addAll(dnevnoStanjeRacunaRepozitorijum.findByPrometNaTeret(dnevnoStanjeRacuna.prometNaTeret));
		}
		if(idRacuna != -1)
			lista.addAll(dnevnoStanjeRacunaRepozitorijum.findByRacun(racunRepozitorijum.findOne(idRacuna)));
		lista.retainAll(d);
		Set<DnevnoStanjeRacuna> set = new HashSet<DnevnoStanjeRacuna>();
		set.addAll(lista);
		lista.clear();
		lista.addAll(set);
		return new ResponseEntity<List<DnevnoStanjeRacuna>>(lista, HttpStatus.OK);
	}

}
