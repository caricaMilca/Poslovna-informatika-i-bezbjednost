package poslovna.repozitorijumi;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poslovna.model.Valuta;

public interface ValutaRepozitorijum extends JpaRepository<Valuta, Long> {

	Valuta findByZvanicnaSifra(String zvanicnaSifra);

	@Query("select v from Valuta v where lower(v.zvanicnaSifra) like ?1 or lower(v.naziv) like ?2 or v.domicilna = ?3")
	List<Valuta> findByZvanicnaSifraLikeOrNazivLikeOrDomicilna(String zvanicnaSifra, String naziv,
			boolean domicilna);

}
