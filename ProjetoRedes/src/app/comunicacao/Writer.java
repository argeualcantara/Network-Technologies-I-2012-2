package app.comunicacao;

import java.io.IOException;
import java.io.OutputStream;

public class Writer {

	private final OutputStream outputStream;

	public Writer(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void write(byte[] msg) {

		try {

			outputStream.write(msg);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
