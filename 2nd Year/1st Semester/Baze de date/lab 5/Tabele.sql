-- Crearea bazei de date
USE MagazinTelefoane;
GO

-- Crearea tabelului pentru Telefoane
CREATE TABLE Telefoane (
    ID_Telefon INT IDENTITY(1,1) PRIMARY KEY,    -- Cheie primară
    Marca NVARCHAR(100) NOT NULL,                 -- Marca telefonului
    Model NVARCHAR(100) NOT NULL,                 -- Model telefon
    Pret DECIMAL(10, 2) NOT NULL,                 -- Preț telefon
    Stoc INT NOT NULL,                            -- Stoc telefon
    Categorie NVARCHAR(100) NOT NULL             -- Categorie telefon
);
GO

-- Crearea tabelului pentru Furnizori
CREATE TABLE Furnizori (
    ID_Furnizor INT IDENTITY(1,1) PRIMARY KEY,   -- Cheie primară
    Nume NVARCHAR(100) NOT NULL,                  -- Nume furnizor
    Adresa NVARCHAR(255) NOT NULL,                -- Adresa furnizor
    NR_Telefon NVARCHAR(20) NOT NULL              -- Telefon furnizor
);
GO

-- Crearea tabelului pentru Comenzi_Furnizori
CREATE TABLE Comenzi_Furnizori (
    ID_Comanda INT IDENTITY(1,1) PRIMARY KEY,    -- Cheie primară
    Data_Comenzi DATETIME NOT NULL,              -- Data comenzii
    ID_Furnizor INT NOT NULL,                     -- Cheie străină către Furnizori
    CONSTRAINT FK_Comenzi_Furnizori_Furnizori FOREIGN KEY (ID_Furnizor)
        REFERENCES Furnizori(ID_Furnizor) ON DELETE CASCADE
);
GO

-- Crearea tabelului pentru Detalii_Comenzi_Furnizori
CREATE TABLE Detalii_Comenzi_Furnizori (
    ID_Comanda INT NOT NULL,                     -- Cheie străină către Comenzi_Furnizori
    ID_Telefon INT NOT NULL,                     -- Cheie străină către Telefoane
    Cantitate INT NOT NULL,                      -- Cantitate comandată
    Pret_Furnizor DECIMAL(10, 2) NOT NULL,        -- Preț furnizor
    PRIMARY KEY (ID_Comanda, ID_Telefon),        -- Cheie primară compusă
    CONSTRAINT FK_Detalii_Comenzi_Furnizori_Comenzi FOREIGN KEY (ID_Comanda)
        REFERENCES Comenzi_Furnizori(ID_Comanda) ON DELETE CASCADE,
    CONSTRAINT FK_Detalii_Comenzi_Furnizori_Telefoane FOREIGN KEY (ID_Telefon)
        REFERENCES Telefoane(ID_Telefon) ON DELETE CASCADE
);
GO

-- Crearea tabelului pentru Clienti
CREATE TABLE Clienti (
    ID_Client INT IDENTITY(1,1) PRIMARY KEY,    -- Cheie primară
    Nume NVARCHAR(100) NOT NULL,                 -- Nume client
    Adresa NVARCHAR(255) NOT NULL,               -- Adresa client
    Telefon NVARCHAR(20) NOT NULL                -- Telefon client
);
GO

-- Crearea tabelului pentru Comenzi_Clienti
CREATE TABLE Comenzi_Clienti (
    ID_Comanda_Client INT IDENTITY(1,1) PRIMARY KEY,  -- Cheie primară
    Data_Comenzi DATETIME NOT NULL,                   -- Data comenzii
    ID_Client INT NOT NULL,                            -- Cheie străină către Clienti
    CONSTRAINT FK_Comenzi_Clienti_Clienti FOREIGN KEY (ID_Client)
        REFERENCES Clienti(ID_Client) ON DELETE CASCADE
);
GO

-- Crearea tabelului pentru Detalii_Comenzi_Clienti
CREATE TABLE Detalii_Comenzi_Clienti (
    ID_Comanda_Client INT NOT NULL,                    -- Cheie străină către Comenzi_Clienti
    ID_Telefon INT NOT NULL,                           -- Cheie străină către Telefoane
    Cantitate INT NOT NULL,                            -- Cantitate comandată
    Pret_Vanzare DECIMAL(10, 2) NOT NULL,              -- Preț vânzare
    PRIMARY KEY (ID_Comanda_Client, ID_Telefon),       -- Cheie primară compusă
    CONSTRAINT FK_Detalii_Comenzi_Clienti_Comenzi FOREIGN KEY (ID_Comanda_Client)
        REFERENCES Comenzi_Clienti(ID_Comanda_Client) ON DELETE CASCADE,
    CONSTRAINT FK_Detalii_Comenzi_Clienti_Telefoane FOREIGN KEY (ID_Telefon)
        REFERENCES Telefoane(ID_Telefon) ON DELETE CASCADE
);
GO

-- Crearea tabelului pentru Angajati
CREATE TABLE Angajati (
    ID_Angajat INT IDENTITY(1,1) PRIMARY KEY,  -- Cheie primară
    Nume NVARCHAR(100) NOT NULL,                 -- Nume angajat
    Pozitie NVARCHAR(100) NOT NULL,              -- Poziție angajat
    NR_Telefon NVARCHAR(20) NOT NULL             -- Telefon angajat
);
GO

-- Crearea tabelului pentru Rapoarte_Vanzari
CREATE TABLE Rapoarte_Vanzari (
    ID_Raport INT IDENTITY(1,1) PRIMARY KEY,     -- Cheie primară
    ID_Angajat INT NOT NULL,                      -- Cheie străină către Angajati
    ID_Comanda_Client INT NOT NULL,               -- Cheie străină către Comenzi_Clienti
    Data_Raport DATETIME NOT NULL,                -- Data raportului
    Total_Vanzari DECIMAL(10, 2) NOT NULL,        -- Total vânzări
    CONSTRAINT FK_Rapoarte_Vanzari_Angajati FOREIGN KEY (ID_Angajat)
        REFERENCES Angajati(ID_Angajat) ON DELETE CASCADE,
    CONSTRAINT FK_Rapoarte_Vanzari_Comenzi FOREIGN KEY (ID_Comanda_Client)
        REFERENCES Comenzi_Clienti(ID_Comanda_Client) ON DELETE CASCADE
);
GO

-- Crearea tabelului pentru Recenzii_Telefoane
CREATE TABLE Recenzii_Telefoane (
    ID_Recenzie INT IDENTITY(1,1) PRIMARY KEY,    -- Cheie primară
    ID_Telefon INT NOT NULL,                       -- Cheie străină către Telefoane
    ID_Client INT NOT NULL,                        -- Cheie străină către Clienti
    Rating INT CHECK (Rating >= 1 AND Rating <= 5), -- Rating recenzie
    Comentariu NVARCHAR(500) NOT NULL,             -- Comentariu recenzie
    CONSTRAINT FK_Recenzii_Telefoane_Telefoane FOREIGN KEY (ID_Telefon)
        REFERENCES Telefoane(ID_Telefon) ON DELETE CASCADE,
    CONSTRAINT FK_Recenzii_Telefoane_Clienti FOREIGN KEY (ID_Client)
        REFERENCES Clienti(ID_Client) ON DELETE CASCADE
);
GO

