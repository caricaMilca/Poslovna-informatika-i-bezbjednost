package poslovna.servisiImplementacija;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.hesiranje.Password;
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

	final static Logger logger = Logger.getLogger(KlijentServisImpl.class);

	@Override
	public ResponseEntity<Klijent> registracijaKlijenta(Klijent k, Long idDjelatnosti) {
		if (k.ulogaK == UlogaKlijenta.POSLOVNO)
			k.djelatnost = djelatnostRepozitorijum.findOne(idDjelatnosti);
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		k.banka = zaposleni.banka;
		if (klijentRepozitorijum.findByKorisnickoIme(k.korisnickoIme) != null) {
			logger.info("Zaposleni " + zaposleni.korisnickoIme + " neuspesno pokusao registraciju novog korisnika "
					+ k.korisnickoIme + ".");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Pattern p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
		Matcher m = p.matcher(k.lozinka);
		if (m.matches()) {
			k.lozinka = Password.hashPassword(k.lozinka);
			k.uloga = UlogaKorisnika.Klijent;
			k.roles.add(roleServis.findOne(Long.valueOf(1)));
			k.roles.add(roleServis.findOne(Long.valueOf(6)));
			logger.info("Zaposleni " + zaposleni.korisnickoIme + " uspesno registrovao novog korisnika "
					+ k.korisnickoIme + ".");
			return new ResponseEntity<Klijent>(klijentRepozitorijum.save(k), HttpStatus.CREATED);
		} else
			return new ResponseEntity<Klijent>(HttpStatus.BAD_REQUEST);
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
		List<Klijent> ime = new ArrayList<Klijent>();
		List<Klijent> prezime = new ArrayList<Klijent>();
		List<Klijent> korisnickoIme = new ArrayList<Klijent>();
		if (klijent == null && idDjelatnosti == -1)
			return new ResponseEntity<List<Klijent>>(lista, HttpStatus.OK);

		List<Klijent> djelatnost = new ArrayList<Klijent>();
		List<Klijent> uloga = new ArrayList<Klijent>();
		if (klijent != null) {
			if (klijent.korisnickoIme != null) {
				korisnickoIme = klijentRepozitorijum.findByKorisnickoImeLike(klijent.korisnickoIme);
				k.retainAll(korisnickoIme);
			}
			if (klijent.prezime != null) {
				prezime = klijentRepozitorijum.findByPrezime(klijent.prezime);
				k.retainAll(prezime);
			}
			if (klijent.ime != null) {
				ime = klijentRepozitorijum.findByIme(klijent.ime);
				k.retainAll(ime);
			}
			if (klijent.ulogaK != null) {
				uloga = klijentRepozitorijum.findByUlogaK(klijent.ulogaK);
				k.retainAll(uloga);
			}
		}
		if (idDjelatnosti != -1) {
			djelatnost = klijentRepozitorijum.findByDjelatnost(djelatnostRepozitorijum.findOne(idDjelatnosti));
			k.retainAll(djelatnost);
		}
		Set<Klijent> set = new HashSet<Klijent>();
		set.addAll(k);
		k.clear();
		k.addAll(set);
		return new ResponseEntity<List<Klijent>>(k, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Klijent> izmjeniKlijenta(Klijent klijent, Long idDjelatnosti) {
		Klijent k = klijentRepozitorijum.findOne(klijent.id);
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		if (klijentRepozitorijum.findByKorisnickoIme(klijent.korisnickoIme) != null
				&& !klijent.korisnickoIme.equals(k.korisnickoIme)) {
			logger.info("Zaposleni " + zaposleni.korisnickoIme + " neuspesno pokusao izmenu polja korisnika "
					+ k.korisnickoIme + ".");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if (k.ulogaK == UlogaKlijenta.POSLOVNO && idDjelatnosti != -1)
			k.djelatnost = djelatnostRepozitorijum.findOne(idDjelatnosti);
		if (klijent.ime != null)
			k.ime = klijent.ime;
		if (klijent.prezime != null)
			k.prezime = klijent.prezime;
		if (klijent.korisnickoIme != null)
			k.korisnickoIme = klijent.korisnickoIme;
		logger.info("Zaposleni " + zaposleni.korisnickoIme + " uspesno izvrsio izmenu polja korisnika "
				+ k.korisnickoIme + ".");

		return new ResponseEntity<Klijent>(klijentRepozitorijum.save(k), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> izbrisiKlijenta(Long idKlijenta) {
		klijentRepozitorijum.delete(idKlijenta);
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		logger.info("Zaposleni " + zaposleni.korisnickoIme + " uspesno izbrisao korisnika "
				+ klijentRepozitorijum.getOne(idKlijenta).korisnickoIme + ".");
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
