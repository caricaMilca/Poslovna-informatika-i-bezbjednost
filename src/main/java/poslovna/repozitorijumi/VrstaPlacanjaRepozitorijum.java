package poslovna.repozitorijumi;

import org.springframework.data.jpa.repository.JpaRepository;

import poslovna.model.VrstaPlacanja;

public interface VrstaPlacanjaRepozitorijum extends JpaRepository<VrstaPlacanja, Long> {

}
