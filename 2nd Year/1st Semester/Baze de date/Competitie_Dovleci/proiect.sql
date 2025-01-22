use Competitie_Dovleci

create table Participanti(
	ID_Participant int primary key identity(1,1),
	Nume nvarchar(100) not null,
	Nr_Telefon nvarchar(100) not null,
	Adresa nvarchar(100) not null
);

create table Dovleci(
	ID_Dovleac int primary key identity(1,1),
	Decoratiune_data date,
	Timp_decorare int,
	e_complet bit default 0
);

create table Evaluatori(
	ID_Evaluator int primary key identity(1,1),
	Nume nvarchar(100) not null,
	Email nvarchar(100) not null,
	Data_nasterii date
);

create table Activitate_Decorare(
	ID_Activitate_Decorare int primary key identity(1,1),
	ID_Participant int,
	ID_Dovleac int,
	Descriere text,
	foreign key (ID_Participant) references Participanti(ID_Participant),
	foreign key (ID_Dovleac) references Dovleci(ID_Dovleac)
);

create table Nota_Dovleac(
	ID_Evaluare int primary key identity(1,1),
	ID_Evaluator int,
	ID_Dovleac int,
	Nota int check (Nota between 1 and 10),
	foreign key (ID_Evaluator) references Evaluatori(ID_Evaluator),
	foreign key (ID_Dovleac) references Dovleci(ID_Dovleac)
);

create procedure AddOrUpdateActivitate
	@ID_Participant int,
	@ID_Dovleac int,
	@Descriere TEXT
AS 
BEGIN
	IF EXISTS (SELECT 1 FROM Activitate_Decorare
				where ID_Participant = @ID_Participant
				and ID_Dovleac = @ID_Dovleac)

	BEGIN
		UPDATE Activitate_Decorare
		SET Descriere = @Descriere
		where ID_Participant = @ID_Participant
		and ID_Dovleac = @ID_Dovleac;
	END 
ELSE 
	BEGIN 
		insert into Activitate_Decorare(ID_Participant,ID_Dovleac,Descriere)
		values(@ID_Participant,@ID_Dovleac,@Descriere)
	end
end


create view TopDecorator
as 
	select top 1
	p.Nume,
	COUNT(*) as nr_dovleci
	from Participanti p
	join Activitate_Decorare d on p.ID_Participant = d.ID_Participant
	join Dovleci dv on d.ID_Dovleac = dv.ID_Dovleac
	where dv.e_complet = 1
	group by p.ID_Participant, p.Nume
	order by nr_dovleci desc;

insert into Participanti(Nume,Nr_Telefon,Adresa) values
('Ana Pop', '0722123456', 'Strada Florilor 1'),
('Ion Ionescu', '0733234567', 'Bulevardul Primaverii 2'),
('Maria Popa', '0744345678', 'Aleea Teilor 3'),
('Dan Marin', '0755456789', 'Strada Victoriei 4');

INSERT INTO Dovleci (Decoratiune_data, Timp_decorare, e_complet) VALUES
('2024-10-25', 120, 1),  -- 120 minute, finalizat
('2024-10-26', 90, 1),   -- 90 minute, finalizat
('2024-10-26', 150, 0),  -- 150 minute, nefinalizat
('2024-10-27', 100, 1),  -- 100 minute, finalizat
('2024-10-27', 80, 1);   -- 80 minute, finalizat

INSERT INTO Activitate_Decorare(ID_Participant, ID_Dovleac, Descriere) VALUES
(1, 1, 'Dovleac decorat cu stele și luna'),
(1, 2, 'Dovleac cu față înfricoșătoare'),
(2, 3, 'Dovleac cu model de păianjen'),
(3, 4, 'Dovleac cu fantome și lilieci'),
(3, 5, 'Dovleac cu model de vrăjitoare');

INSERT INTO Evaluatori (Nume, Email, Data_nasterii) VALUES
('George Pop', 'george@email.com', '1990-05-15'),
('Elena Stan', 'elena@email.com', '1985-08-20'),
('Mihai Dinu', 'mihai@email.com', '1992-03-10');

INSERT INTO Nota_Dovleac(id_evaluator, id_dovleac, nota) VALUES
(1, 1, 8),
(1, 2, 7),
(2, 1, 9),
(2, 2, 8),
(3, 4, 10),
(3, 5, 9);

select * from TopDecorator

select * from Activitate_Decorare

exec AddOrUpdateActivitate 1, 4, 'Test descriere 4'