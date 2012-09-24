package app.comunicacao;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import app.domain.EstablishedConnection;
import app.main.MainWindow;
import app.protocol.Frame;

public class WriterDaemon extends Thread {

	private final InputStream inputStream;
	private final OutputStream outputStream;
	private final LinkedList<Frame> frames;
	private final int secondsTimeout;
	private final int maxTries;
	private byte[] ack;
	private boolean hasToListen = true;
	private int tries = 0;
    private Frame frameToSend;
	
	public WriterDaemon(EstablishedConnection connection, LinkedList<Frame> frames, Integer secondsTimeout, int maxTries) {
		this.inputStream = connection.getInputStream();
		this.outputStream = connection.getOutputStream();
		this.frames = frames;
		this.secondsTimeout = secondsTimeout * 1000;
		this.maxTries = maxTries;
	}

	public void startDaemon() {
		this.setPriority(MIN_PRIORITY);
		frameToSend = frames.poll();
		this.start();
		startListening();
	}

	@Override
	public void run() {

		try {

			while (!frames.isEmpty() | (!gotAck() && tries < maxTries)  ) {
				tries++;
				
				
				outputStream.write(frameToSend.retrieveContent());
				outputStream.flush();
				MainWindow.logger.setText("Tentando enviar o pacote pela " + tries + " vez.\n"+
						MainWindow.logger.getText());
				Thread.sleep(secondsTimeout);

			}

			if(frames.isEmpty()){
				stopListening();
				MainWindow.mensagemRecebida.setText(MainWindow.mensagemTemp+"\n"+
						MainWindow.mensagemRecebida.getText());
				MainWindow.logger.setText("Todos os pacotes foram enviados!\n"+
						MainWindow.logger.getText());
			}

			if (tries == maxTries && !gotAck()) {
				stopListening();
				MainWindow.logger.setText("[FALHA] Pacote foi enviado " + tries + " vezes, sem sucesso!\n"+
						MainWindow.logger.getText());

			} 
//				
////				frameToSend = frames.poll();
//				System.out.println("[OK] Pacote enviado com sucesso, com " + tries + " tentativas.");
//			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void stopListening() {

		hasToListen = false;
	}

	private void startListening() {
		while (hasToListen) {
			try {
				byte[] readMsg = new byte[this.inputStream.available()];
				this.inputStream.read(readMsg);

				if (readMsg != null && readMsg.length > 0)
					ack = readMsg;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private boolean gotAck() {

		boolean hasAck = ack != null && ack.length > 0;
			if(hasAck){
				MainWindow.logger.setText("[OK] Pacote enviado com sucesso, com " + tries + " tentativas.\n"+
						MainWindow.logger.getText());
				tries = 0;
				frameToSend = frames.poll();
			}
		return hasAck;
	}
}
