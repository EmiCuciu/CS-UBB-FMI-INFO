create table Producatori(
	ID_Producator int primary key identity (1,1),
	Nume nvarchar(100) not null,
	Site_web nvarchar(100) not null
);

insert into Producatori(Nume, Site_web) values 
('Gelu', 'geluweb.com'),
('Marian', 'mariansite.com')

create table Tip_Deserturi(
	ID_Tip_Desert int primary key identity (1,1),
	Nume_Tip_Desert nvarchar(100) not null
);

insert into Tip_Deserturi(Nume_Tip_Desert) values
('Tiramisu'),
('Papanas');

create table Deserturi(
	ID_Desert int primary key identity (1,1),
	Nume nvarchar(100) not null,
	Mod_Preparare text,
	Pret decimal(10,2),
	Nr_calorii int,
	ID_Producator int,
	ID_Tip_Desert int,
	foreign key (ID_Producator) references Producatori(ID_Producator),
	foreign key (ID_Tip_Desert) references Tip_Deserturi(ID_Tip_Desert)
);

insert into Deserturi(ID_Producator, ID_Tip_Desert, Nume, Mod_Preparare, Pret, Nr_calorii) values
(1,1,'Tiramisu Tiganesc','te duci la tigani',999.99,10),
(1,2,'Papanas Boom', 'faci o bomba',145.0,100),
(2,1,'Tiramisu si atat', 'atat', 555,130);

create table Clienti(
	ID_Client int primary key identity(1,1),
	Nume_Client nvarchar(100) not null,
	email nvarchar(100) not null,
	data_nasterii DATE
);

insert into Clienti(Nume_Client,email,data_nasterii) values
('Emi','emicuiu@gmail.com','2003-10-30'),
('Adelin','adelin.cuciurean@gmail.com','1999-10-21')

insert into Clienti(Nume_Client,email,data_nasterii) values
('Andrei','andreiandrei@gmail.com','2003-01-03'),
('Laur','asd','1234-12-12')

create table Carucior(
	ID_Desert int,
	ID_Client int,
	foreign key (ID_Desert) references Deserturi(ID_Desert),
	foreign key (ID_Client) references Clienti(ID_Client),
	Cantitate int not null,
	Primary key (ID_Desert, ID_Client)
);

insert into Carucior(ID_Desert,ID_Client,Cantitate) values
(1,2,10),
(2,3,29),
(1,3,20)

insert into Carucior(ID_Desert,ID_Client,Cantitate) values
(3,4,12)

select * from Carucior

create procedure AddOrUpdateCantitate
	@ID_Desert int,
	@ID_Client int,
	@Cantitate int
AS
BEGIN
	if exists (select 1 from Carucior
				where ID_Desert = @ID_Desert
				and ID_Client = @ID_Client)
	BEGIN
		UPDATE Carucior
		set Cantitate = @Cantitate
		where ID_Desert = @ID_Desert
		and ID_Client = @ID_Client
	END
ELSE
	BEGIN
		insert into Carucior (ID_Desert, ID_Client, Cantitate)
		values (@ID_Desert, @ID_Client, @Cantitate)
	END
END;

exec AddOrUpdateCantitate 3,2,12

select * from Carucior

create function ClientiCuA()
	returns table
as 
return
	select distinct 
		Producatori.Nume as NumeProducator,
		Deserturi.Nume as NumeDesert,
		Deserturi.Nr_calorii as NumarCalorii,
		Carucior.Cantitate as Cantitate,
		Clienti.Nume_Client as NumeClient

	from Clienti
	join Carucior on Clienti.ID_Client = Carucior.ID_Client
	join Deserturi on Carucior.ID_Desert = Deserturi.ID_Desert
	join Producatori on Deserturi.ID_Producator = Producatori.ID_Producator
	where Clienti.Nume_Client like 'A%';

select * from dbo.ClientiCuA()