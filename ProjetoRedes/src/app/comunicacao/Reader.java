package app.comunicacao;

import java.io.IOException;
import java.io.InputStream;

import app.protocol.parser.CoolProtocolParser;

public class Reader extends Thread {

	private final InputStream inputStream;

	public Reader(InputStream inputStream) {
		this.inputStream = inputStream;
		this.setPriority(MIN_PRIORITY);
	}

	@Override
	public void run() {

		while (true) {

			byte[] buffer = new byte[1024];
			int len = -1;
			try {
				while ((len = this.inputStream.read(buffer)) > -1) {
					
//					String parsedResult = CoolProtocolParser.parseFrom(buffer);
//					System.out.println("READ -> "+parsedResult);
//					System.out.print(new String(buffer, 0, len));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
