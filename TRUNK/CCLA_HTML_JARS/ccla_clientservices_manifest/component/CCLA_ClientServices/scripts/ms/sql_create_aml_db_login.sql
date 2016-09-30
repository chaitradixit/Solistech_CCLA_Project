USE AML
GO
--Add New User Permissions for AML database.
IF NOT EXISTS (SELECT * FROM master.dbo.syslogins WHERE name = N'AML_UCM')
BEGIN
EXEC master.dbo.sp_addlogin @loginame = N'AML_UCM', @passwd = N'4ml_db_u3m', @deflanguage = N'us_english'
END
GO
IF NOT EXISTS (SELECT * FROM dbo.sysusers WHERE name = N'AML_UCM')
EXEC dbo.sp_grantdbaccess @loginame = N'AML_UCM', @name_in_db = N'AML_UCM'
GO
EXEC sp_addrolemember N'db_owner', N'AML_UCM'
GO