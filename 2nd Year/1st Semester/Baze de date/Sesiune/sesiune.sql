-- CREATE TABLE Tari (
--     IdTara INT PRIMARY KEY IDENTITY(1,1),
--     Nume VARCHAR(100) NOT NULL,
--     Suprafata DECIMAL(10, 2) NOT NULL
-- );

-- INSERT INTO Tari(Nume,Suprafata) VALUES
-- ('Romania',2355),
-- ('Anglia',1988)

-- SELECT * FROM Tari

-- CREATE TABLE Posesori (
--     IdPosesor INT PRIMARY KEY IDENTITY(1,1),
--     Nume VARCHAR(100) NOT NULL,
--     Prenume VARCHAR(100) NOT NULL,
--     Adresa VARCHAR(255) NOT NULL,
--     NumarTelefon VARCHAR(20) NOT NULL,
--     IdTara INT NOT NULL,
--     FOREIGN KEY (IdTara) REFERENCES Tari(IdTara)
-- );
-- INSERT INTO Posesori(Nume,Prenume,Adresa,NumarTelefon,IdTara) VALUES
-- ('Cuciurean','Emilian','Strada Calea Bucovinei','0754568065',1),
-- ('Adomnitei','Mihail','Strada Primaverii','0754896321',1),
-- ('Corleone','Michael','London street','0345128697',2)

-- SELECT * FROM Posesori

-- CREATE TABLE Struti (
--     IdStrut INT PRIMARY KEY IDENTITY(1,1),
--     Nume VARCHAR(100) NOT NULL,
--     DataNastere DATE NOT NULL,
--     Descriere TEXT,
--     Inaltime DECIMAL(5, 2) NOT NULL,
--     Greutate DECIMAL(5, 2) NOT NULL,
--     IdPosesor INT NOT NULL,
--     FOREIGN KEY (IdPosesor) REFERENCES Posesori(IdPosesor)
-- );

-- INSERT INTO Struti(Nume,DataNastere,Descriere,Inaltime,Greutate,IdPosesor) VALUES
-- ('Miki','2003-10-14','un strut frumos',1.56,65,1),
-- ('Alex','2005-12-01','cel mai bun',1.45,70.3,2),
-- ('Joy','2008-01-12','fain',1.65,60,3),
-- ('Kiki','2010-10-30','foarte rapid',1.55,60,1)

-- SELECT * FROM Struti

-- CREATE TABLE Competitii (
--     IdCompetitie INT PRIMARY KEY IDENTITY(1,1),
--     Nume VARCHAR(100) NOT NULL,
--     Descriere TEXT,
--     DataOraInceput DATETIME NOT NULL,
--     LocDesfasurare VARCHAR(255) NOT NULL
-- );

-- INSERT INTO Competitii(Nume,Descriere,DataOraInceput,LocDesfasurare) VALUES
-- ('Competitia Marilor Struti','se intrec in toate','2025-10-30 12:00','Cluj')

-- INSERT INTO Competitii(Nume,Descriere,DataOraInceput,LocDesfasurare) VALUES
-- ('Batalia Greilor','Greutate mare','2024-03-17 13:30','Suceava'),
-- ('Pac Pac','Concurs de fuga','2019-05-18 15:45','Bucuresti')

-- INSERT INTO Competitii(Nume,Descriere,DataOraInceput,LocDesfasurare) VALUES
-- ('Seara Buna','descriere','2021-05-14 10:00','Iasi'),
-- ('Buna dimineata','Desteptarea','2023-09-01 14:25','Radauti'),
-- ('Grodala','Examen','2025-01-21 12:54','FSEGA')

-- SELECT * FROM Competitii

-- CREATE TABLE CompetitiiStruti (
--     IdCompetitie INT NOT NULL,
--     IdStrut INT NOT NULL,
--     PozitieFinala INT NOT NULL CHECK (PozitieFinala > 0),
--     PRIMARY KEY (IdCompetitie, IdStrut),
--     FOREIGN KEY (IdCompetitie) REFERENCES Competitii(IdCompetitie),
--     FOREIGN KEY (IdStrut) REFERENCES Struti(IdStrut)
-- );


-- CREATE PROCEDURE AddOrUpdateStrutInCompetitie
--     @IdStrut INT,
--     @IdCompetitie INT,
--     @PozitieFinala INT
--     AS
--     BEGIN
--     IF EXISTS (SELECT 1 FROM CompetitiiStruti
-- 	WHERE IdStrut = @IdStrut 
-- 	AND IdCompetitie = @IdCompetitie)
--     BEGIN
--         UPDATE CompetitiiStruti
--         SET PozitieFinala = @PozitieFinala
--         WHERE IdStrut = @IdStrut 
-- 		AND IdCompetitie = @IdCompetitie;
--     END
--     ELSE
--     BEGIN
--         INSERT INTO CompetitiiStruti (IdStrut, IdCompetitie, PozitieFinala)
--         VALUES (@IdStrut, @IdCompetitie, @PozitieFinala);
--     END
-- END;

-- EXEC AddOrUpdateStrutInCompetitie 1,1,1;
-- EXEC AddOrUpdateStrutInCompetitie 2,1,9;
-- EXEC AddOrUpdateStrutInCompetitie 2,2,1;
-- EXEC AddOrUpdateStrutInCompetitie 3,1,3;

-- EXEC AddOrUpdateStrutInCompetitie 1,2,2;

-- SELECT * FROM Struti;
-- SELECT * FROM Competitii;
-- SELECT * FROM CompetitiiStruti;


-- CREATE FUNCTION Afisare()
-- RETURNS TABLE
-- AS
-- RETURN
-- (
--     SELECT
--         P.Nume AS NumePosesor,
--         P.NumarTelefon,
--         S.Nume AS NumeStrut,
--         S.DataNastere
--     FROM
--         Posesori P
--         INNER JOIN Struti S ON P.IdPosesor = S.IdPosesor
--         INNER JOIN CompetitiiStruti CS ON S.IdStrut = CS.IdStrut
--     WHERE
--         CS.PozitieFinala = 1
--         AND S.IdStrut NOT IN (
--             SELECT IdStrut
--             FROM CompetitiiStruti
--             WHERE PozitieFinala = 2
--         )
-- );

-- SELECT * FROM Afisare();
-- SELECT * FROM Struti;
-- SELECT * FROM CompetitiiStruti;
