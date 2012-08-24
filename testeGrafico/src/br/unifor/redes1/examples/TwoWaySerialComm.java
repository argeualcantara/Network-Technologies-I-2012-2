package br.unifor.redes1.examples;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.unifor.redes1.layout.JanelaPrincipal;
/**
 * Classe de comunicação serial bi-direcional. Ao final do arquivo, deve ser dita qual porta abrir.
 * Devem ser abertas 2 portas. 
 * @author ThiagoPonte
 *
 */
public class TwoWaySerialComm {
	private JanelaPrincipal principal;
	private InputStream in;
	private OutputStream out;
	private String portName;
	public TwoWaySerialComm(JanelaPrincipal principal) {
		super();
		this.principal = principal;
	}

	/**
	 * método que inicia a conexao com a porta
	 * @param portName
	 * @throws Exception
	 */
	public void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		this.portName = portName;
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				this.in = serialPort.getInputStream();
				this.out = serialPort.getOutputStream();

			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	public void mandarMensagem() {
		byte[] texto = this.principal.getTextFieldCom().getText().getBytes();
		try {
			int len = 0;
			while( (len = this.in.read(texto)) > -1){
				this.out.write(texto);
				len = -1;
			}
			this.principal.getTextAreaCom().setText(
					this.principal.getTextFieldCom().getText()+"\n"+this.principal.getTextAreaCom().getText()
					);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeMessage() {
		try {
			int c = 0;
			boolean first = true;
			String msg = (portName.concat(": "));
			while ((c = this.in.read()) > -1) {
				if (first) {
					first = false;
					this.out.write(msg.getBytes());
				}
				this.out.write(c);
				if (c == 10) { // if character is an 'enter' then the
					first = true; // next time the port name must be displayed
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//		try {
//			(new TwoWaySerialComm()).connect("COM2"); // trocar por porta disponível
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}