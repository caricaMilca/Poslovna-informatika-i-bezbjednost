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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.springframework.format.annotation.DateTimeFormat;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	    "duznik",
	    "povjerilac",
	    "svrhaPlacanja",
	    "datumPrimanja",
	    "datumValute",
	    "racunDuznika",
	    "racunPovjerioca",
	    "modelZaduzenja",
	    "pozivNaBrojZaduzenja",
	    "pozivNaBrojOdobrenja",
	    "modelOdobrenja",
	    "hitno",
	    "iznos",
	    "tipTransakcije",
	    "valuta"
	})
@XmlRootElement(name = "analitikaIzvoda")
@Entity
public class AnalitikaIzvoda {

	@XmlTransient
	@Id
	@GeneratedValue
	public Long id;

	@XmlElement
	public String duznik;

	@XmlElement
	public String povjerilac;

	@XmlElement
	public String svrhaPlacanja;

	@XmlElement(name = "datumPrimanja", required = true)
	@XmlSchemaType(name = "datumPrimanja")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "mm.dd.yyyy")
	public Date datumPrimanja;

	@XmlElement(name = "datumValute")
	@XmlSchemaType(name = "datumValute")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "mm.dd.yyyy")
	public Date datumValute;

	@XmlElement
	public String racunDuznika;

	@XmlElement
	public String racunPovjerioca;

	@XmlElement
	public Integer modelZaduzenja;

	@XmlElement
	public String pozivNaBrojZaduzenja;

	@XmlElement
	public String pozivNaBrojOdobrenja;

	@XmlElement
	public Integer modelOdobrenja;

	@XmlElement
	@Column(columnDefinition = "boolean default false", insertable = true)
	public Boolean hitno = false;

	@XmlElement
	public Double iznos;

	@XmlTransient
	@Enumerated(EnumType.STRING)
	public TipGreske tipGreske;

	@XmlElement
	@Enumerated(EnumType.STRING)
	public TipTransakcije tipTransakcije;

	@XmlTransient
	@Enumerated(EnumType.STRING)
	public SmijerTransakcije smijer;

	@XmlTransient
	@Enumerated(EnumType.STRING)
	public TipPoruke tipPoruke;

	
	@XmlTransient
	@ManyToOne(cascade = CascadeType.ALL)
	public DnevnoStanjeRacuna dnevnoStanjeRacuna;

	@XmlTransient
	@ManyToOne
	public VrstaPlacanja vrstaPlacanja;

	@XmlElement
	@ManyToOne(cascade = CascadeType.ALL)
	public Valuta valuta;

	@XmlTransient
	@ManyToOne
	public MedjubankarskiPrenos medjubankarskiPrenos;

	@XmlTransient
	@ManyToOne
	public NaseljenoMjesto mestoPlacanja;

	public AnalitikaIzvoda() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AnalitikaIzvoda(String duznik, String povjerilac, String svrhaPlacanja, Date datumPrimanja, Date datumValute,
			String racunDuznika, String racunPovjerioca, Integer modelZaduzenja, String pozivNaBrojZaduzenja,
			String pozivNaBrojOdobrenja, Integer modelOdobrenja, Boolean hitno, Double iznos,
			TipTransakcije tipTransakcije, SmijerTransakcije smijer) {
		super();
		this.duznik = duznik;
		this.povjerilac = povjerilac;
		this.svrhaPlacanja = svrhaPlacanja;
		this.datumPrimanja = datumPrimanja;
		this.datumValute = datumValute;
		this.racunDuznika = racunDuznika;
		this.racunPovjerioca = racunPovjerioca;
		this.modelZaduzenja = modelZaduzenja;
		this.pozivNaBrojZaduzenja = pozivNaBrojZaduzenja;
		this.pozivNaBrojOdobrenja = pozivNaBrojOdobrenja;
		this.modelOdobrenja = modelOdobrenja;
		this.hitno = hitno;
		this.iznos = iznos;
		this.tipTransakcije = tipTransakcije;
		this.smijer = smijer;

	}

}
