package app.utils;

public class ByteUtil {

	public static boolean compare(byte[] content1, byte[] content2) {

		if (content1[0] == content2[0] && content1[1] == content2[1]) {
			return true;
		}
		return false;
	}

	public static byte[] retrievePartOfContent(byte[] content, int start, int end) {

		byte[] toResult = new byte[end - start +1];

		int j = 0;
		for (int i = start; i <= end; i++ , j++) {
			toResult[j] = content[i];
		}

		return toResult;
	}
}
