package poslovna.model;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Racun {

	@Id
	@GeneratedValue
	public Long id;
	
	@Column(unique=true,nullable=false)
	public String brojRacuna;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "mm.dd.yyyy")
	public Date datumOtvaranja;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "mm.dd.yyyy")
	public Date datumZatvaranja;
	
	@Column(columnDefinition = "boolean default true", insertable = true)
	public Boolean vazeci = true;
	
	public String racunPrenosa; //razmisliti o realizaciji posebno za racunNasljednik iz iste banke 
	
	@ManyToOne
	public Klijent klijent;
	
	@ManyToOne
	public Banka banka;
	
	@ManyToOne
	public Valuta valuta;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "racun", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<DnevnoStanjeRacuna> dnevnaStanja = new HashSet<DnevnoStanjeRacuna>();
	
	public Racun() {
		super();
	}
	
	
}
