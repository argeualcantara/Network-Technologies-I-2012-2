import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Window implements ActionListener{

	private JFrame frame;
	private JTextField filePath;
	private JPanel panelClient = null;
	private JPanel panelServer = null;
	private JFileChooser fileChooser = null;
	private File fileToSend = null;
	private JTextField savePath = null;
	JProgressBar progressBar = null;
	JButton btnStop = null;
	JButton btnStart = null;
	static JTextArea logServer = null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 741, 667);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Protocolo 2012.2");
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnComportamento = new JMenu("Comportamento");
		menuBar.add(mnComportamento);
		
		JMenuItem mntmClient = new JMenuItem("Client");
		mntmClient.addActionListener(this);
		mntmClient.setActionCommand("showClient");
		mnComportamento.add(mntmClient);
		
		JMenuItem mntmServidor = new JMenuItem("Server");
		mntmServidor.addActionListener(this);
		mntmServidor.setActionCommand("showServer");
		mnComportamento.add(mntmServidor);
		
		JLayeredPane layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);
		
		panelServer = new JPanel();
		panelServer.setBounds(0, 0, 741, 634);
		layeredPane.add(panelServer);
		panelServer.setVisible(false);
		panelServer.setLayout(null);
		
		JButton btnEscolha = new JButton("Escolha");
		btnEscolha.addActionListener(this);
		btnEscolha.setActionCommand("chooseSavePath");
		btnEscolha.setBounds(287, 112, 117, 25);
		panelServer.add(btnEscolha);
		
		JLabel lblEscolhaOndeOs = new JLabel("Escolha onde os arquivos serão armazenados:");
		lblEscolhaOndeOs.setBounds(12, 30, 392, 15);
		panelServer.add(lblEscolhaOndeOs);
		
		savePath = new JTextField();
		savePath.setBounds(12, 112, 263, 25);
		savePath.setEnabled(false);
		panelServer.add(savePath);
		savePath.setColumns(10);
		
		btnStart = new JButton("Start");
		btnStart.setBounds(12, 178, 117, 25);
		btnStart.addActionListener(this);
		btnStart.setActionCommand("startServer");
		btnStart.setEnabled(true);
		panelServer.add(btnStart);
		
		btnStop = new JButton("Stop");
		btnStop.setBounds(144, 178, 117, 25);
		btnStop.addActionListener(this);
		btnStop.setEnabled(false);
		btnStop.setActionCommand("stopServer");
		panelServer.add(btnStop);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(38, 266, 666, 345);
		panelServer.add(scrollPane);
		
		logServer = new JTextArea();
		scrollPane.setViewportView(logServer);
		
		JLabel lblLog = new JLabel("Log:");
		lblLog.setBounds(38, 248, 70, 15);
		panelServer.add(lblLog);
		
		panelClient = new JPanel();
		panelClient.setBounds(0, 0, 739, 634);
		panelClient.setVisible(false);
		layeredPane.add(panelClient);
		panelClient.setLayout(null);
		
		JButton btnArquivo = new JButton("Arquivo");
		btnArquivo.addActionListener(this);
		btnArquivo.setActionCommand("chooseFileToSend");
		
		btnArquivo.setBounds(478, 11, 93, 23);
		panelClient.add(btnArquivo);
		
		filePath = new JTextField();
		filePath.setBounds(136, 12, 332, 20);
		filePath.setEnabled(true);
		panelClient.add(filePath);
		filePath.setColumns(10);
		
		JButton buttonEnviar = new JButton("Enviar");
		buttonEnviar.addActionListener(this);
		buttonEnviar.setActionCommand("sendFile");
		buttonEnviar.setBounds(136, 68, 93, 23);
		panelClient.add(buttonEnviar);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(154, 177, 417, 36);
		panelClient.add(progressBar);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("showClient")){
			panelClient.setVisible(true);
			panelServer.setVisible(false);
		}else if(e.getActionCommand().equals("showServer")){
			panelClient.setVisible(false);
			panelServer.setVisible(true);
		}else if(e.getActionCommand().equals("sendFile")){
			if(filePath.getText().trim().length() > 0){
				FileSendTest.enviarArquivo(fileToSend);
			}else{
				JOptionPane.showMessageDialog(null, "Escolha um arquivo", "Aviso", JOptionPane.OK_OPTION);
				new Thread(){
					public void run(){
						int i = 0;
						while(i <= 100){
							try {
								progressBar.setValue(i);
								Thread.sleep(400);
								i++;
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}.start();
			}
		}else if(e.getActionCommand().equals("chooseFileToSend")){
			fileChooser = new JFileChooser();
			int action = fileChooser.showOpenDialog(null);
			if(action == JFileChooser.APPROVE_OPTION){
				fileToSend = fileChooser.getSelectedFile();
				filePath.setText(fileToSend.getName());
			}
		}else if(e.getActionCommand().equals("chooseSavePath")){
			fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int action = fileChooser.showSaveDialog(null);
			if(action == JFileChooser.APPROVE_OPTION){
				File temp = fileChooser.getSelectedFile();
				savePath.setText(temp.getPath());
			}
		}else if(e.getActionCommand().equals("startServer")){
			if(savePath.getText().trim().length() > 0){
				btnStop.setEnabled(true);
				btnStart.setEnabled(false);
				Server.startServer(savePath.getText());
			}else{
				JOptionPane.showMessageDialog(null, "Escolha o local de recebimento do arquivo.", "Aviso", JOptionPane.OK_OPTION);
			}
		}else if(e.getActionCommand().equals("stopServer")){
			btnStop.setEnabled(false);
			btnStart.setEnabled(true);
			Server.stopServer();
		}
	}
}
