package main;

import java.sql.Connection;
import config.BDCommands;
import config.ConnMGFP01;
import controllers.ScriptsController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

/**
 * Classe principal da aplicação MGScripts.
 * Responsável por conectar ao banco de dados, executar scripts SQL da pasta ScriptsRaw
 * e gerenciar o histórico de scripts executados.
 * 
 * @author Tom Mendes
 * @email contato@tommendes.com.br
 */
public class MGScripts {

    /** Comandos de banco de dados */
    private static BDCommands bDCommands;
    /** Conexão ativa com o banco de dados */
    private static Connection conn;

    /** Identificador de execução do script */
    private String execId;
    /** Conteúdo do script SQL */
    private String script;
    /** Indica se a execução deve ser forçada */
    private boolean force;
    /** Versão da aplicação */
    private static final String version = "1.0.2";

    /**
     * Ponto de entrada da aplicação.
     * Conecta ao banco, executa scripts SQL e move scripts processados.
     */
    public static void main(String[] args) throws IOException {
        ConnMGFP01 connMGFP01 = new ConnMGFP01();
        connMGFP01.conectar();
        setConn(connMGFP01.getConnection());
        setBDCommands(new BDCommands(getConn()));

		// Verificar se existe e se existir, descompactar o arquivo ScriptsRaw.zip
		File scriptsRawZip = new File("./ScriptsRaw.zip");
		if (scriptsRawZip.exists()) {
			// Descompacta o arquivo ScriptsRaw.zip
			config.Functions.unzip(scriptsRawZip.getAbsolutePath(), "./ScriptsRaw");
			System.out.println("ScriptsRaw.zip descompactado com sucesso.");
		} else {
			System.out.println("Arquivo ScriptsRaw.zip não encontrado. Verifique se ele existe na pasta correta.");
		}

        File scriptsDir = new File("./ScriptsRaw");
        if (scriptsDir.exists() && scriptsDir.isDirectory()) {
            File[] sqlFiles = scriptsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".sql"));
            if (sqlFiles != null) {
                ScriptsController scriptsController = new ScriptsController();
                for (File sqlFile : sqlFiles) {
                    try {
                        String scriptContent = new String(Files.readAllBytes(sqlFile.toPath()), StandardCharsets.UTF_8);
                        String execId = sqlFile.getName().replace(".sql", "").replaceAll(" ", "");
                        // Formata o script para facilitar a leitura e execução
                        scriptContent = scriptContent.replaceAll("\\s*;\\s*", ";\n");
                        scriptContent = scriptContent.replaceAll("\\s+", " ");
                        scriptContent = scriptContent.trim();

                        System.out.println("Executando script: " + sqlFile.getName());
                        int execCount = 0;
                        boolean isFixed = sqlFile.getName().startsWith("#f-");
                        for (String statement : scriptContent.split(";")) {
                            String trimmedStatement = statement.trim();
                            if (!trimmedStatement.isEmpty()) {
                                System.out.println("Executando: " + trimmedStatement);
                                scriptsController.doScript(execId + "_" + (++execCount), trimmedStatement, isFixed);
                            }
                        }

                        // Move o arquivo para ScriptsExecuted se não for fixo
                        File executedDir = new File("./ScriptsExecuted");
                        if (!executedDir.exists()) {
                            executedDir.mkdirs();
                        }
                        if (isFixed) {
                            System.out.println("Arquivo " + sqlFile.getName() + " fixo mantido na pasta ScriptsRaw.");
                            continue;
                        }
                        File executedFile = new File(executedDir, sqlFile.getName());
                        if (sqlFile.renameTo(executedFile)) {
                            System.out.println("Arquivo movido para: " + executedFile.getAbsolutePath());
                        } else {
                            System.err.println("Falha ao mover o arquivo: " + sqlFile.getName());
                        }
                    } catch (IOException e) {
                        System.err.println("Erro ao ler o arquivo: " + sqlFile.getName());
                        e.printStackTrace();
                    }
                }
            }
        }
        // Encerra a aplicação após processar todos os arquivos
        System.exit(0);
    }

    /** Retorna a instância de comandos de banco de dados */
    public static BDCommands getBDCommands() {
        return MGScripts.bDCommands;
    }

    /** Define a instância de comandos de banco de dados */
    public static void setBDCommands(BDCommands bDCommands) {
        MGScripts.bDCommands = bDCommands;
    }

    /** Retorna a conexão ativa */
    public static Connection getConn() {
        return MGScripts.conn;
    }

    /** Define a conexão ativa */
    public static void setConn(Connection conn) {
        MGScripts.conn = conn;
    }

    /** Retorna o identificador de execução do script */
    public String getExecId() {
        return this.execId;
    }

    /** Define o identificador de execução do script */
    public void setExecId(String execId) {
        this.execId = execId;
    }

    /** Retorna o conteúdo do script SQL */
    public String getScript() {
        return this.script;
    }

    /** Define o conteúdo do script SQL */
    public void setScript(String script) {
        this.script = script;
    }

    /** Retorna se a execução é forçada */
    public boolean isForce() {
        return this.force;
    }

    /** Define se a execução é forçada */
    public void setForce(boolean force) {
        this.force = force;
    }
}
