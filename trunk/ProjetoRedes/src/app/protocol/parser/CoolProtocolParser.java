package app.protocol.parser;

import sun.misc.CRC16;
import app.protocol.Frame;

public class CoolProtocolParser {

	//ida
	public static Frame parseTo(byte[] destination, byte[] source, byte groupID, byte[] payload) {

		// do the magic to create a frame packet
		Frame parsed = new Frame(destination, source, groupID, payload);
		parsed.setMessageID((byte) 0x01);
		parsed.setPayloadLength(new Integer(payload.length).byteValue());

		parsed.setCrc16(app.utils.CRC16.calcularCRC(parsed.retrieveContentWithoutCRC()));
        // TODO - criar na classe frame o metodo que devolve o conteudo concatenado em um so array de byte
		
		return parsed;
	}

	//volta
	public static String parseFrom(byte[] message) {

		// gera string baseada no byte array 
		if(true/*cheagem do crc estiver correta*/){
			
			Frame created = Frame.createFromContent(message);
			
			if(created.getGroupID() == created.getGroupID()){
				byte[] source = created.getSource();
				byte[] destination = created.getDestination();
				created.setDestination(source);
				created.setSource(destination);
			}
		}
		
		return "";
	}

}
