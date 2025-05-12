USE MagazinTelefoane;

CREATE OR ALTER PROCEDURE T2_InsertTelefon AS
BEGIN
    BEGIN TRY
        BEGIN TRAN;

        INSERT INTO Telefoane (Marca, Model, Pret, Stoc, Categorie)
        VALUES ('TestBrand', 'TestModel', 1500, 10, 'Smartphone');

        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        INSERT INTO Log_Erori_Phantom_Reads(Mesaj_Eroare, Procedura)
        VALUES (ERROR_MESSAGE(), 'T2_InsertTelefon');
    END CATCH
END;

EXEC T2_InsertTelefon

SELECT * FROM Log_Erori_Phantom_Reads;