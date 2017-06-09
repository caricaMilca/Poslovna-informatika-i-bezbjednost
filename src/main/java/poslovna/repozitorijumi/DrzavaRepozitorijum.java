package poslovna.repozitorijumi;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poslovna.model.Drzava;
import poslovna.model.Valuta;

public interface DrzavaRepozitorijum extends JpaRepository<Drzava, Long> {

	@Query("select d from Drzava d where lower(d.sifra) like %?1%")
	List<Drzava> findBySifra(String sifra);

	List<Drzava> findByValuta(Valuta v);

	@Query("select d from Drzava d where lower(d.naziv) like ?1 or lower(d.sifra) like ?2")
	List<Drzava> findByNazivLikeOrSifraLike(String naziv, String sifra);
	
	@Query("select d from Drzava d where lower(d.naziv) like ?1 or lower(d.sifra) like ?2 or d.valuta = valuta")
	List<Drzava> findByNazivLikeOrSifraLikeOrValuta(String naziv, String sifra, Valuta valuta);

	@Query("select d from Drzava d where lower(d.naziv) like %?1%")
	List<Drzava> findByNaziv(String naziv);

	List<Drzava> findByValuta(Drzava findOne);

}
