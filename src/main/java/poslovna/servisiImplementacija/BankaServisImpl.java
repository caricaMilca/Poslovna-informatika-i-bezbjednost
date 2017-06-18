package poslovna.servisiImplementacija;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import poslovna.model.Banka;
import poslovna.model.Korisnik;
import poslovna.model.Zaposleni;
import poslovna.repozitorijumi.BankaRepozitorijum;
import poslovna.repozitorijumi.KorisnikRepozitorijum;
import poslovna.servisi.BankaServis;

@Service
@Transactional
public class BankaServisImpl implements BankaServis {

	@Autowired
	BankaRepozitorijum bankaRepozitorijum;
	
	@Autowired
	KorisnikRepozitorijum korisnikRep;
	
	@Autowired
	HttpSession sesija;
	
	@Autowired
	DataSource dataSource;
	
	@Override
	public ResponseEntity<Banka> preuzmiBanku() {
		return new ResponseEntity<Banka>(bankaRepozitorijum.findOne((long) 1), HttpStatus.OK);
	}

	@Override
	public void exportToPDF(HttpServletResponse response) throws JRException, IOException{
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

	@Override
	public void exportToPDF2(HttpServletResponse response,Date datumPocetka,Date datumKraja,Long id) throws JRException, IOException {		
	    String outputFile ="D:\\IzvodiKlijenta.pdf";
	    
		Korisnik k = korisnikRep.findOne(id);
		Map<String,Object> parametersMap = new HashMap<>();  
		parametersMap.put("datumPocetka", datumPocetka);
		parametersMap.put("datumKraja", datumKraja);
		parametersMap.put("klijentid", id);
		parametersMap.put("klijent", k.ime+" "+k.prezime);
		
		JasperPrint jp;
		try {
			jp = JasperFillManager.fillReport(getClass().getResource("/jasper/izvestajZaKlijenta.jasper").openStream(),parametersMap,dataSource.getConnection());			
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
