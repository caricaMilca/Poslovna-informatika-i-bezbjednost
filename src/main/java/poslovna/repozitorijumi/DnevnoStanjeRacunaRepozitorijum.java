package poslovna.repozitorijumi;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poslovna.model.Banka;
import poslovna.model.DnevnoStanjeRacuna;
import poslovna.model.Racun;

public interface DnevnoStanjeRacunaRepozitorijum extends JpaRepository<DnevnoStanjeRacuna, Long> {

	@Query("select k from DnevnoStanjeRacuna k inner join k.racun as r where r.banka=?1")
	List<DnevnoStanjeRacuna> dnevnaStanjaUBanci(Banka b);

	List<DnevnoStanjeRacuna> findByRacun(Racun findOne);

	List<DnevnoStanjeRacuna> findByDatumPrometa(Date datumPrometa);

	List<DnevnoStanjeRacuna> findByNovoStanje(Double novoStanje);

	List<DnevnoStanjeRacuna> findByPrethodnoStanje(Double prethodnoStanje);

	List<DnevnoStanjeRacuna> findByPrometNaKorist(Double prometNaKorist);

	List<DnevnoStanjeRacuna> findByPrometNaTeret(Double prometNaTeret);

}
