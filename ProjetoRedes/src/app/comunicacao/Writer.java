package app.comunicacao;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import app.domain.EstablishedConnection;

public class Writer implements SerialPortEventListener {
	private final InputStream inputStream;
	private final OutputStream outputStream;

	public Writer(EstablishedConnection connection) {
		this.inputStream = connection.getInputStream();
		this.outputStream = connection.getOutputStream();
	}
	
	public void write(byte[] msg) {

		try {
			outputStream.write(msg);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		switch(arg0.getEventType()){
		 case SerialPortEvent.DATA_AVAILABLE:
			try {
				inputStream.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
