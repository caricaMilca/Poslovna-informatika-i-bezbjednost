package poslovna.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class DnevnoStanjeRacuna {

	@Id
	@GeneratedValue
	public Long id;

	public Double prethodnoStanje;

	public Double prometNaTeret;

	public Double prometNaKorist;

	public Double novoStanje;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "mm.dd.yyyy")
	public Date datumPrometa;

	@ManyToOne
	public Racun racun;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dnevnoStanjeRacuna", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<AnalitikaIzvoda> izvodi = new HashSet<AnalitikaIzvoda>();

	public DnevnoStanjeRacuna() {
		super();
	}

}
