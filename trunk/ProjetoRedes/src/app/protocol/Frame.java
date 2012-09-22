package app.protocol;

import java.util.ArrayList;
import java.util.List;

public class Frame {

	private byte[] destination;
	private byte messageID;
	private byte groupID;
	private byte payloadLength;
	private byte[] source;
	private byte[] payload;
	private byte[] crc16;

	public Frame() {

	}

	public Frame(byte[] destination, byte[] source, byte groupID, byte[] payload) {

		this.destination = destination;
		this.source = source;
		this.groupID = groupID;
		this.payload = payload;
	}

	public byte[] getDestination() {
		return destination;
	}

	public void setDestination(byte[] destination) {
		this.destination = destination;
	}

	public byte getMessageID() {
		return messageID;
	}

	public void setMessageID(byte messageID) {
		this.messageID = messageID;
	}

	public byte getGroupID() {
		return groupID;
	}

	public void setGroupID(byte groupID) {
		this.groupID = groupID;
	}

	public byte getPayloadLength() {
		return payloadLength;
	}

	public void setPayloadLength(byte payloadLength) {
		this.payloadLength = payloadLength;
	}

	public byte[] getSource() {
		return source;
	}

	public void setSource(byte[] source) {
		this.source = source;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	public byte[] getCrc16() {
		return crc16;
	}

	public void setCrc16(byte[] crc16) {
		this.crc16 = crc16;
	}

	public byte[] retrieveContent() {

		List<Byte> content = new ArrayList<Byte>();
		
		for(int i=0; i < destination.length; i++){
			content.add(destination[i]);
		}
		
		content.add(messageID);
		content.add(groupID);
		content.add(payloadLength);
		
		for(int i=0; i < source.length; i++){
			content.add(source[i]);
		}
		
		for(int i=0; i < payload.length; i++){
			content.add(payload[i]);
		}
		
		for(int i=0; i < crc16.length; i++){
			content.add(crc16[i]);
		}

     
		Object[] result = content.toArray();
		
		byte[] toReturn = new byte[result.length];
		
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = (Byte)result[i];
		}
		
		return toReturn;
	}

	public byte[] retrieveContentWithoutCRC() {

	List<Byte> content = new ArrayList<Byte>();
		
		for(int i=0; i < destination.length; i++){
			content.add(destination[i]);
		}
		
		content.add(messageID);
		content.add(groupID);
		content.add(payloadLength);
		
		for(int i=0; i < source.length; i++){
			content.add(source[i]);
		}
		
		for(int i=0; i < payload.length; i++){
			content.add(payload[i]);
		}
     
		Object[] result =  content.toArray();
		
		byte[] toReturn = new byte[result.length];
		
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = (Byte)result[i];
		}
		
		return toReturn;
	}

}
