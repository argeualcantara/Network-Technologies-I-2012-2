package app.utils;

public class ByteUtil {

	public static boolean compare(byte[] content1, byte[] content2) {

		if (content1[0] == content2[0] && content1[1] == content2[1]) {
			return true;
		}
		return false;
	}
}
