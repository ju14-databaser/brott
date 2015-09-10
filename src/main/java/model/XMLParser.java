package model;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {

	private final static String POLICE_RSS = "https://polisen.se/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Stockholms-lan/?feed=rss";

	public static void main(String[] args) {
		XMLParser.parseTOCrime(POLICE_RSS);
	}

	public static Crime parseTOCrime(String xml) {
		String title="";
		String description="";
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(POLICE_RSS);
			document.getDocumentElement().normalize();

			// Node item = elementsByTagName.item(0);
			NodeList childNodes = document.getElementsByTagName("item");

			for (int i = 0; i < 1; i++) {
				Node item = childNodes.item(i);
				if (item.getNodeType() == Node.ELEMENT_NODE) {

					title = ((Element) item).getElementsByTagName("title").item(0)
							.getTextContent();
					System.out.println(title);
					description = ((Element) item).getElementsByTagName("description")
							.item(0).getTextContent();
					System.out.println(description);
				}
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Crime crime = new Crime(title, description);
		return crime;
	}
}
