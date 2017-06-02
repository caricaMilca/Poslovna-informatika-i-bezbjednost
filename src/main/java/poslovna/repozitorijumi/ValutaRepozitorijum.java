package poslovna.repozitorijumi;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poslovna.model.Valuta;

public interface ValutaRepozitorijum extends JpaRepository<Valuta, Long> {

	Valuta findByZvanicnaSifra(Integer zvanicnaSifra);

	@Query("select v from Valuta v where v.zvanicnaSifra = ?1 or lower(v.naziv) like ?2 or v.domicilna = ?3")
	List<Valuta> findByZvanicnaSifraOrNazivLikeOrDomicilna(Integer zvanicnaSifra, String naziv,
			Boolean domicilna);

	@Query("select v from Valuta v where lower(v.naziv) like ?1 or v.domicilna = ?2")
	List<Valuta> findByNazivLikeOrDomicilna(String naziv, Boolean domicilna);

	@Query("select v from Valuta v where v.zvanicnaSifra = ?1 or lower(v.naziv) like ?2")
	List<Valuta> findByZvanicnaSifraOrNazivLike(Integer zvanicnaSifra, String naziv);

	@Query("select v from Valuta v where lower(v.naziv) like ?1")
	List<Valuta> findByNazivLike(String naziv);

}
