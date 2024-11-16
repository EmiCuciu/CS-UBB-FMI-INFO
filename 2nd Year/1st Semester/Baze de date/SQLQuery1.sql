CREATE DATABASE MagazinTelefoane;
USE MagazinTelefoane;

-- Tabela Telefoane
CREATE TABLE Telefoane (
    ID_Telefon INT PRIMARY KEY IDENTITY(1,1),
    Marca NVARCHAR(100) NOT NULL,
    Model NVARCHAR(100) NOT NULL,
    Pret DECIMAL(10, 2) NOT NULL,
    Stoc INT NOT NULL,
    Categorie NVARCHAR(100) NOT NULL
);

-- Tabela Furnizori
CREATE TABLE Furnizori (
    ID_Furnizor INT PRIMARY KEY IDENTITY(1,1),
    Nume NVARCHAR(100) NOT NULL,
    Adresa NVARCHAR(255) NOT NULL,
    NR_Telefon NVARCHAR(20) NOT NULL
);

-- Tabela Comenzi_Furnizori
CREATE TABLE Comenzi_Furnizori (
    ID_Comanda INT PRIMARY KEY IDENTITY(1,1),
    Data_Comenzi DATETIME NOT NULL,
    ID_Furnizor INT NOT NULL,
    FOREIGN KEY (ID_Furnizor) REFERENCES Furnizori(ID_Furnizor)
);

-- Tabela Detalii_Comenzi_Furnizori
CREATE TABLE Detalii_Comenzi_Furnizori (
    ID_Comanda INT NOT NULL,
    ID_Telefon INT NOT NULL,
    Cantitate INT NOT NULL,
    Pret_Furnizor DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (ID_Comanda, ID_Telefon),
    FOREIGN KEY (ID_Comanda) REFERENCES Comenzi_Furnizori(ID_Comanda),
    FOREIGN KEY (ID_Telefon) REFERENCES Telefoane(ID_Telefon)
);

-- Tabela Clienti
CREATE TABLE Clienti (
    ID_Client INT PRIMARY KEY IDENTITY(1,1),
    Nume NVARCHAR(100) NOT NULL,
    Adresa NVARCHAR(255) NOT NULL,
    Telefon NVARCHAR(20) NOT NULL
);

-- Tabela Comenzi_Clienti
CREATE TABLE Comenzi_Clienti (
    ID_Comanda_Client INT PRIMARY KEY IDENTITY(1,1),
    Data_Comenzi DATETIME NOT NULL,
    ID_Client INT NOT NULL,
    FOREIGN KEY (ID_Client) REFERENCES Clienti(ID_Client)
);

-- Tabela Detalii_Comenzi_Clienti
CREATE TABLE Detalii_Comenzi_Clienti (
    ID_Comanda_Client INT NOT NULL,
    ID_Telefon INT NOT NULL,
    Cantitate INT NOT NULL,
    Pret_Vanzare DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (ID_Comanda_Client, ID_Telefon),
    FOREIGN KEY (ID_Comanda_Client) REFERENCES Comenzi_Clienti(ID_Comanda_Client),
    FOREIGN KEY (ID_Telefon) REFERENCES Telefoane(ID_Telefon)
);

-- Tabela Angajati
CREATE TABLE Angajati (
    ID_Angajat INT PRIMARY KEY IDENTITY(1,1),
    Nume NVARCHAR(100) NOT NULL,
    Pozitie NVARCHAR(100) NOT NULL,
    NR_Telefon NVARCHAR(20) NOT NULL
);

-- Tabela Rapoarte_Vanzari
CREATE TABLE Rapoarte_Vanzari (
    ID_Raport INT PRIMARY KEY IDENTITY(1,1),
    ID_Angajat INT NOT NULL,
    ID_Comanda_Client INT NOT NULL,
    Data_Raport DATETIME NOT NULL,
    Total_Vanzari DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (ID_Angajat) REFERENCES Angajati(ID_Angajat),
    FOREIGN KEY (ID_Comanda_Client) REFERENCES Comenzi_Clienti(ID_Comanda_Client)
);

-- Tabela Recenzii_Telefoane
CREATE TABLE Recenzii_Telefoane (
    ID_Recenzie INT PRIMARY KEY IDENTITY(1,1),
    ID_Telefon INT NOT NULL,
    ID_Client INT NOT NULL,
    Rating INT CHECK (Rating >= 1 AND Rating <= 5),
    Comentariu NVARCHAR(500) NOT NULL,
    FOREIGN KEY (ID_Telefon) REFERENCES Telefoane(ID_Telefon),
    FOREIGN KEY (ID_Client) REFERENCES Clienti(ID_Client)
);