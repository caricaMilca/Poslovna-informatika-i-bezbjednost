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

import poslovna.model.AnalitikaIzvoda;
import poslovna.model.Banka;
import poslovna.model.Zaposleni;
import poslovna.repozitorijumi.AnalitikaIzvodaRepozitorijum;
import poslovna.repozitorijumi.DnevnoStanjeRacunaRepozitorijum;
import poslovna.repozitorijumi.ValutaRepozitorijum;
import poslovna.repozitorijumi.VrstaPlacanjaRepozitorijum;
import poslovna.servisi.AnalitikaIzvodaServis;

@Service
@Transactional
public class AnalitikaIzvodaServisImpl implements AnalitikaIzvodaServis {

	@Autowired
	HttpSession sesija;

	@Autowired
	AnalitikaIzvodaRepozitorijum analitikaIzvodaRepozitorijum;

	@Autowired
	ValutaRepozitorijum valutaRepozitorijum;

	@Autowired
	VrstaPlacanjaRepozitorijum vrstaPlacanjaRepozitorijum;

	@Autowired
	DnevnoStanjeRacunaRepozitorijum dnevnoStanjeRacunaRepozitorijum;

	@Override
	public ResponseEntity<AnalitikaIzvoda> registracijaAnalitikeIzvoda(AnalitikaIzvoda analitikaIzvoda,
			Long idDnevnogStanjaRacuna, Long idValute, Long idTipaPlacanja) {

		analitikaIzvoda.valuta = valutaRepozitorijum.findOne(idValute);
		analitikaIzvoda.vrstaPlacanja = vrstaPlacanjaRepozitorijum.findOne(idTipaPlacanja);
		analitikaIzvoda.dnevnoStanjeRacuna = dnevnoStanjeRacunaRepozitorijum.findOne(idDnevnogStanjaRacuna);
		return new ResponseEntity<AnalitikaIzvoda>(analitikaIzvodaRepozitorijum.save(analitikaIzvoda),
				HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvoda() {
		Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
		Banka b = z.banka;
		return new ResponseEntity<List<AnalitikaIzvoda>>(analitikaIzvodaRepozitorijum.izvodiBanke(b), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvodaDnevnog(Long idDnevnogStanjaRacuna) {
		Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
		Banka b = z.banka;
		return new ResponseEntity<List<AnalitikaIzvoda>>(
				analitikaIzvodaRepozitorijum.findByDnevnoStanjeRacuna(idDnevnogStanjaRacuna, b), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvodaValute(Long idValute) {
		Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
		Banka b = z.banka;
		return new ResponseEntity<List<AnalitikaIzvoda>>(analitikaIzvodaRepozitorijum.findByValuta(idValute, b),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvodaTipaPlacanja(Long idTipaPlacanja) {
		Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
		Banka b = z.banka;
		return new ResponseEntity<List<AnalitikaIzvoda>>(
				analitikaIzvodaRepozitorijum.findByVrstaPlacanja(idTipaPlacanja, b), HttpStatus.OK);

	}

	@Override
	public ResponseEntity<List<AnalitikaIzvoda>> pretraziAnalitikeIzvoda(AnalitikaIzvoda analitikaIzvoda,
			Long idDnevnogStanjaRacuna, Long idValute, Long idTipaPlacanja) {
		Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
		// ko bude sredjivao pretragu nek doda i za tip uplate
		Banka b = z.banka;
		List<AnalitikaIzvoda> lista = new ArrayList<AnalitikaIzvoda>();
		if (analitikaIzvoda != null) {
			if (analitikaIzvoda.hitno != null)
				lista.addAll(analitikaIzvodaRepozitorijum.findByHitno(analitikaIzvoda.hitno, b));
			if (analitikaIzvoda.datumPrimanja != null)
				lista.addAll(analitikaIzvodaRepozitorijum.findByDatumPrimanja(analitikaIzvoda.datumPrimanja, b));
			if (analitikaIzvoda.datumValute != null)
				lista.addAll(analitikaIzvodaRepozitorijum.findByDatumValute(analitikaIzvoda.datumValute, b));
			if (analitikaIzvoda.duznik != null)
				lista.addAll(analitikaIzvodaRepozitorijum.findByDuznik(analitikaIzvoda.duznik, b));
			if (analitikaIzvoda.iznos != null)
				lista.addAll(analitikaIzvodaRepozitorijum.findByIznos(analitikaIzvoda.iznos, b));
			if (analitikaIzvoda.modelOdobrenja != null)
				lista.addAll(analitikaIzvodaRepozitorijum.findByModelOdobrenja(analitikaIzvoda.modelOdobrenja, b));
			if (analitikaIzvoda.modelZaduzenja != null)
				lista.addAll(analitikaIzvodaRepozitorijum.findByModelZaduzenja(analitikaIzvoda.modelZaduzenja, b));
			if (analitikaIzvoda.povjerilac != null)
				lista.addAll(analitikaIzvodaRepozitorijum.findByPovjerilac(analitikaIzvoda.povjerilac, b));
			if (analitikaIzvoda.pozivNaBrojOdobrenja != null)
				lista.addAll(analitikaIzvodaRepozitorijum
						.findByPozivNaBrojOdobrenja(analitikaIzvoda.pozivNaBrojOdobrenja, b));
			if (analitikaIzvoda.pozivNaBrojZaduzenja != null)
				lista.addAll(analitikaIzvodaRepozitorijum
						.findByPozivNaBrojZaduzenja(analitikaIzvoda.pozivNaBrojZaduzenja, b));
			if (analitikaIzvoda.racunDuznika != null)
				lista.addAll(analitikaIzvodaRepozitorijum.findByRacunDuznika(analitikaIzvoda.racunDuznika, b));
			if (analitikaIzvoda.racunPovjerioca != null)
				lista.addAll(analitikaIzvodaRepozitorijum.findByRacunPovjerioca(analitikaIzvoda.racunPovjerioca, b));
			if (analitikaIzvoda.svrhaPlacanja != null)
				lista.addAll(analitikaIzvodaRepozitorijum.findBySvrhaPlacanja(analitikaIzvoda.svrhaPlacanja, b));
			if (analitikaIzvoda.tipGreske != null)
				lista.addAll(analitikaIzvodaRepozitorijum.findByTipGreske(analitikaIzvoda.tipGreske, b));
		}
		if (idDnevnogStanjaRacuna != -1)
			lista.addAll(analitikaIzvodaRepozitorijum.findByDnevnoStanjeRacuna(idDnevnogStanjaRacuna, b));
		if (idValute != -1)
			lista.addAll(analitikaIzvodaRepozitorijum.findByValuta(idValute, b));
		if (idTipaPlacanja != -1)
			lista.addAll(analitikaIzvodaRepozitorijum.findByVrstaPlacanja(idTipaPlacanja, b));

		Set<AnalitikaIzvoda> set = new HashSet<AnalitikaIzvoda>();
		set.addAll(lista);
		lista.clear();
		lista.addAll(set);
		return new ResponseEntity<List<AnalitikaIzvoda>>(lista, HttpStatus.OK);
	}

}
