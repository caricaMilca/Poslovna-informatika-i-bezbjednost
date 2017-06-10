package poslovna.servisi;

import java.util.List;

import org.springframework.http.ResponseEntity;

import poslovna.model.KursUValuti;

public interface KursUValutiServis {

	ResponseEntity<KursUValuti> registracijaKursaUValuti(KursUValuti kursUValuti, Long idValutePrema,
			Long idValuteOsnovni, Long idKursneListe);

	ResponseEntity<List<KursUValuti>> sviKurseviUValuti();

	ResponseEntity<List<KursUValuti>> sviKurseviUValutiValute(Long idValute);

	ResponseEntity<List<KursUValuti>> sviKurseviUValutiKursneListe(Long idKursneListe);

	ResponseEntity<List<KursUValuti>> pretraziKursUValuti(KursUValuti kursUValuti, Long idValuteOsnovni,
			Long idValutePrema, Long idKursneListe);

}
