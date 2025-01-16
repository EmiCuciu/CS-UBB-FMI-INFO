USE MagazinTelefoane;
GO

-- View Telefoane cu Comenzi și Rating
CREATE OR ALTER VIEW View_Telefoane_Detaliat
AS
SELECT 
    t.ID_Telefon, 
    t.Marca, 
    t.Model, 
    t.Pret,
    t.Stoc,
    COALESCE(SUM(dcc.Cantitate), 0) as Total_Comenzi,	-- inlocuieste NULL cu 0
    COALESCE(AVG(rt.Rating), 0) as Rating_Mediu
FROM 
    Telefoane t
LEFT JOIN 
    Detalii_Comenzi_Clienti dcc ON t.ID_Telefon = dcc.ID_Telefon
LEFT JOIN 
    Recenzii_Telefoane rt ON t.ID_Telefon = rt.ID_Telefon
GROUP BY 
    t.ID_Telefon, t.Marca, t.Model, t.Pret, t.Stoc;
GO

-- View Comenzi Detaliate
CREATE OR ALTER VIEW View_Comenzi_Detaliate
AS
SELECT 
    c.ID_Client,
    c.Nume as Nume_Client,
    cc.ID_Comanda_Client,
    cc.Data_Comenzi,
    t.Marca,
    t.Model,
    dcc.Cantitate,
    dcc.Pret_Vanzare,
    (dcc.Cantitate * dcc.Pret_Vanzare) as Total_Comanda
FROM 
    Clienti c
JOIN 
    Comenzi_Clienti cc ON c.ID_Client = cc.ID_Client
JOIN 
    Detalii_Comenzi_Clienti dcc ON cc.ID_Comanda_Client = dcc.ID_Comanda_Client
JOIN 
    Telefoane t ON dcc.ID_Telefon = t.ID_Telefon;
GO