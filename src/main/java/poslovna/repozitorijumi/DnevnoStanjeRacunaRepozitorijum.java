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
	@Query("select k from DnevnoStanjeRacuna k inner join k.racun as r where r.banka=?2 and r = ?1")
	List<DnevnoStanjeRacuna> findByRacun(Racun findOne, Banka b);
	@Query("select k from DnevnoStanjeRacuna k inner join k.racun as r where r.banka=?2 and k.datumPrometa = ?1")
	List<DnevnoStanjeRacuna> findByDatumPrometa(Date datumPrometa, Banka b);
	@Query("select k from DnevnoStanjeRacuna k inner join k.racun as r where r.banka=?2 and k.novoStanje = ?1")
	List<DnevnoStanjeRacuna> findByNovoStanje(Double novoStanje, Banka b);
	@Query("select k from DnevnoStanjeRacuna k inner join k.racun as r where r.banka=?2 and k.prethodnoStanje = ?1")
	List<DnevnoStanjeRacuna> findByPrethodnoStanje(Double prethodnoStanje, Banka b);
	@Query("select k from DnevnoStanjeRacuna k inner join k.racun as r where r.banka=?2 and k.prometNaKorist = ?1")
	List<DnevnoStanjeRacuna> findByPrometNaKorist(Double prometNaKorist, Banka b);
	@Query("select k from DnevnoStanjeRacuna k inner join k.racun as r where r.banka=?2 and k.prometNaTeret = ?1")
	List<DnevnoStanjeRacuna> findByPrometNaTeret(Double prometNaTeret, Banka b);

}
