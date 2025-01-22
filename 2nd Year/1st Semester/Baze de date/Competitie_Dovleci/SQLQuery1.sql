create table Participanti(
	ID_Participant int primary key identity(1,1),
	Nume nvarchar(100) not null,
	Nr_telefon nvarchar(100) not null,
	Adresa nvarchar(199) not null
);

insert into Participanti(Nume,Nr_telefon,Adresa) values
('Catalin','0754568065','Bilca'),
('Emi','0754587212','Cluj'),
('Mama','0754521658','Acasa');

create table Dovleci(
	ID_Dovleac int primary key identity(1,1),
	Descriere nvarchar(100) not null,
	Data_Decoratiune Date,
	Timp_Decorare int,
	e_complet nvarchar(100) not null,
);
insert into Dovleci(Descriere,Data_Decoratiune,Timp_Decorare,e_complet) values
('Dovleac fain','2001-12-13',13,'True'),
('Micul Alexandru','2002-11-16',20,'False'),
('Lalala','2000-10-01',200,'True'),
('Kala','2005-10-11',100,'False'),
('Terminat','2019-07-07',123,'True');

create table Activitate_Decorare(
	ID_Activitate int primary key identity(1,1),
	Descriere Text,
	ID_Dovleac int,
	ID_Participant int,
	foreign key (ID_Dovleac) references Dovleci(ID_Dovleac),
	foreign key (ID_Participant) references Participanti(ID_Participant)
);
insert into Activitate_Decorare(Descriere,ID_Dovleac,ID_Participant) values
('Cu spor',1,1),
('Fara spor',1,2),
('Doamne ajuta',2,3)

create table Evaluatori(
	ID_Evaluator int primary key identity(1,1),
	Nume nvarchar(100) not null,
	Adresa nvarchar(100) not null,
	Data_Nasterii Date
);
insert into Evaluatori(Nume,Adresa,Data_Nasterii) values
('Gelu mineru','gelu@gmail.com','2000-10-10'),
('Adelin','adle@gmail.com','2001-09-09')

create table Evaluare(
	ID_Evaluare int primary key identity(1,1),
	Nota int check (Nota between 0 and 10),
	ID_Evaluator int,
	ID_Dovleac int,
	foreign key (ID_Dovleac) references Dovleci(ID_Dovleac),
	foreign key (ID_Evaluator) references Evaluatori(ID_Evaluator)
);
insert into Evaluare(ID_Dovleac,ID_Evaluator,Nota) values
(1,1,10),
(1,2,9),
(3,1,8),
(4,1,10),
(5,2,5),
(2,1,7)

create procedure AddOrUpdateDescriereActivitate
	@ID_Participant int,
	@ID_Dovleac int,
	@Descriere text
as
begin
	if exists (select 1 from Activitate_Decorare
			where ID_Dovleac=@ID_Dovleac 
			and ID_Participant=@ID_Participant)
	begin
		UPDATE Activitate_Decorare
		set Descriere = @Descriere
		where ID_Participant = @ID_Participant and ID_Dovleac = @ID_Dovleac;
	end
else
	begin
		insert into Activitate_Decorare(ID_Dovleac,ID_Participant,Descriere)
		values (@ID_Dovleac,@ID_Participant,@Descriere)
	end
end

create view TopDecorator
as
	select top 1
	p.Nume, COUNT(*) as nr_dovleci
	from Participanti p
	join Activitate_Decorare as ad on ad.ID_Participant = p.ID_Participant
	join Dovleci as d on d.ID_Dovleac = ad.ID_Dovleac
	where d.e_complet Like 'T%'
	group by p.ID_Participant,p.Nume
	order by nr_dovleci desc;

exec AddOrUpdateDescriereActivitate 1,5,'Misto rau sa mor'
select * from Activitate_Decorare

select * from TopDecorator

select * from Participanti

select * from Dovleci