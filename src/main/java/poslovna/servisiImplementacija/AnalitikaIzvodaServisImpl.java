package poslovna.servisiImplementacija;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.model.AnalitikaIzvoda;
import poslovna.model.Banka;
import poslovna.model.DnevnoStanjeRacuna;
import poslovna.model.KursUValuti;
import poslovna.model.KursnaLista;
import poslovna.model.MedjubankarskiPrenos;
import poslovna.model.Racun;
import poslovna.model.SmijerTransakcije;
import poslovna.model.TipGreske;
import poslovna.model.TipPoruke;
import poslovna.model.Valuta;
import poslovna.model.Zaposleni;
import poslovna.repozitorijumi.AnalitikaIzvodaRepozitorijum;
import poslovna.repozitorijumi.BankaRepozitorijum;
import poslovna.repozitorijumi.DnevnoStanjeRacunaRepozitorijum;
import poslovna.repozitorijumi.KlijentRepozitorijum;
import poslovna.repozitorijumi.KursUValutiRepozitorijum;
import poslovna.repozitorijumi.KursnaListaRepozitorijum;
import poslovna.repozitorijumi.MedjubankarskiPrenosRepozitorijum;
import poslovna.repozitorijumi.RacunRepozitorijum;
import poslovna.repozitorijumi.ValutaRepozitorijum;
import poslovna.repozitorijumi.VrstaPlacanjaRepozitorijum;
import poslovna.servisi.AnalitikaIzvodaServis;

@Service
@Transactional
public class AnalitikaIzvodaServisImpl implements AnalitikaIzvodaServis {

	@Autowired
	HttpSession sesija;

	final static Logger logger = Logger.getLogger(AnalitikaIzvodaServisImpl.class);

	@Autowired
	AnalitikaIzvodaRepozitorijum analitikaIzvodaRepozitorijum;

	@Autowired
	ValutaRepozitorijum valutaRepozitorijum;
	
	@Autowired
	KlijentRepozitorijum klijentRepozitorijum;

	@Autowired
	VrstaPlacanjaRepozitorijum vrstaPlacanjaRepozitorijum;

	@Autowired
	DnevnoStanjeRacunaRepozitorijum dnevnoStanjeRacunaRepozitorijum;

	@Autowired
	RacunRepozitorijum racunRepozitorijum;

	@Autowired
	MedjubankarskiPrenosRepozitorijum medjubankarskiPrenosRepozitorijum;

	@Autowired
	KursUValutiRepozitorijum kursUValutiRepozitorijum;

	@Autowired
	KursnaListaRepozitorijum kursnaListaRepozitorijum;

	@Autowired
	BankaRepozitorijum bankaRepozitorijum;

	@Override
	public ResponseEntity<AnalitikaIzvoda> uplataNaRacun(AnalitikaIzvoda analitikaIzvoda, String sifraValute,
			Long idTipaPlacanja) {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		Banka banka = zaposleni.banka;
		if (analitikaIzvoda.racunPovjerioca == null || valutaRepozitorijum.findByZvanicnaSifra(sifraValute) == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		analitikaIzvoda.smijer = SmijerTransakcije.NA_KORIST;
		analitikaIzvoda.vrstaPlacanja = vrstaPlacanjaRepozitorijum.findOne(idTipaPlacanja);
		analitikaIzvoda.valuta = valutaRepozitorijum.findByZvanicnaSifra(sifraValute);

		Racun racun = racunRepozitorijum.findByBrojRacunaAndBanka(analitikaIzvoda.racunPovjerioca, banka);
		if (analitikaIzvoda.racunPovjerioca.substring(0, 3).equals(banka.banka3kod) && racun == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		if (racun != null) { // nije medjubankarski
			if (!racun.vazeci)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			if (analitikaIzvoda.datumValute == null)
				analitikaIzvoda.datumValute = new Date();
			analitikaIzvoda.iznos = konvertujNovac(analitikaIzvoda.valuta, racun.valuta, analitikaIzvoda.datumValute,
					analitikaIzvoda.iznos, racun.banka);
			analitikaIzvoda.valuta = racun.valuta;
			analitikaIzvoda.dnevnoStanjeRacuna = kreirajDnevnoStanje(analitikaIzvoda.racunPovjerioca,
					analitikaIzvoda.datumPrimanja);
			analitikaIzvoda.dnevnoStanjeRacuna.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
			analitikaIzvoda.dnevnoStanjeRacuna.novoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje
					+ analitikaIzvoda.iznos;
			analitikaIzvoda.dnevnoStanjeRacuna.prometNaKorist += analitikaIzvoda.iznos;

			analitikaIzvoda.tipGreske = TipGreske.PROCESIRAN;

			analitikaIzvodaRepozitorijum.save(analitikaIzvoda);
		} else { // medjubankarski prenos
			Banka ban = bankaRepozitorijum.findByBanka3kod(analitikaIzvoda.racunPovjerioca.substring(0, 3));
			if (ban == null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			MedjubankarskiPrenos mp = kreirajMedjubankarskiPrenos(banka, ban, analitikaIzvoda.datumPrimanja);

			if (analitikaIzvoda.iznos >= 250000)
				analitikaIzvoda.tipPoruke = TipPoruke.MT103;
			else
				analitikaIzvoda.tipPoruke = TipPoruke.MT102;

			analitikaIzvoda.tipGreske = TipGreske.NEPROCESIRAN;

			mp.iznos += analitikaIzvoda.iznos;
			mp.izvodi.add(analitikaIzvoda);
			analitikaIzvoda.medjubankarskiPrenos = mp;
			medjubankarskiPrenosRepozitorijum.save(mp);
		}

		logger.info("Uspesno izvrsena uplata na racun poverioca " + analitikaIzvoda.povjerilac + " u iznosu od "
				+ analitikaIzvoda.iznos + sifraValute + ". Uplatu izvrsio/la zaposleni/a " + zaposleni.ime + " "
				+ zaposleni.prezime + ".");
		return new ResponseEntity<>(HttpStatus.CREATED);

	}

	@Override
	public ResponseEntity<AnalitikaIzvoda> isplataSaRacuna(AnalitikaIzvoda analitikaIzvoda, String sifraValute,
			Long idTipaPlacanja) {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		Banka banka = zaposleni.banka;
		if (analitikaIzvoda.racunDuznika == null || valutaRepozitorijum.findByZvanicnaSifra(sifraValute) == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		analitikaIzvoda.smijer = SmijerTransakcije.NA_TERET;
		analitikaIzvoda.vrstaPlacanja = vrstaPlacanjaRepozitorijum.findOne(idTipaPlacanja);
		analitikaIzvoda.valuta = valutaRepozitorijum.findByZvanicnaSifra(sifraValute);
		Racun duznik = racunRepozitorijum.findByBrojRacunaAndBanka(analitikaIzvoda.racunDuznika, banka);
		if (duznik == null && analitikaIzvoda.racunDuznika.substring(0, 3).equals(banka.banka3kod)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (duznik != null) { // nije medjubankarski
			if (!duznik.vazeci)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			if (analitikaIzvoda.datumValute == null)
				analitikaIzvoda.datumValute = new Date();
			analitikaIzvoda.iznos = konvertujNovac(analitikaIzvoda.valuta, duznik.valuta, analitikaIzvoda.datumValute,
					analitikaIzvoda.iznos, duznik.banka);
			List<DnevnoStanjeRacuna> stanja = dnevnoStanjeRacunaRepozitorijum.findByRacun(duznik);
			if (stanja.isEmpty()) {
				analitikaIzvoda.tipGreske = TipGreske.NELIKVIDAN_KORISNIK;
				return new ResponseEntity<>(HttpStatus.ACCEPTED);
			} else {
				DnevnoStanjeRacuna dsr = stanja.get(stanja.size() - 1);
				if (dsr.novoStanje < analitikaIzvoda.iznos) { // nema dovoljno
																// stanja na
																// racunu
																// da uradi
																// isplatu

					analitikaIzvoda.tipGreske = TipGreske.NELIKVIDAN_KORISNIK;
					return new ResponseEntity<>(HttpStatus.ACCEPTED);
				} else {
					dsr = kreirajDnevnoStanje(analitikaIzvoda.racunDuznika, analitikaIzvoda.datumPrimanja);
					analitikaIzvoda.dnevnoStanjeRacuna = dsr;
					analitikaIzvoda.valuta = duznik.valuta;
					analitikaIzvoda.dnevnoStanjeRacuna.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
					analitikaIzvoda.dnevnoStanjeRacuna.novoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje
							- analitikaIzvoda.iznos;
					analitikaIzvoda.dnevnoStanjeRacuna.prometNaTeret += analitikaIzvoda.iznos;
					analitikaIzvoda.tipGreske = TipGreske.PROCESIRAN;
					analitikaIzvodaRepozitorijum.save(analitikaIzvoda);
				}
			}
		} else { // medjubankarski prenos
			Banka ban = bankaRepozitorijum.findByBanka3kod(analitikaIzvoda.racunDuznika.substring(0, 3));
			if (ban == null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			MedjubankarskiPrenos mp = kreirajMedjubankarskiPrenos(banka, ban, analitikaIzvoda.datumPrimanja);
			if (analitikaIzvoda.iznos >= 250000)
				analitikaIzvoda.tipPoruke = TipPoruke.MT103;
			else
				analitikaIzvoda.tipPoruke = TipPoruke.MT102;
			analitikaIzvoda.tipGreske = TipGreske.NEPROCESIRAN;
			mp.iznos += analitikaIzvoda.iznos;
			mp.izvodi.add(analitikaIzvoda);
			analitikaIzvoda.medjubankarskiPrenos = mp;
			medjubankarskiPrenosRepozitorijum.save(mp);
		}
		logger.info("Uspesno izvrsena isplata sa racuna duznika " + analitikaIzvoda.duznik + " u iznosu od "
				+ analitikaIzvoda.iznos + sifraValute + ". Uplatu izvrsio/la zaposleni/a " + zaposleni.ime + " "
				+ zaposleni.prezime + ".");

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<AnalitikaIzvoda> transferSredstava(AnalitikaIzvoda analitikaIzvoda, String sifraValute,
			Long idTipaPlacanja) {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		Banka banka = zaposleni.banka;
		analitikaIzvoda.smijer = SmijerTransakcije.NA_TERET;

		AnalitikaIzvoda izvodPovjerioca = new AnalitikaIzvoda(analitikaIzvoda.duznik, analitikaIzvoda.povjerilac,
				analitikaIzvoda.svrhaPlacanja, analitikaIzvoda.datumPrimanja, analitikaIzvoda.datumValute,
				analitikaIzvoda.racunDuznika, analitikaIzvoda.racunPovjerioca, analitikaIzvoda.modelZaduzenja,
				analitikaIzvoda.pozivNaBrojZaduzenja, analitikaIzvoda.pozivNaBrojOdobrenja,
				analitikaIzvoda.modelOdobrenja, analitikaIzvoda.hitno, analitikaIzvoda.iznos,
				analitikaIzvoda.tipTransakcije, SmijerTransakcije.NA_KORIST);

		analitikaIzvoda.vrstaPlacanja = vrstaPlacanjaRepozitorijum.findOne(idTipaPlacanja);
		izvodPovjerioca.vrstaPlacanja = analitikaIzvoda.vrstaPlacanja;
		if (analitikaIzvoda.racunPovjerioca == null || analitikaIzvoda.racunDuznika == null
				|| valutaRepozitorijum.findByZvanicnaSifra(sifraValute) == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		analitikaIzvoda.valuta = valutaRepozitorijum.findByZvanicnaSifra(sifraValute);
		izvodPovjerioca.valuta = valutaRepozitorijum.findByZvanicnaSifra(sifraValute);
		Racun duznik = racunRepozitorijum.findByBrojRacunaAndBanka(analitikaIzvoda.racunDuznika, banka);
		Racun povjerilac = racunRepozitorijum.findByBrojRacunaAndBanka(analitikaIzvoda.racunPovjerioca, banka);

		if ((duznik == null && analitikaIzvoda.racunDuznika.substring(0, 3).equals(banka.banka3kod))
				|| (povjerilac == null && analitikaIzvoda.racunPovjerioca.substring(0, 3).equals(banka.banka3kod)))
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		if (duznik != null && povjerilac != null) { // nije medjubankarski
			if (!duznik.vazeci || !povjerilac.vazeci)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);

			if (analitikaIzvoda.datumValute == null)
				analitikaIzvoda.datumValute = new Date();
			analitikaIzvoda.iznos = konvertujNovac(analitikaIzvoda.valuta, duznik.valuta, analitikaIzvoda.datumValute,
					analitikaIzvoda.iznos, duznik.banka);
			izvodPovjerioca.datumValute = analitikaIzvoda.datumValute;
			izvodPovjerioca.valuta = valutaRepozitorijum.findByZvanicnaSifra(sifraValute);
			izvodPovjerioca.iznos = konvertujNovac(izvodPovjerioca.valuta, povjerilac.valuta,
					izvodPovjerioca.datumValute, izvodPovjerioca.iznos, povjerilac.banka);
			List<DnevnoStanjeRacuna> stanja = dnevnoStanjeRacunaRepozitorijum.findByRacun(duznik);
			if (stanja.isEmpty()) {
				analitikaIzvoda.tipGreske = TipGreske.NELIKVIDAN_KORISNIK;
				izvodPovjerioca.tipGreske = TipGreske.NELIKVIDAN_NOSLIAC;
				return new ResponseEntity<>(HttpStatus.ACCEPTED);
			} else {
				DnevnoStanjeRacuna dsrDuznika = stanja.get(stanja.size() - 1);
				if (dsrDuznika.novoStanje < analitikaIzvoda.iznos) {
					analitikaIzvoda.tipGreske = TipGreske.NELIKVIDAN_KORISNIK;
					izvodPovjerioca.tipGreske = TipGreske.NELIKVIDAN_NOSLIAC;
					return new ResponseEntity<>(HttpStatus.ACCEPTED);
				} else {
					dsrDuznika = kreirajDnevnoStanje(analitikaIzvoda.racunDuznika, analitikaIzvoda.datumPrimanja);
					analitikaIzvoda.dnevnoStanjeRacuna = dsrDuznika;
					DnevnoStanjeRacuna dsrPovjerioca = kreirajDnevnoStanje(analitikaIzvoda.racunPovjerioca,
							analitikaIzvoda.datumPrimanja);
					izvodPovjerioca.dnevnoStanjeRacuna = dsrPovjerioca;

					izvodPovjerioca.valuta = povjerilac.valuta;
					analitikaIzvoda.valuta = duznik.valuta;
					analitikaIzvoda.dnevnoStanjeRacuna.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
					analitikaIzvoda.dnevnoStanjeRacuna.novoStanje -= analitikaIzvoda.iznos;
					analitikaIzvoda.dnevnoStanjeRacuna.prometNaTeret += analitikaIzvoda.iznos;
					// prima
					izvodPovjerioca.dnevnoStanjeRacuna.prethodnoStanje = izvodPovjerioca.dnevnoStanjeRacuna.novoStanje;
					izvodPovjerioca.dnevnoStanjeRacuna.novoStanje += izvodPovjerioca.iznos;
					izvodPovjerioca.dnevnoStanjeRacuna.prometNaKorist += izvodPovjerioca.iznos;

					analitikaIzvoda.tipGreske = TipGreske.PROCESIRAN;
					izvodPovjerioca.tipGreske = TipGreske.PROCESIRAN;

					analitikaIzvodaRepozitorijum.save(analitikaIzvoda);
					analitikaIzvodaRepozitorijum.save(izvodPovjerioca);
				}
			}
		} else {
			Banka ban = bankaRepozitorijum.findByBanka3kod(analitikaIzvoda.racunDuznika.substring(0, 3));
			Banka ban1 = bankaRepozitorijum.findByBanka3kod(analitikaIzvoda.racunPovjerioca.substring(0, 3));
			if (ban == null || ban1 == null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			MedjubankarskiPrenos mp = kreirajMedjubankarskiPrenos(ban, ban1, analitikaIzvoda.datumPrimanja);

			if (duznik != null) { // ako je duznik u
									// ovoj
									// banci procesira
									// njegov dio mislim
									// da
									// tako funkcionise,
									// provjeriti
				if (!duznik.vazeci)
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);

				if (analitikaIzvoda.datumValute == null)
					analitikaIzvoda.datumValute = new Date();
				analitikaIzvoda.iznos = konvertujNovac(analitikaIzvoda.valuta, duznik.valuta,
						analitikaIzvoda.datumValute, analitikaIzvoda.iznos, duznik.banka);
				List<DnevnoStanjeRacuna> stanja = dnevnoStanjeRacunaRepozitorijum.findByRacun(duznik);
				if (stanja.isEmpty()) {
					analitikaIzvoda.tipGreske = TipGreske.NELIKVIDAN_KORISNIK;
					izvodPovjerioca.tipGreske = TipGreske.NELIKVIDAN_NOSLIAC;
					return new ResponseEntity<>(HttpStatus.ACCEPTED);
				} else {
					DnevnoStanjeRacuna dsrDuznika = stanja.get(stanja.size() - 1);
					if (dsrDuznika.novoStanje < analitikaIzvoda.iznos) {
						analitikaIzvoda.tipGreske = TipGreske.NELIKVIDAN_KORISNIK;
						return new ResponseEntity<>(HttpStatus.ACCEPTED);
					} else {
						analitikaIzvoda.dnevnoStanjeRacuna = dsrDuznika;
						analitikaIzvoda.valuta = duznik.valuta;
						analitikaIzvoda.tipGreske = TipGreske.PROCESIRAN;
						analitikaIzvoda.dnevnoStanjeRacuna.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
						analitikaIzvoda.dnevnoStanjeRacuna.novoStanje -= analitikaIzvoda.iznos;
						analitikaIzvoda.dnevnoStanjeRacuna.prometNaTeret += analitikaIzvoda.iznos;
						analitikaIzvodaRepozitorijum.save(analitikaIzvoda);
					}
				}
			} else {
				if (analitikaIzvoda.iznos >= 250000 || analitikaIzvoda.hitno)
					analitikaIzvoda.tipPoruke = TipPoruke.MT103;
				else
					analitikaIzvoda.tipPoruke = TipPoruke.MT102;
				mp.iznos += analitikaIzvoda.iznos;
				analitikaIzvoda.tipGreske = TipGreske.NEPROCESIRAN;
				mp.izvodi.add(analitikaIzvoda);
				analitikaIzvoda.medjubankarskiPrenos = mp;
			}

			if (povjerilac != null) {
				if (!povjerilac.vazeci)
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);

				izvodPovjerioca.tipGreske = TipGreske.PROCESIRAN;
				DnevnoStanjeRacuna dsrPovjerioca = kreirajDnevnoStanje(analitikaIzvoda.racunPovjerioca,
						analitikaIzvoda.datumPrimanja);
				izvodPovjerioca.dnevnoStanjeRacuna = dsrPovjerioca;
				izvodPovjerioca.valuta = povjerilac.valuta;
				izvodPovjerioca.dnevnoStanjeRacuna.prethodnoStanje = izvodPovjerioca.dnevnoStanjeRacuna.novoStanje;
				izvodPovjerioca.dnevnoStanjeRacuna.novoStanje -= izvodPovjerioca.iznos;
				izvodPovjerioca.dnevnoStanjeRacuna.prometNaTeret += izvodPovjerioca.iznos;
				analitikaIzvodaRepozitorijum.save(izvodPovjerioca);
			} else {
				if (izvodPovjerioca.iznos >= 250000 || izvodPovjerioca.hitno)
					izvodPovjerioca.tipPoruke = TipPoruke.MT103;
				else
					izvodPovjerioca.tipPoruke = TipPoruke.MT102;
				izvodPovjerioca.tipGreske = TipGreske.NEPROCESIRAN;
				mp.iznos += izvodPovjerioca.iznos;
				mp.izvodi.add(izvodPovjerioca);
				izvodPovjerioca.medjubankarskiPrenos = mp;
			}

			medjubankarskiPrenosRepozitorijum.save(mp);

		}

		logger.info("Uspesno izvrsena transfer novca sa racun duznika " + analitikaIzvoda.duznik
				+ " na racun poverioca " + analitikaIzvoda.povjerilac + " u iznosu od " + analitikaIzvoda.iznos
				+ sifraValute + ". Uplatu izvrsio/la zaposleni/a " + zaposleni.ime + " " + zaposleni.prezime + ".");

		return new ResponseEntity<>(HttpStatus.CREATED);

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
	public ResponseEntity<List<AnalitikaIzvoda>> pretraziAnalitikeIzvoda(AnalitikaIzvoda analitikaIzvoda) {
		Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
		// ko bude sredjivao pretragu nek doda i za tip uplate
		Banka b = z.banka;
		List<AnalitikaIzvoda> hitno = new ArrayList<AnalitikaIzvoda>();
		List<AnalitikaIzvoda> datumPrimanja = new ArrayList<AnalitikaIzvoda>();
		List<AnalitikaIzvoda> duznik = new ArrayList<AnalitikaIzvoda>();
		List<AnalitikaIzvoda> povjerilac = new ArrayList<AnalitikaIzvoda>();
		List<AnalitikaIzvoda> iznos = new ArrayList<AnalitikaIzvoda>();
		List<AnalitikaIzvoda> racunDuznika = new ArrayList<AnalitikaIzvoda>();
		List<AnalitikaIzvoda> racunPovjerioca = new ArrayList<AnalitikaIzvoda>();

		List<AnalitikaIzvoda> lista = new ArrayList<AnalitikaIzvoda>();
		List<AnalitikaIzvoda> ai = analitikaIzvodaRepozitorijum.findAll();
		if (analitikaIzvoda == null)
			return new ResponseEntity<List<AnalitikaIzvoda>>(lista, HttpStatus.OK);
		if (analitikaIzvoda != null) {
			if (analitikaIzvoda.hitno != null) {
				hitno.addAll(analitikaIzvodaRepozitorijum.findByHitno(analitikaIzvoda.hitno, b));
				ai.retainAll(hitno);
			}
			if (analitikaIzvoda.datumPrimanja != null) {
				datumPrimanja
						.addAll(analitikaIzvodaRepozitorijum.findByDatumPrimanja(analitikaIzvoda.datumPrimanja, b));
				ai.retainAll(datumPrimanja);
			}
			if (analitikaIzvoda.duznik != null) {
				duznik.addAll(analitikaIzvodaRepozitorijum.findByDuznik(analitikaIzvoda.duznik, b));
				ai.retainAll(duznik);
			}
			if (analitikaIzvoda.iznos != null) {
				iznos.addAll(analitikaIzvodaRepozitorijum.findByIznos(analitikaIzvoda.iznos, b));
				ai.retainAll(iznos);
			}
			if (analitikaIzvoda.povjerilac != null) {
				povjerilac.addAll(analitikaIzvodaRepozitorijum.findByPovjerilac(analitikaIzvoda.povjerilac, b));
				ai.retainAll(povjerilac);
			}
			if (analitikaIzvoda.racunDuznika != null) {
				racunDuznika.addAll(analitikaIzvodaRepozitorijum.findByRacunDuznika(analitikaIzvoda.racunDuznika, b));
				ai.retainAll(racunDuznika);
			}
			if (analitikaIzvoda.racunPovjerioca != null) {
				racunPovjerioca
						.addAll(analitikaIzvodaRepozitorijum.findByRacunPovjerioca(analitikaIzvoda.racunPovjerioca, b));
				ai.retainAll(racunPovjerioca);
			}
		}

		Set<AnalitikaIzvoda> set = new HashSet<AnalitikaIzvoda>();
		set.addAll(ai);
		ai.clear();
		ai.addAll(set);
		return new ResponseEntity<List<AnalitikaIzvoda>>(ai, HttpStatus.OK);
	}

	public DnevnoStanjeRacuna kreirajDnevnoStanje(String brojRacuna, Date datum) {
		DnevnoStanjeRacuna dsr;
		Racun r = racunRepozitorijum.findByBrojRacuna(brojRacuna);
		if (dnevnoStanjeRacunaRepozitorijum.findByDatumPrometaAndRacun(datum, r) == null) {
			dsr = new DnevnoStanjeRacuna((double) 0, (double) 0, (double) 0, (double) 0, datum);
			dsr.racun = racunRepozitorijum.findByBrojRacuna(brojRacuna);
		} else {
			dsr = dnevnoStanjeRacunaRepozitorijum.findByDatumPrometaAndRacun(datum, r);
		}
		return dsr;
	}

	public MedjubankarskiPrenos kreirajMedjubankarskiPrenos(Banka b1, Banka b2, Date datum) {
		if (medjubankarskiPrenosRepozitorijum.findByBankaPosiljalacAndBankaPrimalac(b1, b2) != null) {
			MedjubankarskiPrenos mp = medjubankarskiPrenosRepozitorijum.findByBankaPosiljalacAndBankaPrimalac(b1, b2);
			if (mp.izvodi != null)
				if (mp.izvodi.size() > 8) { // osam izvoda po prenosu
					mp = new MedjubankarskiPrenos();
					mp.bankaPosiljalac = b1;
					mp.bankaPrimalac = b2;
					mp.datum = new Timestamp(datum.getTime());
					mp.iznos = (double) 0;
					return mp;
				}
			return medjubankarskiPrenosRepozitorijum.findByBankaPosiljalacAndBankaPrimalac(b1, b2);
		} else {
			MedjubankarskiPrenos mp = new MedjubankarskiPrenos();
			mp.bankaPosiljalac = b1;
			mp.bankaPrimalac = b2;
	//		mp.datum = new Timestamp(datum.getTime());
			mp.datum = datum;
			mp.iznos = (double) 0;
			return mp;
		}
	}

	private Double konvertujNovac(Valuta izValute, Valuta uValutu, Date datumValute, Double iznos, Banka banka) {
		List<KursnaLista> kl = kursnaListaRepozitorijum.findByBanka(banka);
		double praviIznos = 0;
		KursUValuti kuv = null;
		for (int i = 0; i < kl.size(); i++) {
			if ((datumValute.before(kl.get(i).datum) && datumValute.after(kl.get(i).primjenjujeSeOd))
					|| datumValute.equals(kl.get(i).primjenjujeSeOd) || datumValute.equals(kl.get(i).datum))
				kuv = kursUValutiRepozitorijum.findByOsnovnaValutaAndPremaValutiAndKursnaLista(izValute, uValutu,
						kl.get(i));
			if (kuv != null) {
				praviIznos = iznos * kuv.kupovni;
				break;
			}
		}
		if (kuv == null)
			praviIznos = iznos;
		return praviIznos;
	}

	@Override
	public List<AnalitikaIzvoda> preuzmiAnalitikeKlijenta(Long id) {
		return analitikaIzvodaRepozitorijum.sveAnalitikeKlijenta(klijentRepozitorijum.findOne(id));
	}

}
