package poslovna.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class MedjubankarskiPrenos {

	@Id
	@GeneratedValue
	public Long id;
	
	@ManyToOne
	public Banka bankaPosiljalac;
	
	@ManyToOne
	public Banka bankaPrimalac;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "medjubankarskiPrenos", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<AnalitikaIzvoda> izvodi = new HashSet<AnalitikaIzvoda>();
	
	public Timestamp datum; 
	
	@Enumerated(EnumType.STRING)
	public TipPoruke tipPoruke;

	public Double iznos;
	
	public MedjubankarskiPrenos() {
		super();
	}

}
