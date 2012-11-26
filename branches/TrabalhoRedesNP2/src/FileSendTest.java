
import infra.HeaderUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;

import sun.misc.IOUtils;
import util.ArrayUtil;
import util.CRC16;

public class FileSendTest {
	public static void enviarArquivo(File fileToSend, String ipDestino, int port, final int bufferSize) {
		Socket socket = null;
		ByteArrayInputStream in = null;
		OutputStream out = null;
		try {
			if (fileToSend.canRead()) {
				socket = new Socket(ipDestino,port);
				out = socket.getOutputStream();
				byte[] header = new HeaderUtils(fileToSend.getName(), (int)fileToSend.length()).getHeader();
				
				final byte[] fullContent = ArrayUtil.concat(header, IOUtils.readFully(new FileInputStream(fileToSend), (int) fileToSend.length(), true));
				fileToSend = null;
				System.gc();
				String calculate = CRC16.calculate(fullContent);
				System.out.println("CRC-16 READ FROM CLIENT: "+calculate);
				
				in = new ByteArrayInputStream(fullContent);
				byte[] buffer = new byte[bufferSize];
				
				Window.progressBar.setValue(0);
				Window.progressBar.setMaximum(fullContent.length);
				int read = 0;
				while (( read = in.read(buffer) )!= -1) {
					out.write(buffer, 0, read);
					out.flush();
					Window.progressBar.setValue(Window.progressBar.getValue()+read);
					Window.progressBar.repaint();
				}
				System.gc();
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