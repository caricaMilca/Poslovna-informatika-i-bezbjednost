insert into Banka(sifra, pib, adresa, naziv, email, web, telefon, fax, obracunski_racun, banka3kod) values (21, 213,'bankaAdresa', 'BankaA', 'banka@gmail.com','banka.com', '0650650650', 'banka.fax', '1321321', '123');
insert into Banka(sifra, pib, adresa, naziv, email, web, telefon, fax, obracunski_racun, banka3kod) values (21, 215,'bankaAdresa', 'BankaB', 'banka@gmail.com','banka.com', '0650650650', 'banka.fax', '1321381', '126');
insert into Banka(sifra, pib, adresa, naziv, email, web, telefon, fax, obracunski_racun, banka3kod) values (21, 654,'bankaAdresa', 'BankaC', 'banka@gmail.com','banka.com', '0650650650', 'banka.fax', '1321881', '120');


insert into Korisnik(korisnicko_ime, lozinka, uloga, banka_id) values ('ceks', 'ceks', 'Zaposleni', 1);
insert into Korisnik(korisnicko_ime, lozinka, uloga, banka_id) values ('ceks1', 'ceks', 'Zaposleni', 1);
insert into Korisnik(korisnicko_ime, lozinka, uloga, banka_id) values ('ceks2', 'ceks', 'Zaposleni', 1);
insert into Korisnik(korisnicko_ime, lozinka, uloga, banka_id) values ('ceks3', 'ceks', 'Klijent', 1);
insert into Korisnik(korisnicko_ime, lozinka, uloga, banka_id) values ('ceks4', 'ceks', 'Klijent', 1);
insert into Korisnik(korisnicko_ime, lozinka, uloga, banka_id) values ('ceks5', 'ceks', 'Klijent', 1);
insert into Korisnik(korisnicko_ime, lozinka, uloga, banka_id) values ('ceks6', 'ceks', 'Klijent', 2);
insert into Korisnik(korisnicko_ime, lozinka, uloga, banka_id) values ('ceks7', 'ceks', 'Klijent', 3);
insert into Korisnik(korisnicko_ime, lozinka, uloga, banka_id) values ('ceks8', 'ceks', 'Klijent', 2);


insert into Vrsta_placanja(naziv) values ('karticom');
insert into Vrsta_placanja(naziv) values ('kes');

insert into Kursna_lista(datum, primjenjuje_se_od, broj, banka_id) values ('20-11-20', '11-11-15', 98, 1);
insert into Kursna_lista(datum, primjenjuje_se_od, broj, banka_id) values ('22-11-20', '11-08-15', 95, 2);
insert into Kursna_lista(datum, primjenjuje_se_od, broj, banka_id) values ('20-11-20', '11-09-15', 90, 3);
insert into Kursna_lista(datum, primjenjuje_se_od, broj, banka_id) values ('21-01-20', '11-11-16', 58, 1);
insert into Kursna_lista(datum, primjenjuje_se_od, broj, banka_id) values ('24-01-20', '11-11-16', 59, 2);
insert into Kursna_lista(datum, primjenjuje_se_od, broj, banka_id) values ('20-01-20', '11-11-16', 56, 3);
insert into Kursna_lista(datum, primjenjuje_se_od, broj, banka_id) values ('11-11-15', '11-11-13', 91, 1);



insert into Valuta(naziv, zvanicna_sifra, domicilna) values ('Euro', 'eur', true);
insert into Valuta(naziv, zvanicna_sifra, domicilna) values ('Dinar', 'rsd', true);
insert into Valuta(naziv, zvanicna_sifra, domicilna) values ('Marka', 'mar', true);

insert into KursUValuti(kupovni, prodajni, srednji, osnovna_valuta_id, prema_valuti_id, kursna_lista_id) values (119.742, 125.129, 122.435, 1, 2, 1);
insert into KursUValuti(kupovni, prodajni, srednji, osnovna_valuta_id, prema_valuti_id, kursna_lista_id) values (119.742, 125.129, 122.435, 1, 2, 2);
insert into KursUValuti(kupovni, prodajni, srednji, osnovna_valuta_id, prema_valuti_id, kursna_lista_id) values (119.742, 125.129, 122.435, 1, 2, 3);
insert into KursUValuti(kupovni, prodajni, srednji, osnovna_valuta_id, prema_valuti_id, kursna_lista_id) values (60.910, 64.290, 62.600, 3, 2, 4);
insert into KursUValuti(kupovni, prodajni, srednji, osnovna_valuta_id, prema_valuti_id, kursna_lista_id) values (60.910, 64.290, 62.600, 3, 2, 5);
insert into KursUValuti(kupovni, prodajni, srednji, osnovna_valuta_id, prema_valuti_id, kursna_lista_id) values (60.910, 64.290, 62.600, 3, 2, 6);
insert into KursUValuti(kupovni, prodajni, srednji, osnovna_valuta_id, prema_valuti_id, kursna_lista_id) values (40.742, 40.129, 40.435, 1, 2, 7);


insert into Djelatnost(naziv, sifra) values ('Vodoinstalater', 'VVVV');
insert into Djelatnost(naziv, sifra) values ('Elektricar', 'EEEE');

insert into Zaposleni(id, ulogaZ) values (1, 'Salterusa');
insert into Zaposleni(id, ulogaZ) values (2, 'Salterusa');
insert into Zaposleni(id, ulogaZ) values (3, 'Administrator');
insert into Klijent(id, ulogaK) values (4, 'FIZICKO');
insert into Klijent(id, ulogaK, djelatnost_id) values (5, 'POSLOVNO', 1);
insert into Klijent(id, ulogaK) values (6, 'FIZICKO');
insert into Klijent(id, ulogaK, djelatnost_id) values (7, 'POSLOVNO', 2);
insert into Klijent(id, ulogaK) values (8, 'FIZICKO');


insert into Racun(broj_racuna, datum_otvaranja, vazeci, klijent_id, banka_id, valuta_id) values ('123-123123123123-123', '11-11-11', true, 4, 1, 1);
insert into Racun(broj_racuna, datum_otvaranja, vazeci, klijent_id, banka_id, valuta_id) values ('123-333444555334-312', '11-11-11', true, 5, 1, 2);
insert into Racun(broj_racuna, datum_otvaranja, vazeci, klijent_id, banka_id, valuta_id) values ('123-545444555334-312', '11-11-11', true, 6, 1, 2);
insert into Racun(broj_racuna, datum_otvaranja, vazeci, klijent_id, banka_id, valuta_id) values ('126-333444555334-312', '11-11-11', true, 7, 3, 2);
insert into Racun(broj_racuna, datum_otvaranja, vazeci, klijent_id, banka_id, valuta_id) values ('120-333444555334-312', '11-11-11', true, 8, 2, 2);


insert into Dnevno_stanje_racuna(prethodno_stanje, promet_na_teret, promet_na_korist, novo_stanje, datum_prometa, racun_id) values (0, 0, 0, 0, '11-11-11', 1);

insert into Analitika_izvoda(duznik, povjerilac, svrha_placanja, datum_primanja, datum_valute, racun_duznika, racun_povjerioca, model_zaduzenja, poziv_na_broj_zaduzenja,poziv_na_broj_odobrenja, model_odobrenja, hitno, iznos, tip_transakcije, dnevno_stanje_racuna_id, vrsta_placanja_id, valuta_id) values ('aa', 'aa', 'aa', '11-11-11', '11-11-11', '123-123123123123-123','123-333444555334-312',1,'aa','aa',2,false, 2, 'UPLATA', 1, 1, 1); 

insert into Drzava(naziv, sifra, valuta_id) values ('Bosna i hercegovina', 'BiH', 3);
insert into Drzava(naziv, sifra, valuta_id) values ('Srbija', 'RS', 2);
insert into Drzava(naziv, sifra, valuta_id) values ('Crna Gora', 'CG', 1);

insert into Naseljeno_mjesto(naziv, ptt_oznaka, drzava_id) values ('Gacko', 'GC', 1);
insert into Naseljeno_mjesto(naziv, ptt_oznaka, drzava_id) values ('Novi Sad', 'NS', 2);

insert into Privilege(name) values ('promenaLozinke'); --1
insert into Privilege(name) values ('registracijaSalteruse'); --2
insert into Privilege(name) values ('registracijaKlijenta');--3
insert into Privilege(name) values ('nekaGlupost');--4
insert into Privilege(name) values ('preuzmiZaposlenog');--5
insert into Privilege(name) values ('preuzmiKlijenta');--6
insert into Privilege(name) values ('registracijaDjelatnosti');--7
insert into Privilege(name) values ('sveDjelatnosti');--8
insert into Privilege(name) values ('registracijaDrzave');--9
insert into Privilege(name) values ('sveDrzave');--10
insert into Privilege(name) values ('sveDrzaveValute');--11
insert into Privilege(name) values ('registracijaValute');--12
insert into Privilege(name) values ('sveValute');--13
insert into Privilege(name) values ('pretraziValute');--14
insert into Privilege(name) values ('pretraziDrzave');--15
insert into Privilege(name) values ('pretraziDjelatnosti');--16
insert into Privilege(name) values ('sviKlijenti');--17
insert into Privilege(name) values ('sviKlijentiDjelatnosti');--18
insert into Privilege(name) values ('pretraziKlijente');--19
insert into Privilege(name) values ('registracijaNaseljenogMjesta');--20
insert into Privilege(name) values ('svaNaseljenaMjesta');--21
insert into Privilege(name) values ('svaNaseljenaMjestaDrzave');--22
insert into Privilege(name) values ('pretraziNaseljenaMjesta');--23
insert into Privilege(name) values ('logovanje');--24
insert into Privilege(name) values ('logout');--25
insert into Privilege(name) values ('registracijaKursaUValuti');--26
insert into Privilege(name) values ('sviKurseviUValuti');--27
insert into Privilege(name) values ('sviKurseviUValutiValute');--28
insert into Privilege(name) values ('sviKurseviUValutiKursneListe');--29
insert into Privilege(name) values ('pretraziKursUValuti');--30
insert into Privilege(name) values ('registracijaKursneListe');--31
insert into Privilege(name) values ('sveKursneListe');--32
insert into Privilege(name) values ('sveKursneListeBanke');--33
insert into Privilege(name) values ('pretraziKursneListe');--34
insert into Privilege(name) values ('preuzmiBanku');--35
insert into Privilege(name) values ('registracijaVrstePlacanja');--36
insert into Privilege(name) values ('sveVrstePlacanja');--37
insert into Privilege(name) values ('registracijaRacuna');--38
insert into Privilege(name) values ('sviRacuni');--39
insert into Privilege(name) values ('sviRacuniValute');--40
insert into Privilege(name) values ('sviRacuniKlijenta');--41
insert into Privilege(name) values ('pretraziRacune');--42
insert into Privilege(name) values ('registracijaDnevnogStanjaRacuna');--43
insert into Privilege(name) values ('svaDnevnaStanjaRacuna');--44
insert into Privilege(name) values ('svaDnevnaStanjaRacunaDatog');--45
insert into Privilege(name) values ('pretraziDnevnaStanjeRacuna');--46
insert into Privilege(name) values ('transakcija');--47
insert into Privilege(name) values ('sveAnalitikeIzvoda');--48
insert into Privilege(name) values ('sveAnalitikeIzvodaDnevnog');--49
insert into Privilege(name) values ('sveAnalitikeIzvodaValute');--50
insert into Privilege(name) values ('sveAnalitikeIzvodaTipaPlacanja');--51
insert into Privilege(name) values ('pretraziAnalitikeIzvoda');--52
insert into Privilege(name) values ('izbrisiKlijenta');--53
insert into Privilege(name) values ('izmjeniKlijenta');--54
insert into Privilege(name) values ('izbrisiDjelatnost');--55
insert into Privilege(name) values ('izmjeniDjelatnost');--56
insert into Privilege(name) values ('registracijaVrstePlacanja');--57
insert into Privilege(name) values ('sveVrstePlacanja');--58
insert into Privilege(name) values ('izmjeniVrstuPlacanja');--59
insert into Privilege(name) values ('izbrisiVrstuPlacanja');--60
insert into Privilege(name) values ('izmeniKursnuListu');--61
insert into Privilege(name) values ('izbrisiKursnuListu');--62
insert into Privilege(name) values ('izmeniValutu');--63
insert into Privilege(name) values ('izbrisiValutu');--64
insert into Privilege(name) values ('izmeniKursUValuti');--65
insert into Privilege(name) values ('izbrisiKursUValuti');--66
insert into Privilege(name) values ('ucitajFajl');--67

 
insert into Role(name) values ('klijentRole');--1
insert into Role(name) values ('adminRole');--2
insert into Role(name) values ('salterusaRole');--3 samo tako stoji
insert into Role(name) values ('superSalterusaRole');--4 samo tako stoji
insert into Role(name) values ('zajednickoSalteruse');--5
insert into Role(name) values ('zajednickoSvi');--6


insert into Roles_privileges(role_id, privilege_id) values (1, 6);

insert into Roles_privileges(role_id, privilege_id) values (2, 2);
insert into Roles_privileges(role_id, privilege_id) values (2, 5);
insert into Roles_privileges(role_id, privilege_id) values (2, 7);
insert into Roles_privileges(role_id, privilege_id) values (2, 8);
insert into Roles_privileges(role_id, privilege_id) values (2, 9);
insert into Roles_privileges(role_id, privilege_id) values (2, 12);
insert into Roles_privileges(role_id, privilege_id) values (2, 13);
insert into Roles_privileges(role_id, privilege_id) values (2, 20);
insert into Roles_privileges(role_id, privilege_id) values (2, 21);
insert into Roles_privileges(role_id, privilege_id) values (2, 26);
insert into Roles_privileges(role_id, privilege_id) values (2, 27);
insert into Roles_privileges(role_id, privilege_id) values (2, 31);
insert into Roles_privileges(role_id, privilege_id) values (2, 32);
insert into Roles_privileges(role_id, privilege_id) values (2, 34);
insert into Roles_privileges(role_id, privilege_id) values (2, 55);
insert into Roles_privileges(role_id, privilege_id) values (2, 56);
insert into Roles_privileges(role_id, privilege_id) values (2, 57);
insert into Roles_privileges(role_id, privilege_id) values (2, 59);
insert into Roles_privileges(role_id, privilege_id) values (2, 60);
insert into Roles_privileges(role_id, privilege_id) values (2, 61);
insert into Roles_privileges(role_id, privilege_id) values (2, 62);
insert into Roles_privileges(role_id, privilege_id) values (2, 63);
insert into Roles_privileges(role_id, privilege_id) values (2, 64);
insert into Roles_privileges(role_id, privilege_id) values (2, 65);
insert into Roles_privileges(role_id, privilege_id) values (2, 66);

insert into Roles_privileges(role_id, privilege_id) values (5, 3);
insert into Roles_privileges(role_id, privilege_id) values (5, 4);
insert into Roles_privileges(role_id, privilege_id) values (5, 5);
insert into Roles_privileges(role_id, privilege_id) values (5, 8);
insert into Roles_privileges(role_id, privilege_id) values (5, 16);
insert into Roles_privileges(role_id, privilege_id) values (5, 18);
insert into Roles_privileges(role_id, privilege_id) values (5, 10);
insert into Roles_privileges(role_id, privilege_id) values (5, 11);
insert into Roles_privileges(role_id, privilege_id) values (5, 13);
insert into Roles_privileges(role_id, privilege_id) values (5, 14);
insert into Roles_privileges(role_id, privilege_id) values (5, 15);
insert into Roles_privileges(role_id, privilege_id) values (5, 17);
insert into Roles_privileges(role_id, privilege_id) values (5, 19);
insert into Roles_privileges(role_id, privilege_id) values (5, 21);
insert into Roles_privileges(role_id, privilege_id) values (5, 22);
insert into Roles_privileges(role_id, privilege_id) values (5, 23);
insert into Roles_privileges(role_id, privilege_id) values (5, 27);
insert into Roles_privileges(role_id, privilege_id) values (5, 28);
insert into Roles_privileges(role_id, privilege_id) values (5, 29);
insert into Roles_privileges(role_id, privilege_id) values (5, 30);
insert into Roles_privileges(role_id, privilege_id) values (5, 32);
insert into Roles_privileges(role_id, privilege_id) values (5, 33);
insert into Roles_privileges(role_id, privilege_id) values (5, 34);
insert into Roles_privileges(role_id, privilege_id) values (5, 35);
insert into Roles_privileges(role_id, privilege_id) values (5, 36);
insert into Roles_privileges(role_id, privilege_id) values (5, 37);
insert into Roles_privileges(role_id, privilege_id) values (5, 38);
insert into Roles_privileges(role_id, privilege_id) values (5, 39);
insert into Roles_privileges(role_id, privilege_id) values (5, 40);
insert into Roles_privileges(role_id, privilege_id) values (5, 41);
insert into Roles_privileges(role_id, privilege_id) values (5, 42);
insert into Roles_privileges(role_id, privilege_id) values (5, 43);
insert into Roles_privileges(role_id, privilege_id) values (5, 44);
insert into Roles_privileges(role_id, privilege_id) values (5, 45);
insert into Roles_privileges(role_id, privilege_id) values (5, 46);
insert into Roles_privileges(role_id, privilege_id) values (5, 47);
insert into Roles_privileges(role_id, privilege_id) values (5, 48);
insert into Roles_privileges(role_id, privilege_id) values (5, 49);
insert into Roles_privileges(role_id, privilege_id) values (5, 50);
insert into Roles_privileges(role_id, privilege_id) values (5, 51);
insert into Roles_privileges(role_id, privilege_id) values (5, 52);
insert into Roles_privileges(role_id, privilege_id) values (5, 53);
insert into Roles_privileges(role_id, privilege_id) values (5, 54);


insert into Roles_privileges(role_id, privilege_id) values (6, 1);
insert into Roles_privileges(role_id, privilege_id) values (6, 24);
insert into Roles_privileges(role_id, privilege_id) values (6, 25);
insert into Roles_privileges(role_id, privilege_id) values (6, 58);
insert into Roles_privileges(role_id, privilege_id) values (6, 67);



insert into Korisnicke_roles(korisnik_id, role_id) values (1, 5);
insert into Korisnicke_roles(korisnik_id, role_id) values (1, 6);
insert into Korisnicke_roles(korisnik_id, role_id) values (2, 4);
insert into Korisnicke_roles(korisnik_id, role_id) values (2, 5);
insert into Korisnicke_roles(korisnik_id, role_id) values (2, 6);
insert into Korisnicke_roles(korisnik_id, role_id) values (3, 6);
insert into Korisnicke_roles(korisnik_id, role_id) values (4, 1);
insert into Korisnicke_roles(korisnik_id, role_id) values (4, 6);
insert into Korisnicke_roles(korisnik_id, role_id) values (5, 1);
insert into Korisnicke_roles(korisnik_id, role_id) values (5, 6);
