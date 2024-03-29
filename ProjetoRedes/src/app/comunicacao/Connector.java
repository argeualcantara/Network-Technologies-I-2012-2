package app.comunicacao;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;

import javax.swing.JOptionPane;

import app.domain.EstablishedConnection;
import app.main.MainWindow;

public class Connector {

	public EstablishedConnection connect(String portName, byte groupID, String name, int palavra, int paridade, int stopbit, int taxa) {

		CommPortIdentifier portIdentifier;

		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			if (portIdentifier.isCurrentlyOwned()) {
				System.out.println("Error: Port is currently in use");
			} else {
				CommPort commPort = portIdentifier.open(Connector.class.getName(), 2000);
				if (commPort instanceof SerialPort) {
					SerialPort serialPort = (SerialPort) commPort;
					serialPort.setSerialPortParams(taxa, palavra, stopbit, paridade);

					return new EstablishedConnection(portName, serialPort.getInputStream(),
							serialPort.getOutputStream(), groupID, name.getBytes(), serialPort);
				} else {
					System.out.println("Error: Only serial ports are handled by this example.");
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(MainWindow.getFrames()[0], "Porta n�o existe ou j� est� em uso!");
			//e.printStackTrace();
		}
		return null;
	}
	
	public static void disconnect() throws IOException {
//		MainWindow.readerConnection.getInputStream().close();
//		MainWindow.readerConnection.getOutputStream().close();
		if(MainWindow.readerConnection != null && MainWindow.readerConnection.getPorta()!= null){
			MainWindow.readerConnection.getPorta().close();
		}
		
//		MainWindow.writerConnection.getInputStream().close();
//		MainWindow.writerConnection.getOutputStream().close();
		if(MainWindow.writerConnection != null && MainWindow.readerConnection.getPorta()!= null){
			MainWindow.writerConnection.getPorta().close();
		}
	}
}
