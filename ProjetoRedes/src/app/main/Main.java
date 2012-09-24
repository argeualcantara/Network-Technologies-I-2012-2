package app.main;

import java.util.LinkedList;

import app.comunicacao.Connector;
import app.comunicacao.Reader;
import app.comunicacao.WriterDaemon;
import app.domain.EstablishedConnection;
import app.protocol.Frame;
import app.protocol.parser.CoolProtocolParser;
import app.utils.ByteUtil;

public class Main {

	private static Integer payloadMaxSize = 6;
	static String portReader = "COM1";
	static String portWriter = "COM2";
	static String destination = "E2";
	static String source = "E1";
	static byte groupIDReader = 0x01;
	static byte groupIDWriter = 0x01;

	public static void main(String[] args) {

		// Informado pelo usuario

		EstablishedConnection writerConnection = new Connector().connect(portWriter, groupIDWriter, source);
		EstablishedConnection readerConnection = new Connector().connect(portReader, groupIDReader, destination);

		if (writerConnection != null && readerConnection != null) {

			Reader reader = new Reader(readerConnection);

//			Writer writer = new Writer(writerConnection);
			// try{
			// CommPortIdentifier cpi =
			// CommPortIdentifier.getPortIdentifier(portReader);
			// CommPort commPort = cpi.open(Connector.class.getName(), 2000);
			// SerialPort portaReader = (SerialPort) commPort;
			// portaReader.notifyOnDataAvailable(true);
			// }catch(Exception e){
			//
			// }

			// Informado pelo usuario
			reader.start();

			String message = "TESTE1 TESTE2 TESTE3 TESTE4 TESTE5";

			LinkedList<Frame> frames = retrieveFramesFromMessage(message);
//
//			for (Frame frame : frames) {
//
//				// ESCREVENDO
//				// Frame packetResult =
//				// CoolProtocolParser.parseTo(destination.getBytes(),
//				// source.getBytes(), writerConnection.getGroupID(),
//				// message.getBytes());
//				// Frame packetResult =
//				// CoolProtocolParser.parseTo(destination.getBytes(),
//				// source.getBytes(), writerConnection.getGroupID(),
//				// frame.re());
//
//				// writer.write(packetResult.retrieveContent());
//				// byte[] content = packetResult.retrieveContent();
//
//				// TODO - Cria um metodo que adiciona e faz o erro
//				// content[13] = 0x00;
//
//			}

			new WriterDaemon(writerConnection, frames, 4, 5).startDaemon();
		}

	}

	private static LinkedList<Frame> retrieveFramesFromMessage(String message) {

		LinkedList<Frame> toResult = new LinkedList<Frame>();
		byte[] totalBytes = message.getBytes();
		Integer totalSize = totalBytes.length;

		Integer packetsQuantity = totalSize / payloadMaxSize;
		Integer rest = totalSize % payloadMaxSize;
		packetsQuantity = packetsQuantity + (rest > 0 ? 1 : 0);
		
		for(int i=0; i<packetsQuantity ; i++){
			Frame currentFrame = null;
			if(i == packetsQuantity-1){
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
