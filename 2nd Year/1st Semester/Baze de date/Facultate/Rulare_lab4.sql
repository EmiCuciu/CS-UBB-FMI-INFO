-- Configurare test de exemplu
INSERT INTO Tests(Name) VALUES
('Test MagazinTelefoane Performance');
GO

DECLARE @TestID INT = SCOPE_IDENTITY();

-- Adăugare tabele la test
INSERT INTO TestTables(TestID, TableID, NoOfRows, Position) VALUES
(@TestID, (SELECT TableID FROM Tables WHERE Name = 'Telefoane'), 1000, 1),
(@TestID, (SELECT TableID FROM Tables WHERE Name = 'Comenzi_Clienti'), 2000, 2),
(@TestID, (SELECT TableID FROM Tables WHERE Name = 'Detalii_Comenzi_Clienti'), 3000, 3);

-- Adăugare view-uri la test
INSERT INTO TestViews(TestID, ViewID)
SELECT @TestID, ViewID FROM Views;

-- Rulare test
EXEC RunTests @TestID;

-- Verificare rezultate
SELECT * FROM TestRuns;
SELECT * FROM TestRunTables;
SELECT * FROM TestRunViews;







--DELETE FROM TestViews WHERE TestID IN (SELECT TestID FROM Tests);
--DELETE FROM TestTables WHERE TestID IN (SELECT TestID FROM Tests);
--DELETE FROM Tests;
--DELETE FROM TestRuns;