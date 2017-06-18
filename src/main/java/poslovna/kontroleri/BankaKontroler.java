package poslovna.kontroleri;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JRException;
import poslovna.autorizacija.AutorizacijaAnnotation;
import poslovna.servisi.BankaServis;

@RestController
@RequestMapping("/banka")
public class BankaKontroler {
		
	@Autowired
	BankaServis bankaServis;
	
	
	@AutorizacijaAnnotation(imeMetode = "izvestaj")
	@GetMapping(path = "/izvestaj")
	@ResponseStatus(HttpStatus.OK)
	public void exportToPdf(HttpServletResponse response)throws JRException, IOException{
		bankaServis.exportToPDF(response);
	}


	@AutorizacijaAnnotation(imeMetode = "izvestajZaKlijenta")
	@GetMapping(path = "/kreirajIzvestajZaKlijenta/{datumPocetka}/{datumKraja}/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void exportToPdf2(HttpServletResponse response,@PathVariable("datumPocetka") Date datumPocetka,@PathVariable("datumKraja") Date datumKraja,@PathVariable("id") Long id)throws JRException, IOException{
		bankaServis.exportToPDF2(response,datumPocetka,datumKraja,id);
	}

}
