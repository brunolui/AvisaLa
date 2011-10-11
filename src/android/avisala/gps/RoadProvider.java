package android.avisala.gps;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import android.avisala.objetos.Rota;

public class RoadProvider {

	public static Rota getRoute(InputStream is) {
        KMLHandler handler = new KMLHandler();
        try {
        	SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(is, handler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return handler.rota;
	}
}
