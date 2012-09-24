package app.main;

import gnu.io.SerialPort;

import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import app.comunicacao.Connector;
import app.comunicacao.Reader;
import app.comunicacao.WriterDaemon;
import app.protocol.Frame;

public class FrameRender extends SwingWorker<Void, Void>{
	public FrameRender() {
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		Integer payloadMaxSize = Integer.parseInt(MainWindow.payload.getText());
		String portReader = MainWindow.porta1.getText();
		String portWriter = MainWindow.porta2.getText();
		MainWindow.mensagemTemp = "";
		String destination = "E2";
		String source = "E1";
		byte groupIDReader = MainWindow.groupIdDestination.getText().getBytes()[0];
		byte groupIDWriter = MainWindow.groupIdSource.getText().getBytes()[0];
		String message = MainWindow.mensagem.getText();
		int tries = Integer.parseInt(MainWindow.tentativas.getText());
		int timeout = Integer.parseInt(MainWindow.timeOut.getText());
		MainWindow.logger.setText("Iniciando Transmissão.\n"+MainWindow.logger.getText());
		if(MainWindow.connected){
			Connector.disconnect();
			MainWindow.connected = false;
		}
		int taxa = Integer.parseInt(MainWindow.taxa.getText());
		if(!MainWindow.connected){
			int paridade = 0;
			switch( MainWindow.paridade.getSelectedIndex()){
				case 0: paridade = SerialPort.PARITY_NONE;break;
				case 1: paridade = SerialPort.PARITY_EVEN;break;
				case 2: paridade = SerialPort.PARITY_ODD;break;
			}
			int palavra = 0;
			switch( MainWindow.palavra.getSelectedIndex()){
				case 0: palavra = SerialPort.DATABITS_8;break;
				case 1: palavra = SerialPort.DATABITS_7;break;
				case 2: paridade = SerialPort.DATABITS_6;break;
			}
			int stopbit = 0;
			switch( MainWindow.stopBit.getSelectedIndex()){
				case 0: stopbit = SerialPort.STOPBITS_1;break;
				case 1: stopbit = SerialPort.STOPBITS_2;break;
			}
			MainWindow.writerConnection = new Connector().connect(portWriter, groupIDWriter, source, palavra, paridade, stopbit, taxa);
			MainWindow.readerConnection = new Connector().connect(portReader, groupIDReader, destination, palavra, paridade, stopbit, taxa);
			MainWindow.connected = true;
		}
		MainWindow.reader = new Reader(MainWindow.readerConnection);
		LinkedList<Frame> frames = MainWindow.retrieveFramesFromMessage(message, payloadMaxSize, source, destination, groupIDWriter);
		MainWindow.reader.start();
		MainWindow.writer = new WriterDaemon(MainWindow.writerConnection, frames, timeout, tries);
		MainWindow.writer.startDaemon();
		return null;
	}
	
}
