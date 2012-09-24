package app.main;

import java.util.LinkedList;

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
		byte groupIDReader = 0x01;
		byte groupIDWriter = 0x01;
		String message = MainWindow.mensagem.getText();
		int tries = Integer.parseInt(MainWindow.tentativas.getText());
		int timeout = Integer.parseInt(MainWindow.timeOut.getText());
		
		if(!MainWindow.connected){
			MainWindow.writerConnection = new Connector().connect(portWriter, groupIDWriter, source);
			MainWindow.readerConnection = new Connector().connect(portReader, groupIDReader, destination);
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
