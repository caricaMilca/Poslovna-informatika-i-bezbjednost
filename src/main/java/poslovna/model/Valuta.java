package poslovna.model;

import java.util.ArrayList;
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
	public Integer zvanicnaSifra;

	@Column(nullable = false)
	public String naziv;

	@Column(columnDefinition = "boolean default true", insertable = true)
	public Boolean domicilna = true;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "valuta", cascade = CascadeType.ALL)
	@JsonIgnore
	public Collection<Drzava> drzave = new ArrayList<Drzava>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "osnovnaValuta", cascade = CascadeType.ALL)
	@JsonIgnore
	public Collection<KursUValuti> osnovniKurs = new ArrayList<KursUValuti>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "premaValuti", cascade = CascadeType.ALL)
	@JsonIgnore
	public Collection<KursUValuti> premaKurs = new ArrayList<KursUValuti>();

	// kad se bude radila analitika izvoda dodat je :D 
	public Valuta() {
		super();
	}

}
