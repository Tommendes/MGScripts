package controllers;

/**
 *
 * @author Tom Mendes
 * @email contato@tommendes.com.br
 */
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
    public String _000(int i) {
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
    
    /**
     * Executa a correção do eSocial
     * i = 0: script
     * i = 1: texto
     * 
     * @param i
     * @return
     */
    public String _001(int i) {
    	StringBuilder sb = new StringBuilder();
    	if (i == 0) {
    		sb.append("update FINANCEIRO F ").append("\n");
            sb.append("set N_VALOR = (N_VALOR + ?????) ").append("\n");
            sb.append("where F.IDEVENTO = '998' and ").append("\n");
            sb.append("      F.ANO = '2023' and ").append("\n");
            sb.append("      F.MES = '??' and ").append("\n");
            sb.append("      F.PARCELA = '000' and ").append("\n");
            sb.append("      F.IDSERVIDOR = '????????'").append("\n");
    	} else if (i == 1) {
    		sb.append("1. Esta operação corrige pequenas diferenças no resultado do eSocial.").append("\n");
    		sb.append("2. Selecione uma matrícula qualquer onde o desconto da rúbrica 998 ocorra normalmente.").append("\n");
    		sb.append("3. A seguir, informe o valo a adicionar ou subtrairr na primeira ocorrência de ????? (N_VALOR).").append("\n");
    		sb.append("4. ATENÇÃO!!! Valores decimais deverão ser informados com \".\"(ponto)").append("\n");
    		sb.append("5. Confirme o exercício (Ano, Mês e Complementar).").append("\n");
    		sb.append("6. Finalmente, execute a operação.");
    	}
    	return sb.toString();
    }

}
