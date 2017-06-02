package poslovna.repozitorijumi;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import poslovna.model.KursUValuti;
import poslovna.model.KursnaLista;
import poslovna.model.Valuta;

public interface KursUValutiRepozitorijum extends JpaRepository<KursUValuti, Long> {

	List<KursUValuti> findByOsnovnaValuta(Valuta findOne);

	List<KursUValuti> findByOsnovnaValutaOrPremaValuti(Valuta findOne, Valuta findOne1);

	List<KursUValuti> findByKursnaLista(KursnaLista findOne);

	List<KursUValuti> findByProdajni(Double kupovni);

	List<KursUValuti> findByKupovni(Double kupovni);

	List<KursUValuti> findBySrednji(Double srednji);

	List<KursUValuti> findByPremaValuti(Valuta findOne);

}
