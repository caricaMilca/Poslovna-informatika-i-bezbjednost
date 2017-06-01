package poslovna.servisiImplementacija;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.model.Klijent;
import poslovna.model.Role;
import poslovna.model.UlogaKlijenta;
import poslovna.model.UlogaKorisnika;
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
	RoleServis roleServis;

	@Override
	public ResponseEntity<Klijent> registracijaKlijenta(Klijent k) {
		if (klijentRepozitorijum.findByKorisnickoIme(k.korisnickoIme) != null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		k.ulogaK = UlogaKlijenta.POSLOVNO;
		k.uloga = UlogaKorisnika.Klijent;
		k.roles = new ArrayList<Role>();
		k.roles.add(roleServis.findOne(Long.valueOf(1)));
		return new ResponseEntity<Klijent>(klijentRepozitorijum.save(k), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<Klijent> registracijaKlijentaF(Klijent k) {
		if (klijentRepozitorijum.findByKorisnickoIme(k.korisnickoIme) != null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		k.ulogaK = UlogaKlijenta.FIZICKO;
		k.uloga = UlogaKorisnika.Klijent;
		k.roles = new ArrayList<Role>();
		k.roles.add(roleServis.findOne(Long.valueOf(1)));
		return new ResponseEntity<Klijent>(klijentRepozitorijum.save(k), HttpStatus.CREATED);
	}

	@Override
	public Klijent preuzmiKlijenta(Long id) {
		return klijentRepozitorijum.findOne(id);
	}

	@Override
	public ResponseEntity<List<Klijent>> sviKlijenti() {
		return new ResponseEntity<List<Klijent>>(klijentRepozitorijum.findAll(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Klijent>> sviKlijentiDjelatnosti(Long idDjelatnosti) {
		return new ResponseEntity<List<Klijent>>(
				klijentRepozitorijum.findByDjelatnost(djelatnostRepozitorijum.findOne(idDjelatnosti)), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Klijent>> pretraziKlijente(Klijent klijent, Long idDjelatnosti) {
		if (klijent == null) {
			return new ResponseEntity<List<Klijent>>(
					klijentRepozitorijum.findByDjelatnost(djelatnostRepozitorijum.findOne(idDjelatnosti)),
					HttpStatus.OK);
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
			if (idDjelatnosti == -1)
				return new ResponseEntity<List<Klijent>>(
						klijentRepozitorijum.findByImeLikeOrPrezimeLikeOrKorisnickoImeLikeOrUlogaK(klijent.ime,
								klijent.prezime, klijent.korisnickoIme, klijent.ulogaK),
						HttpStatus.OK);
			else
				return new ResponseEntity<List<Klijent>>(klijentRepozitorijum
						.findByImeLikeOrPrezimeLikeOrKorisnickoImeLikeOrUlogaKOrDjelatnost(klijent.ime, klijent.prezime,
								klijent.korisnickoIme, klijent.ulogaK, djelatnostRepozitorijum.findOne(idDjelatnosti)),
						HttpStatus.OK);
		}
	}

}
