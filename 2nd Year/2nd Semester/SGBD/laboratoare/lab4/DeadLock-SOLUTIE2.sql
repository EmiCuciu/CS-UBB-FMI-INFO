USE MagazinTelefoane;

CREATE OR ALTER PROCEDURE T2_Solutie_Deadlock AS
BEGIN 
    --SET DEADLOCK_PRIORITY HIGH  
    BEGIN TRY
        BEGIN TRAN;

        UPDATE Telefoane SET Pret = Pret - 100 WHERE ID_Telefon = 1;
        WAITFOR DELAY '00:00:05';
        UPDATE Telefoane SET Pret = Pret - 200 WHERE ID_Telefon = 2;

        COMMIT;
		INSERT INTO Log_Erori_Deadlock(Mesaj_Eroare, Procedura)
        VALUES ('A REUSIT T2_SOL', 'T2_Solutie_Deadlock');    
	END TRY
    BEGIN CATCH
        ROLLBACK;
        INSERT INTO Log_Erori_Deadlock(Mesaj_Eroare, Procedura)
        VALUES (ERROR_MESSAGE(), 'T2_Solutie_Deadlock');
    END CATCH
END;

EXECUTE T2_Solutie_Deadlock;

SELECT * FROM Log_Erori_Deadlock;

SELECT * FROM Telefoane;