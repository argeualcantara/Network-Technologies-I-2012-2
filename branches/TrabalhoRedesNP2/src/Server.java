import infra.HeaderUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import util.CRC16;

public class Server extends Thread {
	public static final int BUFFER_SIZE = 8192;
	private String filePath = "";
	private int serverPort = -1;
	static ServerSocket server = null;
	OutputStream out = null;
	InputStream entrada = null;
	private OutputStream saida = null;
	static Socket socket = null;
	static Server instance = null;
	
	public Server(String savePath, int serverPort) {
		this.filePath = savePath;
		this.serverPort = serverPort;
	}
	public void run() {
		try {
			Window.logServer.setText("Server iniciado\n"+Window.logServer.getText());
			server = new ServerSocket(serverPort);
			socket = server.accept();
			Window.logServer.setText("Conexao aceita\n"+Window.logServer.getText());
			entrada = socket.getInputStream();
			saida = socket.getOutputStream();
			int read = 0;
			byte[] bytes = new byte[BUFFER_SIZE];
			int pckg = 0;
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			while ((read = entrada.read(bytes)) != -1) {
				pckg++;
//				out.write(bytes, 0, read);
				b.write(bytes,0, read);
				Window.logServer.setText("Recebendo o pacote "+pckg+"\n"+Window.logServer.getText());
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
			headerLenght = null;
			String fileName = "";
			byte[] fineNameByte = new byte[headerFinalPos - 9];
			int j = 0;
			for (int i = 9; i < headerFinalPos; i++) {
				fineNameByte[j] = total[i];
				j++;
			}
			fileName = new String(fineNameByte);
			Window.logServer.setText("Recebendo o arquivo "+fileName+"\n"+Window.logServer.getText());
			System.gc();
			fineNameByte = null;
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
			Window.logServer.setText("CRC Recebido "+Integer.toHexString((int)value)+"\n"+Window.logServer.getText());
			
			// write the byte array to a FileOutputStream
			out = new FileOutputStream(new File(this.filePath + File.separator + fileName));
			String crcCalculado = CRC16.calculate(total);
			byte [] resposta;
			if(crcCalculado.equals(Integer.toHexString((int)value))){
				//respota OK - crc bateu
				resposta = new byte[] { ((byte)0xf2), ((byte)0xf1) };
				saida.write(resposta);
				total = HeaderUtils.getFile(total);
				
			}else{
				//respota ERRO - crc nao bateu
				resposta = new byte[] { ((byte)0xf2), ((byte)0xf2) };
			}
			saida.write(resposta);
			saida.close();
			System.gc();
			out.write(total);
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
			Window.logServer.setText("Server finazou a execucao...\n"+Window.logServer.getText());
		}
	}
	
	public static void startServer(String savePath, int serverPort) {
		Server.instance = new Server(savePath, serverPort);
		Server.instance.start();
	}
	
	public static void stopServer() {
		try {
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
