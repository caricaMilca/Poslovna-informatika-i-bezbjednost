package poslovna.repozitorijumi;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poslovna.model.Djelatnost;

public interface DjelatnostRepozitorijum extends JpaRepository<Djelatnost, Long> {

	Djelatnost findBySifra(String sifra);

	@Query("select d from Djelatnost d where lower(d.naziv) like ?1 or lower(d.sifra) like ?2")
	List<Djelatnost> findByNazivLikeOrSifraLike(String naziv, String sifra);
}
