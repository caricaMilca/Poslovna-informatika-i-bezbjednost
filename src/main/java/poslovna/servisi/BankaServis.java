package poslovna.servisi;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import net.sf.jasperreports.engine.JRException;
import poslovna.model.Banka;

public interface BankaServis {

	ResponseEntity<Banka> preuzmiBanku();
	
	 void exportToPDF(HttpServletResponse response) throws JRException, IOException;
	 
	 void exportToPDF2(HttpServletResponse response,Date datum,Date datum2, Long id) throws JRException, IOException;
}
