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

import poslovna.model.KursnaLista;
import poslovna.model.Zaposleni;
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

	@Autowired
	HttpSession sesija;
	
	@Override
	public ResponseEntity<KursnaLista> registracijaKursneListe(KursnaLista kursnaLista) {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		kursnaLista.banka = zaposleni.banka;
		return new ResponseEntity<KursnaLista>(kursnaListaRepozitorijum.save(kursnaLista), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<List<KursnaLista>> sveKursneListe() {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		return new ResponseEntity<List<KursnaLista>>(kursnaListaRepozitorijum.findByBanka(zaposleni.banka), HttpStatus.OK);
	}
	@Override
	public ResponseEntity<List<KursnaLista>> pretraziKursneListe(KursnaLista kursnaLista) {
		List<KursnaLista> lista = new ArrayList<KursnaLista>();
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		List<KursnaLista> b = kursnaListaRepozitorijum.findByBanka(zaposleni.banka);
		if (kursnaLista.broj != null)
			lista.addAll(kursnaListaRepozitorijum.findByBroj(kursnaLista.broj));
		if (kursnaLista.datum != null)
			lista.addAll(kursnaListaRepozitorijum.findByDatum(kursnaLista.datum));
		if (kursnaLista.primjenjujeSeOd != null)
			lista.addAll(kursnaListaRepozitorijum.findByPrimjenjujeSeOd(kursnaLista.primjenjujeSeOd));
		lista.retainAll(b);
		Set<KursnaLista> set= new HashSet<KursnaLista>();
		set.addAll(lista);
		lista.clear();
		lista.addAll(set);
		return new ResponseEntity<List<KursnaLista>>(lista, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<KursnaLista> izmeniKL(KursnaLista kl) {
		// TODO Auto-generated method stub
		KursnaLista kursnaLista = kursnaListaRepozitorijum.findOne(kl.id);

		if (kl.datum != null)
			kursnaLista.datum = kl.datum;
		if (kl.primjenjujeSeOd != null)
			kursnaLista.primjenjujeSeOd = kl.primjenjujeSeOd;
		if (kl.broj != null)
			kursnaLista.broj = kl.broj;
		return new ResponseEntity<KursnaLista>(kursnaListaRepozitorijum.save(kursnaLista), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> izbrisiKL(Long id) {
		// TODO Auto-generated method stub
		kursnaListaRepozitorijum.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
