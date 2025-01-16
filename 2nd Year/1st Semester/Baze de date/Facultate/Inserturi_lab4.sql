-- Inserare tabele pentru testare
INSERT INTO Tables(Name) VALUES
('Telefoane'),         -- tabel cu PK simplu, fără FK
('Comenzi_Clienti'),   -- tabel cu PK simplu și FK
('Detalii_Comenzi_Clienti');  -- tabel cu PK compus
GO

-- Procedura pentru generarea datelor de test pentru Telefoane
CREATE PROCEDURE InsertTestData_Telefoane
    @NoOfRows INT
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @Counter INT = 1;

    WHILE @Counter <= @NoOfRows
    BEGIN
        INSERT INTO Telefoane (Marca, Model, Pret, Stoc, Categorie)
        VALUES (
            'Marca_' + CAST(@Counter % 5 AS NVARCHAR(10)),
            'Model_' + CAST(@Counter AS NVARCHAR(10)),
            CAST((RAND() * 4000 + 1000) AS DECIMAL(10,2)),
            CAST((RAND() * 100) AS INT),
            CASE @Counter % 3 
                WHEN 0 THEN 'Premium'
                WHEN 1 THEN 'Mid-range'
                ELSE 'Budget'
            END
        );
        SET @Counter = @Counter + 1;
    END;
END;
GO

-- Procedura pentru generarea datelor de test pentru Comenzi_Clienti
CREATE PROCEDURE InsertTestData_Comenzi_Clienti
    @NoOfRows INT
AS
BEGIN
    SET NOCOUNT ON;
    
    -- Mai întâi ne asigurăm că avem clienți
    IF NOT EXISTS (SELECT 1 FROM Clienti)
    BEGIN
        DECLARE @ClientCounter INT = 1;
        WHILE @ClientCounter <= @NoOfRows
        BEGIN
            INSERT INTO Clienti (Nume, Adresa, Telefon)
            VALUES (
                'Client_' + CAST(@ClientCounter AS NVARCHAR(10)),
                'Adresa_' + CAST(@ClientCounter AS NVARCHAR(10)),
                '07' + RIGHT('00000000' + CAST(@ClientCounter AS VARCHAR(8)), 8)
            );
            SET @ClientCounter = @ClientCounter + 1;
        END;
    END;

    DECLARE @Counter INT = 1;
    WHILE @Counter <= @NoOfRows
    BEGIN
        INSERT INTO Comenzi_Clienti (Data_Comenzi, ID_Client)
        VALUES (
            DATEADD(DAY, -@Counter, GETDATE()),
            (@Counter % (SELECT COUNT(*) FROM Clienti)) + 1
        );
        SET @Counter = @Counter + 1;
    END;
END;
GO

-- Procedura pentru generarea datelor de test pentru Detalii_Comenzi_Clienti
CREATE PROCEDURE InsertTestData_Detalii_Comenzi_Clienti
    @NoOfRows INT
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @Counter INT = 1;
    DECLARE @MaxComandaID INT, @MaxTelefonID INT;
    DECLARE @CurrentComandaID INT = 1;
    DECLARE @CurrentTelefonID INT = 1;

    SELECT @MaxComandaID = ISNULL(MAX(ID_Comanda_Client), 0) FROM Comenzi_Clienti;
    SELECT @MaxTelefonID = ISNULL(MAX(ID_Telefon), 0) FROM Telefoane;

    -- Ștergem datele existente
    DELETE FROM Detalii_Comenzi_Clienti;

    WHILE @Counter <= @NoOfRows
    BEGIN
        -- Generăm ID-uri unice pentru fiecare combinație
        SET @CurrentComandaID = (@Counter % @MaxComandaID) + 1;
        SET @CurrentTelefonID = ((@Counter - 1) % @MaxTelefonID) + 1;

        -- Verificăm dacă combinația există deja
        IF NOT EXISTS (
            SELECT 1 
            FROM Detalii_Comenzi_Clienti 
            WHERE ID_Comanda_Client = @CurrentComandaID 
            AND ID_Telefon = @CurrentTelefonID
        )
        BEGIN
            INSERT INTO Detalii_Comenzi_Clienti (
                ID_Comanda_Client,
                ID_Telefon,
                Cantitate,
                Pret_Vanzare
            )
            VALUES (
                @CurrentComandaID,
                @CurrentTelefonID,
                CAST((RAND() * 4 + 1) AS INT),  -- Cantitate între 1 și 5
                CAST((RAND() * 3000 + 500) AS DECIMAL(10,2))  -- Preț între 500 și 3500
            );
        END
        SET @Counter = @Counter + 1;
    END;
END;
GO



-- Crearea view-urilor pentru testare
CREATE VIEW View_StocTelefoane AS
SELECT Marca, Model, Pret, Stoc, Categorie
FROM Telefoane
WHERE Stoc > 0;
GO

CREATE VIEW View_ComenziClienti AS
SELECT 
    cc.ID_Comanda_Client,
    c.Nume AS NumeClient,
    COUNT(dcc.ID_Telefon) AS NumarProduse,
    SUM(dcc.Cantitate * dcc.Pret_Vanzare) AS ValoareTotala
FROM Comenzi_Clienti cc
JOIN Clienti c ON cc.ID_Client = c.ID_Client
JOIN Detalii_Comenzi_Clienti dcc ON cc.ID_Comanda_Client = dcc.ID_Comanda_Client
GROUP BY cc.ID_Comanda_Client, c.Nume;
GO

CREATE VIEW View_VanzariPerMarca AS
SELECT 
    t.Marca,
    COUNT(DISTINCT cc.ID_Comanda_Client) AS NumarComenzi,
    SUM(dcc.Cantitate) AS TotalProduse,
    SUM(dcc.Cantitate * dcc.Pret_Vanzare) AS VanzariTotale
FROM Telefoane t
JOIN Detalii_Comenzi_Clienti dcc ON t.ID_Telefon = dcc.ID_Telefon
JOIN Comenzi_Clienti cc ON dcc.ID_Comanda_Client = cc.ID_Comanda_Client
GROUP BY t.Marca;
GO

-- Inserare view-uri în tabela Views
INSERT INTO Views(Name) VALUES
('View_StocTelefoane'),
('View_ComenziClienti'),
('View_VanzariPerMarca');
GO