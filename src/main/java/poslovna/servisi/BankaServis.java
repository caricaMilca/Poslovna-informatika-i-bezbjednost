package poslovna.servisi;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import net.sf.jasperreports.engine.JRException;
import poslovna.model.Banka;

public interface BankaServis {

	ResponseEntity<Banka> preuzmiBanku();
	
	 void exportToPDF(HttpServletResponse response) throws JRException, IOException;
}
