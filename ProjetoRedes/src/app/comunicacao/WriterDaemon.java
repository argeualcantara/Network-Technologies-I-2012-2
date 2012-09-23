package app.comunicacao;

import java.io.InputStream;
import java.io.OutputStream;

import app.domain.EstablishedConnection;

public class WriterDaemon extends Thread {

	private final InputStream inputStream;
	private final OutputStream outputStream;
	private final byte[] content;
	private final int secondsTimeout;
	private final int maxTries;
	private byte[] ack;
	private boolean hasToListen = true;
	private int tries = 0;

	public WriterDaemon(EstablishedConnection connection, byte[] content, Integer secondsTimeout, int maxTries) {
		this.inputStream = connection.getInputStream();
		this.outputStream = connection.getOutputStream();
		this.content = content;
		this.secondsTimeout = secondsTimeout * 1000;
		this.maxTries = maxTries;
	}

	public void startDaemon() {
		this.setPriority(MIN_PRIORITY);
		this.start();
		startListening();
	}

	@Override
	public void run() {

		try {

			while (!gotAck() && tries < maxTries) {
				tries++;
				outputStream.write(content);
				outputStream.flush();
				System.out.println("Tentando enviar o pacote pela " + tries + " vez.");
				Thread.sleep(secondsTimeout);

			}

			stopListening();

			if (tries == maxTries && !gotAck()) {
				System.out.println("[FALHA] Pacote foi enviado " + tries + " vezes, sem sucesso!");

			} else {
				System.out.println("[OK] Pacote enviado com sucesso, com " + tries + " tentativas.");
			}

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

		return ack != null && ack.length > 0;
	}
}
