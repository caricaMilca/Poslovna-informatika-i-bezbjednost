package poslovna.servisi;

import java.util.List;

import org.springframework.http.ResponseEntity;

import poslovna.model.MedjubankarskiPrenos;

public interface MedjubankarskiPrenosServis {

	MedjubankarskiPrenos preuzmiMedjubankarskiPrenos(Long id);

	ResponseEntity<List<MedjubankarskiPrenos>> sviMedjubankarskiPrenosi();

}
