package poslovna.kontroleri;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
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

	

}
