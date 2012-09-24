package app.protocol.parser;

import app.main.MainWindow;
import app.protocol.Frame;

public class CoolProtocolParser {

	//ida
	public static Frame parseTo(byte[] destination, byte[] source, byte groupID, byte[] payload) {

		// do the magic to create a frame packet
		Frame parsed = new Frame(destination, source, groupID, payload);
		parsed.setMessageID((byte) 0x04);
		parsed.setPayloadLength(new Integer(payload.length).byteValue());
		if(MainWindow.erroCrc){
			parsed.setCrc16(new byte [] {0x0f,0x0f});
		}else{
			parsed.setCrc16(app.utils.CRC16.calcularCRC(parsed.retrieveContentWithoutCRC()));
		}
        // TODO - criar na classe frame o metodo que devolve o conteudo concatenado em um so array de byte
		
		return parsed;
	}

	//volta
	public static String parseFrom(byte[] message) {

		// gera string baseada no byte array 
//		if(true/*cheagem do crc estiver correta*/){
//			
//			Frame created = new Frame();
//			created = created.createFromContent(message);
//			String crcRecebido = new String(created.getCrc16());
//			String crclocal = new String(CRC16.calcularCRC(created.retrieveContentWithoutCRC()));
				
//			if(created.getGroupID() == created.getGroupID()){
//				byte[] source = created.getSource();
//				byte[] destination = created.getDestination();
//				created.setDestination(source);
//				created.setSource(destination);
//			}
//		}
//		return new String(created.getPayload());
		return null;
	}

}
