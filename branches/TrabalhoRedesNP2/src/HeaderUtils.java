
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
	
	/**
	 * O nome do arquivo deve ser enviado com a extensão e o tamanho do arquivo.
	 * @param fileName
	 * @param fileLength
	 */
	public HeaderUtils(String fileName, Integer fileLength){
		this.fileName = fileName.getBytes();
		this.fileLength = String.valueOf(fileLength).getBytes();
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
}
