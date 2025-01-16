USE MagazinTelefoane;
GO

-- Indexuri Non-Clustered
CREATE NONCLUSTERED INDEX IX_Telefoane_Pret 
ON Telefoane (Pret) INCLUDE (Marca, Model);
go

CREATE NONCLUSTERED INDEX IX_Comenzi_Data 
ON Comenzi_Clienti (Data_Comenzi) INCLUDE (ID_Client);
go

CREATE NONCLUSTERED INDEX IX_Clienti_Nume 
ON Clienti (Nume) INCLUDE (Telefon);
go

CREATE NONCLUSTERED INDEX IX_Comenzi_Clienti_Detalii
ON Detalii_Comenzi_Clienti (ID_Comanda_Client, ID_Telefon);
go

CREATE NONCLUSTERED INDEX IX_Recenzii_Rating
ON Recenzii_Telefoane (Rating) INCLUDE (ID_Telefon, ID_Client);
go
