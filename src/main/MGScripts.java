package main;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import config.BDCommands;
import config.ConnMGFP01;
import controllers.Scripts;
import controllers.ScriptsController;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;

/**
 *
 * @author Tom Mendes
 * @email contato@tommendes.com.br
 */
public class MGScripts {

	private static BDCommands bDCommands;
	private static Connection conn;

	private JFrame frmCorrecoesNoBanco;
	private static JLabel lblTitle = new JLabel("Selecione um modelo para executar");
	private static JButton btnExecutar = new JButton("Executar");
	private static JTextArea taInstruct = new JTextArea();
	private static JTextArea taMsgs = new JTextArea();
	private static JTextArea taExplanation = new JTextArea();
	private static JLabel jlCharging = new JLabel();
	private static final JCheckBox cbForce = new JCheckBox("Force Execução");

	private String execId;
	private String script;
	private String explanation;
	private boolean force;
	private static String version = "1.0.1";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MGScripts window = new MGScripts();
					window.frmCorrecoesNoBanco.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		ConnMGFP01 connMGFP01 = new ConnMGFP01();
		connMGFP01.conectar();
		setConn(connMGFP01.getConnection());
		setBDCommands(new BDCommands(getConn()));
	}

	/**
	 * Create the application.
	 */
	public MGScripts() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCorrecoesNoBanco = new JFrame();
		frmCorrecoesNoBanco.setBounds(100, 100, 794, 715);
		frmCorrecoesNoBanco.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
		frmCorrecoesNoBanco.setResizable(false);
		frmCorrecoesNoBanco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCorrecoesNoBanco.getContentPane().setLayout(null);
		frmCorrecoesNoBanco.setTitle("Correções no Banco de Dados MGFolha V." + version);
		frmCorrecoesNoBanco.setResizable(false);
		frmCorrecoesNoBanco.setLocationRelativeTo(null);

		jlCharging.setIcon(new ImageIcon(MGScripts.class.getResource("/elements/loading.gif")));
		jlCharging.setBounds(243, 259, 292, 229);
		jlCharging.setVisible(false);
		frmCorrecoesNoBanco.getContentPane().add(jlCharging);
		taInstruct.setFont(new Font("Arial", Font.PLAIN, 13));
		taInstruct.setLineWrap(true);
		taInstruct.setWrapStyleWord(true);

		lblTitle.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblTitle.setBounds(15, 11, 475, 14);
		frmCorrecoesNoBanco.getContentPane().add(lblTitle);
		btnExecutar.setEnabled(false);
		btnExecutar.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnExecutar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getBtnExecutar().getText().equals("Executar")) {
					jlCharging.setVisible(true);
					taMsgs.append("\n" + "Executando!" + "\n");
					taMsgs.append("Aguarde..." + "\n");
					ScriptsController sc = new ScriptsController();
					sc.doScript(getExecId(), getTaInstruct().getText(), isForce());
				} else if (getBtnExecutar().getText().equals("Fechar")) {
					try {
						getConn().close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					System.exit(0);
				}
			}
		});
		btnExecutar.setBounds(114, 259, 89, 23);
		frmCorrecoesNoBanco.getContentPane().add(btnExecutar);
		taExplanation.setFont(new Font("Arial", Font.PLAIN, 13));
		taExplanation.setWrapStyleWord(true);

		taExplanation.setLineWrap(true);
		taExplanation.setEditable(false);

		/**
		 * A component that combines a button or editable field and a drop-down list.
		 */
		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox.getSelectedIndex() > 0) {
					try {
						// Habilita o botão
						btnExecutar.setEnabled(true);
						// Construa o nome do método com base na seleção
						String methodName = String.format("%03d", comboBox.getSelectedIndex());

						// Obtenha a classe que contém os métodos
						Class<?> myClass = Scripts.class; // Substitua SuaClasse pelo nome da sua classe

						// Obtenha o método com base no nome
						Method method;
						method = myClass.getDeclaredMethod("_" + methodName, int.class);

						// Crie uma instância da classe
						Scripts s = new Scripts();

						// Chame o método e passe o parâmetro
						Object sql = method.invoke(s, 0);
						Object explanation = method.invoke(s, 1);
						setExecId(methodName);
						setScript(sql.toString());
						setExplanation(explanation.toString());
						getTaExplanation().setText(getExplanation());
						getTaInstruct().setText(getScript());
						getTaMsgs().append("\n" + "");
					} catch (NoSuchMethodException | SecurityException e1) {
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						e1.printStackTrace();
					} catch (IllegalArgumentException e1) {
						e1.printStackTrace();
					} catch (InvocationTargetException e1) {
						e1.printStackTrace();
					}
				} else if (comboBox.getSelectedIndex() == 0) {
					// Desabilita o botão
					btnExecutar.setEnabled(false);
					getTaExplanation().append("\n" + "");
					getTaInstruct().append("\n" + "");
					getTaMsgs().append("\n" + "");
				}
			}
		});
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<String>(
				new String[] { "Selecione...", "001-Correção eSocial" });
		comboBox.setModel(comboBoxModel);
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox.setBounds(15, 35, 744, 29);
		frmCorrecoesNoBanco.getContentPane().add(comboBox);

		JLabel lblTitle_1 = new JLabel("O que será executado...");
		lblTitle_1.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblTitle_1.setBounds(15, 74, 475, 14);
		frmCorrecoesNoBanco.getContentPane().add(lblTitle_1);

		JLabel lblResultado = new JLabel("Instrução");
		lblResultado.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblResultado.setBounds(15, 261, 89, 14);
		frmCorrecoesNoBanco.getContentPane().add(lblResultado);
		taMsgs.setFont(new Font("Arial", Font.PLAIN, 13));
		taMsgs.setWrapStyleWord(true);
		taMsgs.setLineWrap(true);

		taMsgs.setEditable(false);

		JLabel lblResultado_1 = new JLabel("Resultado");
		lblResultado_1.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblResultado_1.setBounds(15, 482, 89, 14);
		frmCorrecoesNoBanco.getContentPane().add(lblResultado_1);
		cbForce.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setForce(cbForce.isSelected());
			}
		});
		cbForce.setFont(new Font("Arial Black", Font.PLAIN, 14));
		cbForce.setHorizontalAlignment(SwingConstants.RIGHT);
		cbForce.setBounds(209, 259, 162, 21);

		frmCorrecoesNoBanco.getContentPane().add(cbForce);

		JScrollPane scrollPaneMsgs = new JScrollPane(taMsgs);
		scrollPaneMsgs.setBounds(15, 506, 744, 151);
		frmCorrecoesNoBanco.getContentPane().add(scrollPaneMsgs);

		JScrollPane scrollPaneExpl = new JScrollPane(taExplanation);
		scrollPaneExpl.setBounds(15, 98, 744, 151);
		frmCorrecoesNoBanco.getContentPane().add(scrollPaneExpl);

		JScrollPane scrollPaneInstr = new JScrollPane(taInstruct);
		scrollPaneInstr.setBounds(15, 285, 744, 184);
		frmCorrecoesNoBanco.getContentPane().add(scrollPaneInstr);
	}

	public JFrame getFrmCorrecoesNoBanco() {
		return this.frmCorrecoesNoBanco;
	}

	public void setFrmCorrecoesNoBanco(JFrame frmCorrecoesNoBanco) {
		this.frmCorrecoesNoBanco = frmCorrecoesNoBanco;
	}

	public static JLabel getLblTitle() {
		return MGScripts.lblTitle;
	}

	public static void setLblTitle(JLabel lblTitle) {
		MGScripts.lblTitle = lblTitle;
	}

	public static JButton getBtnExecutar() {
		return MGScripts.btnExecutar;
	}

	public static void setBtnExecutar(JButton btnExecutar) {
		MGScripts.btnExecutar = btnExecutar;
	}

	public static BDCommands getBDCommands() {
		return MGScripts.bDCommands;
	}

	public static void setBDCommands(BDCommands bDCommands) {
		MGScripts.bDCommands = bDCommands;
	}

	public static Connection getConn() {
		return MGScripts.conn;
	}

	public static void setConn(Connection conn) {
		MGScripts.conn = conn;
	}

	public static JLabel getJlCharging() {
		return MGScripts.jlCharging;
	}

	public static void setJlCharging(JLabel jlCharging) {
		MGScripts.jlCharging = jlCharging;
	}

	public String getExecId() {
		return this.execId;
	}

	public void setExecId(String execId) {
		this.execId = execId;
	}

	public String getScript() {
		return this.script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getExplanation() {
		return this.explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public static JTextArea getTaMsgs() {
		return MGScripts.taMsgs;
	}

	public JTextArea getTaExplanation() {
		return MGScripts.taExplanation;
	}

	public JTextArea getTaInstruct() {
		return MGScripts.taInstruct;
	}

	public boolean isForce() {
		return this.force;
	}

	public void setForce(boolean force) {
		this.force = force;
	}
}
