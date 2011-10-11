package android.avisala.gps;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.avisala.objetos.Destino;
import android.avisala.objetos.Rota;

public class KMLHandler extends DefaultHandler {
	Rota rota;
	boolean isPlacemark;
	boolean isRoute;
	boolean isItemIcon;
	private Stack mCurrentElement = new Stack();
	private String mString;

	public KMLHandler() {
		rota = new Rota();
	}

	@SuppressWarnings("unchecked")
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		mCurrentElement.push(localName);
		if (localName.equalsIgnoreCase("Placemark")) {
			isPlacemark = true;
			rota.pontos = addPoint(rota.pontos);
		} else if (localName.equalsIgnoreCase("ItemIcon")) {
			if (isPlacemark)
				isItemIcon = true;
		}
		mString = new String();
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		String chars = new String(ch, start, length).trim();
		mString = mString.concat(chars);
	}

	public void endElement(String uri, String localName, String name) throws SAXException {
		if (mString.length() > 0) {
			if (localName.equalsIgnoreCase("name")) {
				if (isPlacemark) {
					isRoute = mString.equalsIgnoreCase("Route");
					if (!isRoute) {
						rota.pontos[rota.pontos.length - 1].nome = mString;
					}
				} else {
					rota.nome = mString;
				}
			} else if (localName.equalsIgnoreCase("color") && !isPlacemark) {
				rota.cor = Integer.parseInt(mString, 16);
			} else if (localName.equalsIgnoreCase("width") && !isPlacemark) {
				rota.largura = Integer.parseInt(mString);
			} else if (localName.equalsIgnoreCase("description")) {
				if (isPlacemark) {
					String description = cleanup(mString);
					if (!isRoute)
						rota.pontos[rota.pontos.length - 1].endereco = description;
					else
						rota.descricao = description;
				}
			} else if (localName.equalsIgnoreCase("href")) {
				if (isItemIcon) {
					rota.pontos[rota.pontos.length - 1].iconUrl = mString;
				}
			} else if (localName.equalsIgnoreCase("coordinates")) {
				if (isPlacemark) {
					if (!isRoute) {
						String[] xyParsed = split(mString, ",");
						double lon = Double.parseDouble(xyParsed[0]);
						double lat = Double.parseDouble(xyParsed[1]);
						rota.pontos[rota.pontos.length - 1].latitude = lat;
						rota.pontos[rota.pontos.length - 1].longitude = lon;
					} else {
						String[] coodrinatesParsed = split(mString, " ");
						rota.coordenadas = new double[coodrinatesParsed.length][2];
						for (int i = 0; i < coodrinatesParsed.length; i++) {
							String[] xyParsed = split(coodrinatesParsed[i], ",");
							for (int j = 0; j < 2 && j < xyParsed.length; j++)
								rota.coordenadas[i][j] = Double
										.parseDouble(xyParsed[j]);
						}
					}
				}
			}
		}
		mCurrentElement.pop();
		if (localName.equalsIgnoreCase("Placemark")) {
			isPlacemark = false;
			if (isRoute)
				isRoute = false;
		} else if (localName.equalsIgnoreCase("ItemIcon")) {
			if (isItemIcon)
				isItemIcon = false;
		}
	}

	private String cleanup(String value) {
		String remove = "<br/>";
		int index = value.indexOf(remove);
		if (index != -1)
			value = value.substring(0, index);
		remove = "&#160;";
		index = value.indexOf(remove);
		int len = remove.length();
		
		while (index != -1) {
			value = value.substring(0, index).concat(
					value.substring(index + len, value.length()));
			index = value.indexOf(remove);
		}
		return value;
	}

	public Destino[] addPoint(Destino[] points) {
		Destino[] result = new Destino[points.length + 1];
		for (int i = 0; i < points.length; i++)
			result[i] = points[i];
		result[points.length] = new Destino();
		return result;
	}

	private static String[] split(String strString, String strDelimiter) {
		String[] strArray;
		int iOccurrences = 0;
		int iIndexOfInnerString = 0;
		int iIndexOfDelimiter = 0;
		int iCounter = 0;
		
		if (strString == null) {
			throw new IllegalArgumentException("Input string cannot be null.");
		}
		if (strDelimiter.length() <= 0 || strDelimiter == null) {
			throw new IllegalArgumentException("Delimeter cannot be null or empty.");
		}
		if (strString.startsWith(strDelimiter)) {
			strString = strString.substring(strDelimiter.length());
		}
		if (!strString.endsWith(strDelimiter)) {
			strString += strDelimiter;
		}
		
		while ((iIndexOfDelimiter = strString.indexOf(strDelimiter,	iIndexOfInnerString)) != -1) {
			iOccurrences += 1;
			iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();
		}
		strArray = new String[iOccurrences];
		iIndexOfInnerString = 0;
		iIndexOfDelimiter = 0;
		
		while ((iIndexOfDelimiter = strString.indexOf(strDelimiter,	iIndexOfInnerString)) != -1) {
			strArray[iCounter] = strString.substring(iIndexOfInnerString, iIndexOfDelimiter);
			iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();
			iCounter += 1;
		}

		return strArray;
	}
}
