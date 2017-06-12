package poslovna.servisiImplementacija;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

		List<Valuta> lista = new ArrayList<Valuta>();
		List<Valuta> v = valutaRepozitorijum.findAll();
		List<Valuta> naziv = new ArrayList<Valuta>();
		List<Valuta> zvanicnaSifra = new ArrayList<Valuta>();
		List<Valuta> domicilna = new ArrayList<Valuta>();
		if (valuta == null)
			return new ResponseEntity<List<Valuta>>(lista, HttpStatus.OK);
		if (valuta != null) {
			if (valuta.naziv != null) {
				naziv = valutaRepozitorijum.findByNaziv(valuta.naziv);
				v.retainAll(naziv);
			}
			if (valuta.zvanicnaSifra != null) {
				zvanicnaSifra = valutaRepozitorijum.findByZvanicnaSifraLike(valuta.zvanicnaSifra);
				v.retainAll(zvanicnaSifra);
			}
			if(valuta.domicilna != null){
				domicilna = valutaRepozitorijum.findByDomicilna(valuta.domicilna);
				v.retainAll(domicilna);
			}
		}
		Set<Valuta> set = new HashSet<Valuta>();
		set.addAll(v);
		v.clear();
		v.addAll(set);
		return new ResponseEntity<List<Valuta>>(v, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> izbrisiV(Long id) {
		// TODO Auto-generated method stub
		valutaRepozitorijum.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Valuta> izmeniV(Valuta v) {
		// TODO Auto-generated method stub
		Valuta vv = valutaRepozitorijum.findOne(v.id);

		if (v.naziv != null)
			vv.naziv = v.naziv;
		if (v.zvanicnaSifra != null)
			vv.zvanicnaSifra = v.zvanicnaSifra;
		if (v.domicilna != null)
			vv.domicilna = v.domicilna;
		return new ResponseEntity<Valuta>(valutaRepozitorijum.save(vv), HttpStatus.OK);
	}

}
