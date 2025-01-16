USE MagazinTelefoane;
GO

-- Funcție validare NOT NULL
CREATE OR ALTER FUNCTION IS_NOT_NULL(@input NVARCHAR(MAX))
RETURNS INT
AS
BEGIN
    IF @input IS NOT NULL AND LEN(@input) > 0
        RETURN 1
    RETURN 0
END
GO

-- Funcție validare lungime model telefon
CREATE OR ALTER FUNCTION IS_Valid_Model(@Model NVARCHAR(100))
RETURNS INT
AS
BEGIN
    IF LEN(@Model) BETWEEN 2 AND 100
        RETURN 1
    RETURN 0
END
GO

-- Funcție validare preț pozitiv
CREATE OR ALTER FUNCTION IS_Positive_Price(@Price DECIMAL(10,2))
RETURNS INT
AS
BEGIN
    IF @Price > 0
        RETURN 1
    RETURN 0
END
GO

-- Funcție validare telefon mobil
CREATE OR ALTER FUNCTION IS_Valid_Phone(@Phone NVARCHAR(20))
RETURNS INT
AS
BEGIN
    IF @Phone LIKE '07[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'
        RETURN 1
    RETURN 0
END
GO