import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	public static final int PORT = 9999;
	public static final int BUFFER_SIZE = 1024;

	public void run() {
		ServerSocket server = null;
		OutputStream out = null;
		InputStream entrada = null;
		Socket socket = null;
		try {
			server = new ServerSocket(PORT);
			socket = server.accept();
			System.out.println("conexao aceita");
			
			entrada = socket.getInputStream();
			// write the inputStream to a FileOutputStream
			 out = new FileOutputStream(new File(File.separator + "home"+File.separator+"thiagoponte"+
						File.separator+"received"+File.separator+"file.mp4"));
		 
			int read = 0;
			byte[] bytes = new byte[BUFFER_SIZE];
		 
			while ((read = entrada.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
		 
			entrada.close();
			out.flush();
			out.close();
			
//			System.out.println(file.getPath());
//			System.out.println("Server CRC: "+out.getChecksum().getValue());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out != null){
				try {
					out.flush();
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if(server != null){
				try {
					server.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			System.out.println("Server out...");
		}
	}
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}

}
