package poslovna.repozitorijumi;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poslovna.model.Drzava;
import poslovna.model.NaseljenoMjesto;

public interface NaseljenoMjestoRepozitorijum extends JpaRepository<NaseljenoMjesto, Long> {

	List<NaseljenoMjesto> findByPttOznaka(String pttOznaka);

	List<NaseljenoMjesto> findByDrzava(Drzava findOne);
	
	List<NaseljenoMjesto> findByNaziv(String naziv);

	@Query("select nm from NaseljenoMjesto nm where lower(nm.naziv) like ?1 or lower(nm.pttOznaka) like ?2")
	List<NaseljenoMjesto> findByNazivLikeOrPttOznakaLike(String naziv, String pttOznaka);
	
	@Query("select nm from NaseljenoMjesto nm where lower(nm.naziv) like ?1 or lower(nm.pttOznaka) like ?2 or nm.drzava = ?3")
	List<NaseljenoMjesto> findByNazivLikeOrPttOznakaLikeOrDrzava(String naziv, String pttOznaka, Drzava findOne);

}
