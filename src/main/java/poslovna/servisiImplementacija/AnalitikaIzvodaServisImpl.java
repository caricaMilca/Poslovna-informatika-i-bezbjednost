package poslovna.servisiImplementacija;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
import poslovna.repozitorijumi.DnevnoStanjeRacunaRepozitorijum;
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

	@Autowired
	AnalitikaIzvodaRepozitorijum analitikaIzvodaRepozitorijum;

	@Autowired
	ValutaRepozitorijum valutaRepozitorijum;

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

	@Override
	public ResponseEntity<AnalitikaIzvoda> uplataNaRacun(AnalitikaIzvoda analitikaIzvoda, String sifraValute,
			Long idTipaPlacanja) {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		Banka banka = zaposleni.banka;
		if (analitikaIzvoda.racunPovjerioca == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		analitikaIzvoda.dnevnoStanjeRacuna = kreirajDnevnoStanje(analitikaIzvoda.racunPovjerioca,
				analitikaIzvoda.datumPrimanja);
		analitikaIzvoda.smijer = SmijerTransakcije.NA_KORIST;
		analitikaIzvoda.vrstaPlacanja = vrstaPlacanjaRepozitorijum.findOne(idTipaPlacanja);
		if (valutaRepozitorijum.findByZvanicnaSifra(sifraValute) == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		if (racunRepozitorijum.findByBrojRacuna(analitikaIzvoda.racunPovjerioca) == null) {
			analitikaIzvoda.tipGreske = TipGreske.NEVALIDAN_RACUN;
			analitikaIzvodaRepozitorijum.save(analitikaIzvoda);

		} else {
			analitikaIzvoda.valuta = valutaRepozitorijum.findByZvanicnaSifra(sifraValute);
			Racun racun = racunRepozitorijum.findByBrojRacuna(analitikaIzvoda.racunPovjerioca);
			if (analitikaIzvoda.datumValute == null)
				analitikaIzvoda.datumValute = new Date();
			analitikaIzvoda.iznos = konvertujNovac(analitikaIzvoda.valuta, racun.valuta, analitikaIzvoda.datumValute,
					analitikaIzvoda.iznos, racun.banka);
			analitikaIzvoda.valuta = racun.valuta;
			if (racun.banka.id == banka.id || analitikaIzvoda.iznos >= 250000) {// nije
				analitikaIzvoda.dnevnoStanjeRacuna.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
				analitikaIzvoda.dnevnoStanjeRacuna.novoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje
						+ analitikaIzvoda.iznos;
				analitikaIzvoda.dnevnoStanjeRacuna.prometNaKorist += analitikaIzvoda.iznos;
				analitikaIzvoda.tipGreske = TipGreske.PROCESIRAN;
				if (racun.banka.id != banka.id) {// medjubank
					MedjubankarskiPrenos mp = kreirajMedjubankarskiPrenos(banka, racun.banka,
							analitikaIzvoda.datumPrimanja);
					analitikaIzvoda.tipPoruke = TipPoruke.MT103;
					mp.iznos += analitikaIzvoda.iznos;
					mp.izvodi.add(analitikaIzvoda);
					medjubankarskiPrenosRepozitorijum.save(mp);
				} else {
					analitikaIzvodaRepozitorijum.save(analitikaIzvoda);
				}
			} else { // medjubankarski prenos
				MedjubankarskiPrenos mp = kreirajMedjubankarskiPrenos(banka, racun.banka,
						analitikaIzvoda.datumPrimanja);
				analitikaIzvoda.tipPoruke = TipPoruke.MT102;
				analitikaIzvoda.tipGreske = TipGreske.NEPROCESIRAN;
				mp.iznos += analitikaIzvoda.iznos;
				mp.izvodi.add(analitikaIzvoda);
				medjubankarskiPrenosRepozitorijum.save(mp);
			}
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<AnalitikaIzvoda> isplataSaRacuna(AnalitikaIzvoda analitikaIzvoda, String sifraValute,
			Long idTipaPlacanja) {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		Banka banka = zaposleni.banka;
		if (analitikaIzvoda.racunDuznika == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		analitikaIzvoda.smijer = SmijerTransakcije.NA_TERET;
		if (racunRepozitorijum.findByBrojRacuna(analitikaIzvoda.racunDuznika) == null
				|| valutaRepozitorijum.findByZvanicnaSifra(sifraValute) == null) {
			analitikaIzvoda.tipGreske = TipGreske.NEVALIDAN_RACUN;
		} else {
			Racun duznik = racunRepozitorijum.findByBrojRacuna(analitikaIzvoda.racunDuznika);
			if (analitikaIzvoda.datumValute == null)
				analitikaIzvoda.datumValute = new Date();
			analitikaIzvoda.valuta = valutaRepozitorijum.findByZvanicnaSifra(sifraValute);
			analitikaIzvoda.iznos = konvertujNovac(analitikaIzvoda.valuta, duznik.valuta, analitikaIzvoda.datumValute,
					analitikaIzvoda.iznos, duznik.banka);
			List<DnevnoStanjeRacuna> stanja = dnevnoStanjeRacunaRepozitorijum.findByRacun(duznik);
			if (stanja.isEmpty()) {
				analitikaIzvoda.tipGreske = TipGreske.NELIKVIDAN_KORISNIK;
				analitikaIzvodaRepozitorijum.save(analitikaIzvoda);
			} else {
				DnevnoStanjeRacuna dsr = stanja.get(stanja.size() - 1);
				if (dsr.novoStanje < analitikaIzvoda.iznos) { // nema dovoljno
																// stanja na
																// racunu
																// da uradi
																// isplatu
					analitikaIzvoda.tipGreske = TipGreske.NELIKVIDAN_KORISNIK;
					analitikaIzvodaRepozitorijum.save(analitikaIzvoda);
				} else {
					dsr = kreirajDnevnoStanje(analitikaIzvoda.racunDuznika, analitikaIzvoda.datumPrimanja);
					analitikaIzvoda.dnevnoStanjeRacuna = dsr;
					analitikaIzvoda.valuta = duznik.valuta;
					if (duznik.banka.id == banka.id || analitikaIzvoda.iznos >= 250000) {// odmah
						// procesira
						analitikaIzvoda.dnevnoStanjeRacuna.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
						analitikaIzvoda.dnevnoStanjeRacuna.novoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje
								- analitikaIzvoda.iznos;
						analitikaIzvoda.dnevnoStanjeRacuna.prometNaTeret += analitikaIzvoda.iznos;
						analitikaIzvoda.tipGreske = TipGreske.PROCESIRAN;
						dnevnoStanjeRacunaRepozitorijum.save(analitikaIzvoda.dnevnoStanjeRacuna);
						if (duznik.banka.id != banka.id) {// medjubankarski
							MedjubankarskiPrenos mp = kreirajMedjubankarskiPrenos(banka, duznik.banka,
									analitikaIzvoda.datumPrimanja);
							analitikaIzvoda.tipPoruke = TipPoruke.MT103;
							mp.iznos += analitikaIzvoda.iznos;
							mp.izvodi.add(analitikaIzvoda);
							medjubankarskiPrenosRepozitorijum.save(mp);
						} else {
							analitikaIzvodaRepozitorijum.save(analitikaIzvoda);
						}
					} else { // medjubankarski prenos
						MedjubankarskiPrenos mp = kreirajMedjubankarskiPrenos(banka, duznik.banka,
								analitikaIzvoda.datumPrimanja);
						analitikaIzvoda.tipPoruke = TipPoruke.MT102;
						analitikaIzvoda.tipGreske = TipGreske.NEPROCESIRAN;
						mp.iznos += analitikaIzvoda.iznos;
						mp.izvodi.add(analitikaIzvoda);
						medjubankarskiPrenosRepozitorijum.save(mp);
					}
				}
			}
		}
		analitikaIzvoda.vrstaPlacanja = vrstaPlacanjaRepozitorijum.findOne(idTipaPlacanja);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<AnalitikaIzvoda> transferSredstava(AnalitikaIzvoda analitikaIzvoda, String sifraValute,
			Long idTipaPlacanja) {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		Banka banka = zaposleni.banka;
		analitikaIzvoda.smijer = SmijerTransakcije.NA_TERET;
		
		AnalitikaIzvoda izvodPovjerioca = new AnalitikaIzvoda();
		izvodPovjerioca.racunDuznika = analitikaIzvoda.racunPovjerioca;
		izvodPovjerioca.racunPovjerioca = analitikaIzvoda.racunDuznika;
		izvodPovjerioca.datumPrimanja = analitikaIzvoda.datumPrimanja;
		izvodPovjerioca.tipTransakcije = analitikaIzvoda.tipTransakcije;
		if (analitikaIzvoda.datumValute != null)
			izvodPovjerioca.datumValute = analitikaIzvoda.datumValute;
		izvodPovjerioca.duznik = analitikaIzvoda.povjerilac;
		izvodPovjerioca.povjerilac = analitikaIzvoda.duznik;
		izvodPovjerioca.hitno = analitikaIzvoda.hitno;
		izvodPovjerioca.iznos = analitikaIzvoda.iznos;
		izvodPovjerioca.modelOdobrenja = analitikaIzvoda.modelZaduzenja;
		izvodPovjerioca.modelZaduzenja = analitikaIzvoda.modelOdobrenja;
		izvodPovjerioca.svrhaPlacanja = analitikaIzvoda.svrhaPlacanja;
		izvodPovjerioca.pozivNaBrojOdobrenja = analitikaIzvoda.pozivNaBrojZaduzenja;
		izvodPovjerioca.pozivNaBrojZaduzenja = analitikaIzvoda.pozivNaBrojOdobrenja;
		izvodPovjerioca.smijer = SmijerTransakcije.NA_KORIST;
		
		if (analitikaIzvoda.racunPovjerioca == null || analitikaIzvoda.racunDuznika == null
				|| valutaRepozitorijum.findByZvanicnaSifra(sifraValute) == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		if (racunRepozitorijum.findByBrojRacuna(analitikaIzvoda.racunPovjerioca) == null
				|| racunRepozitorijum.findByBrojRacuna(analitikaIzvoda.racunDuznika) == null) {
			analitikaIzvoda.tipGreske = TipGreske.NEVALIDAN_RACUN;
			izvodPovjerioca.tipGreske = TipGreske.NEVALIDAN_RACUN;
			analitikaIzvodaRepozitorijum.save(analitikaIzvoda);
			analitikaIzvodaRepozitorijum.save(izvodPovjerioca);
		} else {
			Racun duznik = racunRepozitorijum.findByBrojRacuna(analitikaIzvoda.racunDuznika);
			Racun povjerilac = racunRepozitorijum.findByBrojRacuna(analitikaIzvoda.racunPovjerioca);
			if (analitikaIzvoda.datumValute == null)
				analitikaIzvoda.datumValute = new Date();
			analitikaIzvoda.valuta = valutaRepozitorijum.findByZvanicnaSifra(sifraValute);
			izvodPovjerioca.valuta = valutaRepozitorijum.findByZvanicnaSifra(sifraValute);
			analitikaIzvoda.iznos = konvertujNovac(analitikaIzvoda.valuta, duznik.valuta, analitikaIzvoda.datumValute,
					analitikaIzvoda.iznos, duznik.banka);
			if (izvodPovjerioca.datumValute == null)
				izvodPovjerioca.datumValute = new Date();
			izvodPovjerioca.valuta = valutaRepozitorijum.findByZvanicnaSifra(sifraValute);
			izvodPovjerioca.iznos = konvertujNovac(izvodPovjerioca.valuta, povjerilac.valuta,
					izvodPovjerioca.datumValute, izvodPovjerioca.iznos, povjerilac.banka);
			List<DnevnoStanjeRacuna> stanja = dnevnoStanjeRacunaRepozitorijum.findByRacun(duznik);
			if (stanja.isEmpty()) {
				analitikaIzvoda.tipGreske = TipGreske.NELIKVIDAN_KORISNIK;
				izvodPovjerioca.tipGreske = TipGreske.NELIKVIDAN_NOSLIAC;
				analitikaIzvodaRepozitorijum.save(analitikaIzvoda);
				analitikaIzvodaRepozitorijum.save(izvodPovjerioca);

			} else {
				DnevnoStanjeRacuna dsrDuznika = stanja.get(stanja.size() - 1);
				if (dsrDuznika.novoStanje < analitikaIzvoda.iznos) {
					analitikaIzvoda.tipGreske = TipGreske.NELIKVIDAN_KORISNIK;
					izvodPovjerioca.tipGreske = TipGreske.NELIKVIDAN_NOSLIAC;
					analitikaIzvodaRepozitorijum.save(analitikaIzvoda);
					analitikaIzvodaRepozitorijum.save(izvodPovjerioca);

				} else {
					dsrDuznika = kreirajDnevnoStanje(analitikaIzvoda.racunDuznika, analitikaIzvoda.datumPrimanja);
					analitikaIzvoda.dnevnoStanjeRacuna = dsrDuznika;
					DnevnoStanjeRacuna dsrPovjerioca = kreirajDnevnoStanje(analitikaIzvoda.racunPovjerioca,
							analitikaIzvoda.datumPrimanja);
					izvodPovjerioca.dnevnoStanjeRacuna = dsrPovjerioca;

					izvodPovjerioca.valuta = povjerilac.valuta;
					analitikaIzvoda.valuta = duznik.valuta;
					if ((duznik.banka.id == povjerilac.banka.id && duznik.banka.id == banka.id)
							|| analitikaIzvoda.iznos >= 250000 || analitikaIzvoda.hitno) { // odmah
																							// procesira
						analitikaIzvoda.dnevnoStanjeRacuna.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
						analitikaIzvoda.dnevnoStanjeRacuna.novoStanje -= analitikaIzvoda.iznos;
						analitikaIzvoda.dnevnoStanjeRacuna.prometNaTeret += analitikaIzvoda.iznos;
						// prima
						izvodPovjerioca.dnevnoStanjeRacuna.prethodnoStanje = izvodPovjerioca.dnevnoStanjeRacuna.novoStanje;
						izvodPovjerioca.dnevnoStanjeRacuna.novoStanje += izvodPovjerioca.iznos;
						izvodPovjerioca.dnevnoStanjeRacuna.prometNaKorist += izvodPovjerioca.iznos;

						analitikaIzvoda.tipGreske = TipGreske.PROCESIRAN;
						izvodPovjerioca.tipGreske = TipGreske.PROCESIRAN;

						if (duznik.banka.id != povjerilac.banka.id) {
							MedjubankarskiPrenos mp = kreirajMedjubankarskiPrenos(duznik.banka, povjerilac.banka,
									analitikaIzvoda.datumPrimanja);
							analitikaIzvoda.tipPoruke = TipPoruke.MT103;
							izvodPovjerioca.tipPoruke = TipPoruke.MT102;
							mp.iznos += analitikaIzvoda.iznos;
							mp.izvodi.add(analitikaIzvoda);
							mp.izvodi.add(izvodPovjerioca);
							medjubankarskiPrenosRepozitorijum.save(mp);
						} else {
							analitikaIzvodaRepozitorijum.save(analitikaIzvoda);
							analitikaIzvodaRepozitorijum.save(izvodPovjerioca);
						}
					} else {
						MedjubankarskiPrenos mp = kreirajMedjubankarskiPrenos(duznik.banka, povjerilac.banka,
								analitikaIzvoda.datumPrimanja);
						analitikaIzvoda.tipPoruke = TipPoruke.MT102;
						izvodPovjerioca.tipPoruke = TipPoruke.MT102;
						izvodPovjerioca.tipGreske = TipGreske.NEPROCESIRAN;
						mp.iznos += analitikaIzvoda.iznos;
						if (duznik.banka.id == banka.id) { // ako je duznik u
															// ovoj
															// banci procesira
															// njegov dio mislim
															// da
															// tako funkcionise,
															// provjeriti
							analitikaIzvoda.tipGreske = TipGreske.PROCESIRAN;
							analitikaIzvoda.dnevnoStanjeRacuna.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
							analitikaIzvoda.dnevnoStanjeRacuna.novoStanje -= analitikaIzvoda.iznos;
							analitikaIzvoda.dnevnoStanjeRacuna.prometNaTeret += analitikaIzvoda.iznos;
						}
						mp.izvodi.add(analitikaIzvoda);
						mp.izvodi.add(izvodPovjerioca);
						medjubankarskiPrenosRepozitorijum.save(mp);

					}
				}
			}
		}
		analitikaIzvoda.vrstaPlacanja = vrstaPlacanjaRepozitorijum.findOne(idTipaPlacanja);
		izvodPovjerioca.vrstaPlacanja = analitikaIzvoda.vrstaPlacanja;
		return new ResponseEntity<>(HttpStatus.CREATED);

	}

	@Override
	public ResponseEntity<List<AnalitikaIzvoda>> sveAnalitikeIzvoda() {
		// Zaposleni z = (Zaposleni) sesija.getAttribute("korisnik");
		// Banka b = z.banka;
		return new ResponseEntity<List<AnalitikaIzvoda>>(analitikaIzvodaRepozitorijum.findAll(), HttpStatus.OK);
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

	public DnevnoStanjeRacuna kreirajDnevnoStanje(String brojRacuna, Date datum) {
		DnevnoStanjeRacuna dsr;
		Racun r = racunRepozitorijum.findByBrojRacuna(brojRacuna);
		if (dnevnoStanjeRacunaRepozitorijum.findByDatumPrometaAndRacun(datum, r) == null) {
			dsr = new DnevnoStanjeRacuna();
			dsr.racun = racunRepozitorijum.findByBrojRacuna(brojRacuna);
			dsr.prethodnoStanje = (double) 0;
			dsr.novoStanje = (double) 0;
			dsr.prometNaKorist = (double) 0;
			dsr.prometNaTeret = (double) 0;
			dsr.datumPrometa = datum;
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
			mp.datum = new Timestamp(datum.getTime());
			mp.iznos = (double) 0;
			return mp;
		}
	}

	private Double konvertujNovac(Valuta izValute, Valuta uValutu, Date datumValute, Double iznos, Banka banka) {
		List<KursnaLista> kl = kursnaListaRepozitorijum.findByBanka(banka);
		double praviIznos = 0;
		KursUValuti kuv = null;
		for (int i = 0; i < kl.size(); i++) {
			// System.out.println(datumValute.before(kl.get(i).datum) + " aaaa "
			// + datumValute.after(kl.get(i).primjenjujeSeOd) + " bbbb " +
			// datumValute.equals(kl.get(i).primjenjujeSeOd) + " ccc " +
			// datumValute.equals(kl.get(i).datum));
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

}
