use MagazinTelefoane;

CREATE TABLE Log_Erori_Phantom_Reads(
    ID_Log INT IDENTITY(1,1) PRIMARY KEY,
    Data_Eroare DATETIME DEFAULT GETDATE(),
    Mesaj_Eroare NVARCHAR(MAX),
    Procedura NVARCHAR(100)
);

CREATE OR ALTER PROCEDURE T1_PhantomRead AS
BEGIN
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;

    BEGIN TRY
        BEGIN TRAN;

        PRINT 'Prima citire:';
        SELECT * FROM Telefoane WHERE Pret < 2000;

        WAITFOR DELAY '00:00:10';  -- timp în care rulează T2

        PRINT 'A doua citire:';
        SELECT * FROM Telefoane WHERE Pret < 2000;

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        INSERT INTO Log_Erori_Phantom_Reads(Mesaj_Eroare, Procedura)
        VALUES (ERROR_MESSAGE(), 'T1_PhantomRead');
    END CATCH
END;

EXEC T1_PhantomRead;

SELECT * FROM Log_Erori_Phantom_Reads;

DELETE FROM Telefoane
WHERE Marca = 'TestBrand'
  AND Model = 'TestModel'
  AND Pret = 1500.00
  AND Stoc = 10
  AND Categorie = 'Smartphone';
