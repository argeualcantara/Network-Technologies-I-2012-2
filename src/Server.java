import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

public class Server extends Thread {
	public static final int PORT = 9999;
	public static final int BUFFER_SIZE = 8192;
	private FileSendTest test;
	public Server(FileSendTest test) {
		this.test = test;
	}

	public void run() {
		ServerSocket server = null;
		FileOutputStream out = null;
		CheckedInputStream entrada = null;
		Socket socket = null;
		File file = null;
		try {
			server = new ServerSocket(PORT);
			socket = server.accept();
			byte[] buffer = new byte[BUFFER_SIZE];
			
			entrada = new CheckedInputStream(socket.getInputStream(), new CRC32());
			file = new File(File.separator + "home"+File.separator+"argeu"+
								File.separator+"workspace"+File.separator+
								"TrabalhoRedesNP2"+File.separator+"recieved" + File.separator
					+ test.getFILE_NAME());
			file.createNewFile();
			
			out = new FileOutputStream(file);
			while ((entrada.read(buffer)) >= 0) {
				out.write(buffer);
			}
			out.close();
			entrada.close();
			System.out.println(file.getPath());
			System.out.println("Server CRC: "+Long.toHexString(entrada.getChecksum().getValue()));
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out != null){
				try {
					out.flush();
					out.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(server != null){
				try {
					server.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			System.out.println("Server out...");
		}
	}

}
