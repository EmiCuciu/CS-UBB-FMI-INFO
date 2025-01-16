USE Deserturi

CREATE TABLE Producatori(
	ID_Producator INT PRIMARY KEY IDENTITY(1,1),
	Nume NVARCHAR(100) NOT NULL,
	Website NVARCHAR(200)
);
GO


CREATE TABLE Tipuri_Deserturi(
	ID_Tip_Desert INT PRIMARY KEY IDENTITY(1,1),
	Nume NVARCHAR(100) NOT NULL
);
GO

CREATE TABLE Deserturi(
	ID_Desert INT PRIMARY KEY IDENTITY(1,1),
	Nume NVARCHAR(100) NOT NULL,
	Mod_de_preparare NVARCHAR(MAX),
	Pret DECIMAL(10,2) NOT NULL,	-- Numar real
	Numar_Calorii INT NOT NULL,
	ID_Producator INT,
	ID_Tip_Desert INT,
	CONSTRAINT FK_Deserturi_Producatori FOREIGN KEY (ID_Producator) 
		REFERENCES Producatori(ID_Producator) ON DELETE CASCADE,
	CONSTRAINT FK_Deserturi_TipuriDeserturi FOREIGN KEY (ID_Tip_Desert)
		REFERENCES Tipuri_Deserturi(ID_Tip_Desert) ON DELETE CASCADE
);
GO

CREATE TABLE Clienti(
	ID_Client INT PRIMARY KEY IDENTITY(1,1),
	Nume NVARCHAR(100) NOT NULL,
	Email NVARCHAR(100) NOT NULL,
	Data_Nasterii DATE NOT NULL
);
GO


CREATE TABLE DeserturiClienti(
	ID_Desert INT,
	ID_Client INT,
	Cantitate INT NOT NULL,
	PRIMARY KEY (ID_Desert, ID_Client),
	CONSTRAINT FK_DeserturiClienti_Desert FOREIGN KEY (ID_Desert)
		REFERENCES Deserturi(ID_Desert) ON DELETE CASCADE,
	CONSTRAINT FK_DeserturiClineti_Client FOREIGN KEY (ID_Client)
		REFERENCES Clienti(ID_Client) ON DELETE CASCADE
);
GO