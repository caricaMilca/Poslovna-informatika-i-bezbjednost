package poslovna.repozitorijumi;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poslovna.model.Djelatnost;
import poslovna.model.Klijent;
import poslovna.model.UlogaKlijenta;

public interface KlijentRepozitorijum extends JpaRepository<Klijent, Long> {

	Klijent findByKorisnickoIme(String korisnickoIme);

	List<Klijent> findByDjelatnost(Djelatnost findOne);

	@Query("select k from Klijent k where lower(k.ime) like ?1 or lower(k.prezime) like ?2 or lower(k.korisnickoIme) like ?3 or k.ulogaK = ?4")
	List<Klijent> findByImeLikeOrPrezimeLikeOrKorisnickoImeLikeOrUlogaK(String ime, String prezime,
			String korisnickoIme, UlogaKlijenta ulogaK);
	
	@Query("select k from Klijent k where lower(k.ime) like ?1 or lower(k.prezime) like ?2 or lower(k.korisnickoIme) like ?3 or k.ulogaK = ?4 or k.djelatnost = ?5")
	List<Klijent> findByImeLikeOrPrezimeLikeOrKorisnickoImeLikeOrUlogaKOrDjelatnost(String ime, String prezime,
			String korisnickoIme, UlogaKlijenta ulogaK, Djelatnost findOne);

	
}
