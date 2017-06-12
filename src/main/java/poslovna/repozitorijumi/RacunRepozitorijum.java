package poslovna.repozitorijumi;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poslovna.model.Banka;
import poslovna.model.Klijent;
import poslovna.model.Racun;
import poslovna.model.Valuta;

public interface RacunRepozitorijum extends JpaRepository<Racun, Long> {

	List<Racun> findByBanka(Banka banka);

	List<Racun> findByVazeci(Boolean vazeci);

	@Query("select r from Racun r where lower(r.brojRacuna) like %?1%")
	List<Racun> findByRacunLike(String brojRacuna);

	List<Racun> findByDatumOtvaranja(Date datumOtvaranja);

	List<Racun> findByDatumZatvaranja(Date datumZatvaranja);

	List<Racun> findByKlijent(Klijent findOne);

	List<Racun> findByValuta(Valuta findOne);

	Racun findByBrojRacuna(String brojRacuna);

	Racun findByBrojRacunaAndBanka(String racunPovjerioca, Banka banka);

	

}
