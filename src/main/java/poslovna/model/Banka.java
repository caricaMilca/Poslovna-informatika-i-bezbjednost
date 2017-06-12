package poslovna.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Banka {

	@Id
	@GeneratedValue
	public Long id;

	public Integer sifra;

	public Integer pib;
	
	public String banka_3kod;

	public String adresa;

	public String naziv;

	public String email;

	public String web;

	public String telefon;

	public String fax;

	public String obracunskiRacun;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "banka", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<KursnaLista> kursneListe = new HashSet<KursnaLista>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "banka", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<Racun> racuni = new HashSet<Racun>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "banka", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<Korisnik> korisnici = new HashSet<Korisnik>();
	
	public Banka() {
		super();
		// TODO Auto-generated constructor stub
	}

}
