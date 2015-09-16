package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Component
public class XMLParser {

	private final static String POLICE_RSS = "https://polisen.se/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Stockholms-lan/?feed=rss";
	private final static String DALARNA_RSS = "https://polisen.se/Stockholms_lan/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Dalarna/?feed=rss";
	private DocumentBuilderFactory documentBuilderFactory;
	private DocumentBuilder documentBuilder;
	private static final Logger LOGGER = LoggerFactory.getLogger(XMLParser.class);

	public XMLParser() {
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();

		}
	}

	public String getValue(Element parent, String nodeName) {
		return parent.getElementsByTagName(nodeName).item(0).getFirstChild().getNodeValue();
	}

	public List<Crime> parseAllCrimes() {
		List<Crime> crimes = new ArrayList<>();

		try {
			Document document = documentBuilder.parse(POLICE_RSS);
			LOGGER.error("Börjar parsning...");
			document.getDocumentElement().normalize();
			NodeList items = document.getElementsByTagName("item");
			for (int i = 0; i < items.getLength()-1; i++) {
				Node item = items.item(i);
				if (item.getNodeType() == Node.ELEMENT_NODE) {
					Element itemE = (Element) item;
					String title = getValue(itemE, "title");
					String description = getValue(itemE, "description");
					crimes.add(new Crime(title, description));
				}
			}
		} catch (SAXException e) {
			LOGGER.error("SAXException at parsing of document..." + e.getMessage());
		} catch (IOException e) {
			LOGGER.error("IOException at parsing of document..." + e.getMessage());
		}

		return crimes;
	}

	public Crime parseTOCrime(String xml) {
		String title = "";
		String description = "";
		boolean error = false;
		try {
			Document document = documentBuilder.parse(POLICE_RSS);
			LOGGER.error("Börjar parsning...");
			document.getDocumentElement().normalize();
			NodeList items = document.getElementsByTagName("item");
			for (int i = 0; i < 1; i++) {
				Node item = items.item(i);
				if (item.getNodeType() == Node.ELEMENT_NODE) {
					Element itemE = (Element) item;
					title = getValue(itemE, "title");
					description = getValue(itemE, "description");
				}
			}
		} catch (SAXException e) {
			LOGGER.error("SAXException at parsing of document..." + e.getMessage());
			error = true;
		} catch (IOException e) {
			LOGGER.error("IOException at parsing of document..." + e.getMessage());
			error = true;
		}
		if (error) {
			// TODO: errorhandling to avoid further errors in the Crime creation
		}
		Crime crime = new Crime(title, description);

		documentBuilder.reset();
		return crime;
	}
}
