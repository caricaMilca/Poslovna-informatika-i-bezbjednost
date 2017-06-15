package poslovna.servisiImplementacija;

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
import poslovna.model.Korisnik;
import poslovna.model.UlogaKorisnika;
import poslovna.model.UlogaZaposlenog;
import poslovna.model.Zaposleni;
import poslovna.repozitorijumi.ZaposleniRepozitorijum;
import poslovna.servisi.RoleServis;
import poslovna.servisi.ZaposleniServis;

@Service
@Transactional
public class ZaposleniServisImpl implements ZaposleniServis {

	@Autowired
	ZaposleniRepozitorijum zaposleniRepozitorijum;

	@Autowired
	HttpSession sesija;

	@Autowired
	RoleServis roleServis;

	final static Logger logger = Logger.getLogger(ZaposleniServisImpl.class);

	@Override
	public ResponseEntity<Zaposleni> registracijaSalteruse(Zaposleni z) {
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		z.uloga = UlogaKorisnika.Zaposleni;
		z.roles.add(roleServis.findOne(Long.valueOf(5)));
		z.roles.add(roleServis.findOne(Long.valueOf(6)));
		if (z.ulogaZ == UlogaZaposlenog.Salterusa)
			z.roles.add(roleServis.findOne(Long.valueOf(3)));
		else
			z.roles.add(roleServis.findOne(Long.valueOf(4)));
		Zaposleni zap = (Zaposleni) sesija.getAttribute("korisnik");
		z.banka = zap.banka;
		Pattern p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
		Matcher m = p.matcher(z.lozinka);
		if (m.matches()) {

			z.lozinka = Password.hashPassword(z.lozinka);
			logger.info("Zaposleni " + k.korisnickoIme + " uspesno registrovao novog zaposlenog " + zap.korisnickoIme
					+ ".");
			return new ResponseEntity<Zaposleni>(zaposleniRepozitorijum.save(z), HttpStatus.CREATED);
		} else
			return new ResponseEntity<Zaposleni>(HttpStatus.BAD_REQUEST);
	}

	@Override
	public Zaposleni preuzmiZaposlenog(Long id) {
		// TODO Auto-generated method stub
		return zaposleniRepozitorijum.findOne(id);
	}

}
