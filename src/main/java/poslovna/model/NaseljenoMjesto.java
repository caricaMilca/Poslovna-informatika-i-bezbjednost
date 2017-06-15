package poslovna.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement()
@Entity
public class NaseljenoMjesto {

	@Id
	@GeneratedValue
	public Long id;

	@Column(nullable = false)
	public String naziv;

	@Column(unique = true, nullable = false)
	public String pttOznaka;

	@ManyToOne
	public Drzava drzava;

	public NaseljenoMjesto(String naziv, String pttOznaka) {
		super();
		this.naziv = naziv;
		this.pttOznaka = pttOznaka;
	}

	public NaseljenoMjesto() {
		super();
	}

}
