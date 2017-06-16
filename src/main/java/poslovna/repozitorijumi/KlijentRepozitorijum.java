package poslovna.repozitorijumi;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poslovna.model.Banka;
import poslovna.model.Djelatnost;
import poslovna.model.Klijent;
import poslovna.model.UlogaKlijenta;

public interface KlijentRepozitorijum extends JpaRepository<Klijent, Long> {

	@Query("select k from Klijent k where lower(k.korisnickoIme) like %?1%")
	List<Klijent> findByKorisnickoImeLike(String korisnickoIme);
	
	Klijent findByKorisnickoIme(String korisnickoIme);

	List<Klijent> findByDjelatnost(Djelatnost findOne);

	@Query("select k from Klijent k where k.ime like ?1% and k.prezime like ?2% and k.korisnickoIme like ?3%")
	List<Klijent> findByImeLikeAndPrezimeLikeAndKorisnickoImeLike(String ime, String prezime,
			String korisnickoIme);

	List<Klijent> findByUlogaK(UlogaKlijenta ulogaK);

	@Query("select k from Klijent k inner join k.racuni as r where r.banka = ?1")
	List<Klijent> findByBanka(Banka banka);
	
	@Query("select k from Klijent k where lower(k.prezime) like %?1%")
	List<Klijent> findByPrezime(String prezime);

	@Query("select k from Klijent k where lower(k.ime) like %?1%")
	List<Klijent> findByIme(String ime);
	
}
