package poslovna.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Korisnik {

	@Id
	@GeneratedValue
	public Long id;

	@Enumerated(EnumType.STRING)
	public UlogaKorisnika uloga;

	// @Size(min = 3, max = 30)
	// @Pattern(regexp = "^[A-Z][a-z_ A-Z]*")
	public String ime;

	// @Size(min = 3, max = 30)
	// @Pattern(regexp = "^[A-Z][a-z_ A-Z]*")
	public String prezime;

	// @Pattern(regexp="\\w*")
	// @NotNull
	// @Size(min = 3, max = 30)
    @Column(unique=true,nullable=false)
	public String korisnickoIme;

	// @Pattern(regexp="\\w*")
	// @NotNull
	// @Size(min = 3, max = 30)
	// @Column(nullable=false)
	public String lozinka;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE, CascadeType.MERGE })
	@JoinTable(name = "Korisnicke_roles", joinColumns = @JoinColumn(name = "korisnik_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	@JsonIgnore
	public Set<Role> roles  = new HashSet<Role>();

	public Korisnik(String ime, String prezime, String korisnickoIme, String lozinka) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
	}

	public Korisnik() {
		super();
		// TODO Auto-generated constructor stub
	}

}
