package app.domain;

import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;

public class EstablishedConnection {
	private final SerialPort porta;
	private final String portName;
	private final InputStream inputStream;
	private final OutputStream outputStream;
	private final byte groupID;
	private final byte[] identification;

	public EstablishedConnection(String portName, InputStream inputStream, OutputStream outputStream, byte groupID,
			byte[] identification, SerialPort porta) {
		this.porta = porta;
		this.portName = portName;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.groupID = groupID;
		this.identification = identification;
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

	public byte getGroupID() {
		return groupID;
	}

	public byte[] getIdentification() {
		return identification;
	}

	public SerialPort getPorta() {
		return porta;
	}

}
