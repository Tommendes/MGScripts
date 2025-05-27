/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Classe utilitária com funções para manipulação de strings e datas.
 * 
 * @author Tom Mendes
 * @email contato@tommendes.com.br
 */
public class Functions {

    /**
     * Remove acentos de uma string usando Normalizer.
     * 
     * @param str String de entrada.
     * @return String sem acentos.
     */
    public static String deAccent(String str) {
        if (str == null)
            return "";
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    /**
     * Remove acentos de uma string usando substituições manuais.
     * 
     * @param string String de entrada.
     * @return String sem acentos.
     * @deprecated Prefira usar {@link #deAccent(String)} por ser mais eficiente e
     *             abrangente.
     */
    @Deprecated
    public static String removeAcentos(String string) {
        if (string != null && !string.isEmpty()) {
            string = string.replaceAll("[ÂÀÁÄÃ]", "A");
            string = string.replaceAll("[âãàáä]", "a");
            string = string.replaceAll("[ÊÈÉË]", "E");
            string = string.replaceAll("[êèéë]", "e");
            string = string.replaceAll("ÎÍÌÏ", "I");
            string = string.replaceAll("îíìï", "i");
            string = string.replaceAll("[ÔÕÒÓÖ]", "O");
            string = string.replaceAll("[ôõòóö]", "o");
            string = string.replaceAll("[ÛÙÚÜ]", "U");
            string = string.replaceAll("[ûúùü]", "u");
            string = string.replaceAll("Ç", "C");
            string = string.replaceAll("ç", "c");
            string = string.replaceAll("[ýÿ]", "y");
            string = string.replaceAll("Ý", "Y");
            string = string.replaceAll("ñ", "n");
            string = string.replaceAll("Ñ", "N");
        } else {
            string = "";
        }
        return string;
    }

    /**
     * Adiciona meses a uma data.
     * 
     * @param date Data base.
     * @param i    Quantidade de meses a adicionar (pode ser negativo).
     * @return Nova data com os meses adicionados.
     */
    public static Date addMonth(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, i);
        return cal.getTime();
    }

    /**
     * Retorna o nome do mês por extenso a partir do número do mês.
     * 
     * @param mes String representando o mês ("01" a "13").
     * @return Nome do mês por extenso, ou vazio se inválido.
     */
    public static String getMesExtenso(String mes) {
        switch (mes) {
            case "01":
                return "Janeiro";
            case "02":
                return "Fevereiro";
            case "03":
                return "Março";
            case "04":
                return "Abril";
            case "05":
                return "Maio";
            case "06":
                return "Junho";
            case "07":
                return "Julho";
            case "08":
                return "Agosto";
            case "09":
                return "Setembro";
            case "10":
                return "Outubro";
            case "11":
                return "Novembro";
            case "12":
                return "Dezembro";
            case "13":
                return "13º";
            default:
                return "";
        }
    }

    /**
     * Retorna o nome do mês por extenso a partir do número do mês.
     * 
     * @param mes Número do mês (1 a 13).
     * @return Nome do mês por extenso, ou vazio se inválido.
     */
    public static String getMesExtenso(int mes) {
        return getMesExtenso(String.format("%02d", mes));
    }

    /**
     * Método para descompactação de arquivos ZIP.
     * 
     * @param zipFile Caminho do arquivo ZIP a ser descompactado.
     * @param destDir Caminho do diretório de destino para os arquivos
     *                descompactados.
     */
    public static void unzip(String zipFile, String destDir) {
        // Implementação do método de descompactação
        // Este método deve ser implementado conforme necessário
        // Exemplo: usando java.util.zip para descompactar arquivos ZIP
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    // Cria diretórios pai, se necessário
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        } catch (IOException e) {
            System.err.println("Erro ao descompactar arquivo ZIP: " + e.getMessage());
        }
    }
}
