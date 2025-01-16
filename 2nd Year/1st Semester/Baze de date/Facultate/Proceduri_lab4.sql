-- Procedura pentru rularea testelor pe tabele
CREATE PROCEDURE RunTestTables
    @TestRunID INT,
    @TestID INT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @TableID INT, @Position INT, @NoOfRows INT;
    DECLARE @TableName NVARCHAR(255);

    -- Delete data in descending position order
    DECLARE TableCursor CURSOR FOR
    SELECT T.TableID, T.Name, TT.Position, TT.NoOfRows 
    FROM TestTables TT
    INNER JOIN Tables T ON TT.TableID = T.TableID
    WHERE TT.TestID = @TestID
    ORDER BY TT.Position DESC;

    OPEN TableCursor;
    FETCH NEXT FROM TableCursor INTO @TableID, @TableName, @Position, @NoOfRows;

    WHILE @@FETCH_STATUS = 0
    BEGIN
        EXEC('DELETE FROM ' + @TableName);
        
        -- Check if table has identity column before resetting
        IF EXISTS (
            SELECT 1 
            FROM sys.identity_columns ic
            JOIN sys.tables t ON ic.object_id = t.object_id
            WHERE t.name = @TableName
        )
        BEGIN
            EXEC('DBCC CHECKIDENT ('''+ @TableName + ''', RESEED, 0)');
        END
        
        FETCH NEXT FROM TableCursor INTO @TableID, @TableName, @Position, @NoOfRows;
    END;

    CLOSE TableCursor;
    DEALLOCATE TableCursor;

    -- Insert data in ascending position order
    DECLARE TableCursorForInsert CURSOR FOR
    SELECT T.TableID, T.Name, TT.Position, TT.NoOfRows 
    FROM TestTables TT
    INNER JOIN Tables T ON TT.TableID = T.TableID
    WHERE TT.TestID = @TestID
    ORDER BY TT.Position ASC;

    OPEN TableCursorForInsert;
    FETCH NEXT FROM TableCursorForInsert INTO @TableID, @TableName, @Position, @NoOfRows;

    WHILE @@FETCH_STATUS = 0
    BEGIN
        DECLARE @StartAt DATETIME = GETDATE();
        EXEC('EXEC InsertTestData_' + @TableName + ' @NoOfRows = ' + @NoOfRows);
        DECLARE @EndAt DATETIME = GETDATE();

        INSERT INTO TestRunTables (TestRunID, TableID, StartAt, EndAt)
        VALUES (@TestRunID, @TableID, @StartAt, @EndAt);

        FETCH NEXT FROM TableCursorForInsert INTO @TableID, @TableName, @Position, @NoOfRows;
    END;

    CLOSE TableCursorForInsert;
    DEALLOCATE TableCursorForInsert;
END;
GO


-- Procedura pentru rularea testelor pe view-uri
CREATE PROCEDURE RunTestViews
    @TestRunID INT,
    @TestID INT
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @ViewID INT;
    DECLARE @ViewName NVARCHAR(255);

    DECLARE ViewCursor CURSOR FOR
    SELECT V.ViewID, V.Name 
    FROM TestViews TV
    INNER JOIN Views V ON TV.ViewID = V.ViewID
    WHERE TV.TestID = @TestID;

    OPEN ViewCursor;
    FETCH NEXT FROM ViewCursor INTO @ViewID, @ViewName;

    WHILE @@FETCH_STATUS = 0
    BEGIN
        DECLARE @StartAt DATETIME = GETDATE();
        EXEC('SELECT * FROM ' + @ViewName);
        DECLARE @EndAt DATETIME = GETDATE();

        INSERT INTO TestRunViews (TestRunID, ViewID, StartAt, EndAt)
        VALUES (@TestRunID, @ViewID, @StartAt, @EndAt);

        FETCH NEXT FROM ViewCursor INTO @ViewID, @ViewName;
    END;

    CLOSE ViewCursor;
    DEALLOCATE ViewCursor;
END;
GO

-- Procedura principală pentru rularea testelor
CREATE PROCEDURE RunTests
    @TestID INT
AS
BEGIN
    SET NOCOUNT ON;

    -- Start Test Run
    INSERT INTO TestRuns (Description, StartAt)
    VALUES ('Test Execution for MagazinTelefoane', GETDATE());
    DECLARE @TestRunID INT = SCOPE_IDENTITY();

    -- Rulează testele pentru tabele
    EXEC RunTestTables @TestRunID, @TestID;

    -- Rulează testele pentru view-uri
    EXEC RunTestViews @TestRunID, @TestID;

    -- Finalizează Test Run
    UPDATE TestRuns
    SET EndAt = GETDATE()
    WHERE TestRunID = @TestRunID;
END;
GO