USE MagazinTelefoane;

CREATE TABLE Versiune (
    cod_v INT PRIMARY KEY IDENTITY,
    nr_versiune INT,
    DataModificare DATETIME
);


IF NOT EXISTS (SELECT 1 FROM Versiune)
BEGIN
    INSERT INTO Versiune (nr_versiune, DataModificare)
    VALUES (0, GETDATE());
END;

CREATE PROCEDURE ModificaColoanaPret
AS
BEGIN
    ALTER TABLE Telefoane
    ALTER COLUMN Pret FLOAT;
    PRINT 'Tipul coloanei Pret a fost modificat la FLOAT';
END;

CREATE PROCEDURE ModificaColoanaPret_UNDO
AS
BEGIN
    ALTER TABLE Telefoane
    ALTER COLUMN Pret DECIMAL(10,2);
    PRINT 'Tipul coloanei Pret a revenit la DECIMAL(10,2)';
END;

CREATE PROCEDURE AdaugaConstrangereImplicitaStoc
AS
BEGIN
    ALTER TABLE Telefoane ADD CONSTRAINT DF_Stoc DEFAULT 10 FOR Stoc;
    PRINT 'Valoare implicită 10 adăugată pentru coloana Stoc';
END;

CREATE PROCEDURE AdaugaConstrangereImplicitaStoc_UNDO
AS
BEGIN
    ALTER TABLE Telefoane DROP CONSTRAINT DF_Stoc;
    PRINT 'Constrângerea implicită pentru Stoc a fost eliminată';
END;

CREATE PROCEDURE CreareTabelPromotii
AS
BEGIN
    CREATE TABLE Promotii (
        ID_Promotie INT PRIMARY KEY IDENTITY,
        Descriere NVARCHAR(255),
        Discount DECIMAL(5, 2)
    );
    PRINT 'Tabelul Promotii a fost creat';
END;

CREATE PROCEDURE CreareTabelPromotii_UNDO
AS
BEGIN
    DROP TABLE Promotii;
    PRINT 'Tabelul Promotii a fost șters';
END;

CREATE PROCEDURE AdaugaCampGarantie
AS
BEGIN
    ALTER TABLE Telefoane ADD Garantie INT DEFAULT 24;
    PRINT 'Câmpul Garantie a fost adăugat în tabelul Telefoane';
END;

DROP PROCEDURE AdaugaCampGarantie

CREATE PROCEDURE AdaugaCampGarantie_UNDO
AS
BEGIN
    ALTER TABLE Telefoane
    DROP CONSTRAINT DF_Garantie;
    
    PRINT 'Constrângerea implicită DF_Garantie pentru coloana Garantie a fost eliminată';

    ALTER TABLE Telefoane
    DROP COLUMN Garantie;

    PRINT 'Coloana Garantie a fost eliminată din tabelul Telefoane';
END;



DROP PROCEDURE AdaugaCampGarantie_UNDO


CREATE PROCEDURE AdaugaCheieStrainaDetaliiComenziClienti
AS
BEGIN
    ALTER TABLE Detalii_Comenzi_Clienti
    ADD CONSTRAINT FK_DetaliiComenziClienti_ComenziClienti
    FOREIGN KEY (ID_Comanda_Client) REFERENCES Comenzi_Clienti(ID_Comanda_Client);
    PRINT 'Constrângerea de cheie străină FK_DetaliiComenziClienti_ComenziClienti a fost adăugată';
END;

CREATE PROCEDURE StergeCheieStrainaDetaliiComenziClienti
AS
BEGIN
    ALTER TABLE Detalii_Comenzi_Clienti
    DROP CONSTRAINT FK_DetaliiComenziClienti_ComenziClienti;
    PRINT 'Constrângerea de cheie străină FK_DetaliiComenziClienti_ComenziClienti a fost eliminată';
END;



CREATE PROCEDURE Schimba_Versiune (@VersiuneDorita INT)
AS
BEGIN
    DECLARE @VersiuneCurenta INT;

    SELECT TOP 1 @VersiuneCurenta = nr_versiune 
    FROM Versiune 
    ORDER BY cod_v DESC;

    IF @VersiuneCurenta IS NULL
    BEGIN
        SET @VersiuneCurenta = 0;
    END

    IF @VersiuneDorita < 0 OR @VersiuneDorita > 5
    BEGIN
        PRINT 'Versiunea dorita nu este valida. Te rugam sa alegi o versiune intre 0 si 5.';
        RETURN;
    END

    PRINT 'Versiunea curenta a bazei de date este ' + CAST(@VersiuneCurenta AS VARCHAR);

    IF @VersiuneCurenta = @VersiuneDorita
    BEGIN
        PRINT 'Baza de date este deja la versiunea dorita: ' + CAST(@VersiuneDorita AS VARCHAR);
        RETURN;
    END

    WHILE @VersiuneCurenta <> @VersiuneDorita
    BEGIN
        IF @VersiuneCurenta < @VersiuneDorita
        BEGIN
            SET @VersiuneCurenta = @VersiuneCurenta + 1;
            PRINT 'Actualizare la versiunea ' + CAST(@VersiuneCurenta AS VARCHAR);
            
            IF @VersiuneCurenta = 1 EXEC ModificaColoanaPret;
            ELSE IF @VersiuneCurenta = 2 EXEC AdaugaConstrangereImplicitaStoc;
            ELSE IF @VersiuneCurenta = 3 EXEC CreareTabelPromotii;
            ELSE IF @VersiuneCurenta = 4 EXEC AdaugaCampGarantie;
            ELSE IF @VersiuneCurenta = 5 EXEC AdaugaCheieStrainaDetaliiComenziClienti;
        END
        ELSE
        BEGIN
            PRINT 'Anulare modificari si revenire la versiunea ' + CAST(@VersiuneCurenta - 1 AS VARCHAR);
            
            IF @VersiuneCurenta = 5 EXEC StergeCheieStrainaDetaliiComenziClienti;
            ELSE IF @VersiuneCurenta = 4 EXEC AdaugaCampGarantie_UNDO;
            ELSE IF @VersiuneCurenta = 3 EXEC CreareTabelPromotii_UNDO;
            ELSE IF @VersiuneCurenta = 2 EXEC AdaugaConstrangereImplicitaStoc_UNDO;
            ELSE IF @VersiuneCurenta = 1 EXEC ModificaColoanaPret_UNDO;

            SET @VersiuneCurenta = @VersiuneCurenta - 1;
        END

        INSERT INTO Versiune (nr_versiune, DataModificare) 
        VALUES (@VersiuneCurenta, GETDATE());
    END

    PRINT 'Baza de date a fost actualizata la versiunea ' + CAST(@VersiuneDorita AS VARCHAR);
END;

DROP PROCEDURE Schimba_Versiune

EXEC Schimba_Versiune @VersiuneDorita = 0;

SELECT * FROM Versiune

TRUNCATE TABLE Versiune