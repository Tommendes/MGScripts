package main;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import config.BDCommands;
import config.ConnMGFP01;
import controllers.ScriptsController;

/**
 *
 * @author Tom Mendes
 * @email contato@tommendes.com.br
 */
public class MGScript {

	private static BDCommands bDCommands;
	private static Connection conn;

	private JFrame frmCorrecoesNoBanco;
	private static JLabel lblTitle = new JLabel("Clique no botão abaixo para executar");
	private static JButton btnExecutar = new JButton("Executar");
	private static JTextArea taMsgs = new JTextArea();
	private static JLabel jlCharging = new JLabel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MGScript window = new MGScript();
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
	public MGScript() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCorrecoesNoBanco = new JFrame();
		frmCorrecoesNoBanco.setBounds(100, 100, 451, 368);
		frmCorrecoesNoBanco.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
		frmCorrecoesNoBanco.setResizable(false);
		frmCorrecoesNoBanco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCorrecoesNoBanco.getContentPane().setLayout(null);
		frmCorrecoesNoBanco.setTitle("Correções no Banco de Dados MGFolha");
		frmCorrecoesNoBanco.setResizable(false);
		frmCorrecoesNoBanco.setLocationRelativeTo(null);

		jlCharging.setIcon(new ImageIcon(MGScript.class.getResource("/elements/loading.gif")));
		jlCharging.setBounds(80, 30, 292, 247);
		jlCharging.setVisible(false);
		frmCorrecoesNoBanco.getContentPane().add(jlCharging);

		taMsgs.setBounds(18, 30, 400, 254);
		frmCorrecoesNoBanco.getContentPane().add(taMsgs);

		lblTitle.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblTitle.setBounds(27, 11, 377, 14);
		frmCorrecoesNoBanco.getContentPane().add(lblTitle);

		btnExecutar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getBtnExecutar().getText().equals("Executar")) {
					jlCharging.setVisible(true);
					getTaMsgs().append("Executando!" + "\n");
					getTaMsgs().append("Aguarde..." + "\n");
					ScriptsController sc = new ScriptsController();
					new Thread(sc.doScript).start();
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
		btnExecutar.setBounds(329, 295, 89, 23);
		frmCorrecoesNoBanco.getContentPane().add(btnExecutar);
	}

	public JFrame getFrmCorrecoesNoBanco() {
		return this.frmCorrecoesNoBanco;
	}

	public void setFrmCorrecoesNoBanco(JFrame frmCorrecoesNoBanco) {
		this.frmCorrecoesNoBanco = frmCorrecoesNoBanco;
	}

	public static JLabel getLblTitle() {
		return MGScript.lblTitle;
	}

	public static void setLblTitle(JLabel lblTitle) {
		MGScript.lblTitle = lblTitle;
	}

	public static JButton getBtnExecutar() {
		return MGScript.btnExecutar;
	}

	public static void setBtnExecutar(JButton btnExecutar) {
		MGScript.btnExecutar = btnExecutar;
	}

	public static JTextArea getTaMsgs() {
		return MGScript.taMsgs;
	}

	public void setTaMsgs(JTextArea taMsgs) {
		MGScript.taMsgs = taMsgs;
	}

	public static BDCommands getBDCommands() {
		return MGScript.bDCommands;
	}

	public static void setBDCommands(BDCommands bDCommands) {
		MGScript.bDCommands = bDCommands;
	}

	public static Connection getConn() {
		return MGScript.conn;
	}

	public static void setConn(Connection conn) {
		MGScript.conn = conn;
	}

	public static JLabel getJlCharging() {
		return MGScript.jlCharging;
	}

	public static void setJlCharging(JLabel jlCharging) {
		MGScript.jlCharging = jlCharging;
	}

}
