package poslovna.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class Role {
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
 
    public String name;
    @ManyToMany(mappedBy = "roles", cascade = { CascadeType.REMOVE, CascadeType.MERGE })
    @JsonIgnore
    private Collection<Korisnik> users = new ArrayList<Korisnik>();
 
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE, CascadeType.MERGE })
    @JoinTable(
        name = "Roles_privileges", 
        joinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "privilege_id", referencedColumnName = "id"))
    @JsonIgnore
    public Collection<Privilege> privileges = new ArrayList<Privilege>();

	public Role(String name) {
		super();
		this.name = name;
	}

	public Role() {
		super();
	}   
    
    
    
}