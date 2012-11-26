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
	static JProgressBar progressBar = null;
	static JButton btnStop = null;
	static JButton btnStart = null;
	static JTextArea logServer = null;
	private JTextField serverPort;
	private JTextField clientPort;
	private JTextField bufferSize;
	private JTextField ipDestino;
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
		buttonEnviar.setBounds(136, 194, 93, 23);
		panelClient.add(buttonEnviar);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(146, 252, 417, 36);
		panelClient.add(progressBar);
		
		clientPort = new JTextField("9999");
		clientPort.setBounds(136, 66, 114, 19);
		panelClient.add(clientPort);
		clientPort.setColumns(10);
		
		JLabel lblArquivo = new JLabel("Arquivo");
		lblArquivo.setBounds(51, 11, 70, 15);
		panelClient.add(lblArquivo);
		
		JLabel lblPort_1 = new JLabel("Port");
		lblPort_1.setBounds(53, 65, 70, 15);
		panelClient.add(lblPort_1);
		
		bufferSize = new JTextField("1024");
		bufferSize.setBounds(136, 100, 114, 19);
		panelClient.add(bufferSize);
		bufferSize.setColumns(10);
		
		JLabel lblSegmentokb = new JLabel("Segmento (bytes)");
		lblSegmentokb.setBounds(0, 97, 130, 20);
		panelClient.add(lblSegmentokb);
		
		ipDestino = new JTextField();
		ipDestino.setBounds(136, 35, 114, 19);
		panelClient.add(ipDestino);
		ipDestino.setColumns(10);
		
		JLabel lblIpDestino = new JLabel("IP Destino");
		lblIpDestino.setBounds(25, 38, 96, 15);
		panelClient.add(lblIpDestino);
		
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
		lblEscolhaOndeOs.setBounds(12, 90, 392, 15);
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
		
		serverPort = new JTextField("9999");
		serverPort.setBounds(147, 59, 114, 19);
		panelServer.add(serverPort);
		serverPort.setColumns(10);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(72, 61, 70, 15);
		panelServer.add(lblPort);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("showClient")){
			panelClient.setVisible(true);
			panelServer.setVisible(false);
		}else if(e.getActionCommand().equals("showServer")){
			panelClient.setVisible(false);
			panelServer.setVisible(true);
		}else if(e.getActionCommand().equals("sendFile")){
			if(filePath.getText().trim().length() > 0 && clientPort.getText().trim().length() > 0 
					&& ipDestino.getText().trim().length() > 0 && bufferSize.getText().trim().length() > 0){
				new Thread(){
					public void run(){
						FileSendTest.enviarArquivo(fileToSend, ipDestino.getText(), 
								Integer.parseInt(clientPort.getText()), Integer.parseInt(bufferSize.getText()));
					}
				}.start();
				
			}else{
				JOptionPane.showMessageDialog(null, "Preencha todos os campos", "Aviso", JOptionPane.OK_OPTION);
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
			if(savePath.getText().trim().length() > 0 && serverPort.getText().trim().length() > 0){
				btnStop.setEnabled(true);
				btnStart.setEnabled(false);
				Server.startServer(savePath.getText());
			}else{
				JOptionPane.showMessageDialog(null, "Escolha o local de recebimento do arquivo e a porta.", "Aviso", JOptionPane.OK_OPTION);
			}
		}else if(e.getActionCommand().equals("stopServer")){
			btnStop.setEnabled(false);
			btnStart.setEnabled(true);
			Server.stopServer();
		}
	}
}
