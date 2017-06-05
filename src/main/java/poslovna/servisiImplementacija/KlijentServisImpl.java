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

import poslovna.model.Klijent;
import poslovna.model.UlogaKlijenta;
import poslovna.model.UlogaKorisnika;
import poslovna.model.Zaposleni;
import poslovna.repozitorijumi.BankaRepozitorijum;
import poslovna.repozitorijumi.DjelatnostRepozitorijum;
import poslovna.repozitorijumi.KlijentRepozitorijum;
import poslovna.servisi.KlijentServis;
import poslovna.servisi.RoleServis;

@Service
@Transactional
public class KlijentServisImpl implements KlijentServis {

	@Autowired
	KlijentRepozitorijum klijentRepozitorijum;

	@Autowired
	DjelatnostRepozitorijum djelatnostRepozitorijum;

	@Autowired
	BankaRepozitorijum bankaRepozitorijum;

	@Autowired
	RoleServis roleServis;

	@Autowired
	HttpSession sesija;

	@Override
	public ResponseEntity<Klijent> registracijaKlijenta(Klijent k, Long idDjelatnosti) {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		k.banka = zaposleni.banka;
		if (klijentRepozitorijum.findByKorisnickoIme(k.korisnickoIme) != null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		k.ulogaK = UlogaKlijenta.POSLOVNO;
		k.uloga = UlogaKorisnika.Klijent;
		k.roles.add(roleServis.findOne(Long.valueOf(1)));
		k.roles.add(roleServis.findOne(Long.valueOf(6)));
		k.djelatnost = djelatnostRepozitorijum.findOne(idDjelatnosti);
		return new ResponseEntity<Klijent>(klijentRepozitorijum.save(k), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<Klijent> registracijaKlijentaF(Klijent k) {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		k.banka = zaposleni.banka;
		if (klijentRepozitorijum.findByKorisnickoIme(k.korisnickoIme) != null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		k.ulogaK = UlogaKlijenta.FIZICKO;
		k.uloga = UlogaKorisnika.Klijent;
		k.roles.add(roleServis.findOne(Long.valueOf(1)));
		k.roles.add(roleServis.findOne(Long.valueOf(6)));
		return new ResponseEntity<Klijent>(klijentRepozitorijum.save(k), HttpStatus.CREATED);
	}

	@Override
	public Klijent preuzmiKlijenta(Long id) {
		return klijentRepozitorijum.findOne(id);
	}

	@Override
	public ResponseEntity<List<Klijent>> sviKlijenti() {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		return new ResponseEntity<List<Klijent>>(klijentRepozitorijum.findByBanka(zaposleni.banka), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Klijent>> sviKlijentiDjelatnosti(Long idDjelatnosti) {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		List<Klijent> lista = klijentRepozitorijum.findByBanka(zaposleni.banka);
		List<Klijent> b = klijentRepozitorijum.findByDjelatnost(djelatnostRepozitorijum.findOne(idDjelatnosti));
		lista.retainAll(b);
		Set<Klijent> set = new HashSet<Klijent>();
		set.addAll(lista);
		lista.clear();
		lista.addAll(set);
		return new ResponseEntity<List<Klijent>>(lista, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Klijent>> pretraziKlijente(Klijent klijent, Long idDjelatnosti) {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		List<Klijent> k = klijentRepozitorijum.findByBanka(zaposleni.banka);
		List<Klijent> lista = new ArrayList<Klijent>();
		if (klijent == null && idDjelatnosti != -1) {
			lista.addAll(klijentRepozitorijum.findByDjelatnost(djelatnostRepozitorijum.findOne(idDjelatnosti)));
		} else {
			if (klijent.ime == null)
				klijent.ime = "%";
			else
				klijent.ime = "%" + klijent.ime + "%";
			if (klijent.prezime == null)
				klijent.prezime = "%";
			else
				klijent.prezime = "%" + klijent.prezime + "%";
			if (klijent.korisnickoIme == null)
				klijent.korisnickoIme = "%";
			else
				klijent.korisnickoIme = "%" + klijent.korisnickoIme + "%";
			if (idDjelatnosti != -1)
				lista.addAll(klijentRepozitorijum.findByDjelatnost(djelatnostRepozitorijum.findOne(idDjelatnosti)));
			lista.addAll(klijentRepozitorijum.findByImeLikeOrPrezimeLikeOrKorisnickoImeLike(klijent.ime,
					klijent.prezime, klijent.korisnickoIme));
			if (klijent.ulogaK != null)
				lista.addAll(klijentRepozitorijum.findByUlogaK(klijent.ulogaK));

		}
		lista.retainAll(k);
		Set<Klijent> set = new HashSet<Klijent>();
		set.addAll(lista);
		lista.clear();
		lista.addAll(set);
		return new ResponseEntity<List<Klijent>>(lista, HttpStatus.OK);
	}
}
