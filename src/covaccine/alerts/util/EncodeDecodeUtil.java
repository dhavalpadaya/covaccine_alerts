package covaccine.alerts.util;

import java.util.Base64;

public class EncodeDecodeUtil {
	public static void main(String[] args) {
		if(args.length > 0) {
			System.out.println("Encoded String :- "+encode(args[0]));
		}else {
			System.out.println("Please provide string to encode as first parameter while running this program");
		}
	}
	public static String encode(String str)
    {
        try {
  
        	// Getting encoder  
            Base64.Encoder encoder = Base64.getEncoder();  
            // Encoding string  
            String encodedStr = encoder.encodeToString(str.getBytes());
            return encodedStr;
        } 
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static String decode(String encodedStr)
    {
        try {
        	 // Getting decoder  
            Base64.Decoder decoder = Base64.getDecoder();  
            // Decoding string  
            String decodedStr = new String(decoder.decode(encodedStr));  
            return decodedStr;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
