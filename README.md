# MGScripts

Aplicação Java desenvolvida no Visual Studio Code para executar scripts SQL em bancos Firebird (ou outros SGBDs compatíveis) sob demanda.

## Estrutura de Pastas

- `src`: código-fonte da aplicação
- `lib`: dependências (ex: jaybird-full-3.0.2.jar)
- `bin`: arquivos compilados (gerados automaticamente)

## Pré-requisitos

- Java 8 ou superior instalado
- Biblioteca do SGBD Firebird (`jaybird-full-3.0.2.jar`) na pasta `lib`
- Arquivo `ScriptsRaw.zip` contendo os scripts `.sql` a serem executados, deve estar na raiz do projeto
- Arquivo `setup.ini` na raiz do projeto, contendo:
  - `@connString` IP ou DNS do servidor (ex: localhost)
  - `@folderToBd` Caminho para o arquivo do banco Firebird (ex: c:/firebird/database.fdb)

## Como Executar

1. **Descompacte** o projeto e coloque o arquivo `ScriptsRaw.zip` e o `setup.ini` na raiz do projeto.
2. **Edite** o arquivo `setup.ini` conforme seu ambiente:
   ```
   @connString localhost
   @folderToBd C:/caminho/para/seu/banco
   ```
3. **Compile** o projeto:
   ```sh
   javac -cp "lib/*" -d bin src/main/MGScripts.java
   ```
4. **Execute** a aplicação:
   ```sh
   java -cp "bin;lib/*" main.MGScripts
   ```
   > No Linux/Mac, troque `;` por `:` no classpath.

## Funcionamento

- O programa descompacta automaticamente o arquivo `ScriptsRaw.zip` para a pasta `ScriptsRaw`.
- Todos os arquivos `.sql` encontrados em `ScriptsRaw` são executados no banco configurado.
- Scripts executados são movidos para a pasta `ScriptsExecuted` (exceto os que começam com `#f-`, que permanecem em `ScriptsRaw`).
- O histórico de execução é registrado na tabela `auxiliares` do banco.

## Observações

- O programa só permite conexão local (`localhost`) por segurança.
- Certifique-se de que o banco Firebird esteja acessível e que o usuário/senha estejam corretos no (`sysdba/masterkey` por padrão).
- Scripts já executados não serão executados novamente, a menos que sejam marcados como "forçados".

---
```<!-- filepath: c:\Fontes\JavaProjects\MGScripts\README.md -->
# MGScripts

Aplicação Java desenvolvida no Visual Studio Code para executar scripts SQL em bancos Firebird (ou outros SGBDs compatíveis) sob demanda.

## Estrutura de Pastas

- `src`: código-fonte da aplicação
- `lib`: dependências (ex: jaybird-full-3.0.2.jar)
- `bin`: arquivos compilados (gerados automaticamente)

## Pré-requisitos

- Java 8 ou superior instalado
- Biblioteca do SGBD Firebird (`jaybird-full-3.0.2.jar`) na pasta `lib`
- Arquivo `ScriptsRaw.zip` contendo os scripts `.sql` a serem executados, deve estar na raiz do projeto
- Arquivo `setup.ini` na raiz do projeto, contendo:
  - `@connString` IP ou DNS do servidor (ex: localhost)
  - `@folderToBd` Caminho para o arquivo do banco Firebird (ex: c:/firebird/database.fdb)

## Como Executar

1. **Descompacte** o projeto e coloque o arquivo `ScriptsRaw.zip` e o `setup.ini` na raiz do projeto.
2. **Edite** o arquivo `setup.ini` conforme seu ambiente:
   ```
   @connString localhost
   @folderToBd C:/caminho/para/seu/banco
   ```
3. **Compile** o projeto:
   ```sh
   javac -cp "lib/*" -d bin src/main/MGScripts.java
   ```
4. **Execute** a aplicação:
   ```sh
   java -cp "bin;lib/*" main.MGScripts
   ```
   > No Linux/Mac, troque `;` por `:` no classpath.

## Funcionamento

- O programa descompacta automaticamente o arquivo `ScriptsRaw.zip` para a pasta `ScriptsRaw`.
- Todos os arquivos `.sql` encontrados em `ScriptsRaw` são executados no banco configurado.
- Scripts executados são movidos para a pasta `ScriptsExecuted` (exceto os que começam com `#f-`, que permanecem em `ScriptsRaw`).
- O histórico de execução é registrado na tabela `auxiliares` do banco.

## Observações

- O programa só permite conexão local (`localhost`) por segurança.
- Certifique-se de que o banco Firebird esteja acessível e que o usuário/senha estejam corretos no (`sysdba/masterkey` por padrão).
- Scripts já executados não serão executados novamente, a menos que sejam marcados como "forçados".

---
