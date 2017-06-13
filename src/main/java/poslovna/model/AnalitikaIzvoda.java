package poslovna.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class AnalitikaIzvoda {

	@Id
	@GeneratedValue
	public Long id;
	
	public String duznik;
	
	public String povjerilac;
	
	public String svrhaPlacanja;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "mm.dd.yyyy")
	public Date datumPrimanja;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "mm.dd.yyyy")
	public Date datumValute;
	
	public String racunDuznika;
	
	public String racunPovjerioca;
	
	public Integer modelZaduzenja;
	
	public String pozivNaBrojZaduzenja;
	
	public String pozivNaBrojOdobrenja;
	
	public Integer modelOdobrenja;
	
	@Column(columnDefinition = "boolean default false", insertable = true)
	public Boolean hitno = false;
	
	public Double iznos;

	@Enumerated(EnumType.STRING)
	public TipGreske tipGreske;
	
	@Enumerated(EnumType.STRING)
	public TipTransakcije tipTransakcije;
	
	@Enumerated(EnumType.STRING)
	public SmijerTransakcije smijer;
	
	@ManyToOne(cascade=CascadeType.ALL)
	public DnevnoStanjeRacuna dnevnoStanjeRacuna;
	
	@ManyToOne
	public VrstaPlacanja vrstaPlacanja;
	
	@ManyToOne(cascade=CascadeType.ALL)
	public Valuta valuta;
	
	@ManyToOne
	public MedjubankarskiPrenos medjubankarskiPrenos;
	
	@ManyToOne
	public NaseljenoMjesto mestoPlacanja;
	
	@Enumerated(EnumType.STRING)
	public TipPoruke tipPoruke;

	public AnalitikaIzvoda() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	
	
}
