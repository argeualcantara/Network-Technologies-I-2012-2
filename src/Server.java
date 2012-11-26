import infra.HeaderUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.omg.CORBA.DataOutputStream;

import util.CRC16;

public class Server extends Thread {
	public static final int PORT = 9999;
	public static final int BUFFER_SIZE = 1024;
	private static boolean running = true;
	private String filePath = "";
	static ServerSocket server = null;
	OutputStream out = null;
	InputStream entrada = null;
	static Socket socket = null;
	static Server instance = null;
	
	public Server(boolean b, String savePath) {
		this.filePath = savePath;
		this.running = b;
	}
	public void run() {
		try {
			System.out.println("Server rodando");
			server = new ServerSocket(PORT);
			socket = server.accept();
			System.out.println("conexao aceita");
			
			entrada = socket.getInputStream();
		 
			int read = 0;
			byte[] bytes = new byte[BUFFER_SIZE];
			
			ByteArrayOutputStream b = new ByteArrayOutputStream();
		 
			while ((read = entrada.read(bytes)) != -1) {
//				out.write(bytes, 0, read);
				b.write(bytes,0, read);
			}
			entrada.close();
			
			
			//retrieve crc value
			byte[] total = b.toByteArray();
			b = null;
			System.gc();
			byte [] headerLenght =  new byte[2];
			headerLenght[0] = total[HeaderUtils.HEADER_LENGTH_POS_1];
			headerLenght[1] = total[HeaderUtils.HEADER_LENGTH_POS_2];
			
			int headerFinalPos = Integer.parseInt(new String(headerLenght));
			
			String fileName = "";
			byte[] fineNameByte = new byte[headerFinalPos - 9];
			int j = 0;
			for (int i = 9; i < headerFinalPos; i++) {
				fineNameByte[j] = total[i];
				j++;
			}
			fileName = new String(fineNameByte);
			
			System.out.println(total[7]);
			System.out.println(total[8]);
			byte [] crc = new byte[2];
			crc[0] = total[7];
			crc[1] = total[8];
			long value = 0;
			for (int i = 0; i < crc.length; i++)
			{
			  value = (value << 8) + (crc[i] & 0xff);
			}
			String crcOK = Integer.toHexString((int)value);
			System.out.println(crcOK);
			
			// write the byte array to a FileOutputStream
			out = new FileOutputStream(new File(this.filePath + File.separator + fileName));
			
			total = HeaderUtils.getFile(total);
			System.gc();
			
			out.write(total);
			
//			byte[] buffer = new byte[BUFFER_SIZE];
//			
//			while ((read = entrada.read(bytes)) != -1) {
////				out.write(bytes, 0, read);
//				b.write(bytes,0, read);
//			}
//			
			
			out.flush();
			out.close();
			
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
			Window.btnStart.setEnabled(true);
			Window.btnStop.setEnabled(false);
			System.out.println("Server out...");
		}
	}
	
	public static void startServer(String savePath) {
		Server.instance = new Server(true,savePath);
		Server.instance.start();
	}

	public static void stopServer() {
		try {
			Server.running  = false;
			if(Server.socket != null){
				Server.socket.close();
			}
			if(Server.server != null){
				Server.server.close();
			}
			Server.instance.finalize();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
