package app.comunicacao;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import app.domain.EstablishedConnection;
import app.protocol.parser.CoolProtocolParser;

public class Reader extends Thread implements SerialPortEventListener{

	private final InputStream inputStream;
    private final OutputStream outputStream;
	
	
	public Reader(EstablishedConnection establishedConnection) {
		this.inputStream = establishedConnection.getInputStream();
		this.outputStream = establishedConnection.getOutputStream();
		this.setPriority(MIN_PRIORITY);
	}
	

	@Override
	public void run() {
			try {
				while(true){
					byte[] buffer = new byte[this.inputStream.available()];
						this.inputStream.read(buffer);
						if(buffer.length>0){
							String parsedResult = CoolProtocolParser.parseFrom(buffer);
//							System.out.println("READ -> "+parsedResult);
//							this.outputStream.write(buffer);
//							this.outputStream.flush();
						}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	}


	@Override
	public void serialEvent(SerialPortEvent arg0) {
		System.out.println("event Reader");
	}

}
