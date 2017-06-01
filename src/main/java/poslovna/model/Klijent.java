package poslovna.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Klijent extends Korisnik {

	@Enumerated(EnumType.STRING)
	public UlogaKlijenta ulogaK;
	
	@ManyToOne
	public Djelatnost djelatnost;

	

	public Klijent(String ime, String prezime, String korisnickoIme, String lozinka, UlogaKlijenta ulogaK) {
		super(ime, prezime, korisnickoIme, lozinka);
		this.ulogaK = ulogaK;
	}

	public Klijent() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	
}
