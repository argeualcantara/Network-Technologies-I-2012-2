
import infra.HeaderUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import sun.misc.IOUtils;
import util.CRC16;

public class FileSendTest {

	public static void enviarArquivo(File fileToSend, String ipDestino, int port, int bufferSize) {
		InputStream entrada;
		Socket socket = null;
		ByteArrayInputStream in = null;
		OutputStream out = null;
		
		try {
			if (fileToSend.canRead()) {
				socket = new Socket(ipDestino,port);
				entrada = socket.getInputStream();
				Window.logClient.setText("Iniciando transmissao...\n"+Window.logClient.getText());
				out = socket.getOutputStream();
				byte[] header = new HeaderUtils(fileToSend.getName(), (int)fileToSend.length()).getHeader();
				
				int newSize = header.length +  (int) fileToSend.length();
				byte[] fullContent = new byte[newSize];
				
				for (int i=0; i<header.length; i++){
					fullContent[i] = header[i];
				}
				byte [] file = IOUtils.readFully(new FileInputStream(fileToSend), (int) fileToSend.length(), true);
				for(int j=header.length, i=0; j<newSize ; j++ , i++){
					fullContent[j] =  file[i];
				}
//				byte[] fullContent = ArrayUtil.concat(header,);
				header = null;
				fileToSend = null;
				System.gc();
				Window.logClient.setText("CRC-16 READ FROM CLIENT: "+CRC16.calculate(fullContent)+"\n"+Window.logClient.getText());
				in = new ByteArrayInputStream(fullContent);
				byte[] buffer = new byte[bufferSize];
				Window.progressBar.setValue(0);
				Window.progressBar.setMaximum(fullContent.length);
				int read = 0;
				int i = 0;
				while (( read = in.read(buffer) )!= -1) {
					i++;
					out.write(buffer, 0, read);
					out.flush();
					Window.logClient.setText("Pacote N� "+i+" de tamanho "+read+"\n"+Window.logClient.getText());
					Window.progressBar.setValue(Window.progressBar.getValue()+read);
					Window.progressBar.repaint();
				}
				byte resposta [] = new byte[2];
				while(entrada.read(resposta) != -1){
					if(resposta[1] == (byte) 0xf1){
						Window.logClient.setText("Arquivo enviado com sucesso.\n"+Window.logClient.getText());
					}else{
						Window.logClient.setText("Arquivo enviado com falhas.\n"+Window.logClient.getText());
					}
				}
				System.gc();
				out.close();
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			Window.logClient.setText("Encerrando transmiss�o...\n"+Window.logClient.getText());
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