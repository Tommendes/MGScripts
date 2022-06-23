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
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public void executeSql(String sql) {
        try {
            PreparedStatement ps;
            ps = this.conn.prepareStatement(sql);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(BDCommands.class.getName()).log(Level.SEVERE, null, ex);
        }
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
