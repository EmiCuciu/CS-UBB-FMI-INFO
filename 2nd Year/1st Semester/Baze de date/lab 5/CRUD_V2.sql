USE MagazinTelefoane;
GO


CREATE OR ALTER PROCEDURE Insert_Telefon
    @Marca NVARCHAR(100),
    @Model NVARCHAR(100),
    @Pret DECIMAL(10,2),
    @Stoc INT,
    @Categorie NVARCHAR(100),
    @MessageOutput NVARCHAR(MAX) = NULL OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (dbo.IS_NOT_NULL(@Marca) = 1 AND 
            dbo.IS_Valid_Model(@Model) = 1 AND 
            dbo.IS_Positive_Price(@Pret) = 1 AND 
            @Stoc >= 0 AND 
            dbo.IS_NOT_NULL(@Categorie) = 1)
        BEGIN
            RAISERROR('Date invalide pentru tabel Telefoane.', 16, 1);
            RETURN;
        END

        INSERT INTO Telefoane (Marca, Model, Pret, Stoc, Categorie)
        VALUES (@Marca, @Model, @Pret, @Stoc, @Categorie);
        
        SET @MessageOutput = 'Inserare reusita pentru telefonul ' + @Marca + ' ' + @Model;
    END TRY
    BEGIN CATCH
        SET @MessageOutput = ERROR_MESSAGE();
        THROW;
    END CATCH
END;
GO

CREATE OR ALTER PROCEDURE Select_Telefon
    @Marca NVARCHAR(100),
    @Model NVARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (dbo.IS_NOT_NULL(@Marca) = 1 AND dbo.IS_Valid_Model(@Model) = 1)
        BEGIN
            RAISERROR('Marca si modelul sunt obligatorii.', 16, 1);
            RETURN;
        END

        SELECT * FROM Telefoane 
        WHERE Marca = @Marca AND Model = @Model;
    END TRY
    BEGIN CATCH
        THROW;
    END CATCH
END;
GO

CREATE OR ALTER PROCEDURE Update_Telefon
    @Marca NVARCHAR(100),
    @Model NVARCHAR(100),
    @Pret DECIMAL(10,2),
    @Stoc INT,
    @Categorie NVARCHAR(100),
    @MessageOutput NVARCHAR(MAX) = NULL OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (dbo.IS_NOT_NULL(@Marca) = 1 AND 
            dbo.IS_Valid_Model(@Model) = 1 AND 
            dbo.IS_Positive_Price(@Pret) = 1 AND 
            @Stoc >= 0 AND 
            dbo.IS_NOT_NULL(@Categorie) = 1)
        BEGIN
            RAISERROR('Date invalide pentru tabel Telefoane.', 16, 1);
            RETURN;
        END

        UPDATE Telefoane
        SET 
            Pret = @Pret,
            Stoc = @Stoc,
            Categorie = @Categorie
        WHERE Marca = @Marca AND Model = @Model;

        SET @MessageOutput = 'Actualizare reusita pentru telefonul ' + @Marca + ' ' + @Model;
    END TRY
    BEGIN CATCH
        SET @MessageOutput = ERROR_MESSAGE();
        THROW;
    END CATCH
END;
GO

CREATE OR ALTER PROCEDURE Delete_Telefon
    @Marca NVARCHAR(100),
    @Model NVARCHAR(100),
    @MessageOutput NVARCHAR(MAX) = NULL OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (dbo.IS_NOT_NULL(@Marca) = 1 AND dbo.IS_Valid_Model(@Model) = 1)
        BEGIN
            RAISERROR('Marca si modelul sunt obligatorii.', 16, 1);
            RETURN;
        END

        DELETE FROM Telefoane
        WHERE Marca = @Marca AND Model = @Model;
        
        SET @MessageOutput = 'Stergere reusita pentru telefonul ' + @Marca + ' ' + @Model;
    END TRY
    BEGIN CATCH
        SET @MessageOutput = ERROR_MESSAGE();
        THROW;
    END CATCH
END;
GO


CREATE OR ALTER PROCEDURE Insert_Client
    @Nume NVARCHAR(100),
    @Adresa NVARCHAR(255),
    @Telefon NVARCHAR(20),
    @MessageOutput NVARCHAR(MAX) = NULL OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (dbo.IS_NOT_NULL(@Nume) = 1 AND 
            dbo.IS_NOT_NULL(@Adresa) = 1 AND 
            dbo.IS_Valid_Phone(@Telefon) = 1)
        BEGIN
            RAISERROR('Date invalide pentru tabel Clienti.', 16, 1);
            RETURN;
        END

        INSERT INTO Clienti (Nume, Adresa, Telefon)
        VALUES (@Nume, @Adresa, @Telefon);
        
        SET @MessageOutput = 'Inserare reusita pentru clientul ' + @Nume;
    END TRY
    BEGIN CATCH
        SET @MessageOutput = ERROR_MESSAGE();
        THROW;
    END CATCH
END;
GO

CREATE OR ALTER PROCEDURE Select_Client
    @Nume NVARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT dbo.IS_NOT_NULL(@Nume) = 1
        BEGIN
            RAISERROR('Numele clientului este obligatoriu.', 16, 1);
            RETURN;
        END

        SELECT * FROM Clienti 
        WHERE Nume = @Nume;
    END TRY
    BEGIN CATCH
        THROW;
    END CATCH
END;
GO

CREATE OR ALTER PROCEDURE Update_Client
    @Nume NVARCHAR(100),
    @Adresa NVARCHAR(255),
    @Telefon NVARCHAR(20),
    @MessageOutput NVARCHAR(MAX) = NULL OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (dbo.IS_NOT_NULL(@Nume) = 1 AND 
            dbo.IS_NOT_NULL(@Adresa) = 1 AND 
            dbo.IS_Valid_Phone(@Telefon) = 1)
        BEGIN
            RAISERROR('Date invalide pentru tabel Clienti.', 16, 1);
            RETURN;
        END

        UPDATE Clienti
        SET 
            Adresa = @Adresa,
            Telefon = @Telefon
        WHERE Nume = @Nume;

        SET @MessageOutput = 'Actualizare reusita pentru clientul ' + @Nume;
    END TRY
    BEGIN CATCH
        SET @MessageOutput = ERROR_MESSAGE();
        THROW;
    END CATCH
END;
GO

CREATE OR ALTER PROCEDURE Delete_Client
    @Nume NVARCHAR(100),
    @MessageOutput NVARCHAR(MAX) = NULL OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT dbo.IS_NOT_NULL(@Nume) = 1
        BEGIN
            RAISERROR('Numele clientului este obligatoriu.', 16, 1);
            RETURN;
        END

        DELETE FROM Clienti
        WHERE Nume = @Nume;
        
        SET @MessageOutput = 'Stergere reusita pentru clientul ' + @Nume;
    END TRY
    BEGIN CATCH
        SET @MessageOutput = ERROR_MESSAGE();
        THROW;
    END CATCH
END;
GO


CREATE OR ALTER PROCEDURE Insert_Comanda_Client
    @ID_Client INT,
    @Data_Comenzi DATETIME = NULL,
    @MessageOutput NVARCHAR(MAX) = NULL OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (@ID_Client > 0)
        BEGIN
            RAISERROR('Date invalide pentru tabel Comenzi_Clienti.', 16, 1);
            RETURN;
        END

        SET @Data_Comenzi = ISNULL(@Data_Comenzi, GETDATE());

        INSERT INTO Comenzi_Clienti (ID_Client, Data_Comenzi)
        VALUES (@ID_Client, @Data_Comenzi);
        
        SET @MessageOutput = 'Inserare reusita pentru comanda clientului cu ID ' + CAST(@ID_Client AS NVARCHAR(10));
    END TRY
    BEGIN CATCH
        SET @MessageOutput = ERROR_MESSAGE();
        THROW;
    END CATCH
END;
GO

CREATE OR ALTER PROCEDURE Select_Comanda_Client
    @ID_Client INT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (@ID_Client > 0)
        BEGIN
            RAISERROR('ID-ul clientului este obligatoriu.', 16, 1);
            RETURN;
        END

        SELECT * FROM Comenzi_Clienti 
        WHERE ID_Client = @ID_Client;
    END TRY
    BEGIN CATCH
        THROW;
    END CATCH
END;
GO

CREATE OR ALTER PROCEDURE Update_Comanda_Client
    @ID_Client INT,
    @Data_Comenzi DATETIME,
    @MessageOutput NVARCHAR(MAX) = NULL OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (@ID_Client > 0)
        BEGIN
            RAISERROR('Date invalide pentru tabel Comenzi_Clienti.', 16, 1);
            RETURN;
        END

        UPDATE Comenzi_Clienti
        SET 
            Data_Comenzi = @Data_Comenzi
        WHERE ID_Client = @ID_Client;

        SET @MessageOutput = 'Actualizare reusita pentru comanda clientului cu ID ' + CAST(@ID_Client AS NVARCHAR(10));
    END TRY
    BEGIN CATCH
        SET @MessageOutput = ERROR_MESSAGE();
        THROW;
    END CATCH
END;
GO

CREATE OR ALTER PROCEDURE Delete_Comanda_Client
    @ID_Client INT,
    @MessageOutput NVARCHAR(MAX) = NULL OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (@ID_Client > 0)
        BEGIN
            RAISERROR('ID-ul clientului este obligatoriu.', 16, 1);
            RETURN;
        END

        DELETE FROM Comenzi_Clienti
        WHERE ID_Client = @ID_Client;
        
        SET @MessageOutput = 'Stergere reusita pentru comenzile clientului cu ID ' + CAST(@ID_Client AS NVARCHAR(10));
    END TRY
    BEGIN CATCH
        SET @MessageOutput = ERROR_MESSAGE();
        THROW;
    END CATCH
END;
GO


CREATE OR ALTER PROCEDURE Insert_Detaliu_Comanda_Client
    @ID_Comanda_Client INT,
    @ID_Telefon INT,
    @Cantitate INT,
    @Pret_Vanzare DECIMAL(10,2),
    @MessageOutput NVARCHAR(MAX) = NULL OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (@ID_Comanda_Client > 0 AND @ID_Telefon > 0 AND 
                @Cantitate > 0 AND @Pret_Vanzare > 0)
        BEGIN
            RAISERROR('Date invalide pentru tabel Detalii_Comenzi_Clienti.', 16, 1);
            RETURN;
        END

        INSERT INTO Detalii_Comenzi_Clienti 
        (ID_Comanda_Client, ID_Telefon, Cantitate, Pret_Vanzare)
        VALUES 
        (@ID_Comanda_Client, @ID_Telefon, @Cantitate, @Pret_Vanzare);
        
        SET @MessageOutput = 'Inserare reusita pentru detaliile comenzii client ' + 
            CAST(@ID_Comanda_Client AS NVARCHAR(10)) + 
            ' si telefonul ' + CAST(@ID_Telefon AS NVARCHAR(10));
    END TRY
    BEGIN CATCH
        SET @MessageOutput = ERROR_MESSAGE();
        THROW;
    END CATCH
END;
GO

CREATE OR ALTER PROCEDURE Select_Detaliu_Comanda_Client
    @ID_Comanda_Client INT,
    @ID_Telefon INT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (@ID_Comanda_Client > 0 AND @ID_Telefon > 0)
        BEGIN
            RAISERROR('ID-urile comenzii si telefonului sunt obligatorii.', 16, 1);
            RETURN;
        END

        SELECT * FROM Detalii_Comenzi_Clienti 
        WHERE ID_Comanda_Client = @ID_Comanda_Client 
        AND ID_Telefon = @ID_Telefon;
    END TRY
    BEGIN CATCH
        THROW;
    END CATCH
END;
GO

CREATE OR ALTER PROCEDURE Update_Detaliu_Comanda_Client
    @ID_Comanda_Client INT,
    @ID_Telefon INT,
    @Cantitate INT,
    @Pret_Vanzare DECIMAL(10,2),
    @MessageOutput NVARCHAR(MAX) = NULL OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (@ID_Comanda_Client > 0 AND @ID_Telefon > 0 AND 
                @Cantitate > 0 AND @Pret_Vanzare > 0)
        BEGIN
            RAISERROR('Date invalide pentru tabel Detalii_Comenzi_Clienti.', 16, 1);
            RETURN;
        END

        UPDATE Detalii_Comenzi_Clienti
        SET 
            Cantitate = @Cantitate,
            Pret_Vanzare = @Pret_Vanzare
        WHERE ID_Comanda_Client = @ID_Comanda_Client 
        AND ID_Telefon = @ID_Telefon;

        SET @MessageOutput = 'Actualizare reusita pentru detaliile comenzii client ' + 
            CAST(@ID_Comanda_Client AS NVARCHAR(10)) + 
            ' si telefonul ' + CAST(@ID_Telefon AS NVARCHAR(10));
    END TRY
    BEGIN CATCH
        SET @MessageOutput = ERROR_MESSAGE();
        THROW;
    END CATCH
END;
GO

CREATE OR ALTER PROCEDURE Delete_Detaliu_Comanda_Client
    @ID_Comanda_Client INT,
    @ID_Telefon INT,
    @MessageOutput NVARCHAR(MAX) = NULL OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (@ID_Comanda_Client > 0 AND @ID_Telefon > 0)
        BEGIN
            RAISERROR('ID-urile comenzii si telefonului sunt obligatorii.', 16, 1);
            RETURN;
        END

        DELETE FROM Detalii_Comenzi_Clienti
        WHERE ID_Comanda_Client = @ID_Comanda_Client 
        AND ID_Telefon = @ID_Telefon;
        
        SET @MessageOutput = 'Stergere reusita pentru detaliile comenzii client ' + 
            CAST(@ID_Comanda_Client AS NVARCHAR(10)) + 
            ' si telefonul ' + CAST(@ID_Telefon AS NVARCHAR(10));
    END TRY
    BEGIN CATCH
        SET @MessageOutput = ERROR_MESSAGE();
        THROW;
    END CATCH
END;
GO


CREATE OR ALTER PROCEDURE Insert_Recenzie_Telefon
    @ID_Telefon INT,
    @ID_Client INT,
    @Rating INT,
    @Comentariu NVARCHAR(500),
    @MessageOutput NVARCHAR(MAX) = NULL OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (@ID_Telefon > 0 AND @ID_Client > 0 AND 
                @Rating BETWEEN 1 AND 5 AND 
                dbo.IS_NOT_NULL(@Comentariu) = 1)
        BEGIN
            RAISERROR('Date invalide pentru tabel Recenzii_Telefoane.', 16, 1);
            RETURN;
        END

        INSERT INTO Recenzii_Telefoane 
        (ID_Telefon, ID_Client, Rating, Comentariu)
        VALUES 
        (@ID_Telefon, @ID_Client, @Rating, @Comentariu);
        
        SET @MessageOutput = 'Inserare reusita pentru recenzia telefonului ' + 
            CAST(@ID_Telefon AS NVARCHAR(10)) + 
            ' de la clientul ' + CAST(@ID_Client AS NVARCHAR(10));
    END TRY
    BEGIN CATCH
        SET @MessageOutput = ERROR_MESSAGE();
        THROW;
    END CATCH
END;
GO

CREATE OR ALTER PROCEDURE Select_Recenzie_Telefon
    @ID_Telefon INT,
    @ID_Client INT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (@ID_Telefon > 0 AND @ID_Client > 0)
        BEGIN
            RAISERROR('ID-urile telefonului si clientului sunt obligatorii.', 16, 1);
            RETURN;
        END

        SELECT * FROM Recenzii_Telefoane 
        WHERE ID_Telefon = @ID_Telefon 
        AND ID_Client = @ID_Client;
    END TRY
    BEGIN CATCH
        THROW;
    END CATCH
END;
GO

CREATE OR ALTER PROCEDURE Update_Recenzie_Telefon
    @ID_Telefon INT,
    @ID_Client INT,
    @Rating INT,
    @Comentariu NVARCHAR(500),
    @MessageOutput NVARCHAR(MAX) = NULL OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (@ID_Telefon > 0 AND @ID_Client > 0 AND 
                @Rating BETWEEN 1 AND 5 AND 
                dbo.IS_NOT_NULL(@Comentariu) = 1)
        BEGIN
            RAISERROR('Date invalide pentru tabel Recenzii_Telefoane.', 16, 1);
            RETURN;
        END

        UPDATE Recenzii_Telefoane
        SET 
            Rating = @Rating,
            Comentariu = @Comentariu
        WHERE ID_Telefon = @ID_Telefon 
        AND ID_Client = @ID_Client;

        SET @MessageOutput = 'Actualizare reusita pentru recenzia telefonului ' + 
            CAST(@ID_Telefon AS NVARCHAR(10)) + 
            ' de la clientul ' + CAST(@ID_Client AS NVARCHAR(10));
    END TRY
    BEGIN CATCH
        SET @MessageOutput = ERROR_MESSAGE();
        THROW;
    END CATCH
END;
GO

CREATE OR ALTER PROCEDURE Delete_Recenzie_Telefon
    @ID_Telefon INT,
    @ID_Client INT,
    @MessageOutput NVARCHAR(MAX) = NULL OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        IF NOT (@ID_Telefon > 0 AND @ID_Client > 0)
        BEGIN
            RAISERROR('ID-urile telefonului si clientului sunt obligatorii.', 16, 1);
            RETURN;
        END

        DELETE FROM Recenzii_Telefoane
        WHERE ID_Telefon = @ID_Telefon 
        AND ID_Client = @ID_Client;
        
        SET @MessageOutput = 'Stergere reusita pentru recenzia telefonului ' + 
            CAST(@ID_Telefon AS NVARCHAR(10)) + 
            ' de la clientul ' + CAST(@ID_Client AS NVARCHAR(10));
    END TRY
    BEGIN CATCH
        SET @MessageOutput = ERROR_MESSAGE();
        THROW;
    END CATCH
END;
GO