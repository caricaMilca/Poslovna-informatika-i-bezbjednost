package poslovna.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@XmlRootElement()
@Entity
public class Banka {

	@Id
	@GeneratedValue
	public Long id;

	public Integer sifra;

	public Integer pib;

	@Column(unique = true, nullable = false)
	public String banka3kod;

	public String adresa;

	public String naziv;

	public String email;

	public String web;

	public String telefon;

	public String fax;

	public String obracunskiRacun;

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "banka", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<KursnaLista> kursneListe = new HashSet<KursnaLista>();

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "banka", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<Racun> racuni = new HashSet<Racun>();

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "banka", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<Zaposleni> korisnici = new HashSet<Zaposleni>();

	public Banka() {
		super();
		// TODO Auto-generated constructor stub
	}

}
