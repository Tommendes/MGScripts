/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados Firebird.
 * Lê parâmetros do arquivo setup.ini e provê métodos para conectar e desconectar.
 * 
 * @author Tom Mendes
 * @email contato@tommendes.com.br
 */
public class ConnMGFP01 {

    // Usuário e senha padrão do Firebird
    private static final String USER = "sysdba";
    private static final String PASSWORD = "masterkey";
    private static String ip;
    private static String folderToBd;
    private static String url;
    private Connection connection;

    public ConnMGFP01() {}

    public ConnMGFP01(Connection connection) {
        this.connection = connection;
    }

    /**
     * Lê o arquivo setup.ini e define IP e pasta do banco.
     * Se não encontrar o arquivo, usa valores padrão.
     */
    public static void getHost() {
        File iniFile = new File(System.getProperty("user.dir") + "/setup.ini");
        try (Scanner myReader = new Scanner(iniFile)) {
            String ipLine = myReader.hasNextLine() ? myReader.nextLine() : "";
            String folderLine = myReader.hasNextLine() ? myReader.nextLine() : "";

            if (ipLine.startsWith("@connString ")) {
                setIp(ipLine.substring(12).trim());
            } else {
                setIp("localhost");
            }

            if (folderLine.startsWith("@folderToBd ")) {
                setFolderToBd(folderLine.substring(12).trim());
            } else {
                setFolderToBd(System.getProperty("user.dir"));
            }

            System.out.println("Pasta do banco: " + getFolderToBd());
            setUrl("jdbc:firebirdsql:" + getIp() + "/3050:" + getFolderToBd()
                    + "/MGFP01.fdb?charSet=utf-8&defaultHoldable=true");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "O sistema não pode encontrar o arquivo setup.ini");
            System.err.println("Arquivo setup.ini não encontrado.");
            setIp("localhost");
            setFolderToBd(System.getProperty("user.dir"));
            setUrl("jdbc:firebirdsql:" + getIp() + "/3050:" + getFolderToBd()
                    + "/MGFP01.fdb?charSet=utf-8&defaultHoldable=true");
        }
    }

    /**
     * Conecta ao banco de dados Firebird usando os parâmetros lidos.
     */
    public void conectar() {
        try {
            getHost();
            // Se IP != localhost, encerrar a aplicação
            if (!getIp().equals("localhost")) {
                mensagem("Conexão com IP diferente de localhost não é permitida.");
                System.exit(1);
            }
            System.out.println("URL de conexão: " + getUrl());
            mensagem("Conectar a: " + getUrl());
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            Connection conn = DriverManager.getConnection(getUrl(), USER, PASSWORD);
            this.setConnection(conn);
            mensagem("Conectado");
        } catch (ClassNotFoundException | SQLException e) {
            mensagem("Erro ao conectar: " + e.getMessage());
        }
    }

    /**
     * Desconecta do banco de dados, se estiver conectado.
     */
    public void desconectar() {
        try {
            mensagem("Desconectar");
            if (this.getConnection() != null && !this.getConnection().isClosed()) {
                this.getConnection().close();
                mensagem("Desconectado");
            }
        } catch (SQLException e) {
            mensagem("Erro ao desconectar: " + e.getMessage());
        }
    }

    /**
     * Exibe e retorna uma mensagem.
     * @param texto Mensagem a ser exibida.
     * @return A própria mensagem.
     */
    public String mensagem(String texto) {
        System.out.println(texto);
        return texto;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        ConnMGFP01.ip = ip;
    }

    public static String getFolderToBd() {
        return folderToBd;
    }

    public static void setFolderToBd(String folderToBd) {
        ConnMGFP01.folderToBd = folderToBd;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        ConnMGFP01.url = url;
    }
}
