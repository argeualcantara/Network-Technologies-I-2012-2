import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;


public class CRC {
	public static long checksumInputStream(String filename) throws IOException
	   {
	      InputStream in = new FileInputStream(filename);
	      CRC32 crc = new CRC32();
	      int c;
	      while ((c = in.read()) != -1)
	         crc.update(c);
	      in.close();
	      return crc.getValue();
	   }
}
