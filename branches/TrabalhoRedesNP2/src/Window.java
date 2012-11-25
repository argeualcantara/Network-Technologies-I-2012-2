import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;


public class Window {

	private JFrame frame;
	private JTextField filePath;
	private JPanel panelClient = null;
	private JPanel panelServer = null;
	private JFileChooser fileChooser = null;
	private File fileToSend = null;
	private JTextField savePath;
	
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
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnComportamento = new JMenu("Comportamento");
		menuBar.add(mnComportamento);
		
		JMenuItem mntmClient = new JMenuItem("Client");
		mntmClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panelClient.setVisible(true);
				panelServer.setVisible(false);
			}
		});
		mnComportamento.add(mntmClient);
		
		JMenuItem mntmServidor = new JMenuItem("Server");
		mntmServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelClient.setVisible(false);
				panelServer.setVisible(true);
			}
		});
		mnComportamento.add(mntmServidor);
		
		JLayeredPane layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);
		
		panelServer = new JPanel();
		panelServer.setBounds(0, 0, 725, 608);
		layeredPane.add(panelServer);
		panelServer.setLayout(null);
		panelServer.setVisible(false);
		
		JButton btnEscolha = new JButton("Escolha");
		btnEscolha.setBounds(287, 112, 117, 25);
		panelServer.add(btnEscolha);
		
		JLabel lblEscolhaOndeOs = new JLabel("Escolha onde os arquivos serão armazenados:");
		lblEscolhaOndeOs.setBounds(12, 30, 392, 15);
		panelServer.add(lblEscolhaOndeOs);
		
		savePath = new JTextField();
		savePath.setBounds(12, 112, 263, 25);
		panelServer.add(savePath);
		savePath.setColumns(10);
		
		panelClient = new JPanel();
		panelClient.setBounds(0, 0, 725, 608);
		layeredPane.add(panelClient);
		panelClient.setLayout(null);
		panelClient.setVisible(false);
		
		JButton btnArquivo = new JButton("Arquivo");
		btnArquivo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				fileChooser = new JFileChooser();
				int action = fileChooser.showOpenDialog(null);
				if(action == JFileChooser.APPROVE_OPTION){
					fileToSend = fileChooser.getSelectedFile();
					filePath.setText(fileToSend.getName());
				}else{
					System.out.println("NOS");
				}
			}
		});
		
		btnArquivo.setBounds(478, 11, 93, 23);
		panelClient.add(btnArquivo);
		
		filePath = new JTextField();
		filePath.setBounds(136, 12, 332, 20);
		panelClient.add(filePath);
		filePath.setColumns(10);
		
		JButton buttonEnviar = new JButton("Enviar");
		buttonEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileSendTest.enviarArquivo(fileToSend);
			}
		});
		buttonEnviar.setBounds(136, 68, 93, 23);
		panelClient.add(buttonEnviar);
	}
}
