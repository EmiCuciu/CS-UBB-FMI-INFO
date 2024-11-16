SELECT Marca, Model, Pret, Stoc
FROM Telefoane
WHERE Categorie = 'Smartphone' AND Stoc > 20;


SELECT CF.ID_Comanda, CF.Data_Comenzi, F.Nume AS Nume_Furnizor
FROM Comenzi_Furnizori CF
JOIN Furnizori F ON CF.ID_Furnizor = F.ID_Furnizor
WHERE CF.Data_Comenzi > '2024-09-25';


SELECT C.Nume AS Client, T.Marca, T.Model, R.Rating, R.Comentariu
FROM Recenzii_Telefoane R
JOIN Clienti C ON R.ID_Client = C.ID_Client
JOIN Telefoane T ON R.ID_Telefon = T.ID_Telefon
WHERE R.Rating = 5;


SELECT CC.ID_Comanda_Client, CC.Data_Comenzi, C.Nume AS Nume_Client
FROM Comenzi_Clienti CC
JOIN Clienti C ON CC.ID_Client = C.ID_Client
WHERE C.Nume = 'Cuciurean Emilian';


SELECT A.Nume AS Nume_Angajat, SUM(RV.Total_Vanzari) AS Total_Vanzari
FROM Rapoarte_Vanzari RV
JOIN Angajati A ON RV.ID_Angajat = A.ID_Angajat
GROUP BY A.Nume
HAVING SUM(RV.Total_Vanzari) < 5000;


SELECT DISTINCT Categorie
FROM Telefoane;


SELECT DISTINCT C.Nume
FROM Clienti C
JOIN Comenzi_Clienti CC ON C.ID_Client = CC.ID_Client;


SELECT Categorie, COUNT(ID_Telefon) AS Numar_Telefoane
FROM Telefoane
GROUP BY Categorie;


SELECT C.Nume AS Nume_Client, T.Marca, T.Model, DC.Cantitate, DC.Pret_Vanzare
FROM Detalii_Comenzi_Clienti DC
JOIN Comenzi_Clienti CC ON DC.ID_Comanda_Client = CC.ID_Comanda_Client
JOIN Clienti C ON CC.ID_Client = C.ID_Client
JOIN Telefoane T ON DC.ID_Telefon = T.ID_Telefon;


SELECT T.Marca, T.Model, COUNT(R.ID_Recenzie) AS Numar_Recenzii
FROM Recenzii_Telefoane R
JOIN Telefoane T ON R.ID_Telefon = T.ID_Telefon
WHERE R.Rating = 5
GROUP BY T.Marca, T.Model
HAVING COUNT(R.ID_Recenzie) >= 1;