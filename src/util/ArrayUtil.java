package util;

public class ArrayUtil {

	
	public static byte[] concat(byte[] first, byte[] second){
		
		int newSize = first.length + second.length;
		byte[] toReturn = new byte[newSize];
		
		
		for (int i=0; i<first.length; i++){
			toReturn[i] = first[i];
		}
		
		for(int j=first.length, i=0; j<newSize ; j++ , i++){
			toReturn[j] = second[i];
		}
		
		return toReturn;
	}
	
}
