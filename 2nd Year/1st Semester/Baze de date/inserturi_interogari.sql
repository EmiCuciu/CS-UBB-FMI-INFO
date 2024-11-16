USE MagazinTelefoane
INSERT INTO Furnizori (Nume, Adresa, NR_Telefon) VALUES 
('Furnizori China', 'Strada Alexandru cel Bun', '0754321687'),
('DistribMobile', 'Strada Lunii', '0230411857');

SELECT * FROM Furnizori

INSERT INTO Telefoane (Marca, Model, Pret, Stoc, Categorie) VALUES 
('Samsung', 'Galaxy S21', 3500.00, 20, 'Smartphone'),
('Apple', 'iPhone 13', 4500.00, 15, 'Smartphone'),
('Xiaomi', 'Redmi Note 10', 1200.00, 30, 'Smartphone'),
('Nokia', '3310', 200.00, 10, 'Feature Phone');

INSERT INTO Clienti (Nume, Adresa, Telefon) VALUES 
('Andrei Popescu', 'Strada Aviatorilor 10, Bucuresti', '0721001234'),
('Maria Ionescu', 'Strada Mihai Eminescu 32, Cluj-Napoca', '0734005678'),
('Cuciurean Emilian', 'Strada Bucovinei, Bilca', '0754568065');

INSERT INTO Clienti (Nume, Adresa, Telefon) 
VALUES ('Ion Popa', 'Strada Libertatii, 5', '0745123456');

INSERT INTO Angajati (Nume, Pozitie, NR_Telefon) VALUES 
('Ion Vasile', 'Vanzator', '0723456789'),
('Alina Georgescu', 'Manager', '0732345678');

INSERT INTO Comenzi_Furnizori (Data_Comenzi, ID_Furnizor) VALUES 
('2024-10-01 12:30:21', 1),
('2024-09-17 20:25:30', 2);

INSERT INTO Detalii_Comenzi_Furnizori (ID_Comanda, ID_Telefon, Cantitate, Pret_Furnizor) VALUES 
(1, 2, 10, 3000.00),
(1, 3, 15, 1000.00),
(2, 1, 8, 3000.00);



INSERT INTO Comenzi_Clienti (Data_Comenzi, ID_Client) VALUES 
('2024-10-30 16:14:41', 3),  -- Corectat ID_Client la 3
('2024-10-06 15:48:30', 2),
('2024-06-12 01:12:4', 1),
('2024-10-30 15:12:10', 2);

INSERT INTO Detalii_Comenzi_Clienti (ID_Comanda_Client, ID_Telefon, Cantitate, Pret_Vanzare) VALUES
(1, 1, 1, 3500.00),
(1, 3, 2, 1200.00),
(2, 2, 1, 4500.00),
(3, 4, 10, 200.00),
(4, 2, 1, 4500.00);

INSERT INTO Rapoarte_Vanzari (ID_Angajat, ID_Comanda_Client, Data_Raport, Total_Vanzari) VALUES
(1, 1, '2024-10-10 14:20:14', 5900.00),
(1, 2, '2024-10-11 15:00:07', 4500.00),
(2, 3, '2024-10-14 10:10:10', 2000.00),
(1, 4, '2024-10-19 17:41:30', 4500.00);

INSERT INTO Recenzii_Telefoane (ID_Telefon, ID_Client, Rating, Comentariu) VALUES
(1, 1, 5, 'Foarte multumit de acest telefon!'),
(2, 2, 4, 'Calitate buna, dar putin cam scump.');