package poslovna.repozitorijumi;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import poslovna.model.Banka;
import poslovna.model.KursnaLista;

public interface KursnaListaRepozitorijum extends JpaRepository<KursnaLista, Long> {

	List<KursnaLista> findByBanka(Banka findOne);

	List<KursnaLista> findByBroj(Integer broj);

	List<KursnaLista> findByDatum(Date datum);

	List<KursnaLista> findByPrimjenjujeSeOd(Date primjenjujeSeOd);

}
