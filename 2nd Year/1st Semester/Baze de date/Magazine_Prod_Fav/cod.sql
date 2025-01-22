create table Locatie(
	ID_Locatie int primary key identity(1,1),
	Localitate nvarchar(100) not null,
	Strada nvarchar(100) not null,
	Numar int,
	Cod_Postal int
);

insert into Locatie(Localitate,Strada,Numar,Cod_Postal) values
('SV','Faraonului',666,727404),
('Cj','Lunii',123,124567)

create table Magazine(
	ID_Magazin int primary key identity(1,1),
	Denumire_Magazin nvarchar(100) not null,
	AnDeschidere int,
	ID_Locatie int,
	foreign key (ID_Locatie) references Locatie(ID_Locatie)
);

insert into Magazine(Denumire_Magazin,AnDeschidere,ID_Locatie) values
('KO',2005,1),
('opa',2010,1),
('JAkarta',2025,2)

create table Clienti(
	ID_Client int primary key identity(1,1),
	Nume nvarchar(100) not null,
	Prenume nvarchar(100) not null,
	Gen nvarchar(100) not null,
	data_nasterii date
);

insert into Clienti(Nume,Prenume,Gen,data_nasterii) values
('Roman','Eusebiu','masculin','2004-10-25'),
('Cuciurean','Emilian','masculin','2003-10-30'),
('Cuciurean','Adelin','masculin','1999-10-21')

create table Produse_Favorite(
	ID_Prod_Fav int primary key identity(1,1),
	Denumire_Produs_Fav nvarchar(100) not null,
	Pret float,
	Procent float,
	ID_Client int,
	foreign key (ID_Client) references Clienti(ID_Client)
);
insert into Produse_Favorite(Denumire_Produs_Fav,Pret,Procent,ID_Client) values
('Mouse',100,30,3),
('Tastatura',150,10,3),
('Lupa',10,0,1),
('Laptop',2000,20,2)

insert into Produse_Favorite(Denumire_Produs_Fav,Pret,Procent,ID_Client) values
('Telefon',350,70,3),
('Monitor',150,40,3)

select * from Produse_Favorite

create table Magazine_Clienti(
	Data_Cumparaturi date,
	Pret_Achitat float,
	ID_Magazin int,
	ID_Client int,
	foreign key (ID_Magazin) references Magazine(ID_Magazin),
	foreign key (ID_Client) references Clienti(ID_Client),
	primary key (ID_Magazin,ID_Client)
);

insert into Magazine_Clienti(Data_Cumparaturi,Pret_Achitat,ID_Magazin,ID_Client) values
('2020-10-09',1500,1,1),
('2021-09-07',1000,2,1),
('2009-02-15',123,1,3),
('2019-05-30',1897.00,1,2)

create procedure AddOrUpdateMagazinClient
	@ID_Client int,
	@ID_Magazin int,
	@Data_Cumparaturi date,
	@Pret_Achitat float
as
begin 
	if exists (select 1 from Magazine_Clienti
		where ID_Client = @ID_Client 
		and ID_Magazin = @ID_Magazin)
	begin
		update Magazine_Clienti
		set Data_Cumparaturi = @Data_Cumparaturi, Pret_Achitat = @Pret_Achitat
		where ID_Client = @ID_Client 
		and ID_Magazin = @ID_Magazin
	end
else 
	begin
		insert into Magazine_Clienti(Data_Cumparaturi,Pret_Achitat,ID_Magazin,ID_Client)
		values (@Data_Cumparaturi,@Pret_Achitat,@ID_Magazin,@ID_Client)
	end
end

select * from Magazine_Clienti

exec AddOrUpdateMagazinClient 2,1,'2025-01-20',50000000

create view CelMult3
as 
	select Clienti.Nume
	from Clienti
	join Produse_Favorite on Produse_Favorite.ID_Client = Clienti.ID_Client
	group by Clienti.Nume
	having COUNT(*) <= 3;

create view CelMult3V2
as 
	select Clienti.Nume
	from Clienti
	inner join Produse_Favorite on Produse_Favorite.ID_Client = Clienti.ID_Client
	group by Clienti.Nume, Clienti.Prenume
	having COUNT(*) <= 3;

select * from CelMult3V2