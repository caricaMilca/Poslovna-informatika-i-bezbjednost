package poslovna.kontroleri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import poslovna.autorizacija.AutorizacijaAnnotation;
import poslovna.model.Zaposleni;

@RestController
@RequestMapping("/banka")
public class BankaKontroler {

	@Autowired
	HttpSession sesija;
	
	@Autowired
	DataSource dataSource;
	
	/*@Autowired
	BankaServis bankaServis;

	@AutorizacijaAnnotation(imeMetode = "preuzmiBanku")
	@GetMapping(path = "/preuzmiBanku")
	public ResponseEntity<Banka> preuzmiBanku() {
		return bankaServis.preuzmiBanku();

	}*/
	
	@AutorizacijaAnnotation(imeMetode = "izvestaj")
	@GetMapping(path = "/izvestaj")
	@ResponseStatus(HttpStatus.OK)
	public void exportToPdf(HttpServletResponse response)throws JRException, IOException{
		Zaposleni k = (Zaposleni) sesija.getAttribute("korisnik");
		
	    String outputFile ="D:\\SpisakRacuna.pdf";
		
		Map<String,Object> parametersMap = new HashMap<>();  
		parametersMap.put("idbanke",k.banka.id);
		JasperPrint jp;
		try {
			jp = JasperFillManager.fillReport(getClass().getResource("/jasper/Izvestaj.jasper").openStream(),parametersMap,dataSource.getConnection());			
			//eksport
			File file = new File(outputFile);
			OutputStream outputStream = new FileOutputStream(file);
			JasperExportManager.exportReportToPdfStream(jp, outputStream);
			response.setContentType("application/pdf");
			InputStream inputStream = new FileInputStream(file);
			IOUtils.copy(inputStream, response.getOutputStream());
			} catch (SQLException e) {
					e.printStackTrace();
			}
		}

	

}
