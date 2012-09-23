package app.main;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import app.comunicacao.Connector;
import app.comunicacao.Reader;
import app.comunicacao.Writer;
import app.comunicacao.WriterDaemon;
import app.domain.EstablishedConnection;
import app.protocol.Frame;
import app.protocol.parser.CoolProtocolParser;

public class Main {

	public static void main(String[] args) {

		// Informado pelo usuario
		String portReader = "COM1";
		String portWriter = "COM2";
		String destination = "E2";
		String source = "E1";
		byte groupID = 0x01;

		EstablishedConnection writerConnection = new Connector().connect(portWriter);
		EstablishedConnection readerConnection = new Connector().connect(portReader);
		
		if (writerConnection != null && readerConnection != null) {

			Reader reader = new Reader(readerConnection);
			
			Writer writer = new Writer(writerConnection);
			try{
				CommPortIdentifier cpi = CommPortIdentifier.getPortIdentifier(portReader);
				CommPort commPort = cpi.open(Connector.class.getName(), 2000);
				SerialPort portaReader = (SerialPort) commPort;
				portaReader.notifyOnDataAvailable(true);
				
				cpi = CommPortIdentifier.getPortIdentifier(portReader);
				commPort = cpi.open(Connector.class.getName(), 2000);
				SerialPort portaWriter = (SerialPort) commPort;
				portaWriter.notifyOnDataAvailable(true);
			}catch(Exception e){
				
			}
			
			// Informado pelo usuario
			String message = "TESTE";

			Frame packetResult = CoolProtocolParser.parseTo(destination.getBytes(), source.getBytes(), groupID, message.getBytes());

//			writer.write(packetResult.retrieveContent());
			reader.start();
			new WriterDaemon(writerConnection,packetResult.retrieveContent(),4, 5).startDaemon();
		}

	}
}
