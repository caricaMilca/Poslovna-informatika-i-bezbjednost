insert into Korisnik(korisnicko_ime, lozinka, uloga) values ('ceks', 'ceks', 'Zaposleni');
insert into Korisnik(korisnicko_ime, lozinka, uloga) values ('ceks1', 'ceks', 'Zaposleni');
insert into Korisnik(korisnicko_ime, lozinka, uloga) values ('ceks2', 'ceks', 'Zaposleni');
insert into Korisnik(korisnicko_ime, lozinka, uloga) values ('ceks3', 'ceks', 'Klijent');
insert into Korisnik(korisnicko_ime, lozinka, uloga) values ('ceks4', 'ceks', 'Klijent');

insert into Djelatnost(naziv, sifra) values ('vodoinstalater', 'vi');
insert into Djelatnost(naziv, sifra) values ('elektricar', 'e');

insert into Zaposleni(id, ulogaZ) values (1, 'Salterusa');
insert into Zaposleni(id, ulogaZ) values (2, 'Super_salterusa');
insert into Zaposleni(id, ulogaZ) values (3, 'Administrator');
insert into Klijent(id, ulogaK) values (4, 'FIZICKO');
insert into Klijent(id, ulogaK, djelatnost_id) values (5, 'POSLOVNO', 1);

insert into Valuta(naziv, zvanicna_sifra, domicilna) values ('euro', 'e', true);
insert into Valuta(naziv, zvanicna_sifra, domicilna) values ('dinar', 'rsd', true);
insert into Valuta(naziv, zvanicna_sifra, domicilna) values ('marka', 'km', true);

insert into Drzava(naziv, sifra, valuta_id) values ('Bosna i hercegovina', 'BiH', 3);
insert into Drzava(naziv, sifra, valuta_id) values ('Srbija', 'RS', 2);
insert into Drzava(naziv, sifra, valuta_id) values ('Crna Gora', 'CG', 1);

insert into Naseljeno_mjesto(naziv, ptt_oznaka, drzava_id) values ('Gacko', 'GC', 1);
insert into Naseljeno_mjesto(naziv, ptt_oznaka, drzava_id) values ('Novi Sad', 'NS', 2);

insert into Privilege(name) values ('promenaLozinke'); --1
insert into Privilege(name) values ('registracijaSalteruse'); --2
insert into Privilege(name) values ('registracijaKlijentaFizicko');--3
insert into Privilege(name) values ('registracijaKlijentaPravno');--4
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

insert into Role(name) values ('klijentRole');--1
insert into Role(name) values ('adminRole');--2
insert into Role(name) values ('salterusaRole');--3
insert into Role(name) values ('superSalterusaRole');--4
insert into Role(name) values ('zajednickoSalteruse');--5


insert into Roles_privileges(role_id, privilege_id) values (1, 1);
insert into Roles_privileges(role_id, privilege_id) values (1, 6);

insert into Roles_privileges(role_id, privilege_id) values (2, 1);
insert into Roles_privileges(role_id, privilege_id) values (2, 2);
insert into Roles_privileges(role_id, privilege_id) values (2, 5);
insert into Roles_privileges(role_id, privilege_id) values (2, 7);
insert into Roles_privileges(role_id, privilege_id) values (2, 8);
insert into Roles_privileges(role_id, privilege_id) values (2, 9);
insert into Roles_privileges(role_id, privilege_id) values (2, 12);
insert into Roles_privileges(role_id, privilege_id) values (2, 13);
insert into Roles_privileges(role_id, privilege_id) values (2, 20);
insert into Roles_privileges(role_id, privilege_id) values (2, 21);

insert into Roles_privileges(role_id, privilege_id) values (3, 3);
insert into Roles_privileges(role_id, privilege_id) values (3, 5);
insert into Roles_privileges(role_id, privilege_id) values (3, 8);

insert into Roles_privileges(role_id, privilege_id) values (4, 4);
insert into Roles_privileges(role_id, privilege_id) values (4, 5);
insert into Roles_privileges(role_id, privilege_id) values (4, 8);
insert into Roles_privileges(role_id, privilege_id) values (4, 16);
insert into Roles_privileges(role_id, privilege_id) values (4, 18);

insert into Roles_privileges(role_id, privilege_id) values (5, 1);
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

insert into Korisnicke_roles(korisnik_id, role_id) values (1, 3);
insert into Korisnicke_roles(korisnik_id, role_id) values (1, 5);
insert into Korisnicke_roles(korisnik_id, role_id) values (2, 4);
insert into Korisnicke_roles(korisnik_id, role_id) values (2, 5);
insert into Korisnicke_roles(korisnik_id, role_id) values (3, 2);
insert into Korisnicke_roles(korisnik_id, role_id) values (4, 1);
