package poslovna.servisiImplementacija;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.model.AnalitikaIzvoda;
import poslovna.model.Banka;
import poslovna.model.DnevnoStanjeRacuna;
import poslovna.model.Racun;
import poslovna.model.TipTransakcije;
import poslovna.model.Zaposleni;
import poslovna.repozitorijumi.DnevnoStanjeRacunaRepozitorijum;
import poslovna.repozitorijumi.KlijentRepozitorijum;
import poslovna.repozitorijumi.RacunRepozitorijum;
import poslovna.repozitorijumi.ValutaRepozitorijum;
import poslovna.repozitorijumi.VrstaPlacanjaRepozitorijum;
import poslovna.servisi.RacunServis;

@Service
@Transactional
public class RacunServisImpl implements RacunServis {

	@Autowired
	HttpSession sesija;

	final static Logger logger = Logger.getLogger(RacunServisImpl.class);
	
	@Autowired
	KlijentRepozitorijum klijentRepozitorijum;
	
	@Autowired
	VrstaPlacanjaRepozitorijum vrstaPlacanjaRepozitorijum;

	@Autowired
	ValutaRepozitorijum valutaRepozitorijum;

	@Autowired
	RacunRepozitorijum racunRepozitorijum;
	
	@Autowired
	AnalitikaIzvodaServisImpl analitikaIzvodaServisImpl;
	
	@Autowired
	DnevnoStanjeRacunaRepozitorijum dnevnoStanjeRacunaRepozitorijum;

	
	@Override
	public ResponseEntity<Racun> registracijaRacuna(Long idKlijenta, Long idValute) {

		Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
		Banka b = z.banka;
		Racun racun = new Racun();
		racun.banka = b;
		racun.klijent = klijentRepozitorijum.findOne(idKlijenta);
		racun.valuta = valutaRepozitorijum.findOne(idValute);
		racun.datumOtvaranja = new Date();
		
		String dozvoljeneCifre = "1234567890";
		StringBuilder bilder = new StringBuilder();
		Random rnd = new Random();
		while (bilder.length() < 13) { // length of the random string.
			int index = (int) (rnd.nextFloat() * dozvoljeneCifre.length());
			bilder.append(dozvoljeneCifre.charAt(index));
		}
		String racunGlavniDio = bilder.toString();
		
		BigInteger bigInt = new BigInteger(b.banka3kod + racunGlavniDio);

		BigInteger checksum = new BigInteger("98")
				.subtract(bigInt.multiply(new BigInteger("100")).remainder(new BigInteger("97")));
	
		racun.brojRacuna = b.banka3kod + "-" + racunGlavniDio + "-" + checksum ;
		DnevnoStanjeRacuna dsr = new DnevnoStanjeRacuna(0.0, 0.0, 0.0, 0.0, new Date());
		dsr.racun = racun;
		racun.dnevnaStanja.add(dsr);
		if(racunRepozitorijum.findByBrojRacuna(racun.brojRacuna) != null)
			return new ResponseEntity<>(HttpStatus.OK);
		logger.info("Korisnik " + z.korisnickoIme + " uspesno otvorio racun klijentu " + racun.klijent.korisnickoIme + ".");
		return new ResponseEntity<Racun>(racunRepozitorijum.save(racun), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<List<Racun>> sviRacuni() {

		Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
		Banka b = z.banka;
		return new ResponseEntity<List<Racun>>(racunRepozitorijum.findByBanka(b), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Racun>> sviRacuniValute(Long idValute) {

		Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
		Banka b = z.banka;
		List<Racun> valuta = racunRepozitorijum.findByValuta(valutaRepozitorijum.findOne(idValute));
		List<Racun> banka = racunRepozitorijum.findByBanka(b);
		banka.retainAll(valuta);
		Set<Racun> set = new HashSet<Racun>();
		set.addAll(banka);
		banka.clear();
		banka.addAll(set);
		return new ResponseEntity<List<Racun>>(banka, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeRacuna(Long idRacuna) {
		// TODO Auto-generated method stub
		List<AnalitikaIzvoda> analitike = new ArrayList<AnalitikaIzvoda>();
		Racun racun = racunRepozitorijum.findOne(idRacuna);
		if(racun.dnevnaStanja == null)
			racun.dnevnaStanja = new HashSet<DnevnoStanjeRacuna>();
		Iterator<DnevnoStanjeRacuna> iter = racun.dnevnaStanja.iterator();
		while (iter.hasNext()) {
		    analitike.addAll(iter.next().izvodi);
		}
		return new ResponseEntity<List<AnalitikaIzvoda>>(analitike, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<List<Racun>> sviRacuniKlijenta(Long idKlijenta) {

		Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
		Banka b = z.banka;
		List<Racun> klijent = racunRepozitorijum.findByKlijent(klijentRepozitorijum.findOne(idKlijenta));
		List<Racun> banka = racunRepozitorijum.findByBanka(b);
		banka.retainAll(klijent);
		Set<Racun> set = new HashSet<Racun>();
		set.addAll(banka);
		banka.clear();
		banka.addAll(set);
		return new ResponseEntity<List<Racun>>(banka, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Racun>> pretraziRacune(Racun racun, Long idKlijenta, Long idValute) {
		Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
		Banka b = z.banka;
		List<Racun> vazeci = new ArrayList<Racun>();
		List<Racun> banka = new ArrayList<Racun>();
		List<Racun> brojRacuna = new ArrayList<Racun>();
		List<Racun> datumOtvaranja = new ArrayList<Racun>();
		List<Racun> datumZatvaranja = new ArrayList<Racun>();
		List<Racun> klijent = new ArrayList<Racun>();
		List<Racun> valuta = new ArrayList<Racun>();
		if (racun == null && idKlijenta == -1 && idValute == -1)
			return new ResponseEntity<List<Racun>>(banka, HttpStatus.OK);
		banka = racunRepozitorijum.findByBanka(b);
		if (racun != null) {
			if (racun.vazeci != null) {
				vazeci = racunRepozitorijum.findByVazeci(racun.vazeci);
				banka.retainAll(vazeci);
			}
			if (racun.brojRacuna != null) {
				brojRacuna = racunRepozitorijum.findByRacunLike(racun.brojRacuna);
				banka.retainAll(brojRacuna);
			}
			if (racun.datumOtvaranja != null) {
				datumOtvaranja = racunRepozitorijum.findByDatumOtvaranja(racun.datumOtvaranja);
				banka.retainAll(datumOtvaranja);
			}
			if (racun.datumZatvaranja != null) {
				datumZatvaranja = racunRepozitorijum.findByDatumZatvaranja(racun.datumZatvaranja);
				banka.retainAll(datumZatvaranja);
			}
		}
		if (idKlijenta != -1) {
			klijent = racunRepozitorijum.findByKlijent(klijentRepozitorijum.findOne(idKlijenta));
			banka.retainAll(klijent);
		}
		if (idValute != -1) {
			valuta = racunRepozitorijum.findByValuta(valutaRepozitorijum.findOne(idValute));
			banka.retainAll(valuta);
		}
		Set<Racun> set = new HashSet<Racun>();
		set.addAll(banka);
		banka.clear();
		banka.addAll(set);
		return new ResponseEntity<List<Racun>>(banka, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Racun> zatvoriRacun(Racun racun) {
		Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
		Racun r = racunRepozitorijum.findOne(racun.id);
		r.datumZatvaranja = new Date();
		r.racunPrenosa = racun.racunPrenosa.substring(0, 3) + "-" + racun.racunPrenosa.substring(3, 15) + "-" + racun.racunPrenosa.substring(15);
		AnalitikaIzvoda ai = new AnalitikaIzvoda();
		ai.datumPrimanja = r.datumZatvaranja;
		ai.datumValute = r.datumZatvaranja;
		ai.racunDuznika = r.brojRacuna;
		ai.racunPovjerioca = r.racunPrenosa;
		ai.tipTransakcije = TipTransakcije.TRANSFER;
		ai.hitno = true;
		ai.svrhaPlacanja = "Zatvaranje racuna";
		ai.duznik = r.klijent.korisnickoIme;
		
		List<DnevnoStanjeRacuna> stanja = dnevnoStanjeRacunaRepozitorijum.findByRacun(r);
		if (stanja.isEmpty()) {
			ai.iznos = (double) 0;
			DnevnoStanjeRacuna dsr = new DnevnoStanjeRacuna(0.0, 0.0, 0.0, 0.0, new Date());
			dsr.racun = r;
			dnevnoStanjeRacunaRepozitorijum.save(dsr);
			
		} else {
			DnevnoStanjeRacuna dsrDuznika = stanja.get(stanja.size() - 1);
			ai.iznos = dsrDuznika.novoStanje;
			}
		analitikaIzvodaServisImpl.transferSredstava(ai, r.valuta.zvanicnaSifra, (long) 1);
		r.vazeci = false;
		logger.info("Zaposleni " + z.korisnickoIme + " uspesno zatvorio racun korisnika " + r.klijent.korisnickoIme + "." );
		return new ResponseEntity<Racun>(racunRepozitorijum.save(r), HttpStatus.OK);
	}

	

}
