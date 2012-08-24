package br.unifor.redes1.examples;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * Classe de comunicação serial bi-direcional. Ao final do arquivo, deve ser dita qual porta abrir.
 * Devem ser abertas 2 portas. 
 * @author ThiagoPonte
 *
 */
public class TwoWaySerialComm {
	public TwoWaySerialComm() {
		super();
	}

	/**
	 * método que inicia a conexao com a porta
	 * @param portName
	 * @throws Exception
	 */
	void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();

				(new Thread(new SerialReader(in))).start();
				(new Thread(new SerialWriter(out,portName))).start();

			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	/** */
	public static class SerialReader implements Runnable {
		InputStream in;
		public SerialReader(InputStream in) {
			this.in = in;
		}

		public void run() {
			byte[] buffer = new byte[1024];
			int len = -1;
			try {
				while ((len = this.in.read(buffer)) > -1) {
					System.out.print(new String(buffer, 0, len));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** */
	public static class SerialWriter implements Runnable {
		OutputStream out;
		String portName;
		public SerialWriter(OutputStream out, String portName) throws IOException {
			this.out = out;
			this.portName = portName;
		}

		public void run() {
			try {
				int c = 0;
				boolean first = true;
				String  msg = (portName.concat(": "));
				while ((c = System.in.read()) > -1) {
					if(first){
						first = false;
						this.out.write(msg.getBytes());
					}
					this.out.write(c);
					if(c == 10){        // if character is an 'enter' then the 
						first = true;   // next time the port name must be displayed
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			(new TwoWaySerialComm()).connect("COM1"); // trocar por porta disponível
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}