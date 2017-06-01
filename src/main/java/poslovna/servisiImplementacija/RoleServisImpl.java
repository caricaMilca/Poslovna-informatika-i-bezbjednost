package poslovna.servisiImplementacija;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poslovna.model.Role;
import poslovna.repozitorijumi.RoleRepozitorijum;
import poslovna.servisi.RoleServis;

@Service
@Transactional
public class RoleServisImpl implements RoleServis{

	@Autowired
	private RoleRepozitorijum roleRep;
	
	@Override
	public Role findOne(Long id) {
		// TODO Auto-generated method stub
		return roleRep.findById(id);
	}

}
