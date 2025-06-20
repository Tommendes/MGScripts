/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.MGScripts;

/**
 *
 * @author Tom Mendes
 * @email contato@tommendes.com.br
 */
public class BDCommands {

    private final Connection conn;

    public BDCommands(Connection conn) {
        this.conn = conn;
    }

    public boolean executeSql(String sql) {
        boolean exec = false;
        try {
            PreparedStatement ps;
            ps = this.conn.prepareStatement(sql);
            exec = ps.execute();
            ps.close();
            exec = !exec;
        } catch (SQLSyntaxErrorException e) {
            // Captura a exceção SQLSyntaxErrorException
            System.out.println("\n" + "Erro SQL: " + e.getMessage());
            System.out.println("Código de erro SQL: " + e.getErrorCode());
            System.out.println("Estado SQL: " + e.getSQLState());
            System.out.println("Localização do erro: linha " + e.getLocalizedMessage());
        } catch (SQLException ex) {
            System.out.println("\n" + "Erro ao executar a operação");
            System.out.println(ex.getMessage());
            Logger.getLogger(BDCommands.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exec;
    }

    public ResultSet getTabelaGenerico(String tabela, String salto, String sqlAdd, String sqlRaw, boolean output) {
        ResultSet rs;
        try {
            if (sqlRaw.isEmpty()) {
                sqlRaw = "SELECT " + salto + " * from " + tabela + " " + sqlAdd;
            }
            if (output) {
                System.out.println("SQL: " + sqlRaw);
            }
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlRaw);
            return rs;
        } catch (SQLException ex) {
            System.out.println("Houve um erro ao ler a tabela " + tabela + ". Erro: " + ex.getMessage());
            Logger.getLogger(BDCommands.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
