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
public class Valuta {

	@Id
	@GeneratedValue
	public Long id;

	@Column(nullable = false, unique = true)
	public String zvanicnaSifra;

	@Column( nullable = false)
	public String naziv;
	
	@Column(columnDefinition = "boolean default true", insertable = true)
	public boolean domicilna = true;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "valuta", cascade = CascadeType.ALL)
	@JsonIgnore
	public Collection<Drzava> drzave;

	public Valuta() {
		super();
	}
	
	
}
