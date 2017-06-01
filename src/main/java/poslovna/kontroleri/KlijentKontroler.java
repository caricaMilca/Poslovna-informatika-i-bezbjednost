package poslovna.kontroleri;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poslovna.model.Klijent;
import poslovna.model.Korisnik;
import poslovna.model.Privilege;
import poslovna.model.Role;
import poslovna.servisi.KlijentServis;

@RestController
@RequestMapping("/klijent")
public class KlijentKontroler {

	@Autowired
	HttpSession sesija;
	
	@Autowired
	KlijentServis klijentServis;
	
	@GetMapping(path = "/preuzmiKlijenta")
	public ResponseEntity<Klijent> preuzmiKlijenta() {
		if(authorize("preuzmiKlijenta") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		return new ResponseEntity<Klijent>(klijentServis.preuzmiKlijenta(k.id), HttpStatus.OK);
	}
	
	@GetMapping(path = "/sviKlijenti")
	public ResponseEntity<List<Klijent>> sviKlijenti() {
		if(authorize("sviKlijenti") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return klijentServis.sviKlijenti();
	}
	
	@GetMapping(path = "/sviKlijentiDjelatnosti/{idDjelatnosti}")
	public ResponseEntity<List<Klijent>> sviKlijentiDjelatnosti( @PathVariable("idDjelatnosti") Long idDjelatnosti) {
		if(authorize("sviKlijentiDjelatnostis") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return klijentServis.sviKlijentiDjelatnosti(idDjelatnosti);
	}
	
	@PutMapping(path = "/pretraziKlijente/{idDjelatnosti}")
	public ResponseEntity<List<Klijent>> pretraziKlijente(@RequestBody (required=false) Klijent klijent, @PathVariable("idDjelatnosti") Long idDjelatnosti) {
		if(authorize("pretraziKlijente") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return klijentServis.pretraziKlijente(klijent,idDjelatnosti);
	}
	
	public String authorize(String imeOperacije){
	    if((Korisnik) sesija.getAttribute("korisnik") == null) 
			return "Ne";
	    Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		 Iterator<Role> iterator = k.roles.iterator();

	        while (iterator.hasNext()) {
	        Iterator<Privilege> it = iterator.next().privileges.iterator();
	        while(it.hasNext())
	        	if(it.next().name.equals(imeOperacije))
	        		return "Da";
	        }
	        return "Ne";
	} 
	
}
