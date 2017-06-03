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

import poslovna.model.KursnaLista;
import poslovna.repozitorijumi.BankaRepozitorijum;
import poslovna.repozitorijumi.KursnaListaRepozitorijum;
import poslovna.servisi.KursnaListaServis;

@Service
@Transactional
public class KursnaListaServisImpl implements KursnaListaServis {

	@Autowired
	KursnaListaRepozitorijum kursnaListaRepozitorijum;

	@Autowired
	BankaRepozitorijum bankaRepozitorijum;

	@Override
	public ResponseEntity<KursnaLista> registracijaKursneListe(KursnaLista kursnaLista, Long idBanke) {
		kursnaLista.banka = bankaRepozitorijum.findOne(idBanke);
		return new ResponseEntity<KursnaLista>(kursnaListaRepozitorijum.save(kursnaLista), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<List<KursnaLista>> sveKursneListe() {
		return new ResponseEntity<List<KursnaLista>>(kursnaListaRepozitorijum.findAll(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<KursnaLista>> sveKursneListeBanke(Long idBanke) {
		return new ResponseEntity<List<KursnaLista>>(kursnaListaRepozitorijum.findByBanka(bankaRepozitorijum.findOne(idBanke)),HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<KursnaLista>> pretraziKursneListe(KursnaLista kursnaLista, Long idBanke) {
		List<KursnaLista> lista = new ArrayList<KursnaLista>();
		if (kursnaLista.broj != null)
			lista.addAll(kursnaListaRepozitorijum.findByBroj(kursnaLista.broj));
		if (kursnaLista.datum != null)
			lista.addAll(kursnaListaRepozitorijum.findByDatum(kursnaLista.datum));
		if (kursnaLista.primjenjujeSeOd != null)
			lista.addAll(kursnaListaRepozitorijum.findByPrimjenjujeSeOd(kursnaLista.primjenjujeSeOd));
		if(idBanke != -1)
			lista.addAll(kursnaListaRepozitorijum.findByBanka(bankaRepozitorijum.findOne(idBanke)));
		Set<KursnaLista> set= new HashSet<KursnaLista>();
		set.addAll(lista);
		lista.clear();
		lista.addAll(set);
		return new ResponseEntity<List<KursnaLista>>(lista, HttpStatus.OK);
	}

}
