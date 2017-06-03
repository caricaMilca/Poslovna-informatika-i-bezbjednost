package poslovna.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Drzava {

	@Id
	@GeneratedValue
	public Long id;

	@Column(nullable=false)
	public String naziv;

	@Column(unique=true,nullable=false)
	public String sifra;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "drzava", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<NaseljenoMjesto> naseljenaMjesta  = new HashSet<NaseljenoMjesto>();;

	@ManyToOne
	public Valuta valuta;

	public Drzava(String naziv, String sifra) {
		super();
		this.naziv = naziv;
		this.sifra = sifra;
	}

	public Drzava() {
		super();
	}

}
