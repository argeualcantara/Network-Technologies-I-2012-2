package app.comunicacao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import app.domain.EstablishedConnection;
import app.main.MainWindow;
import app.protocol.Frame;
import app.utils.ByteUtil;
import app.utils.CRC16;

public class Reader extends Thread {// implements SerialPortEventListener{

	private final InputStream inputStream;
	private final OutputStream outputStream;
	private final byte groupID;
	private final byte[] identification;

	public Reader(EstablishedConnection establishedConnection) {
		this.inputStream = establishedConnection.getInputStream();
		this.outputStream = establishedConnection.getOutputStream();
		this.groupID = establishedConnection.getGroupID();
		this.identification = establishedConnection.getIdentification();
		this.setPriority(MIN_PRIORITY);
	}

	@Override
	public void run() {
		try {
			while (MainWindow.runReader) {
				byte[] buffer = new byte[this.inputStream.available()];
				this.inputStream.read(buffer);
				if (buffer.length > 0) {
					Frame colected = Frame.createFromContent(buffer);

					byte[] resultCrc16 = colected.getCrc16();
					byte[] calculatedCRC = CRC16.calcularCRC(colected.retrieveContentWithoutCRC());

					// Verifica CRC
					if (ByteUtil.compare(resultCrc16, calculatedCRC)) {

						// Verifica GroupID
						if (groupID == colected.getGroupID()) {

							// Minha mensagem?
							if (ByteUtil.compare(identification, colected.getDestination())) {

								Frame ackAnswer = colected.ack();
								this.outputStream.write(ackAnswer.retrieveContent());
								this.outputStream.flush();
								MainWindow.mensagemTemp = MainWindow.mensagemTemp+new String(colected.getPayload());
								MainWindow.logger.setText("MSG Recieved -> " + new String(colected.getPayload()) +
															"\n"+MainWindow.logger.getText());

							}

						}

					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
