
import infra.HeaderUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;

import sun.misc.IOUtils;
import util.ArrayUtil;
import util.CRC16;

public class FileSendTest {
	public static void enviarArquivo(File fileToSend) {
		Socket socket = null;
		ByteArrayInputStream in = null;
		OutputStream out = null;
		try {
			if (fileToSend.canRead()) {
				socket = new Socket("localhost",Server.PORT);
//				long crc = calcuarCrc(fileToSend);
//				System.out.println("Client CRC: "+crc);
				out = socket.getOutputStream();
				System.out.println("Integer: "+(int)fileToSend.length());
				System.out.println("Long: "+fileToSend.length());
				byte[] header = new HeaderUtils(fileToSend.getName(), (int)fileToSend.length()).getHeader();
				
//				String calculate = CRC16.calculate(fileToSend);
				byte[] fullContent = ArrayUtil.concat(header, IOUtils.readFully(new FileInputStream(fileToSend), (int) fileToSend.length(), true));
				
				String calculate = CRC16.calculate(fullContent);
//				CRC16.addCRC(fullContent,calculate);
				System.out.println("CRC-16 READ FROM CLIENT: "+calculate);
				
//				in = new FileInputStream(fileToSend);
				in = new ByteArrayInputStream(fullContent);
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
	
}