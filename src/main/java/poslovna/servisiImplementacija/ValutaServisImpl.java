package poslovna.servisiImplementacija;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.model.Valuta;
import poslovna.repozitorijumi.ValutaRepozitorijum;
import poslovna.servisi.ValutaServis;

@Service
@Transactional
public class ValutaServisImpl implements ValutaServis {

	@Autowired
	ValutaRepozitorijum valutaRepozitorijum;
	
	@Override
	public Valuta preuzmiValutu(Long idvalute) {
		return valutaRepozitorijum.findOne(idvalute);
	}

	@Override
	public ResponseEntity<Valuta> registracijaValuta(Valuta valuta) {
		if(valutaRepozitorijum.findByZvanicnaSifra(valuta.zvanicnaSifra) != null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<Valuta>(valutaRepozitorijum.save(valuta), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Valuta>> sveValute() {
		return new ResponseEntity<List<Valuta>>(valutaRepozitorijum.findAll(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Valuta>> pretraziValute(Valuta valuta) {
		if(valuta.naziv == null)
			valuta.naziv = "%";
		else 
			valuta.naziv = "%" + valuta.naziv + "%";
		if(valuta.zvanicnaSifra != null && valuta.domicilna != null)
			return new ResponseEntity<List<Valuta>>(valutaRepozitorijum.findByZvanicnaSifraOrNazivLikeOrDomicilna(valuta.zvanicnaSifra, valuta.naziv, valuta.domicilna), HttpStatus.OK);
		else if(valuta.zvanicnaSifra == null && valuta.domicilna != null) 
			return new ResponseEntity<List<Valuta>>(valutaRepozitorijum.findByNazivLikeOrDomicilna(valuta.naziv, valuta.domicilna), HttpStatus.OK);
		else if(valuta.zvanicnaSifra != null && valuta.domicilna == null)
			return new ResponseEntity<List<Valuta>>(valutaRepozitorijum.findByZvanicnaSifraOrNazivLike(valuta.zvanicnaSifra, valuta.naziv), HttpStatus.OK);
		else 
			return new ResponseEntity<List<Valuta>>(valutaRepozitorijum.findByNazivLike(valuta.naziv), HttpStatus.OK);


	}

}
