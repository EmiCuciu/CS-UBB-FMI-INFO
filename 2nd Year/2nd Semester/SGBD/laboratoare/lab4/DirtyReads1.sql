use MagazinTelefoane;

CREATE TABLE Log_Erori_DirtyRreads (
    ID_Log INT IDENTITY(1,1) PRIMARY KEY,
    Data_Eroare DATETIME DEFAULT GETDATE(),
    Mesaj_Eroare NVARCHAR(MAX),
    Procedura NVARCHAR(100)
);

CREATE OR ALTER PROCEDURE T1_DirtyRead AS
BEGIN
    BEGIN TRY
        BEGIN TRAN;

        UPDATE Telefoane SET Pret = Pret + 100 WHERE ID_Telefon = 1;

        WAITFOR DELAY '00:00:5'; 

        ROLLBACK; -- anulam update-ul

    END TRY
    BEGIN CATCH
        ROLLBACK;
        INSERT INTO Log_Erori_DirtyRreads(Mesaj_Eroare, Procedura)
        VALUES (ERROR_MESSAGE(), 'T1_DirtyRead');
    END CATCH
END;

EXEC T1_DirtyRead;

SELECT * FROM Log_Erori_DirtyRreads;

SELECT ID_Telefon, Marca, Model, Pret FROM Telefoane WHERE ID_Telefon = 1;