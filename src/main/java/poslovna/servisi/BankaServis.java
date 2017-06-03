package poslovna.servisi;

import org.springframework.http.ResponseEntity;

import poslovna.model.Banka;

public interface BankaServis {

	ResponseEntity<Banka> preuzmiBanku();

}
