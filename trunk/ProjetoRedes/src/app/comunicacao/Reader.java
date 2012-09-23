package app.comunicacao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import app.domain.EstablishedConnection;
import app.protocol.Frame;
import app.protocol.parser.CoolProtocolParser;
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
			while (true) {
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
								// System.out.println("READ -> "+parsedResult);

							}

						}

					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//
	// @Override
	// public void serialEvent(SerialPortEvent arg0) {
	// switch(arg0.getEventType()){
	// case SerialPortEvent.DATA_AVAILABLE:
	// this.start();
	// break;
	// }
	// }

}
