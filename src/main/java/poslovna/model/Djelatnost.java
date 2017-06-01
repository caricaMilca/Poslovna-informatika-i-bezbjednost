package poslovna.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Djelatnost {

	@Id
	@GeneratedValue
	public Long id;

	public String naziv;

	@Column(unique=true,nullable=false)
	public String sifra;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "djelatnost", cascade = CascadeType.ALL)
	@JsonIgnore
	public Collection<Klijent> klijenti;

	public Djelatnost(String naziv, String sifra) {
		super();
		this.naziv = naziv;
		this.sifra = sifra;
	}

	public Djelatnost() {
		super();
		// TODO Auto-generated constructor stub
	}

}
