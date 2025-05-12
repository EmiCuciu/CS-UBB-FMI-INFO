use MagazinTelefoane;

CREATE TABLE Log_Erori_Non_Rep(
    ID_Log INT IDENTITY(1,1) PRIMARY KEY,
    Data_Eroare DATETIME DEFAULT GETDATE(),
    Mesaj_Eroare NVARCHAR(MAX),
    Procedura NVARCHAR(100)
);

CREATE OR ALTER PROCEDURE T1_NonRepeatableRead AS
BEGIN
    --SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
	
	--SOLUTIE
	SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;


    BEGIN TRY
        BEGIN TRAN;

        PRINT 'Prima citire:';
        SELECT Pret FROM Telefoane WHERE ID_Telefon = 4;

        WAITFOR DELAY '00:00:10';  -- timp în care rulează T2

        PRINT 'A doua citire:';
        SELECT Pret FROM Telefoane WHERE ID_Telefon = 4;

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        INSERT INTO Log_Erori_Non_Rep(Mesaj_Eroare, Procedura)
        VALUES (ERROR_MESSAGE(), 'T1_NonRepeatableRead');
    END CATCH
END;

EXEC T1_NonRepeatableRead;

SELECT * FROM Log_Erori_Non_Rep;