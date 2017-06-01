package poslovna.repozitorijumi;

import org.springframework.data.jpa.repository.JpaRepository;

import poslovna.model.Zaposleni;

public interface ZaposleniRepozitorijum extends JpaRepository<Zaposleni, Long> {

	
}
