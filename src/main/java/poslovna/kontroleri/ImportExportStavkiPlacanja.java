package poslovna.kontroleri;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
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
import poslovna.model.StavkePlacanja;
import poslovna.model.TipTransakcije;
import poslovna.servisi.AnalitikaIzvodaServis;

@RestController
@RequestMapping("/importExportStavkiPlacanja")
public class ImportExportStavkiPlacanja {

	@Autowired
	HttpSession sesija;
	
	@Autowired
	AnalitikaIzvodaServis analitikaIzvodaServis;
	
	@AutorizacijaAnnotation(imeMetode = "ucitajFajl")
	@GetMapping(path = "/ucitajFajl/{fajlIme:.+}")
	public ResponseEntity<?> ucitajFajl(@PathVariable("fajlIme") String fajlIme) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance("poslovna.model");
		Unmarshaller unmarshaller = context.createUnmarshaller();
		//StavkePlacanja stavke = (StavkePlacanja) unmarshaller.unmarshal(new File(fajlIme));
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
}
