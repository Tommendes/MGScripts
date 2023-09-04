package controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.MGScripts;

/**
 *
 * @author Tom Mendes
 * @email contato@tommendes.com.br
 */
public class ScriptsController {

    public void doScript(String execId, String script, boolean force) {
        try {
            ResultSet tabelaAuxiliares = MGScripts.getBDCommands().getTabelaGenerico("", "", "",
                    "select count(*) from auxiliares where dominio = 'folhaUpdates' "
                            + "and meta = '" + execId + "'",
                    false);
            tabelaAuxiliares.first();
            if (force || tabelaAuxiliares.getInt("count") == 0) {
                try {
                    if (MGScripts.getBDCommands().executeSql(script)) {
                        // Registrar no banco de dados
                        PreparedStatement myStmt;
                        myStmt = MGScripts.getConn().prepareStatement(
                                "insert into auxiliares (id,created_at,dominio,meta,valor,largevalue) values ("
                                        + "(select coalesce(max(id)+1,1) from auxiliares),"
                                        + "(select timestamp 'NOW' from rdb$database),"
                                        + "'folhaUpdates',?,'exec',?)");
                        myStmt.setString(1, execId);
                        myStmt.setString(2, script);

                        int res = myStmt.executeUpdate();

                        MGScripts.getTaMsgs().append("\n" + "Script executado com sucesso" + "\n");
                        MGScripts.getTaMsgs().append(res + " registro inserido em BD.auxiliares" + "\n");
                        MGScripts.getTaMsgs().append("\n");
                    }
                } catch (Exception e) {
                    Logger.getLogger(ScriptsController.class.getName()).log(Level.SEVERE, null, e);
                    // stop execution
                    return;
                }
            } else {
                MGScripts.getTaMsgs().append("\n" + "Script selecionado \"" + execId + "\" já foi executado" + "\n");
                MGScripts.getTaMsgs().append("\n");
                MGScripts.getTaMsgs().append("Operação abortada" + "\n");
                MGScripts.getTaMsgs().append("\n");
            }
        } catch (Exception e) {
            Logger.getLogger(ScriptsController.class.getName()).log(Level.SEVERE, null, e);
            MGScripts.getTaMsgs().append("Erro ao executar a operação" + "\n");
            MGScripts.getTaMsgs().append(e.getMessage() + "\n");
        } finally {
            MGScripts.getJlCharging().setVisible(false);
        }
    };

}
