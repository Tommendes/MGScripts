package controllers;

public class Scripts {

    public Scripts() {
    }

    /**
     * Retorna o script ou o texto explicativo
     * i = 0: script
     * i = 1: texto
     * 
     * @param i
     * @return
     */
    public String _001(int i) {
        StringBuilder sb = new StringBuilder();
        if (i == 0) {
            sb.append("update FINANCEIRO F1 set").append(" ");
            sb.append("ADDATAINICIO = (select first 1 cast((F2.ANO || '-' || F2.MES || '-01') as date)").append(" ");
            sb.append("from FINANCEIRO F2").append(" ");
            sb.append("where F2.IDSERVIDOR = F1.IDSERVIDOR and").append(" ");
            sb.append("F2.IDEVENTO = 108 and").append(" ");
            sb.append("F2.PARCELA = 000").append(" ");
            sb.append("order by F2.ANO, F2.MES)").append(" ");
            sb.append("where F1.IDEVENTO = 108");
        } else if (i == 1) {
            sb.append("Esta operação insere a data de início da").append("\n");
            sb.append("rubrica 108 para todos os servidores").append("\n");
            sb.append("baseado na folha mais antiga em que o").append("\n");
            sb.append("servidor começou a recebê-la").append("\n");
        }
        return sb.toString();
    }

}
