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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	    "zvanicnaSifra",
	    "naziv",
	    "domicilna"
	})
@XmlRootElement(name = "valuta")
@Entity
public class Valuta {

	@XmlTransient
	@Id
	@GeneratedValue
	public Long id;

	@XmlElement
	@Column(nullable = false, unique = true)
	public String zvanicnaSifra;

	@XmlElement
	@Column(nullable = false)
	public String naziv;

	@XmlElement
	@Column(columnDefinition = "boolean default true", insertable = true)
	public Boolean domicilna = true;

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "valuta", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<Drzava> drzave = new HashSet<Drzava>();

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "osnovnaValuta", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<KursUValuti> osnovniKurs = new HashSet<KursUValuti>();

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "premaValuti", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<KursUValuti> premaKurs = new HashSet<KursUValuti>();
	
	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "valuta", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<AnalitikaIzvoda> analitike = new HashSet<AnalitikaIzvoda>();

	// kad se bude radila analitika izvoda dodat je :D 
	public Valuta() {
		super();
	}

}
