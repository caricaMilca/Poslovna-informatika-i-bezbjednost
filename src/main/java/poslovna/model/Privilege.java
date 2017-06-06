package poslovna.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Privilege {
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
 
    public String name;
 
    @ManyToMany(mappedBy = "privileges")
    @JsonIgnore
    private Set<Role> roles = new HashSet<Role>();

	public Privilege(String name) {
		super();
		this.name = name;
	}

	public Privilege() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}