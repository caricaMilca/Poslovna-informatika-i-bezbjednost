package poslovna.repozitorijumi;

import org.springframework.data.jpa.repository.JpaRepository;

import poslovna.model.Banka;

public interface BankaRepozitorijum extends JpaRepository<Banka, Long> {

	Banka findByBanka3kod(String substring);

}
