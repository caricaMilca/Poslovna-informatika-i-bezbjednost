package poslovna.servisiImplementacija;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.model.Banka;
import poslovna.model.MedjubankarskiPrenos;
import poslovna.model.Zaposleni;
import poslovna.repozitorijumi.MedjubankarskiPrenosRepozitorijum;
import poslovna.servisi.MedjubankarskiPrenosServis;

@Service
@Transactional
public class MedjubankarskiPrenosServisImpl implements MedjubankarskiPrenosServis {

	@Autowired
	MedjubankarskiPrenosRepozitorijum medjubankarskiPrenosRepozitorijum;
	
	@Autowired
	HttpSession sesija;
	
	@Override
	public MedjubankarskiPrenos preuzmiMedjubankarskiPrenos(Long id) {
		return medjubankarskiPrenosRepozitorijum.findOne(id);
	}

	@Override
	public ResponseEntity<List<MedjubankarskiPrenos>> sviMedjubankarskiPrenosi() {
	Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
	Banka b = z.banka;
	return new ResponseEntity<List<MedjubankarskiPrenos>>(medjubankarskiPrenosRepozitorijum.findByBankaPosiljalacOrBankaPrimalac(b, b),HttpStatus.OK);
	}

}
