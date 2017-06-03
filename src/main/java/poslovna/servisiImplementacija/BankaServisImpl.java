package poslovna.servisiImplementacija;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.model.Banka;
import poslovna.repozitorijumi.BankaRepozitorijum;
import poslovna.servisi.BankaServis;

@Service
@Transactional
public class BankaServisImpl implements BankaServis {

	@Autowired
	BankaRepozitorijum bankaRepozitorijum;
	
	@Override
	public ResponseEntity<Banka> preuzmiBanku() {
		return new ResponseEntity<Banka>(bankaRepozitorijum.findOne((long) 1), HttpStatus.OK);
	}

}
