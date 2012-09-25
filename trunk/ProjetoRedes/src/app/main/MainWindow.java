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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	public static JTextField idDestination;
	public static JTextField idSource;
	public static JTextField tentativas;
	public static JTextField mensagem;
	public static JTextField taxa;
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
	public static JButton erro;
	public static JButton disconnect;
	public static JComboBox palavra;
	public static JComboBox paridade;
	public static JComboBox stopBit;
	
	public void init(){
		painel = new JPanel(new GridBagLayout());
		GridBagConstraints layout = new GridBagConstraints();
		layout.insets = new Insets(4,4,4,4);
		int gridY = 1;
		layout.gridx = 0;
		layout.gridy = gridY;
		JLabel port1 = new JLabel("Porta 1: ");
		painel.add(port1,layout);
		
		layout.gridx = 1;
		layout.gridy = gridY;
		porta1 = new JTextField("COM1");
		porta1.setSize(45, 19);
		porta1.setPreferredSize(porta1.getSize());
		painel.add(porta1, layout);
		
		gridY++;
		layout.gridx = 0;
		layout.gridy = gridY;
		JLabel port2 = new JLabel("Porta 2: ");
		painel.add(port2, layout);
		
		layout.gridx = 1;
		layout.gridy = gridY;
		porta2 = new JTextField("COM2");
		porta2.setSize(45, 19);
		porta2.setPreferredSize(porta2.getSize());
		painel.add(porta2, layout);
		
		gridY++;
		
		layout.gridx = 0;
		layout.gridy = gridY;
		JLabel taxaLabel = new JLabel("Taxa de transmissão: ");
		painel.add(taxaLabel, layout);
		
		layout.gridx = 1;
		layout.gridy = gridY;
		taxa = new JTextField("57600");
		taxa.setSize(50, 19);
		taxa.setPreferredSize(taxa.getSize());
		painel.add(taxa, layout);
		
		gridY++;
		
		layout.gridx = 0;
		layout.gridy = gridY;
		JLabel palavraLabel = new JLabel("Palavra: ");
		painel.add(palavraLabel, layout);
		
		layout.gridx = 1;
		layout.gridy = gridY;
		palavra = new JComboBox();
		palavra.addItem("8bits");
		palavra.addItem("7bits");
		palavra.addItem("6bits");
		palavra.setSize(55, 19);
		palavra.setPreferredSize(palavra.getSize());
		painel.add(palavra, layout);
		
		gridY++;
		
		layout.gridx = 0;
		layout.gridy = gridY;
		JLabel paridadeLabel = new JLabel("Paridade: ");
		painel.add(paridadeLabel, layout);
		
		layout.gridx = 1;
		layout.gridy = gridY;
		paridade = new JComboBox();
		paridade.addItem("NONE");
		paridade.addItem("EVEN");
		paridade.addItem("ODD");
		paridade.setSize(60, 19);
		paridade.setPreferredSize(paridade.getSize());
		painel.add(paridade, layout);
		
		gridY++;
		
		layout.gridx = 0;
		layout.gridy = gridY;
		JLabel sotibitLabel = new JLabel("StopBit: ");
		painel.add(sotibitLabel, layout);
		
		layout.gridx = 1;
		layout.gridy = gridY;
		stopBit = new JComboBox();
		stopBit.addItem("1bit");
		stopBit.addItem("2bits");
		stopBit.setSize(60, 19);
		stopBit.setPreferredSize(stopBit.getSize());
		painel.add(stopBit, layout);
		
		gridY++;
		
		layout.gridx = 0;
		layout.gridy = gridY;
		JLabel labelTO = new JLabel("Timeout(s): ");
		painel.add(labelTO, layout);
		
		layout.gridx = 1;
		layout.gridy = gridY;
		timeOut = new JTextField("1");
		timeOut.setSize(40, 19);
		timeOut.setPreferredSize(timeOut.getSize());
		painel.add(timeOut, layout);
		
		gridY++;
		
		layout.gridx = 0;
		layout.gridy = gridY;
		JLabel labelNPayload = new JLabel("Payload Size(N): ");
		painel.add(labelNPayload, layout);
		
		layout.gridx = 1;
		layout.gridy = gridY;
		payload = new JTextField("10");
		payload.setSize(40, 19);
		payload.setPreferredSize(payload.getSize());
		painel.add(payload, layout);
		
		gridY++;
		
		layout.gridx = 0;
		layout.gridy = gridY;
		JLabel labelTries = new JLabel("Tentativas(M): ");
		painel.add(labelTries, layout);
		
		layout.gridx = 1;
		layout.gridy = gridY;
		tentativas = new JTextField("2");
		tentativas.setSize(40, 19);
		tentativas.setPreferredSize(tentativas.getSize());
		painel.add(tentativas, layout);
		
		gridY++;
		
		layout.gridx = 0;
		layout.gridy = gridY;
		JLabel labelGIDSource = new JLabel("Group ID Source: ");
		painel.add(labelGIDSource, layout);
		
		layout.gridx = 1;
		layout.gridy = gridY;
		groupIdSource = new JTextField("1");
		groupIdSource.setSize(40, 19);
		groupIdSource.setPreferredSize(groupIdSource.getSize());
		painel.add(groupIdSource, layout);
		
		gridY++;
		
		layout.gridx = 0;
		layout.gridy = gridY;
		JLabel labelGIDDstination = new JLabel("Group ID Destination: ");
		painel.add(labelGIDDstination, layout);
		
		layout.gridx = 1;
		layout.gridy = gridY;
		groupIdDestination = new JTextField("1");
		groupIdDestination.setSize(40, 19);
		groupIdDestination.setPreferredSize(groupIdDestination.getSize());
		painel.add(groupIdDestination, layout);
		
//		gridY++;
//		
//		layout.gridx = 0;
//		layout.gridy = gridY;
//		JLabel labelIDDestination = new JLabel("ID Destination: ");
//		painel.add(labelIDDestination, layout);
//		
//		layout.gridx = 1;
//		layout.gridy = gridY;
//		idDestination = new JTextField("JO");
//		idDestination.setSize(40, 19);
//		idDestination.setPreferredSize(idDestination.getSize());
//		painel.add(idDestination, layout);
//		
//		gridY++;
//		
//		layout.gridx = 0;
//		layout.gridy = gridY;
//		JLabel labelIDSource = new JLabel("ID Source: ");
//		painel.add(labelIDSource, layout);
//		
//		layout.gridx = 1;
//		layout.gridy = gridY;
//		idSource = new JTextField("ZE");
//		idSource.setSize(40, 19);
//		idSource.setPreferredSize(idSource.getSize());
//		painel.add(idSource, layout);
		
		gridY++;
		
		layout.gridx = 0;
		layout.gridy = gridY;
		JLabel labelMessage = new JLabel("Mensagem: ");
		painel.add(labelMessage, layout);
		
		layout.gridx = 1;
		layout.gridy = gridY;
		mensagem = new JTextField("Teste de envio de mensagens usando o protocolo tricolor");
		mensagem.setSize(180, 19);
		mensagem.setPreferredSize(mensagem.getSize());
		painel.add(mensagem, layout);
		
		gridY++;
		
		layout.gridx = 0;
		layout.gridy = gridY;
		erro = new JButton("Add Erro");
		erro.addActionListener(this);
		erro.setActionCommand("erroCRC");
		erro.setSize(110, 19);
		erro.setPreferredSize(erro.getSize());
		painel.add(erro, layout);

		layout.gridx = 1;
		layout.gridy = gridY;
		JButton enviar = new JButton("Enviar");
		enviar.addActionListener(this);
		enviar.setActionCommand("enviar");
		enviar.setSize(80, 19);
		enviar.setPreferredSize(enviar.getSize());
		painel.add(enviar, layout);
		
		gridY++;
		
		layout.gridx = 0;
		layout.gridy = gridY;
		disconnect = new JButton("Disconnect");
		disconnect.addActionListener(this);
		disconnect.setActionCommand("desconectar");
		disconnect.setSize(110, 19);
		disconnect.setPreferredSize(disconnect.getSize());
		painel.add(disconnect, layout);
		
		gridY++;
		
		layout.gridx = 0;
		layout.gridy = gridY;
		layout.gridwidth = 3;
		logger = new JTextArea("-------LOG-------");
		logger.setSize(400, 100);
		logger.setPreferredSize(logger.getSize());
		logger.setEditable(false);
		logger.setColumns(400);
		logger.setRows(200);
		JScrollPane paineScrollLog = new JScrollPane(logger);
		paineScrollLog.setPreferredSize(logger.getSize());
		painel.add(paineScrollLog, layout);
		
		layout.gridx = 2;
		layout.gridy = 0;
		layout.gridheight = gridY;
		mensagemRecebida = new JTextArea("-------Mensagens Recebidas-------");
		mensagemRecebida.setSize(300, 400);
		mensagemRecebida.setEditable(false);
		mensagemRecebida.setRows(200);
		mensagemRecebida.setColumns(400);
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
		this.setIconImage(new ImageIcon(getClass().getResource("/img/leao.jpg").toString().substring(6)).getImage());
		this.setTitle("Protocolo Tricolor");
		this.setBounds(250, 30, 800, 700);
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
		}else
			if(e.getActionCommand().equals("erroCRC")){
				if(!erroCrc){
					erroCrc = true;
					erro.setText("ERRO ["+erroCrc+"]");
				}else{
					erroCrc = false;
					erro.setText("ERRO ["+erroCrc+"]");
				}
			} else if (e.getActionCommand().equals("desconectar")){
				try {
					Connector.disconnect();
					connected = false;
					JOptionPane.showMessageDialog(this, "Conexão interrrompida com sucesso.");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(this, "Falha ao interromper a conexão");
				}
			}
		
	}

	public void iniciarConexao() {
		if(Integer.parseInt(payload.getText()) > 255){
			JOptionPane.showMessageDialog(this, "Payload deve ter no máximo 255 caracteres.");
		}else
		if(groupIdDestination.getText().length() > 1 ){
			JOptionPane.showMessageDialog(this, "O groupID do destination deve conter apenas 1 caractere.");
		}else
		if(groupIdSource.getText().length() > 1 ){
			JOptionPane.showMessageDialog(this, "O groupID do source deve conter apenas 1 caractere.");
		}
		else{
			render = new FrameRender();
			render.execute();
		}
		
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
