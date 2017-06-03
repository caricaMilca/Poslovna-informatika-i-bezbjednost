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
public class VrstaPlacanja {

	@Id
	@GeneratedValue
	public Long id;
	
	public String naziv;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vrstaPlacanja", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<AnalitikaIzvoda> izvodi = new HashSet<AnalitikaIzvoda>();

	public VrstaPlacanja() {
		super();
	}
	
	
}
