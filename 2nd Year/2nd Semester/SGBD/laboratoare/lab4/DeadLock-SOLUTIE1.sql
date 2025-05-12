USE MagazinTelefoane;

CREATE OR ALTER PROCEDURE T1_Solutie_Deadlock AS
BEGIN
    BEGIN TRY
        BEGIN TRAN;

        -- Respectăm ordinea: întâi ID_Telefon = 1, apoi = 2
        UPDATE Telefoane SET Pret = Pret + 100 WHERE ID_Telefon = 1;
        WAITFOR DELAY '00:00:05';
        UPDATE Telefoane SET Pret = Pret + 200 WHERE ID_Telefon = 2;

        COMMIT;
		INSERT INTO Log_Erori_Deadlock(Mesaj_Eroare, Procedura)
        VALUES ('A REUSIT T1_SOL', 'T1_Solutie_Deadlock');    
    END TRY
    BEGIN CATCH
        ROLLBACK;
        INSERT INTO Log_Erori_Deadlock(Mesaj_Eroare, Procedura)
        VALUES (ERROR_MESSAGE(), 'T1_Solutie_Deadlock');
    END CATCH
END;

EXECUTE T1_Solutie_Deadlock;

SELECT * FROM Log_Erori_Deadlock;

SELECT * FROM Telefoane;