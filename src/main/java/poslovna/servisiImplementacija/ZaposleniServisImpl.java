package poslovna.servisiImplementacija;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Override
	public ResponseEntity<Zaposleni> registracijaSalteruse(Zaposleni z) {
		z.uloga = UlogaKorisnika.Zaposleni;
		z.roles.add(roleServis.findOne(Long.valueOf(5)));
		z.roles.add(roleServis.findOne(Long.valueOf(6)));
		if (z.ulogaZ == UlogaZaposlenog.Salterusa)
			z.roles.add(roleServis.findOne(Long.valueOf(3)));
		else
			z.roles.add(roleServis.findOne(Long.valueOf(4)));
		Zaposleni zap = (Zaposleni) sesija.getAttribute("korisnik");
		z.banka = zap.banka;
		return new ResponseEntity<Zaposleni>(zaposleniRepozitorijum.save(z), HttpStatus.CREATED);
	}

	@Override
	public Zaposleni preuzmiZaposlenog(Long id) {
		// TODO Auto-generated method stub
		return zaposleniRepozitorijum.findOne(id);
	}

}
