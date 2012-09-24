package app.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import app.comunicacao.Connector;
import app.comunicacao.Reader;
import app.comunicacao.WriterDaemon;
import app.domain.EstablishedConnection;
import app.protocol.Frame;
import app.protocol.parser.CoolProtocolParser;
import app.utils.ByteUtil;

public class MainWindow extends JFrame implements ActionListener{
	public static JTextField porta1;
	public static JTextField porta2;
	public static JTextField timeOut;
	public static JTextField payload;
	public static JTextField groupIdSource;
	public static JTextField groupIdDestination;
	public static JTextField tentativas;
	public static JTextField mensagem;
	public static JTextArea logger;
	public static JTextArea mensagemRecebida;
	public static String mensagemTemp;
	public static boolean connected = false;
	public static EstablishedConnection writerConnection;
	public static EstablishedConnection readerConnection;
	public static boolean erroCrc = false;
	public static boolean runReader = true;
	public static Reader reader;
	public static WriterDaemon writer;
	public static JPanel painel;
	public static FrameRender render;
	
	public void init(){
		painel = new JPanel(new GridBagLayout());
		GridBagConstraints layout = new GridBagConstraints();
		layout.insets = new Insets(4,4,4,4);
		
		layout.gridx = 0;
		layout.gridy = 1;
		JLabel port1 = new JLabel("Porta 1: ");
		painel.add(port1,layout);
		
		layout.gridx = 1;
		layout.gridy = 1;
		porta1 = new JTextField("COM1");
		porta1.setSize(45, 19);
		porta1.setPreferredSize(porta1.getSize());
		painel.add(porta1, layout);
		
		layout.gridx = 0;
		layout.gridy = 2;
		JLabel port2 = new JLabel("Porta 2: ");
		painel.add(port2, layout);
		
		layout.gridx = 1;
		layout.gridy = 2;
		porta2 = new JTextField("COM2");
		porta2.setSize(45, 19);
		porta2.setPreferredSize(porta2.getSize());
		painel.add(porta2, layout);
		
		layout.gridx = 0;
		layout.gridy = 3;
		JLabel labelTO = new JLabel("Timeout(s): ");
		painel.add(labelTO, layout);
		
		layout.gridx = 1;
		layout.gridy = 3;
		timeOut = new JTextField("3");
		timeOut.setSize(40, 19);
		timeOut.setPreferredSize(timeOut.getSize());
		painel.add(timeOut, layout);
		
		layout.gridx = 0;
		layout.gridy = 4;
		JLabel labelNPayload = new JLabel("Payload Size(N): ");
		painel.add(labelNPayload, layout);
		
		layout.gridx = 1;
		layout.gridy = 4;
		payload = new JTextField("10");
		payload.setSize(40, 19);
		payload.setPreferredSize(payload.getSize());
		painel.add(payload, layout);
		
		layout.gridx = 0;
		layout.gridy = 5;
		JLabel labelTries = new JLabel("Tentativas(M): ");
		painel.add(labelTries, layout);
		
		layout.gridx = 1;
		layout.gridy = 5;
		tentativas = new JTextField("2");
		tentativas.setSize(40, 19);
		tentativas.setPreferredSize(tentativas.getSize());
		painel.add(tentativas, layout);
		
		layout.gridx = 0;
		layout.gridy = 6;
		JLabel labelGIDSource = new JLabel("Group ID Source: ");
		painel.add(labelGIDSource, layout);
		
		layout.gridx = 1;
		layout.gridy = 6;
		groupIdSource = new JTextField("1");
		groupIdSource.setSize(40, 19);
		groupIdSource.setPreferredSize(groupIdSource.getSize());
		painel.add(groupIdSource, layout);
		
		layout.gridx = 0;
		layout.gridy = 7;
		JLabel labelGIDDstination = new JLabel("Group ID Destination: ");
		painel.add(labelGIDDstination, layout);
		
		layout.gridx = 1;
		layout.gridy = 7;
		groupIdDestination = new JTextField("1");
		groupIdDestination.setSize(40, 19);
		groupIdDestination.setPreferredSize(groupIdDestination.getSize());
		painel.add(groupIdDestination, layout);
		
		layout.gridx = 0;
		layout.gridy = 8;
		JLabel labelMessage = new JLabel("Mensagem: ");
		painel.add(labelMessage, layout);
		
		layout.gridx = 1;
		layout.gridy = 8;
		mensagem = new JTextField("TESTE 2 por que é legal");
		mensagem.setSize(180, 19);
		mensagem.setPreferredSize(mensagem.getSize());
		painel.add(mensagem, layout);
		
		layout.gridx = 1;
		layout.gridy = 9;
		JButton enviar = new JButton("Enviar");
		enviar.addActionListener(this);
		enviar.setActionCommand("enviar");
		enviar.setSize(80, 19);
		enviar.setPreferredSize(enviar.getSize());
		painel.add(enviar, layout);
		
		layout.gridx = 0;
		layout.gridy = 10;
		layout.gridwidth = 3;
		logger = new JTextArea("-------LOG-------");
		logger.setSize(400, 100);
		logger.setPreferredSize(logger.getSize());
		logger.setEditable(false);
		logger.setColumns(40);
		logger.setRows(200);
		JScrollPane paineScrollLog = new JScrollPane(logger);
		paineScrollLog.setPreferredSize(logger.getSize());
		painel.add(paineScrollLog, layout);
		
		layout.gridx = 2;
		layout.gridy = 0;
		layout.gridheight = 10;
		mensagemRecebida = new JTextArea("-------Mensagens Recebidas-------");
		mensagemRecebida.setSize(300, 400);
		mensagemRecebida.setEditable(false);
		mensagemRecebida.setPreferredSize(mensagemRecebida.getSize());
		JScrollPane paineScrollMsg = new JScrollPane(mensagemRecebida);
		paineScrollMsg.setPreferredSize(mensagemRecebida.getSize());
		painel.add(paineScrollMsg, layout);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			  public void windowClosed(WindowEvent e) {
				   try {
					Connector.disconnect();
					if(reader != null && reader.isAlive()){
						runReader = false;
					}
					if(writer != null && writer.isAlive()){
						writer.interrupt();
					}
					if(render != null){
						render.cancel(true);
					}
					this.finalize();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (Throwable e1) {
					e1.printStackTrace();
				}
				
			}
		});  
		this.add(painel);
		this.setBounds(250, 100, 800, 600);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		MainWindow mn = new MainWindow();
		mn.init();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("enviar")){
			iniciarConexao();
		}
		
	}

	public void iniciarConexao() {
		render = new FrameRender();
		render.execute();
		
	}
	
	public static LinkedList<Frame> retrieveFramesFromMessage(String message, Integer payloadMaxSize, String source, String destination, byte groupIDWriter) {

		LinkedList<Frame> toResult = new LinkedList<Frame>();
		byte[] totalBytes = message.getBytes();
		Integer totalSize = totalBytes.length;

		Integer packetsQuantity = totalSize / payloadMaxSize;
		Integer rest = totalSize % payloadMaxSize;
		packetsQuantity = packetsQuantity + (rest > 0 ? 1 : 0);
		
		for(int i=0; i<packetsQuantity ; i++){
			Frame currentFrame = null;
			if((i == packetsQuantity-1 && i != 0) || packetsQuantity == 1){
				byte[] payload = ByteUtil.retrievePartOfContent(totalBytes, i*payloadMaxSize, i*payloadMaxSize + rest -1);
				currentFrame = CoolProtocolParser.parseTo(destination.getBytes(), source.getBytes(), groupIDWriter, payload);
			}else{
				byte[] payload = ByteUtil.retrievePartOfContent(totalBytes, i*payloadMaxSize, i*payloadMaxSize + payloadMaxSize-1);
				currentFrame = CoolProtocolParser.parseTo(destination.getBytes(), source.getBytes(), groupIDWriter, payload);
			}
			toResult.add(currentFrame);
		}
		return toResult;
	}

}
