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
import poslovna.model.MedjubankarskiPrenos;
import poslovna.model.Racun;
import poslovna.model.SmijerTransakcije;
import poslovna.model.TipGreske;
import poslovna.model.TipPoruke;
import poslovna.model.Zaposleni;
import poslovna.repozitorijumi.AnalitikaIzvodaRepozitorijum;
import poslovna.repozitorijumi.DnevnoStanjeRacunaRepozitorijum;
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
		if (racunRepozitorijum.findByBrojRacuna(analitikaIzvoda.racunPovjerioca) == null
				|| valutaRepozitorijum.findByZvanicnaSifra(sifraValute) == null) {
			analitikaIzvoda.tipGreske = TipGreske.NEVALIDAN_RACUN;
		} else if (racunRepozitorijum.findByBrojRacunaAndBanka(analitikaIzvoda.racunPovjerioca, banka) != null) {// nije
																													// medjubankarski
			analitikaIzvoda.dnevnoStanjeRacuna.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
			analitikaIzvoda.dnevnoStanjeRacuna.novoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje
					+ analitikaIzvoda.iznos;
			analitikaIzvoda.dnevnoStanjeRacuna.prometNaKorist += analitikaIzvoda.iznos;
			analitikaIzvoda.tipGreske = TipGreske.PROCESIRAN;
			dnevnoStanjeRacunaRepozitorijum.save(analitikaIzvoda.dnevnoStanjeRacuna);
		} else { // medjubankarski prenos
			Racun racunDrugeBanke = racunRepozitorijum.findByBrojRacuna(analitikaIzvoda.racunPovjerioca);
			MedjubankarskiPrenos mp = kreirajMedjubankarskiPrenos(banka, racunDrugeBanke.banka,
					analitikaIzvoda.datumPrimanja);
			if (analitikaIzvoda.iznos >= 250000) { // odmah se procesira
				analitikaIzvoda.dnevnoStanjeRacuna.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
				analitikaIzvoda.dnevnoStanjeRacuna.novoStanje += analitikaIzvoda.iznos;
				analitikaIzvoda.dnevnoStanjeRacuna.prometNaKorist += analitikaIzvoda.iznos;
				analitikaIzvoda.tipGreske = TipGreske.PROCESIRAN;
				mp.tipPoruke = TipPoruke.MT103;
				mp.iznos += analitikaIzvoda.iznos;
			} else { // ide u kliring
				mp.tipPoruke = TipPoruke.MT102;
				analitikaIzvoda.tipGreske = TipGreske.NEPROCESIRAN;
				mp.iznos += analitikaIzvoda.iznos;
			}
			mp.izvodi.add(analitikaIzvoda);
		}
		analitikaIzvoda.vrstaPlacanja = vrstaPlacanjaRepozitorijum.findOne(idTipaPlacanja);
		return new ResponseEntity<AnalitikaIzvoda>(analitikaIzvodaRepozitorijum.save(analitikaIzvoda),
				HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<AnalitikaIzvoda> isplataSaRacuna(AnalitikaIzvoda analitikaIzvoda, String sifraValute,
			Long idTipaPlacanja) {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		Banka banka = zaposleni.banka;
		if (analitikaIzvoda.racunPovjerioca == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		analitikaIzvoda.smijer = SmijerTransakcije.NA_TERET;
		if (racunRepozitorijum.findByBrojRacuna(analitikaIzvoda.racunDuznika) == null
				|| valutaRepozitorijum.findByZvanicnaSifra(sifraValute) == null) {
			analitikaIzvoda.tipGreske = TipGreske.NEVALIDAN_RACUN;
		} else {
			Racun duznik = racunRepozitorijum.findByBrojRacuna(analitikaIzvoda.racunDuznika);
			List<DnevnoStanjeRacuna> stanja = dnevnoStanjeRacunaRepozitorijum.findByRacun(duznik);
			DnevnoStanjeRacuna dsr = stanja.get(stanja.size() - 1);
			if (dsr.novoStanje < analitikaIzvoda.iznos) { // nema dovoljno
															// stanja na racunu
															// da uradi isplatu
				analitikaIzvoda.tipGreske = TipGreske.NELIKVIDAN_KORISNIK;
			} else {
				dsr = kreirajDnevnoStanje(analitikaIzvoda.racunDuznika, analitikaIzvoda.datumPrimanja);
				analitikaIzvoda.dnevnoStanjeRacuna = dsr;
				if (duznik.banka != banka) {// nije
					// medjubankarski
					analitikaIzvoda.dnevnoStanjeRacuna.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
					analitikaIzvoda.dnevnoStanjeRacuna.novoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje
							- analitikaIzvoda.iznos;
					analitikaIzvoda.dnevnoStanjeRacuna.prometNaTeret += analitikaIzvoda.iznos;
					analitikaIzvoda.tipGreske = TipGreske.PROCESIRAN;
					dnevnoStanjeRacunaRepozitorijum.save(analitikaIzvoda.dnevnoStanjeRacuna);
				} else { // medjubankarski prenos
					MedjubankarskiPrenos mp = kreirajMedjubankarskiPrenos(banka, duznik.banka,
							analitikaIzvoda.datumPrimanja);
					if (analitikaIzvoda.iznos >= 250000) { // rtgs
						analitikaIzvoda.dnevnoStanjeRacuna.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
						analitikaIzvoda.dnevnoStanjeRacuna.novoStanje -= analitikaIzvoda.iznos;
						analitikaIzvoda.dnevnoStanjeRacuna.prometNaTeret += analitikaIzvoda.iznos;
						analitikaIzvoda.tipGreske = TipGreske.PROCESIRAN;
						dnevnoStanjeRacunaRepozitorijum.save(analitikaIzvoda.dnevnoStanjeRacuna);
						mp.tipPoruke = TipPoruke.MT103;
						mp.iznos += analitikaIzvoda.iznos;
					} else { // kliring
						mp.tipPoruke = TipPoruke.MT102;
						analitikaIzvoda.tipGreske = TipGreske.NEPROCESIRAN;
						mp.iznos += analitikaIzvoda.iznos;
					}
					mp.izvodi.add(analitikaIzvoda);
				}
			}
		}
		analitikaIzvoda.vrstaPlacanja = vrstaPlacanjaRepozitorijum.findOne(idTipaPlacanja);
		return new ResponseEntity<AnalitikaIzvoda>(analitikaIzvodaRepozitorijum.save(analitikaIzvoda),
				HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<AnalitikaIzvoda> transferSredstava(AnalitikaIzvoda analitikaIzvoda, String sifraValute,
			Long idTipaPlacanja) {
		Zaposleni zaposleni = (Zaposleni) sesija.getAttribute("korisnik");
		Banka banka = zaposleni.banka;
		if (analitikaIzvoda.racunPovjerioca == null || analitikaIzvoda.racunDuznika == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		if (racunRepozitorijum.findByBrojRacuna(analitikaIzvoda.racunPovjerioca) == null
				|| racunRepozitorijum.findByBrojRacuna(analitikaIzvoda.racunDuznika) == null
				|| valutaRepozitorijum.findByZvanicnaSifra(sifraValute) == null) {
			analitikaIzvoda.tipGreske = TipGreske.NEVALIDAN_RACUN;
		} else {
			Racun duznik = racunRepozitorijum.findByBrojRacuna(analitikaIzvoda.racunDuznika);
			Racun povjerilac = racunRepozitorijum.findByBrojRacuna(analitikaIzvoda.racunPovjerioca);
			List<DnevnoStanjeRacuna> stanja = dnevnoStanjeRacunaRepozitorijum.findByRacun(duznik);
			DnevnoStanjeRacuna dsrDuznika = stanja.get(stanja.size() - 1);
			analitikaIzvoda.smijer = SmijerTransakcije.TRANSFER;
			if (dsrDuznika.novoStanje < analitikaIzvoda.iznos) {
				analitikaIzvoda.tipGreske = TipGreske.NELIKVIDAN_KORISNIK;

			} else {
				dsrDuznika = kreirajDnevnoStanje(analitikaIzvoda.racunDuznika, analitikaIzvoda.datumPrimanja);
				analitikaIzvoda.dnevnoStanjeRacuna = dsrDuznika;
				DnevnoStanjeRacuna dsrPovjerioca = kreirajDnevnoStanje(analitikaIzvoda.racunPovjerioca,
						analitikaIzvoda.datumPrimanja);
				analitikaIzvoda.dnevnoStanjeRacunaTransfer = dsrPovjerioca;
				if (duznik.banka == povjerilac.banka && duznik.banka == banka) { // nije
																					// medjubankarski
					// uplacuje
					analitikaIzvoda.dnevnoStanjeRacuna.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
					analitikaIzvoda.dnevnoStanjeRacuna.novoStanje -= analitikaIzvoda.iznos;
					analitikaIzvoda.dnevnoStanjeRacuna.prometNaTeret += analitikaIzvoda.iznos;
					// prima
					analitikaIzvoda.dnevnoStanjeRacunaTransfer.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
					analitikaIzvoda.dnevnoStanjeRacunaTransfer.novoStanje += analitikaIzvoda.iznos;
					analitikaIzvoda.dnevnoStanjeRacuna.prometNaKorist += analitikaIzvoda.iznos;

					analitikaIzvoda.tipGreske = TipGreske.PROCESIRAN;
					dnevnoStanjeRacunaRepozitorijum.save(analitikaIzvoda.dnevnoStanjeRacuna);
					dnevnoStanjeRacunaRepozitorijum.save(analitikaIzvoda.dnevnoStanjeRacunaTransfer);
				} else {
					MedjubankarskiPrenos mp = kreirajMedjubankarskiPrenos(duznik.banka, povjerilac.banka,
							analitikaIzvoda.datumPrimanja);
					if (analitikaIzvoda.iznos >= 250000 || analitikaIzvoda.hitno) {
						// uplacuje
						analitikaIzvoda.dnevnoStanjeRacuna.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
						analitikaIzvoda.dnevnoStanjeRacuna.novoStanje -= analitikaIzvoda.iznos;
						analitikaIzvoda.dnevnoStanjeRacuna.prometNaTeret += analitikaIzvoda.iznos;
						// prima
						analitikaIzvoda.dnevnoStanjeRacunaTransfer.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
						analitikaIzvoda.dnevnoStanjeRacunaTransfer.novoStanje += analitikaIzvoda.iznos;
						analitikaIzvoda.dnevnoStanjeRacuna.prometNaKorist += analitikaIzvoda.iznos;

						analitikaIzvoda.tipGreske = TipGreske.PROCESIRAN;
						dnevnoStanjeRacunaRepozitorijum.save(analitikaIzvoda.dnevnoStanjeRacuna);
						dnevnoStanjeRacunaRepozitorijum.save(analitikaIzvoda.dnevnoStanjeRacunaTransfer);
						mp.tipPoruke = TipPoruke.MT103;
						mp.iznos += analitikaIzvoda.iznos;
					} else {
						mp.tipPoruke = TipPoruke.MT102;
						analitikaIzvoda.tipGreske = TipGreske.NEPROCESIRAN;
						mp.iznos += analitikaIzvoda.iznos;
						if (duznik.banka == banka) { // ako je duznik u ovoj
														// banci procesira
														// njegov dio mislim da tako funkcionise, provjeriti
							analitikaIzvoda.dnevnoStanjeRacuna.prethodnoStanje = analitikaIzvoda.dnevnoStanjeRacuna.novoStanje;
							analitikaIzvoda.dnevnoStanjeRacuna.novoStanje -= analitikaIzvoda.iznos;
							analitikaIzvoda.dnevnoStanjeRacuna.prometNaTeret += analitikaIzvoda.iznos;
							dnevnoStanjeRacunaRepozitorijum.save(analitikaIzvoda.dnevnoStanjeRacuna);
						}

					}

				}
			}
		}
		analitikaIzvoda.vrstaPlacanja = vrstaPlacanjaRepozitorijum.findOne(idTipaPlacanja);
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

	public DnevnoStanjeRacuna kreirajDnevnoStanje(String brojRacuna, Date datum) {
		DnevnoStanjeRacuna dsr;
		if (dnevnoStanjeRacunaRepozitorijum.findByDatumPrometaAndRacun(datum, brojRacuna) == null) {
			dsr = new DnevnoStanjeRacuna();
			dsr.racun = racunRepozitorijum.findByBrojRacuna(brojRacuna);
			dsr.prethodnoStanje = (double) 0;
			dsr.prometNaKorist = (double) 0;
			dsr.prometNaTeret = (double) 0;
			dsr.datumPrometa = datum;
		} else {
			dsr = dnevnoStanjeRacunaRepozitorijum.findByDatumPrometaAndRacun(datum, brojRacuna);
		}
		return dsr;
	}

	public MedjubankarskiPrenos kreirajMedjubankarskiPrenos(Banka b1, Banka b2, Date datum) {
		if (medjubankarskiPrenosRepozitorijum.findByBankaPosiljalacAndBankaPrimalac(b1, b2) != null) {
			return medjubankarskiPrenosRepozitorijum.findByBankaPosiljalacAndBankaPrimalac(b1, b2);
		} else {
			MedjubankarskiPrenos mp = new MedjubankarskiPrenos();
			mp.bankaPosiljalac = b1;
			mp.bankaPrimalac = b2;
			mp.datum = (Timestamp) datum;
			mp.iznos = (double) 0;
			return mp;
		}
	}

}
