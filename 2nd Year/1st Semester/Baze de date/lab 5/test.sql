USE MagazinTelefoane;
GO


--TELEFOANE
select * from Telefoane
DECLARE @MessageOutput NVARCHAR(MAX);
-- Insert telefon
EXEC Insert_Telefon 
    @Marca = 'IPhone',
    @Model = 'S20',
    @Pret = 3999.99,
    @Stoc = 10,
    @Categorie = 'Flagship',
    @MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput;


DECLARE @MessageOutput NVARCHAR(MAX);
EXEC Insert_Telefon
	@Marca = 'Xiaomi',
	@Model = 'Redmi 13',
	@Pret = 4000,
	@Stoc = 12,
	@Categorie = 'Bisniz',
	@MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput;


-- Select telefon
EXEC Select_Telefon 
    @Marca = 'IPhone',
    @Model = 'S20';


EXEC Select_Telefon 
    @Marca = 'Samsung',
    @Model = 'Galaxy S21';

	
DECLARE @MessageOutput NVARCHAR(MAX);
-- Update telefon
EXEC Update_Telefon 
    @Marca = 'IPhone',
    @Model = 'S20',
    @Pret = 3799.99,  -- Reducere preț
    @Stoc = 8,        -- Actualizare stoc
    @Categorie = 'Flagship',
    @MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput;


DECLARE @MessageOutput NVARCHAR(MAX);
EXEC Delete_Telefon
	@Marca = 'IPhone',
	@Model = 'S20',
	@MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput;



--CLIENTI

select * from Clienti

DECLARE @MessageOutput NVARCHAR(MAX);
-- Insert client
EXEC Insert_Client
    @Nume = 'Larisa',
    @Adresa = 'Str. Principală nr. 10, Cluj',
    @Telefon = '0744123456',
    @MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput;

DECLARE @MessageOutput NVARCHAR(MAX);
EXEC Insert_Client
	@Nume = 'Cuciurean Andrei',
	@Adresa = 'Str. George Tofan nr 21, Bilca',
	@Telefon = '0754568421',
	@MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput;

DECLARE @MessageOutput NVARCHAR(MAX);
-- Select client
EXEC Select_Client
    @Nume = 'Larisa';

DECLARE @MessageOutput NVARCHAR(MAX);
EXEC Update_Client
	@Nume = 'Larisa',
	@Adresa = 'Strada Teodor Mihali',
	@Telefon = '0700000000',
	@MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput;

EXEC Select_Client
	@Nume = 'Popescu Ion';

DECLARE @MessageOutput NVARCHAR(MAX);
EXEC Delete_Client
	@Nume = 'Larisa',
	@MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput






--COMENZI_CLIENTI

-- Obținem ID-ul clientului pentru a-l folosi în comandă
DECLARE @MessageOutput NVARCHAR(MAX);
DECLARE @ID_Client INT;
SELECT @ID_Client = ID_Client FROM Clienti WHERE Nume = 'Popescu Ion';
-- Insert comandă
EXEC Insert_Comanda_Client
    @ID_Client = @ID_Client,
    @Data_Comenzi = '2025-01-04',
    @MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput;


DECLARE @ID_Client INT;
SELECT @ID_Client = ID_Client FROM Clienti WHERE Nume = 'Popescu Ion';
-- Select comandă
EXEC Select_Comanda_Client
    @ID_Client = @ID_Client;


select * from Comenzi_Clienti


DECLARE @MessageOutput NVARCHAR(MAX);
DECLARE @ID_Client INT;
SELECT @ID_Client = ID_Client FROM Clienti WHERE Nume = 'Popescu Ion';
EXEC Update_Comanda_Client
	@ID_Client = @ID_Client,
	@Data_Comenzi = '2025-02-12 13:30:51',
	@MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput;


DECLARE @MessageOutput NVARCHAR(MAX);
DECLARE @ID_Client INT;
SELECT @ID_Client = ID_Client FROM Clienti WHERE Nume = 'Popescu Ion';
EXEC Delete_Comanda_Client
	@ID_Client = @ID_Client,
	@MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput;




-- DETALII_COMENZI_CLIENTI

select * from Detalii_Comenzi_Clienti

-- Obținem ID-urile necesare
DECLARE @MessageOutput NVARCHAR(MAX);
DECLARE @ID_Comanda_Client INT;
DECLARE @ID_Telefon INT;
DECLARE @ID_Client INT;
SELECT @ID_Client = ID_Client FROM Clienti WHERE Nume = 'Popescu Ion';
SELECT @ID_Comanda_Client = ID_Comanda_Client FROM Comenzi_Clienti WHERE ID_Client = @ID_Client;
SELECT @ID_Telefon = ID_Telefon FROM Telefoane WHERE Marca = 'Samsung' AND Model = 'Galaxy S21';
-- Insert detalii comandă
EXEC Insert_Detaliu_Comanda_Client
    @ID_Comanda_Client = @ID_Comanda_Client,
    @ID_Telefon = @ID_Telefon,
    @Cantitate = 1,
    @Pret_Vanzare = 3799.99,
    @MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput;


DECLARE @MessageOutput NVARCHAR(MAX);
DECLARE @ID_Comanda_Client INT;
DECLARE @ID_Telefon INT;
DECLARE @ID_Client INT;
SELECT @ID_Client = ID_Client FROM Clienti WHERE Nume = 'Popescu Ion';
SELECT @ID_Comanda_Client = ID_Comanda_Client FROM Comenzi_Clienti WHERE ID_Client = @ID_Client;
SELECT @ID_Telefon = ID_Telefon FROM Telefoane WHERE Marca = 'Samsung' AND Model = 'Galaxy S21';
-- Select detalii comandă
EXEC Select_Detaliu_Comanda_Client
    @ID_Comanda_Client = @ID_Comanda_Client,
    @ID_Telefon = @ID_Telefon;


DECLARE @MessageOutput NVARCHAR(MAX);
DECLARE @ID_Comanda_Client INT;
DECLARE @ID_Telefon INT;
DECLARE @ID_Client INT;
SELECT @ID_Client = ID_Client FROM Clienti WHERE Nume = 'Popescu Ion';
SELECT @ID_Comanda_Client = ID_Comanda_Client FROM Comenzi_Clienti WHERE ID_Client = @ID_Client;
SELECT @ID_Telefon = ID_Telefon FROM Telefoane WHERE Marca = 'Samsung' AND Model = 'Galaxy S21';
-- Update detalii comanda
EXEC Update_Detaliu_Comanda_Client
	@ID_Comanda_Client = @ID_Comanda_Client,
	@ID_Telefon = @ID_Telefon,
	@Cantitate = 2,
    @Pret_Vanzare = 7599.99,
    @MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput;


DECLARE @MessageOutput NVARCHAR(MAX);
DECLARE @ID_Comanda_Client INT;
DECLARE @ID_Telefon INT;
DECLARE @ID_Client INT;
SELECT @ID_Client = ID_Client FROM Clienti WHERE Nume = 'Popescu Ion';
SELECT @ID_Comanda_Client = ID_Comanda_Client FROM Comenzi_Clienti WHERE ID_Client = @ID_Client;
SELECT @ID_Telefon = ID_Telefon FROM Telefoane WHERE Marca = 'Samsung' AND Model = 'Galaxy S21';
-- Delete detaliu comanda client
EXEC Delete_Detaliu_Comanda_Client
	@ID_Comanda_Client = @ID_Comanda_Client,
	@ID_Telefon = @ID_Telefon,
	@MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput




-- RECENZIII

select * from Recenzii_Telefoane

DECLARE @MessageOutput NVARCHAR(MAX);
DECLARE @ID_Telefon INT;
DECLARE @ID_Client INT;
SELECT @ID_Client = ID_Client FROM Clienti WHERE Nume = 'Popescu Ion';
SELECT @ID_Telefon = ID_Telefon FROM Telefoane WHERE Marca = 'Samsung' AND Model = 'Galaxy S21';
-- Insert recenzie
EXEC Insert_Recenzie_Telefon
    @ID_Telefon = @ID_Telefon,
    @ID_Client = @ID_Client,
    @Rating = 5,
    @Comentariu = 'Produs excelent, foarte multumit de achizitie!',
    @MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput;


DECLARE @MessageOutput NVARCHAR(MAX);
DECLARE @ID_Telefon INT;
DECLARE @ID_Client INT;
SELECT @ID_Client = ID_Client FROM Clienti WHERE Nume = 'Popescu Ion';
SELECT @ID_Telefon = ID_Telefon FROM Telefoane WHERE Marca = 'Samsung' AND Model = 'Galaxy S21';
-- Select recenzie
PRINT 'Selectare recenzie:';
EXEC Select_Recenzie_Telefon
    @ID_Telefon = @ID_Telefon,
    @ID_Client = @ID_Client;

DECLARE @MessageOutput NVARCHAR(MAX);
DECLARE @ID_Telefon INT;
DECLARE @ID_Client INT;
SELECT @ID_Client = ID_Client FROM Clienti WHERE Nume = 'Popescu Ion';
SELECT @ID_Telefon = ID_Telefon FROM Telefoane WHERE Marca = 'Samsung' AND Model = 'Galaxy S21';
EXEC Update_Recenzie_Telefon
	@ID_Telefon = @ID_Telefon,
	@ID_Client = @ID_Client,
	@Rating = 4,
	@Comentariu = 'Nu mai imi place asa de tare',
	@MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput;


DECLARE @MessageOutput NVARCHAR(MAX);
DECLARE @ID_Telefon INT;
DECLARE @ID_Client INT;
SELECT @ID_Client = ID_Client FROM Clienti WHERE Nume = 'Popescu Ion';
SELECT @ID_Telefon = ID_Telefon FROM Telefoane WHERE Marca = 'Samsung' AND Model = 'Galaxy S21';
EXEC Delete_Recenzie_Telefon
	@ID_Telefon = @ID_Telefon,
	@ID_Client = @ID_Client,
	@MessageOutput = @MessageOutput OUTPUT;
PRINT @MessageOutput;



SELECT * FROM View_Telefoane_Detaliat 
WHERE Marca = 'Samsung' AND Model = 'Galaxy S21';

SELECT * FROM View_Comenzi_Detaliate 
WHERE Nume_Client = 'Popescu Ion';
