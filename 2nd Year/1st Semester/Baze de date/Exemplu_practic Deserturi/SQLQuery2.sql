create table Producatori(
	ID_Producator int primary key identity(1,1),
	Nume_Producator nvarchar(100) not null,
	Site_Web nvarchar(100) not null
);

create table Tipuri_Desert(
	ID_Tip_Desert int primary key identity(1,1),
	Nume_Tip_Desert nvarchar(100) not null
);

create table Deserturi(
	ID_Desert int primary key identity(1,1),
	Nume_Desert nvarchar(100) not null,
	Mod_Preparare text,
	Pret decimal(10,2),
	Nr_Calorii int,
	ID_Producator int,
	ID_Tip_Desert int,
	foreign key (ID_Tip_Desert) references Tipuri_Desert(ID_Tip_Desert),
	foreign key (ID_Producator) references Producatori(ID_Producator)
);

create table Clienti(
	ID_Client int primary key identity(1,1),
	Nume_Client nvarchar(100) not null,
	email nvarchar(100) not null,
	data_nasterii Date
);

create table Carucior(
	Cantitate int not null,
	ID_Desert int,
	ID_Client int,
	foreign key (ID_Desert) references Deserturi(ID_Desert),
	foreign key (ID_Client) references Clienti(ID_Client),
	primary key (ID_Desert, ID_Client)
);

insert into Producatori(Nume_Producator, Site_web) values 
('Gelu', 'geluweb.com'),
('Marian', 'mariansite.com')

select * from Tipuri_Desert

insert into Tipuri_Desert(Nume_Tip_Desert) values
('Tiramisu'),
('Papanas');

insert into Deserturi(ID_Producator, ID_Tip_Desert, Nume_Desert, Mod_Preparare, Pret, Nr_calorii) values
(1,1,'Tiramisu Tiganesc','te duci la tigani',999.99,10),
(1,2,'Papanas Boom', 'faci o bomba',145.0,100),
(2,1,'Tiramisu si atat', 'atat', 555,130);

insert into Clienti(Nume_Client,email,data_nasterii) values
('Emi','emicuiu@gmail.com','2003-10-30'),
('Adelin','adelin.cuciurean@gmail.com','1999-10-21')

insert into Clienti(Nume_Client,email,data_nasterii) values
('Andrei','andreiandrei@gmail.com','2003-01-03'),
('Laur','asd','1234-12-12')

insert into Carucior(ID_Desert,ID_Client,Cantitate) values
(1,2,10),
(2,3,29),
(1,3,20)

insert into Carucior(ID_Desert,ID_Client,Cantitate) values
(3,4,12)


create procedure AddOrUpdateCarucior
	@ID_Client int,
	@ID_Desert int,
	@Cantitate int
as
begin
	if exists(select 1 from Carucior
				where Carucior.ID_Client = @ID_Client
				AND Carucior.ID_Desert = @ID_Desert)
	begin
			update Carucior
			set Cantitate = @Cantitate
			where ID_Client = @ID_Client
			and ID_Desert = @ID_Desert
	end
	else 
	begin 
			insert into Carucior(ID_Desert,ID_Client,Cantitate) 
			values (@ID_Desert,@ID_Client,@Cantitate)
	end
end

select * from Carucior

exec AddOrUpdateCarucior 2,1,100

create function ClientsWithA()
	returns table
as
return 
	select distinct 
	Producatori.Nume_Producator,
	Deserturi.Nume_Desert,
	Deserturi.Nr_Calorii,
	Carucior.Cantitate,
	Clienti.Nume_Client
	from Clienti
	join Carucior on Carucior.ID_Client = Clienti.ID_Client
	join Deserturi on Deserturi.ID_Desert = Deserturi.ID_Desert
	join Producatori on Deserturi.ID_Producator = Producatori.ID_Producator
	where Clienti.Nume_Client like 'A%';

select * from dbo.ClientsWithA()
where Cantitate>50

select * from Tipuri_Desert