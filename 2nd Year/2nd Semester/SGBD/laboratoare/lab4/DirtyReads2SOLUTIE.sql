use MagazinTelefoane;

CREATE OR ALTER PROCEDURE T2_DirtyRead_SOLUTIE AS
BEGIN
    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;  -- COMMITED 

    BEGIN TRY
        SELECT ID_Telefon, Marca, Model, Pret
        FROM Telefoane
        WHERE ID_Telefon = 1;
    END TRY
    BEGIN CATCH
        INSERT INTO Log_Erori_Deadlock(Mesaj_Eroare, Procedura)
        VALUES (ERROR_MESSAGE(), 'T2_DirtyRead_SOLUTIE');
    END CATCH
END;

EXEC T2_DirtyRead_SOLUTIE;