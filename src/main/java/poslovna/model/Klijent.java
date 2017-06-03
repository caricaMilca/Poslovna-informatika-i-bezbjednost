package poslovna.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class Klijent extends Korisnik {

	@Enumerated(EnumType.STRING)
	public UlogaKlijenta ulogaK;
	
	@ManyToOne
	public Djelatnost djelatnost;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "klijent", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<Racun> racuni = new HashSet<Racun>();

	public Klijent(String ime, String prezime, String korisnickoIme, String lozinka, UlogaKlijenta ulogaK) {
		super(ime, prezime, korisnickoIme, lozinka);
		this.ulogaK = ulogaK;
	}

	public Klijent() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	
}
