package poslovna.repozitorijumi;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poslovna.model.AnalitikaIzvoda;
import poslovna.model.Banka;
import poslovna.model.TipGreske;

public interface AnalitikaIzvodaRepozitorijum extends JpaRepository<AnalitikaIzvoda, Long> {

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?1")
	List<AnalitikaIzvoda> izvodiBanke(Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and dn.id = ?1")
	List<AnalitikaIzvoda> findByDnevnoStanjeRacuna(Long idDnevnogStanjaRacuna, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.valuta.id = ?1")
	List<AnalitikaIzvoda> findByValuta(Long idValute, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.vrstaPlacanja.id = ?1")
	List<AnalitikaIzvoda> findByVrstaPlacanja(Long idTipaPlacanja, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.hitno = ?1")
	List<AnalitikaIzvoda> findByHitno(Boolean hitno, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.datumPrimanja = ?1")
	List<AnalitikaIzvoda> findByDatumPrimanja(Date datumPrimanja, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.datumValute = ?1")
	List<AnalitikaIzvoda> findByDatumValute(Date datumValute, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.duznik = ?1")
	List<AnalitikaIzvoda> findByDuznik(String duznik, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.iznos = ?1")
	List<AnalitikaIzvoda> findByIznos(Double iznos, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.modelOdobrenja = ?1")
	List<AnalitikaIzvoda> findByModelOdobrenja(Integer modelOdobrenja, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.modelZaduzenja = ?1")
	List<AnalitikaIzvoda> findByModelZaduzenja(Integer modelZaduzenja, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.povjerilac = ?1")
	List<AnalitikaIzvoda> findByPovjerilac(String povjerilac, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.pozivNaBrojZaduzenja = ?1")
	List<AnalitikaIzvoda> findByPozivNaBrojZaduzenja(String pozivNaBrojZaduzenja, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.racunDuznika = ?1")
	List<AnalitikaIzvoda> findByRacunDuznika(String racunDuznika, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.racunPovjerioca = ?1")
	List<AnalitikaIzvoda> findByRacunPovjerioca(String racunPovjerioca, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.status = ?1")
	List<AnalitikaIzvoda> findByStatus(Integer status, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.tipGreske = ?1")
	List<AnalitikaIzvoda> findByTipGreske(TipGreske tipGreske, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.pozivNaBrojOdobrenja = ?1")
	List<AnalitikaIzvoda> findByPozivNaBrojOdobrenja(String pozivNaBrojOdobrenja, Banka b);

	@Query("select k from AnalitikaIzvoda k inner join k.dnevnoStanjeRacuna dn inner join dn.racun r where r.banka=?2 and k.svrhaPlacanja = ?1")
	List<AnalitikaIzvoda> findBySvrhaPlacanja(String svrhaPlacanja, Banka b);

}
