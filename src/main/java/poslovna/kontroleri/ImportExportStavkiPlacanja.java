package poslovna.kontroleri;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poslovna.autorizacija.AutorizacijaAnnotation;
import poslovna.model.AnalitikaIzvoda;
import poslovna.model.Klijent;
import poslovna.model.MedjubankarskiPrenos;
import poslovna.model.StavkePlacanja;
import poslovna.model.TipTransakcije;
import poslovna.servisi.AnalitikaIzvodaServis;
import poslovna.servisi.KlijentServis;
import poslovna.servisi.MedjubankarskiPrenosServis;

@RestController
@RequestMapping("/importExportStavkiPlacanja")
public class ImportExportStavkiPlacanja {

	@Autowired
	HttpSession sesija;
	
	@Autowired
	AnalitikaIzvodaServis analitikaIzvodaServis;
	
	@Autowired
	MedjubankarskiPrenosServis medjubankarskiPrenosServis;
	
	@Autowired
	KlijentServis klijentServis;
	
	@AutorizacijaAnnotation(imeMetode = "ucitajFajl")
	@GetMapping(path = "/ucitajFajl/{fajlIme:.+}")
	public ResponseEntity<?> ucitajFajl(@PathVariable("fajlIme") String fajlIme) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance("poslovna.model");
		Unmarshaller unmarshaller = context.createUnmarshaller();
		StavkePlacanja stavke = (StavkePlacanja) unmarshaller.unmarshal(new File("./files/"+ fajlIme));
		List<AnalitikaIzvoda> ai = stavke.analitikaIzvoda;
		for(int i = 0; i < ai.size(); i++) {
			if(ai.get(i).tipTransakcije == TipTransakcije.TRANSFER) 
				analitikaIzvodaServis.transferSredstava(ai.get(i), ai.get(i).valuta.zvanicnaSifra, (long) 1);
			else if(ai.get(i).tipTransakcije == TipTransakcije.UPLATA) 
				analitikaIzvodaServis.uplataNaRacun(ai.get(i), ai.get(i).valuta.zvanicnaSifra, (long) 1);
			else 
				analitikaIzvodaServis.isplataSaRacuna(ai.get(i), ai.get(i).valuta.zvanicnaSifra, (long) 1);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	
	}
	
	
	@AutorizacijaAnnotation(imeMetode = "exportMedjubankarskogPrenosa")
	@GetMapping(path = "/exportMedjubankarskogPrenosa/{idMP}")
	public ResponseEntity<?> exportMedjubankarskogPrenosa(@PathVariable("idMP") Long id) throws JAXBException {
		MedjubankarskiPrenos mp = medjubankarskiPrenosServis.preuzmiMedjubankarskiPrenos(id);
		File fajl = new File("./files/exportovaniMedjubankarskiPrenos"+mp.id.toString()+".xml");
		JAXBContext context = JAXBContext.newInstance(MedjubankarskiPrenos.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(mp, fajl);
		return new ResponseEntity<>(HttpStatus.OK);
	
	}
	
	@AutorizacijaAnnotation(imeMetode = "exportAnalitikaKlijenta")
	@GetMapping(path = "/exportAnalitikaKlijenta/{id}")
	public ResponseEntity<?> exportAnalitikaKlijenta(@PathVariable("id") Long id) throws JAXBException {
		StavkePlacanja sp = new StavkePlacanja();
		List<AnalitikaIzvoda> ai = analitikaIzvodaServis.preuzmiAnalitikeKlijenta(id);
		sp.analitikaIzvoda = ai;
		Klijent k = klijentServis.preuzmiKlijenta(id);
		File fajl = new File("./files/exportovaneAnalitikeKlijenta"+k.id.toString()+".xml");
		JAXBContext context = JAXBContext.newInstance(StavkePlacanja.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(sp, fajl);
		return new ResponseEntity<>(HttpStatus.OK);
	
	}
	
}
