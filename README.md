## MGScripts

Java application coded in Visual Studio Code to execute Firebird(or other SGDB) scripts on demand.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

[Your SGDB lib] jaybird-full-3.0.2.jar.

## SGDB Connection

In ./setup.ini, edit 
- `@connString` IP or DNS(ex: localhost)
- `@folderToBd` Path to Firebird file(ex: c:/firebird/database.fdb)
