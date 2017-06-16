package poslovna.kontroleri;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import poslovna.autorizacija.AutorizacijaAnnotation;
import poslovna.model.Korisnik;

@RestController
@RequestMapping("/banka")
public class BankaKontroler {

	@Autowired
	HttpSession sesija;
	
	@Autowired
	DataSource source;
	
	/*@Autowired
	BankaServis bankaServis;

	@AutorizacijaAnnotation(imeMetode = "preuzmiBanku")
	@GetMapping(path = "/preuzmiBanku")
	public ResponseEntity<Banka> preuzmiBanku() {
		return bankaServis.preuzmiBanku();

	}*/
	
	@SuppressWarnings("unchecked")
	@AutorizacijaAnnotation(imeMetode = "izvestaj")
	@GetMapping(path = "/izvestaj")
	public void exportToPdf(){
		System.out.println("aaaaaaaa");
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		Map<String,Object> parametersMap = new HashMap<>();  
		parametersMap.put("idbanke",k.banka.id);
		try {
			System.out.println(source.getConnection());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			JasperPrint jp = JasperFillManager.fillReport(
					getClass().getResource("/jasper/Izvestaj.jasper").openStream(),
			parametersMap, source.getConnection());
			//eksport
			File pdf = File.createTempFile("output.", ".pdf");
			JasperExportManager.exportReportToPdfStream(jp, new FileOutputStream(pdf));
		}catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	

}
