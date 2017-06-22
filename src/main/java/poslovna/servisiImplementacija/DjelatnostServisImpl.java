package poslovna.servisiImplementacija;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.model.Djelatnost;
import poslovna.model.Korisnik;
import poslovna.repozitorijumi.DjelatnostRepozitorijum;
import poslovna.servisi.DjelatnostServis;

@Service
@Transactional
public class DjelatnostServisImpl implements DjelatnostServis {

	@Autowired
	DjelatnostRepozitorijum djelatnostRepozitorijum;

	@Autowired
	HttpSession sesija;
	
	final static Logger logger = Logger.getLogger(DjelatnostServis.class);
	
	
	@Override
	public ResponseEntity<Djelatnost> registracijaDjelatnosti(Djelatnost djelatnost) {
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		if (djelatnostRepozitorijum.findBySifra(djelatnost.sifra) != null){
			logger.info("Korisnik " + k.korisnickoIme + " neuspesno pokusao da registruje djelatnost.");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		else{
			logger.info("Korisnik " + k.korisnickoIme + " uspesno registrovao delatnost " + djelatnost.naziv + ".");
			return new ResponseEntity<Djelatnost>(djelatnostRepozitorijum.save(djelatnost), HttpStatus.CREATED);
		}
	}

	@Override
	public ResponseEntity<List<Djelatnost>> sveDjelatnosti() {
		return new ResponseEntity<List<Djelatnost>>(djelatnostRepozitorijum.findAll(), HttpStatus.OK);
	}

	@Override
	public Djelatnost preuzmiDjelatnost(Long idDjelatnosti) {
		return djelatnostRepozitorijum.findOne(idDjelatnosti);
	}

	@Override
	public ResponseEntity<List<Djelatnost>> pretraziDjelantosti(Djelatnost djelatnost) {
		if (djelatnost.naziv == null)
			djelatnost.naziv = "%";
		else
			djelatnost.naziv = "%" + djelatnost.naziv + "%";
		if (djelatnost.sifra == null)
			djelatnost.sifra = "%";
		else
			djelatnost.sifra = "%" + djelatnost.sifra + "%";
		return new ResponseEntity<List<Djelatnost>>(
				djelatnostRepozitorijum.findByNazivLikeAndSifraLike(djelatnost.naziv, djelatnost.sifra), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> izbrisiDjelatnost(Long idDjelatnosti) {
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		logger.info("Korisnik " + k.korisnickoIme + " uspesno izbrisao delatnost " + djelatnostRepozitorijum.findOne(idDjelatnosti).naziv + ".");
		djelatnostRepozitorijum.delete(idDjelatnosti);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Djelatnost> izmjeniDjelatnost(Djelatnost dje) {
		Djelatnost d = djelatnostRepozitorijum.findOne(dje.id);
		if (djelatnostRepozitorijum.findBySifra(dje.sifra) != null && !d.sifra.equals(dje.sifra))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		if (dje.sifra != null)
			d.sifra = dje.sifra;
		if (dje.naziv != null)
			d.naziv = dje.naziv;
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		logger.info("Korisnik " + k.korisnickoIme + " uspesno izmenio polja delatnosti " + dje.naziv + ".");
		return new ResponseEntity<Djelatnost>(djelatnostRepozitorijum.save(dje), HttpStatus.OK);
	}

}
