USE MagazinTelefoane;
GO

-- Insert Telefoane
INSERT INTO Telefoane (Marca, Model, Pret, Stoc, Categorie)
VALUES 
('Apple', 'iPhone 13', 3999.99, 50, 'Smartphone'),
('Samsung', 'Galaxy S21', 3499.50, 75, 'Smartphone'),
('Xiaomi', 'Redmi Note 10', 1299.99, 100, 'Smartphone'),
('OnePlus', 'Nord 2', 2199.00, 60, 'Smartphone'),
('Google', 'Pixel 6', 2799.99, 40, 'Smartphone');

-- Insert Furnizori
INSERT INTO Furnizori (Nume, Adresa, NR_Telefon)
VALUES 
('Distribuitor Tech', 'Str. Industriei 25', '0722123456'),
('Global Electronics', 'Bd. Revolutiei 100', '0744567890'),
('Mobila Solutions', 'Str. Progresului 15', '0755234567');

-- Insert Comenzi_Furnizori
INSERT INTO Comenzi_Furnizori (Data_Comenzi, ID_Furnizor)
VALUES 
(GETDATE(), 1),
(GETDATE(), 2),
(GETDATE(), 3);

-- Insert Detalii_Comenzi_Furnizori
INSERT INTO Detalii_Comenzi_Furnizori (ID_Comanda, ID_Telefon, Cantitate, Pret_Furnizor)
VALUES 
(1, 1, 50, 2500.00),
(1, 2, 30, 2200.00),
(2, 3, 100, 800.00),
(3, 4, 40, 1500.00);

-- Insert Clienti
INSERT INTO Clienti (Nume, Adresa, Telefon)
VALUES 
('Ionescu Maria', 'Str. Principala 10', '0723456789'),
('Popescu Andrei', 'Bd. Independentei 25', '0745678901'),
('Constantinescu Elena', 'Str. Libertatii 50', '0756789012'),
('Dumitrescu Mihai', 'Str. Victoriei 75', '0767890123');

-- Insert Comenzi_Clienti
INSERT INTO Comenzi_Clienti (Data_Comenzi, ID_Client)
VALUES 
(GETDATE(), 1),
(GETDATE(), 2),
(GETDATE(), 3),
(GETDATE(), 4);

-- Insert Detalii_Comenzi_Clienti
INSERT INTO Detalii_Comenzi_Clienti (ID_Comanda_Client, ID_Telefon, Cantitate, Pret_Vanzare)
VALUES 
(1, 1, 1, 3999.99),
(2, 2, 1, 3499.50),
(3, 3, 2, 1299.99),
(4, 4, 1, 2199.00);

-- Insert Angajati
INSERT INTO Angajati (Nume, Pozitie, NR_Telefon)
VALUES 
('Popa Cristian', 'Manager Vanzari', '0722987654'),
('Marinescu Ana', 'Consultant Tehnic', '0744345678'),
('Gheorghe Daniel', 'Specialist Logistica', '0755654321');

-- Insert Rapoarte_Vanzari
INSERT INTO Rapoarte_Vanzari (ID_Angajat, ID_Comanda_Client, Data_Raport, Total_Vanzari)
VALUES 
(1, 1, GETDATE(), 3999.99),
(2, 2, GETDATE(), 3499.50),
(3, 3, GETDATE(), 2599.98);

-- Insert Recenzii_Telefoane
INSERT INTO Recenzii_Telefoane (ID_Telefon, ID_Client, Rating, Comentariu)
VALUES 
(1, 1, 5, 'Telefon excelent, foarte mulțumit de performanță!'),
(2, 2, 4, 'Design plăcut, performanță bună.'),
(3, 3, 5, 'Raport calitate-preț foarte bun.');
GO