package poslovna.servisi;

import poslovna.model.Korisnik;

public interface KorisnikServis {

	Korisnik logovanje(String korisnickoIme);

	Korisnik save(Korisnik k);
	
	boolean promenaLozinke(String l);
	
	Korisnik preuzmiKorisnika(Long id);
}
