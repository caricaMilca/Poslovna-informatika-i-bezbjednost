package poslovna.repozitorijumi;

import org.springframework.data.jpa.repository.JpaRepository;

import poslovna.model.Banka;
import poslovna.model.MedjubankarskiPrenos;

public interface MedjubankarskiPrenosRepozitorijum extends JpaRepository<MedjubankarskiPrenos, Long> {

	MedjubankarskiPrenos findByBankaPosiljalacAndBankaPrimalac(Banka b1, Banka b2);

}
