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
		byte groupIDReader = 0x01;
		byte groupIDWriter = 0x01;

		EstablishedConnection writerConnection = new Connector().connect(portWriter, groupIDWriter, source);
		EstablishedConnection readerConnection = new Connector().connect(portReader, groupIDReader, destination);
		
		if (writerConnection != null && readerConnection != null) {

			Reader reader = new Reader(readerConnection);
			
			Writer writer = new Writer(writerConnection);
//			try{
//				CommPortIdentifier cpi = CommPortIdentifier.getPortIdentifier(portReader);
//				CommPort commPort = cpi.open(Connector.class.getName(), 2000);
//				SerialPort portaReader = (SerialPort) commPort;
//				portaReader.notifyOnDataAvailable(true);
//			}catch(Exception e){
//				
//			}
			
			// Informado pelo usuario
			String message = "TESTE";

			
			// ESCREVENDO
			Frame packetResult = CoolProtocolParser.parseTo(destination.getBytes(), source.getBytes(), writerConnection.getGroupID(), message.getBytes());

//			writer.write(packetResult.retrieveContent());
			reader.start();
			new WriterDaemon(writerConnection,packetResult.retrieveContent(),4, 5).startDaemon();
		}

	}
}
