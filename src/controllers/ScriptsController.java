package controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.MGScripts;

/**
 * Controlador responsável pela execução e registro de scripts SQL.
 * Garante que scripts não sejam executados mais de uma vez, a menos que forçado.
 * 
 * @author Tom Mendes
 * @email contato@tommendes.com.br
 */
public class ScriptsController {

    /**
     * Executa um script SQL, registrando sua execução na tabela auxiliares.
     * 
     * @param execId Identificador único da execução do script.
     * @param script Conteúdo do script SQL a ser executado.
     * @param force  Se true, força a execução mesmo que já tenha sido executado.
     */
    public void doScript(String execId, String script, boolean force) {
        ResultSet tabelaAuxiliares = null;
        try {
            tabelaAuxiliares = MGScripts.getBDCommands().getTabelaGenerico(
                "", "", "",
                "select coalesce(max(id), 0) id, count(*) from auxiliares where dominio = 'folhaUpdates' and meta = '" + execId + "'",
                true
            );
            tabelaAuxiliares.first();

            // Se force == true, excluir o registro anterior
            if (force) {
                MGScripts.getBDCommands().executeSql("delete from auxiliares where id = " + tabelaAuxiliares.getInt("id"));
                System.out.println("Registro excluído da tabela auxiliares para nova operação: " + execId);
            }

            if (force || tabelaAuxiliares.getInt("count") == 0) {
                if (MGScripts.getBDCommands().executeSql(script)) {
                    // Registrar execução no banco de dados
                    try (PreparedStatement myStmt = MGScripts.getConn().prepareStatement(
                        "insert into auxiliares (id,created_at,dominio,meta,valor,largevalue) values (" +
                        "(select coalesce(max(id)+1,1) from auxiliares)," +
                        "(select timestamp 'NOW' from rdb$database)," +
                        "'folhaUpdates',?,'exec',?)")) {
                        myStmt.setString(1, execId);
                        myStmt.setString(2, script);

                        int res = myStmt.executeUpdate();
                        System.out.println("Script executado com sucesso: " + execId);
                        System.out.println(res + " registro inserido em BD.auxiliares");
                    }
                }
            } else {
                System.out.println("\nScript \"" + execId + "\" já foi executado anteriormente.");
                System.out.println("Operação abortada.");
            }
        } catch (SQLException e) {
            Logger.getLogger(ScriptsController.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Erro ao executar a operação: " + e.getMessage());
        } finally {
            try {
                if (tabelaAuxiliares != null) tabelaAuxiliares.close();
            } catch (SQLException ex) {
                Logger.getLogger(ScriptsController.class.getName()).log(Level.WARNING, "Erro ao fechar ResultSet", ex);
            }
        }
    }
}
