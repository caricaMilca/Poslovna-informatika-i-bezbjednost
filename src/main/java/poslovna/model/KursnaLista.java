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
public class KursnaLista {

	@Id
	@GeneratedValue
	public Long id;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "mm.dd.yyyy")
	public Date datum;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "mm.dd.yyyy")
	public Date primjenjujeSeOd;

	public Integer broj;

	@ManyToOne
	public Banka banka;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "kursnaLista", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<KursUValuti> kursUValutama  = new HashSet<KursUValuti>();

	public KursnaLista() {
		super();
	}

}
