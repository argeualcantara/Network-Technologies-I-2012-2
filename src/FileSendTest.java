import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

public class FileSendTest {
	public static void enviarArquivo(File fileToSend) {
		Socket socket = null;
		FileInputStream in = null;
		OutputStream out = null;
		try {
			if (fileToSend.canRead()) {
				socket = new Socket("127.0.0.1",Server.PORT);
				long crc = calcularCrc(fileToSend);
				System.out.println("Client CRC: "+crc);
				out = socket.getOutputStream();
				
				in = new FileInputStream(fileToSend);
				
				byte[] buffer = new byte[Server.BUFFER_SIZE];
				while (( in.read(buffer) )!= -1) {
					out.write(buffer);
					out.flush();
				}
				out.close();
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			System.out.println("Client Out...");
			try{
				if(out != null){
					out.close();
				}
				if(socket != null){
					socket.close();
				}
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}
	
	private static long calcularCrc(File file) throws IOException {
		CheckedInputStream cin = new CheckedInputStream(new FileInputStream(file), new CRC32());
		byte[] buffer = new byte[Server.BUFFER_SIZE];
		while(cin.read(buffer) != -1){
			//utilizado para ler o arquivo
		}
		cin.close();
		return cin.getChecksum().getValue();
	}
}
