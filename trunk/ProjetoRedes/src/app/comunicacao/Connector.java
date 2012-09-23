package app.comunicacao;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import app.domain.EstablishedConnection;

public class Connector {

	public EstablishedConnection connect(String portName, byte groupID, String name) {

		CommPortIdentifier portIdentifier;

		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			if (portIdentifier.isCurrentlyOwned()) {
				System.out.println("Error: Port is currently in use");
			} else {
				CommPort commPort = portIdentifier.open(Connector.class.getName(), 2000);
				if (commPort instanceof SerialPort) {
					SerialPort serialPort = (SerialPort) commPort;
					serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);

					return new EstablishedConnection(portName, serialPort.getInputStream(),
							serialPort.getOutputStream(), groupID, name.getBytes());
				} else {
					System.out.println("Error: Only serial ports are handled by this example.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
