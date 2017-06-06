package poslovna.model;

import java.util.HashSet;
import java.util.Set;

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
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<Korisnik> users = new HashSet<Korisnik>();
 
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "Roles_privileges", 
        joinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "privilege_id", referencedColumnName = "id"))
    @JsonIgnore
    public Set<Privilege> privileges = new HashSet<Privilege>();

	public Role(String name) {
		super();
		this.name = name;
	}

	public Role() {
		super();
	}   
    
    
    
}