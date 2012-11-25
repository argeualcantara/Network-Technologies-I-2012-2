package infra;

import java.nio.ByteBuffer;

public class HeaderUtils {
	private byte tipoPdu;
	private byte[] headerLength = new byte[2];
	private byte[] fileLength = new byte[4];
	private byte[] crc = new byte[2];
	private byte[] fileName;
	private byte[] header;
	
	public static final byte PDU_ENVIO = (byte) 0xf1;
	public static final byte PDU_RESPOSTA = (byte) 0xf2;
	
	public static final int CRC_POS_1 = 7;
	public static final int CRC_POS_2 = 8;
	
	public static final int HEADER_LENGTH_POS_1 = 1;
	public static final int HEADER_LENGTH_POS_2 = 2;
	
	/**
	 * O nome do arquivo deve ser enviado com a extensão e o tamanho do arquivo.
	 * @param fileName
	 * @param fileLength
	 */
	public HeaderUtils(String fileName, Integer fileLength){
		this.fileName = fileName.getBytes();
		
		this.fileLength = ByteBuffer.allocate(4).putInt(fileLength).array();
	}
	
	/**
	 * Retorna um array de bytes com o header, menos o CRC
	 * @return
	 */
	public byte[] getHeader(){
		this.tipoPdu = HeaderUtils.PDU_ENVIO;
		
		short headerLength = (short) ((short) 9 + this.fileName.length);
		
		
		this.headerLength = String.valueOf(headerLength).getBytes();
		
		this.header = new byte[headerLength];
		
		//tipo da pdu
		this.header[0] = this.tipoPdu;
		//tamanho do cabeçalho
		this.header[1] = this.headerLength[0];
		this.header[2] = this.headerLength[1];
		
		//tamanho do arquivo
		this.header[3] = this.fileLength[0];
		this.header[4] = this.fileLength[1];
		this.header[5] = this.fileLength[2];
		this.header[6] = this.fileLength[3];
		
		//crc
		this.header[7] = 0x00;
		this.header[8] = 0x00;
		
		//fileName
		int j = 9;
		for (int i = 0; i < this.fileName.length; i++) {
			this.header[j] = this.fileName[i];
			j++;
		}
		return this.header;
	}
	
	/**
	 * Método que retorna o nome do arquivo, é necessário passar a PDU completa.
	 * @param total
	 * @return
	 */
	public static byte[] getFile(byte [] total){
		byte [] headerSize = new byte [2];
		headerSize[0] = total[HeaderUtils.HEADER_LENGTH_POS_1];
		headerSize[1] = total[HeaderUtils.HEADER_LENGTH_POS_2];
		int headerFinalPos = Integer.parseInt(new String(headerSize));
		
		byte [] fileSize = new byte [4];
		fileSize[0] = total[3];
		fileSize[1] = total[4];
		fileSize[2] = total[5];
		fileSize[3] = total[6];
		int fileSIzeN = Integer.parseInt(new String(fileSize));
		
		byte [] file = new byte[fileSIzeN];
		int j = 0;
		for (int i = headerFinalPos; i < file.length; i++) {
			file[j] = total[i];
			j++;
		}
		return file;
	}
	
	/**
	 * Retorna o Header a partir da PDU completa.
	 * @param total
	 * @return
	 */
	public byte[] getHeaderPDU(byte[] total){
		byte [] headerSize = new byte [2];
		headerSize[0] = total[HeaderUtils.HEADER_LENGTH_POS_1];
		headerSize[1] = total[HeaderUtils.HEADER_LENGTH_POS_2];
		int headerFinalPos = Integer.parseInt(new String(headerSize));
		
		byte [] header = new byte[headerFinalPos];
		for (int i = 0; i < header.length; i++) {
			header[i] = total[i];
		}
		return header;
	}
	
	/**
	 * Método que retorna o nome do arquivo, é necessário passar o header.
	 * @param total
	 * @return
	 */
	public static String getFileName(byte[] header){
		
		int headerFinalPos = header.length;
		int fileNameInitPos = 9;
		
		int j = 0;
		byte[] fileName = new byte[headerFinalPos - fileNameInitPos];
		for (int i = fileNameInitPos; i < headerFinalPos; i++) {
			fileName[j] = header[i];
		}
		return new String(fileName);
	}
}

