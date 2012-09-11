package app.domain;

import java.io.InputStream;
import java.io.OutputStream;

public class EstablishedConnection {

	private final String portName;
	private final InputStream inputStream;
	private final OutputStream outputStream;
	

	public EstablishedConnection(String portName, InputStream inputStream, OutputStream outputStream) {
		this.portName = portName;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	public String getPortName() {
		return portName;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

}
