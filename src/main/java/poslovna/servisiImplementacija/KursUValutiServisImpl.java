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

import poslovna.model.KursUValuti;
import poslovna.repozitorijumi.KursUValutiRepozitorijum;
import poslovna.repozitorijumi.KursnaListaRepozitorijum;
import poslovna.repozitorijumi.ValutaRepozitorijum;
import poslovna.servisi.KursUValutiServis;

@Service
@Transactional
public class KursUValutiServisImpl implements KursUValutiServis {

	@Autowired
	KursUValutiRepozitorijum kursUValutiRepozitorijum;

	@Autowired
	ValutaRepozitorijum valutaRepozitorijum;

	@Autowired
	KursnaListaRepozitorijum kursnaListaRepozitorijum;

	@Override
	public ResponseEntity<KursUValuti> registracijaKursaUValuti(KursUValuti kursUValuti, Long idValuteOsnovni,
			Long idValutePrema, Long idKursneListe) {
		kursUValuti.osnovnaValuta = valutaRepozitorijum.findOne(idValuteOsnovni);
		kursUValuti.premaValuti = valutaRepozitorijum.findOne(idValutePrema);
		kursUValuti.kursnaLista = kursnaListaRepozitorijum.findOne(idKursneListe);
		return new ResponseEntity<KursUValuti>(kursUValutiRepozitorijum.save(kursUValuti), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<List<KursUValuti>> sviKurseviUValuti() {
		return new ResponseEntity<List<KursUValuti>>(kursUValutiRepozitorijum.findAll(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<KursUValuti>> sviKurseviUValutiValute(Long idValute) {
		return new ResponseEntity<List<KursUValuti>>(kursUValutiRepozitorijum.findByOsnovnaValutaOrPremaValuti(
				valutaRepozitorijum.findOne(idValute), valutaRepozitorijum.findOne(idValute)), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<KursUValuti>> sviKurseviUValutiKursneListe(Long idKursneListe) {
		return new ResponseEntity<List<KursUValuti>>(
				kursUValutiRepozitorijum.findByKursnaLista(kursnaListaRepozitorijum.findOne(idKursneListe)),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<KursUValuti>> pretraziKursUValuti(KursUValuti kursUValuti, Long idValuteOsnovni,
			Long idValutePrema, Long idKursneListe) {
		List<KursUValuti> lista = new ArrayList<KursUValuti>();
		if (kursUValuti != null) {
			if (kursUValuti.prodajni != null)
				lista.addAll(kursUValutiRepozitorijum.findByProdajni(kursUValuti.prodajni));
			if (kursUValuti.kupovni != null)
				lista.addAll(kursUValutiRepozitorijum.findByKupovni(kursUValuti.kupovni));
			if (kursUValuti.srednji != null)
				lista.addAll(kursUValutiRepozitorijum.findBySrednji(kursUValuti.srednji));
		}
		if (idValuteOsnovni != -1)
			lista.addAll(kursUValutiRepozitorijum.findByOsnovnaValuta(valutaRepozitorijum.findOne(idValuteOsnovni)));
		if (idValutePrema != -1)
			lista.addAll(kursUValutiRepozitorijum.findByPremaValuti(valutaRepozitorijum.findOne(idValutePrema)));
		if (idKursneListe != -1)
			lista.addAll(kursUValutiRepozitorijum.findByKursnaLista(kursnaListaRepozitorijum.findOne(idKursneListe)));
		Set<KursUValuti> set= new HashSet<KursUValuti>();
		set.addAll(lista);
		lista.clear();
		lista.addAll(set);
		return new ResponseEntity<List<KursUValuti>>(lista, HttpStatus.OK);
	}

}
