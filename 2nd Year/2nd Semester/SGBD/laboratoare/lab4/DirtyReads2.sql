use MagazinTelefoane;

CREATE OR ALTER PROCEDURE T2_DirtyRead AS
BEGIN
	SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; -- uncommited -> de aici ne citeste ce nu exista defapt in bd

	BEGIN TRY
		SELECT ID_Telefon, Marca, Model, Pret
        FROM Telefoane
        WHERE ID_Telefon = 1;
	END TRY
	BEGIN CATCH
		INSERT INTO Log_Erori_DirtyRreads(Mesaj_Eroare, Procedura)
        VALUES (ERROR_MESSAGE(), 'T2_DirtyRead');
	END CATCH
END;

EXEC T2_DirtyRead;
