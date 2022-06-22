package controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.MGScript;

/**
 *
 * @author TomMe
 */
public class ScriptsController extends Thread {

    public Runnable doScript = () -> {
        String execId = "_001";
        Scripts s = new Scripts();
        String script = s._001(0).toString();
        String texto = s._001(1).toString();
        try {
            ResultSet tabelaAuxiliares = MGScript.getBDCommands().getTabelaGenerico("", "", "",
                    "select count(*) from auxiliares where dominio = 'folhaUpdates' "
                            + "and meta = '" + execId + "'",
                    false);
            tabelaAuxiliares.first();
            if (tabelaAuxiliares.getInt("count") == 0) {
                MGScript.getBDCommands().executeSql(script);
                // Registrar no banco de dados
                PreparedStatement myStmt;
                myStmt = MGScript.getConn().prepareStatement(
                        "insert into auxiliares (id,created_at,dominio,meta,valor,largevalue) values ("
                                + "(select coalesce(max(id)+1,1) from auxiliares),"
                                + "(select timestamp 'NOW' from rdb$database),"
                                + "'folhaUpdates',?,'exec',?)");
                myStmt.setString(1, execId);
                myStmt.setString(2, script);

                int res = myStmt.executeUpdate();

                MGScript.getTaMsgs().setText("Script executado com sucesso" + "\n");
                MGScript.getTaMsgs().append(res + " registro inserido em BD.auxiliares" + "\n");
                MGScript.getTaMsgs().append("\n");
                MGScript.getTaMsgs().append(texto);
                MGScript.getTaMsgs().append("\n");
                MGScript.getTaMsgs().append("Clique abaixo para fechar" + "\n");
            } else {
                MGScript.getTaMsgs().setText("Script selecionado \"" + execId + "\" já foi executado" + "\n");
                MGScript.getTaMsgs().append("\n");
                MGScript.getTaMsgs().append("Operação abortada" + "\n");
                MGScript.getTaMsgs().append("\n");
                MGScript.getTaMsgs().append(texto);
                MGScript.getTaMsgs().append("\n");
                MGScript.getTaMsgs().append("Clique abaixo para fechar" + "\n");
            }
            MGScript.getLblTitle().setText("Clique no botão abaixo para fechar");
            MGScript.getBtnExecutar().setText("Fechar");
        } catch (Exception e) {
            Logger.getLogger(ScriptsController.class.getName()).log(Level.SEVERE, null, e);
            MGScript.getTaMsgs().append("Erro ao executar a operação" + "\n");
            MGScript.getTaMsgs().append(e.getMessage() + "\n");
        } finally {
            MGScript.getJlCharging().setVisible(false);
        }
    };

}
