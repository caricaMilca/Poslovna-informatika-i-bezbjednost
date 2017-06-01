package poslovna.repozitorijumi;

import org.springframework.data.jpa.repository.JpaRepository;

import poslovna.model.Role;

public interface RoleRepozitorijum extends JpaRepository<Role, Long> {

	public Role findById(Long id);
}
