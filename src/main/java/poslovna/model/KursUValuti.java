package poslovna.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class KursUValuti {

	@Id
	@GeneratedValue
	public Long id;

	public Double kupovni;

	public Double prodajni;

	public Double srednji;

	@ManyToOne
	public Valuta premaValuti;
	
	@ManyToOne
	public Valuta osnovnaValuta;
	
	@ManyToOne
	public KursnaLista kursnaLista;

	public KursUValuti() {
		super();
	}

}
