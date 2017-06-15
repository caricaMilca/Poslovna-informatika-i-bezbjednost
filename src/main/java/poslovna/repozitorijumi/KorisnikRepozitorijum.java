package poslovna.repozitorijumi;

import org.springframework.data.jpa.repository.JpaRepository;

import poslovna.model.Korisnik;

public interface KorisnikRepozitorijum extends JpaRepository<Korisnik, Long> {

	
	Korisnik findByKorisnickoImeAndLozinka(String korisnickoIme, String lozinka);

	Korisnik findByKorisnickoIme(String korisnickoIme);

}
