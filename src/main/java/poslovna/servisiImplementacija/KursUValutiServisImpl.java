package poslovna.servisiImplementacija;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.model.KursUValuti;
import poslovna.model.Zaposleni;
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

	@Autowired
	HttpSession sesija;
	
	@Override
	public ResponseEntity<KursUValuti> registracijaKursaUValuti(KursUValuti kursUValuti, Long idValutePrema,
			Long idValuteOsnovni, Long idKursneListe) {
		kursUValuti.osnovnaValuta = valutaRepozitorijum.findOne(idValuteOsnovni);
		kursUValuti.premaValuti = valutaRepozitorijum.findOne(idValutePrema);
		kursUValuti.kursnaLista = kursnaListaRepozitorijum.findOne(idKursneListe);
		return new ResponseEntity<KursUValuti>(kursUValutiRepozitorijum.save(kursUValuti), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<List<KursUValuti>> sviKurseviUValuti() {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		return new ResponseEntity<List<KursUValuti>>(kursUValutiRepozitorijum.uBanci(zaposleni.banka), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<KursUValuti>> sviKurseviUValutiValute(Long idValute) {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		List<KursUValuti> k = kursUValutiRepozitorijum.uBanci(zaposleni.banka);
		List<KursUValuti> lista = kursUValutiRepozitorijum.findByOsnovnaValutaOrPremaValuti(
				valutaRepozitorijum.findOne(idValute), valutaRepozitorijum.findOne(idValute));
		lista.retainAll(k);
		Set<KursUValuti> set= new HashSet<KursUValuti>();
		set.addAll(lista);
		lista.clear();
		lista.addAll(set);
		return new ResponseEntity<List<KursUValuti>>(lista, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<KursUValuti>> sviKurseviUValutiKursneListe(Long idKursneListe) {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		List<KursUValuti> k = kursUValutiRepozitorijum.uBanci(zaposleni.banka);
		List<KursUValuti> lista = kursUValutiRepozitorijum.findByKursnaLista(kursnaListaRepozitorijum.findOne(idKursneListe));
		lista.retainAll(k);
		Set<KursUValuti> set= new HashSet<KursUValuti>();
		set.addAll(lista);
		lista.clear();
		lista.addAll(set);
		return new ResponseEntity<List<KursUValuti>>(lista, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<KursUValuti>> pretraziKursUValuti(KursUValuti kursUValuti, Long idValuteOsnovni,
			Long idValutePrema, Long idKursneListe) {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		List<KursUValuti> k = kursUValutiRepozitorijum.uBanci(zaposleni.banka);
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
		lista.retainAll(k);
		Set<KursUValuti> set= new HashSet<KursUValuti>();
		set.addAll(lista);
		lista.clear();
		lista.addAll(set);
		return new ResponseEntity<List<KursUValuti>>(lista, HttpStatus.OK);
	}

}
