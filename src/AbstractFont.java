

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.net.URL;

/**
 * This class is used to create new font using polymorphism. 
 * @author Vittunyuta Maeprasart
 *
 */
public class AbstractFont {
	/**
	 * Creating new font from external files
	 * @param fileName is name of a external file 
	 * @return Font type of external font
	 */
	public static Font getFont(String fileName) {
	    String path = "/res/" + fileName;
	    URL url = Thread.currentThread().getClass().getResource(path);
	    Font font=null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
		} catch (FontFormatException | IOException e) {
			//TODO log the error instead of printStackTrace
			e.printStackTrace();
		}
	    return font;
	}
}
