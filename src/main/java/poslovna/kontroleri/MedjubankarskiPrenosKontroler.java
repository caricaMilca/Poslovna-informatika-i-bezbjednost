package poslovna.kontroleri;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poslovna.autorizacija.AutorizacijaAnnotation;
import poslovna.model.MedjubankarskiPrenos;
import poslovna.servisi.MedjubankarskiPrenosServis;

@RestController
@RequestMapping("/medjubankarskiPrenos")
public class MedjubankarskiPrenosKontroler {

	@Autowired
	MedjubankarskiPrenosServis medjubankarskiPrenosServis;
	
	@AutorizacijaAnnotation(imeMetode = "sviMedjubankarskiPrenosi")
	@GetMapping(path = "/sviMedjubankarskiPrenosi")
	public ResponseEntity<List<MedjubankarskiPrenos>> sviMedjubankarskiPrenosi() {
		return medjubankarskiPrenosServis.sviMedjubankarskiPrenosi();
	}

}
