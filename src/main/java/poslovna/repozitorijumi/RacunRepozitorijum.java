package poslovna.repozitorijumi;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import poslovna.model.Banka;
import poslovna.model.Klijent;
import poslovna.model.Racun;
import poslovna.model.Valuta;

public interface RacunRepozitorijum extends JpaRepository<Racun, Long> {

	List<Racun> findByBanka(Banka banka);

	List<Racun> findByBankaAndValuta(Banka b, Valuta findOne);

	List<Racun> findByBankaAndKlijent(Banka b, Klijent findOne);

	List<Racun> findByBankaAndVazeci(Banka b, Boolean vazeci);

	List<Racun> findByBankaAndBrojRacuna(Banka b, String brojRacuna);

	List<Racun> findByBankaAndDatumOtvaranja(Banka b, Date datumOtvaranja);

	List<Racun> findByBankaAndDatumZatvaranja(Banka b, Date datumZatvaranja);

	

}
