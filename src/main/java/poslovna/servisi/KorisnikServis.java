package poslovna.servisi;

import poslovna.model.Korisnik;

public interface KorisnikServis {

	Korisnik logovanje(String korisnickoIme, String lozinka);

	Korisnik save(Korisnik k);
	
	void promenaLozinke(String l);
	
	Korisnik preuzmiKorisnika(Long id);
}
