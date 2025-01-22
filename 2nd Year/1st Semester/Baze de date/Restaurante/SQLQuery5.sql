create table Tipuri_Restaurante(
	ID_Tip_Restaurant int primary key identity(1,1),
	Nume_Tip nvarchar(100) not null,
	Descriere text
);

create table Orase(
	ID_Oras int primary key identity(1,1),
	Nume_Oras nvarchar(100)
);

create table Restaurant(
	ID_Restaurant int primary key identity(1,1),
	Nume_Restaurant nvarchar(100) not null,
	Adresa nvarchar(100) not null,
	NR_Telefon nvarchar(100) not null,
	ID_Oras int,
	ID_Tip_Restaurant int,
	foreign key (ID_Oras) references Orase(ID_Oras),
	foreign key (ID_Tip_Restaurant) references Tipuri_Restaurante(ID_Tip_Restaurant)
);

create table Utilizator(
	ID_Utilizator int primary key identity(1,1),
	Nume_Utilizator nvarchar(100) not null,
	email nvarchar(100) not null,
	parola nvarchar(100) not null,
);

create table Note(
	Nota float,
	ID_Restaurant int,
	ID_Utilizator int,
	foreign key (ID_Restaurant) references Restaurant(ID_Restaurant),
	foreign key (ID_Utilizator) references Utilizator(ID_Utilizator),
	primary key (ID_Restaurant, ID_Utilizator)
);

create procedure AddOrUpdateNota
	@ID_Restaurant int ,
	@ID_Utilizator int,
	@Nota int
as 
begin
	if exists(select 1 from Note
			where ID_Utilizator = @ID_Utilizator
			and ID_Restaurant = @ID_Restaurant)
	begin
		update Note
		set Nota = @Nota
		where ID_Restaurant = @ID_Restaurant 
		and ID_Utilizator = @ID_Utilizator;
	end
	else
	begin 
		insert into Note(ID_Restaurant,ID_Utilizator,Nota)
		values (@ID_Restaurant,@ID_Utilizator,@Nota);
	end
end

CREATE FUNCTION GetUserRatings
(
    @email nvarchar(100)
)
RETURNS TABLE
AS
RETURN
(
    SELECT 
        tr.Nume_Tip AS 'Tip Restaurant',
        r.Nume_Restaurant AS 'Nume Restaurant',
        r.NR_Telefon AS 'Numar Telefon',
        o.Nume_Oras AS 'Oras',
        n.Nota AS 'Nota',
        u.Nume_Utilizator AS 'Nume Utilizator',
        u.email AS 'Email'
    FROM Note n
    JOIN Restaurant r ON n.ID_Restaurant = r.ID_Restaurant
    JOIN Tipuri_Restaurante tr ON r.ID_Tip_Restaurant = tr.ID_Tip_Restaurant
    JOIN Orase o ON r.ID_Oras = o.ID_Oras
    JOIN Utilizator u ON n.ID_Utilizator = u.ID_Utilizator
    WHERE u.email = @email
);

-- Scenariu de testare
-- 1. Inserare date de test
INSERT INTO Tipuri_Restaurante (Nume_Tip, Descriere) VALUES
    ('Italian', 'Restaurant cu specific italian'),
    ('Traditional', 'Restaurant cu specific romanesc'),
    ('Fast Food', 'Restaurant tip fast food');

INSERT INTO Orase (Nume_Oras) VALUES
    ('București'),
    ('Cluj-Napoca'),
    ('Timișoara');

INSERT INTO Restaurant (Nume_Restaurant, Adresa, NR_Telefon, ID_Oras, ID_Tip_Restaurant) VALUES
    ('La Pasta', 'Str. Victoriei nr. 10', '0722111222', 1, 1),
    ('Casa Românească', 'Str. Unirii nr. 15', '0733222333', 1, 2),
    ('Quick Bite', 'Str. Eroilor nr. 5', '0744333444', 2, 3);

INSERT INTO Utilizator (Nume_Utilizator, email, parola) VALUES
    ('ion_popescu', 'ion.popescu@email.com', 'parola123'),
    ('maria_ionescu', 'maria.ionescu@email.com', 'parola456');

-- 2. Adăugare note folosind procedura stocată
EXEC AddOrUpdateNota 1, 1, 4;  -- Ion dă nota 4 la La Pasta
EXEC AddOrUpdateNota 2, 1, 5;  -- Ion dă nota 5 la Casa Românească
EXEC AddOrUpdateNota 1, 2, 3;  -- Maria dă nota 3 la La Pasta

-- 3. Testare actualizare notă
EXEC AddOrUpdateNota 1, 1, 5;  -- Ion modifică nota la La Pasta de la 4 la 5

-- 4. Testare funcție - Afișare evaluări pentru utilizatorul Ion
SELECT * FROM GetUserRatings('ion.popescu@email.com');

-- 5. Testare funcție - Afișare evaluări pentru utilizatorul Maria
SELECT * FROM GetUserRatings('maria.ionescu@email.com');