package app.protocol;

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

		byte[] concatanedFields = new byte[12];
		
		return concatanedFields;
	}

}
