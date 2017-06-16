package poslovna.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Zaposleni extends Korisnik{
	
	@Enumerated(EnumType.STRING)
	public UlogaZaposlenog ulogaZ;
	
	@ManyToOne
	public Banka banka;
	
	public Zaposleni(String ime, String prezime, String korisnickoIme, String lozinka, UlogaZaposlenog uloga) {
		super(ime, prezime, korisnickoIme, lozinka);
		this.ulogaZ = uloga;
	}

	public Zaposleni() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
