package poslovna.servisiImplementacija;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.hesiranje.Password;
import poslovna.model.Korisnik;
import poslovna.repozitorijumi.KorisnikRepozitorijum;
import poslovna.servisi.KorisnikServis;

@Service
@Transactional
public class KorisnikServisImpl implements KorisnikServis {

	@Autowired
	KorisnikRepozitorijum korisnikRepozitorijum;

	@Autowired
	HttpSession sesija;

	@Override
	public Korisnik logovanje(String korisnickoIme) {
		return korisnikRepozitorijum.findByKorisnickoIme(korisnickoIme);
	}

	@Override
	public boolean promenaLozinke(String l) {
		// TODO Auto-generated method stub
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		Pattern p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
		Matcher m = p.matcher(l);
		if (m.matches()) {
			k.lozinka = Password.hashPassword(l);
			korisnikRepozitorijum.save(k);
			return true;
		}
		return false;
	}

	@Override
	public Korisnik save(Korisnik k) {
		// TODO Auto-generated method stub
		return korisnikRepozitorijum.save(k);
	}

	@Override
	public Korisnik preuzmiKorisnika(Long id) {
		// TODO Auto-generated method stub
		return korisnikRepozitorijum.findOne(id);
	}

}
