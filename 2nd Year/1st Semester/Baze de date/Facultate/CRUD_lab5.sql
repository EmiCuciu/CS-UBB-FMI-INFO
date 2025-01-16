USE MagazinTelefoane;
GO

-- Validation Functions

-- Generic NOT NULL validation
CREATE OR ALTER FUNCTION IS_NOT_NULL(@input NVARCHAR(MAX))
RETURNS INT
AS
BEGIN
    IF @input IS NOT NULL AND LEN(@input) > 0
    BEGIN
        RETURN 1
    END
    RETURN 0
END
GO

-- Function to validate phone model length
CREATE OR ALTER FUNCTION IS_Valid_Model(@Model NVARCHAR(100))
RETURNS INT
AS
BEGIN
    IF LEN(@Model) BETWEEN 2 AND 100
    BEGIN
        RETURN 1
    END
    RETURN 0
END
GO

-- Function to validate positive price
CREATE OR ALTER FUNCTION IS_Positive_Price(@Price DECIMAL(10,2))
RETURNS INT
AS
BEGIN
    IF @Price > 0
    BEGIN
        RETURN 1
    END
    RETURN 0
END
GO

-- CRUD Procedure for Telefoane
CREATE OR ALTER PROCEDURE CRUD_Telefoane
    @Marca NVARCHAR(100),
    @Model NVARCHAR(100),
    @Pret DECIMAL(10,2),
    @Stoc INT,
    @Categorie NVARCHAR(100),
    @num_of_rows INT = 1,
    @Operation NVARCHAR(10) = 'ALL'
AS
BEGIN
    SET NOCOUNT ON;
    IF (dbo.IS_NOT_NULL(@Marca) = 1 AND 
        dbo.IS_Valid_Model(@Model) = 1 AND 
        dbo.IS_Positive_Price(@Pret) = 1 AND 
        @Stoc >= 0 AND 
        dbo.IS_NOT_NULL(@Categorie) = 1)
    BEGIN
        IF @Operation IN ('INSERT', 'ALL')
        BEGIN
            DECLARE @n INT = 0;
            WHILE (@n < @num_of_rows)
            BEGIN
                INSERT INTO Telefoane (Marca, Model, Pret, Stoc, Categorie)
                VALUES (@Marca, @Model, @Pret, @Stoc, @Categorie);
                SET @n = @n + 1;
            END
        END

        IF @Operation IN ('SELECT', 'ALL')
        BEGIN
            SELECT * FROM Telefoane 
            WHERE Marca = @Marca AND Model = @Model;
        END

        IF @Operation IN ('UPDATE', 'ALL')
        BEGIN
            UPDATE Telefoane
            SET 
                Pret = @Pret + 100,
                Stoc = @Stoc + 10
            WHERE Marca = @Marca AND Model = @Model;

            SELECT * FROM Telefoane 
            WHERE Marca = @Marca AND Model = @Model;
        END

        IF @Operation IN ('DELETE', 'ALL')
        BEGIN
            DELETE FROM Telefoane
            WHERE Marca = @Marca AND Model = @Model;
        END

        PRINT 'CRUD operations for Telefoane completed.';
    END
    ELSE
    BEGIN
        RAISERROR('Invalid input data for Telefoane.', 16, 1);
    END
END
GO

-- CRUD Procedure for Clienti
CREATE OR ALTER PROCEDURE CRUD_Clienti
    @Nume NVARCHAR(100),
    @Adresa NVARCHAR(255),
    @Telefon NVARCHAR(20),
    @num_of_rows INT = 1,
    @Operation NVARCHAR(10) = 'ALL'
AS
BEGIN
    SET NOCOUNT ON;
    IF (dbo.IS_NOT_NULL(@Nume) = 1 AND 
        dbo.IS_NOT_NULL(@Adresa) = 1 AND 
        dbo.IS_NOT_NULL(@Telefon) = 1)
    BEGIN
        IF @Operation IN ('INSERT', 'ALL')
        BEGIN
            DECLARE @n INT = 0;
            WHILE (@n < @num_of_rows)
            BEGIN
                INSERT INTO Clienti (Nume, Adresa, Telefon)
                VALUES (@Nume, @Adresa, @Telefon);
                SET @n = @n + 1;
            END
        END

        IF @Operation IN ('SELECT', 'ALL')
        BEGIN
            SELECT * FROM Clienti 
            WHERE Nume = @Nume;
        END

        IF @Operation IN ('UPDATE', 'ALL')
        BEGIN
            UPDATE Clienti
            SET 
                Adresa = @Adresa + ' - Actualizat',
                Telefon = '0000000000'
            WHERE Nume = @Nume;

            SELECT * FROM Clienti 
            WHERE Nume = @Nume;
        END

        IF @Operation IN ('DELETE', 'ALL')
        BEGIN
            DELETE FROM Clienti
            WHERE Nume = @Nume;
        END

        PRINT 'CRUD operations for Clienti completed.';
    END
    ELSE
    BEGIN
        RAISERROR('Invalid input data for Clienti.', 16, 1);
    END
END
GO

-- CRUD Procedure for Comenzi_Clienti (Many-to-Many related)
CREATE OR ALTER PROCEDURE CRUD_Comenzi_Clienti
    @ID_Client INT,
    @Data_Comenzi DATETIME = NULL,
    @num_of_rows INT = 1,
    @Operation NVARCHAR(10) = 'ALL'
AS
BEGIN
    SET NOCOUNT ON;
    IF (@ID_Client > 0)
    BEGIN
        SET @Data_Comenzi = ISNULL(@Data_Comenzi, GETDATE());

        IF @Operation IN ('INSERT', 'ALL')
        BEGIN
            DECLARE @n INT = 0;
            WHILE (@n < @num_of_rows)
            BEGIN
                INSERT INTO Comenzi_Clienti (ID_Client, Data_Comenzi)
                VALUES (@ID_Client, @Data_Comenzi);
                SET @n = @n + 1;
            END
        END

        IF @Operation IN ('SELECT', 'ALL')
        BEGIN
            SELECT * FROM Comenzi_Clienti 
            WHERE ID_Client = @ID_Client;
        END

        IF @Operation IN ('UPDATE', 'ALL')
        BEGIN
            UPDATE Comenzi_Clienti
            SET 
                Data_Comenzi = DATEADD(day, 7, Data_Comenzi)
            WHERE ID_Client = @ID_Client;

            SELECT * FROM Comenzi_Clienti 
            WHERE ID_Client = @ID_Client;
        END

        IF @Operation IN ('DELETE', 'ALL')
        BEGIN
            DELETE FROM Comenzi_Clienti
            WHERE ID_Client = @ID_Client;
        END

        PRINT 'CRUD operations for Comenzi_Clienti completed.';
    END
    ELSE
    BEGIN
        RAISERROR('Invalid input data for Comenzi_Clienti.', 16, 1);
    END
END
GO

-- CRUD Procedure for Detalii_Comenzi_Clienti (Many-to-Many)
CREATE OR ALTER PROCEDURE CRUD_Detalii_Comenzi_Clienti
    @ID_Comanda_Client INT,
    @ID_Telefon INT,
    @Cantitate INT,
    @Pret_Vanzare DECIMAL(10,2),
    @num_of_rows INT = 1,
    @Operation NVARCHAR(10) = 'ALL'
AS
BEGIN
    SET NOCOUNT ON;
    IF (@ID_Comanda_Client > 0 AND @ID_Telefon > 0 AND @Cantitate > 0 AND @Pret_Vanzare > 0)
    BEGIN
        IF @Operation IN ('INSERT', 'ALL')
        BEGIN
            DECLARE @n INT = 0;
            WHILE (@n < @num_of_rows)
            BEGIN
                INSERT INTO Detalii_Comenzi_Clienti 
                (ID_Comanda_Client, ID_Telefon, Cantitate, Pret_Vanzare)
                VALUES 
                (@ID_Comanda_Client, @ID_Telefon, @Cantitate, @Pret_Vanzare);
                SET @n = @n + 1;
            END
        END

        IF @Operation IN ('SELECT', 'ALL')
        BEGIN
            SELECT * FROM Detalii_Comenzi_Clienti 
            WHERE ID_Comanda_Client = @ID_Comanda_Client 
            AND ID_Telefon = @ID_Telefon;
        END

        IF @Operation IN ('UPDATE', 'ALL')
        BEGIN
            UPDATE Detalii_Comenzi_Clienti
            SET 
                Cantitate = @Cantitate + 1,
                Pret_Vanzare = @Pret_Vanzare * 1.1
            WHERE ID_Comanda_Client = @ID_Comanda_Client 
            AND ID_Telefon = @ID_Telefon;

            SELECT * FROM Detalii_Comenzi_Clienti 
            WHERE ID_Comanda_Client = @ID_Comanda_Client 
            AND ID_Telefon = @ID_Telefon;
        END

        IF @Operation IN ('DELETE', 'ALL')
        BEGIN
            DELETE FROM Detalii_Comenzi_Clienti
            WHERE ID_Comanda_Client = @ID_Comanda_Client 
            AND ID_Telefon = @ID_Telefon;
        END

        PRINT 'CRUD operations for Detalii_Comenzi_Clienti completed.';
    END
    ELSE
    BEGIN
        RAISERROR('Invalid input data for Detalii_Comenzi_Clienti.', 16, 1);
    END
END
GO

-- CRUD Procedure for Recenzii_Telefoane
CREATE OR ALTER PROCEDURE CRUD_Recenzii_Telefoane
    @ID_Telefon INT,
    @ID_Client INT,
    @Rating INT,
    @Comentariu NVARCHAR(500),
    @num_of_rows INT = 1,
    @Operation NVARCHAR(10) = 'ALL'
AS
BEGIN
    SET NOCOUNT ON;
    IF (@ID_Telefon > 0 AND @ID_Client > 0 AND @Rating BETWEEN 1 AND 5 AND dbo.IS_NOT_NULL(@Comentariu) = 1)
    BEGIN
        IF @Operation IN ('INSERT', 'ALL')
        BEGIN
            DECLARE @n INT = 0;
            WHILE (@n < @num_of_rows)
            BEGIN
                INSERT INTO Recenzii_Telefoane 
                (ID_Telefon, ID_Client, Rating, Comentariu)
                VALUES 
                (@ID_Telefon, @ID_Client, @Rating, @Comentariu);
                SET @n = @n + 1;
            END
        END

        IF @Operation IN ('SELECT', 'ALL')
        BEGIN
            SELECT * FROM Recenzii_Telefoane 
            WHERE ID_Telefon = @ID_Telefon 
            AND ID_Client = @ID_Client;
        END

        IF @Operation IN ('UPDATE', 'ALL')
        BEGIN
            UPDATE Recenzii_Telefoane
            SET 
                Rating = CASE WHEN @Rating < 5 THEN @Rating + 1 ELSE @Rating END,
                Comentariu = @Comentariu + ' (Actualizat)'
            WHERE ID_Telefon = @ID_Telefon 
            AND ID_Client = @ID_Client;

            SELECT * FROM Recenzii_Telefoane 
            WHERE ID_Telefon = @ID_Telefon 
            AND ID_Client = @ID_Client;
        END

        IF @Operation IN ('DELETE', 'ALL')
        BEGIN
            DELETE FROM Recenzii_Telefoane
            WHERE ID_Telefon = @ID_Telefon 
            AND ID_Client = @ID_Client;
        END

        PRINT 'CRUD operations for Recenzii_Telefoane completed.';
    END
    ELSE
    BEGIN
        RAISERROR('Invalid input data for Recenzii_Telefoane.', 16, 1);
    END
END
GO

-- Views
CREATE OR ALTER VIEW View_Telefoane_Cu_Comenzi
AS
SELECT 
    t.ID_Telefon, 
    t.Marca, 
    t.Model, 
    t.Pret,
    SUM(dcc.Cantitate) as Total_Comenzi,
    AVG(r.Rating) as Rating_Mediu
FROM 
    Telefoane t
LEFT JOIN 
    Detalii_Comenzi_Clienti dcc ON t.ID_Telefon = dcc.ID_Telefon
LEFT JOIN 
    Recenzii_Telefoane r ON t.ID_Telefon = r.ID_Telefon
GROUP BY 
    t.ID_Telefon, t.Marca, t.Model, t.Pret;
GO

CREATE OR ALTER VIEW View_Comenzi_Client_Detaliate
AS
SELECT 
    c.ID_Client,
    c.Nume,
    cc.ID_Comanda_Client,
    cc.Data_Comenzi,
    t.Marca,
    t.Model,
    dcc.Cantitate,
    dcc.Pret_Vanzare
FROM 
    Clienti c
JOIN 
    Comenzi_Clienti cc ON c.ID_Client = cc.ID_Client
JOIN 
    Detalii_Comenzi_Clienti dcc ON cc.ID_Comanda_Client = dcc.ID_Comanda_Client
JOIN 
    Telefoane t ON dcc.ID_Telefon = t.ID_Telefon;
GO

-- Non-Clustered Indexes
CREATE NONCLUSTERED INDEX N_idx_Telefoane_Pret ON Telefoane (Pret);
CREATE NONCLUSTERED INDEX N_idx_Comenzi_Clienti_Data ON Comenzi_Clienti (Data_Comenzi);
CREATE NONCLUSTERED INDEX N_idx_Clienti_Nume ON Clienti (Nume);
CREATE NONCLUSTERED INDEX N_idx_Comenzi_Detalii ON Detalii_Comenzi_Clienti (ID_Comanda_Client, ID_Telefon);
CREATE NONCLUSTERED INDEX N_idx_Recenzii_Rating ON Recenzii_Telefoane (Rating);
GO

-- DMV Index Usage Check
CREATE OR ALTER PROCEDURE Check_Index_Usage
AS
BEGIN
    SELECT 
        OBJECT_NAME(i.object_id) AS TableName,
        i.name AS IndexName,
        s.user_seeks,
        s.user_scans,
        s.user_lookups,
        s.user_updates
    FROM 
        sys.dm_db_index_usage_stats s
    INNER JOIN 
        sys.indexes i ON i.object_id = s.object_id AND i.index_id = s.index_id
    WHERE 
        s.database_id = DB_ID('MagazinTelefoane');
END
GO

select * from View_Telefoane_Cu_Comenzi
EXEC CRUD_Telefoane 'Samsung','S8 PLUS','1000','10','Smartphone';
EXEC CRUD_Clienti 'Popescu Ion', 'Str. Exemple 15', '0723111222';