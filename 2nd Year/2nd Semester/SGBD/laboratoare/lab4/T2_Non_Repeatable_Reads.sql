USE MagazinTelefoane;

-- T2: tranzacția care modifică prețul
CREATE OR ALTER PROCEDURE T2_ModifyTelefon AS
BEGIN
    BEGIN TRY
        BEGIN TRAN;

        UPDATE Telefoane SET Pret = Pret + 300 WHERE ID_Telefon = 4;

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        INSERT INTO Log_Erori_Non_Rep(Mesaj_Eroare, Procedura)
        VALUES (ERROR_MESSAGE(), 'T2_ModifyTelefon');
    END CATCH
END;

EXEC T2_ModifyTelefon;