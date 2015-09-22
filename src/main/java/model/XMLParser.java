package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Class for parsing the Police RSS feed into Crime Objects
 * 
 * @author Erik, Lina
 *
 */
public class XMLParser {

//	private final static String POLICE_RSS = "https://polisen.se/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Stockholms-lan/?feed=rss";
	private DocumentBuilderFactory documentBuilderFactory;
	private DocumentBuilder documentBuilder;
	private static final Logger LOGGER = LoggerFactory.getLogger(XMLParser.class);
	private String rssFeedSource;

	// TODO: Do we need to have a constructor here or can the methods be static
	// instead?

//	// TODO: remove POLICE_RSS when Erik has commited CrimeHandler
//	public XMLParser() {
//		rssFeedSource = POLICE_RSS;
//		documentBuilderFactory = DocumentBuilderFactory.newInstance();
//		try {
//			documentBuilder = documentBuilderFactory.newDocumentBuilder();
//		} catch (ParserConfigurationException e) {
//			LOGGER.error("Error when creating the DocumentBuilder in the XMLParser");
//		}
//	}

	/**
	 * Constructor where the RSS feed is sent in. 
	 * @param rssFeed
	 */
	public XMLParser(String rssFeed) {
		rssFeedSource = rssFeed;
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LOGGER.error("Error when creating the DocumentBuilder in the XMLParser");
		}
	}

	/**
	 * Method for getting all crimes from the police rss feed.
	 * 
	 * @return a list of Crimes
	 * @throws IOException
	 *             if there was problems connecting to the rss feed.
	 * @throws SAXException
	 *             if the RSS source has incorrect formatting
	 */
	public List<Crime> parseAllCrimes() throws IOException, SAXException {

		List<Crime> crimes = new ArrayList<>();

		try {
			NodeList items = parsedRSSAsAList();
			for (int i = 0; i < items.getLength(); i++) {

				Node item = items.item(i);
				if (item.getNodeType() == Node.ELEMENT_NODE) {
					Element itemE = (Element) item;
					String title = getValueFromNode(itemE, "title");
					String description = getValueFromNode(itemE, "description");
					crimes.add(new Crime(title, description));
				}

			}

		} catch (SAXException e) {
			LOGGER.error("SAXException at parsing of document in XMLParser. " + e.getMessage());
			throw e;
		} catch (IOException e) {
			LOGGER.error("IOException at parsing of document in XMLParser. " + e.getMessage());
			throw e;
		}

		return crimes;
	}

	/**
	 * Method for getting all crimes that do not exist in the current database
	 * from the Police Rss.
	 * 
	 * @param latestCrimeTitle
	 *            the title of the latest crime existing in the Database
	 * @return a list of all new Crimes
	 * @throws SAXException
	 *             if the RSS source has incorrect formatting
	 * @throws IOException
	 *             if there was problems connecting to the rss feed.
	 */
	public List<Crime> parseNewCrimes(String latestCrimeTitle) throws SAXException, IOException {
		List<Crime> crimes = new ArrayList<>();

		try {
			NodeList items = parsedRSSAsAList();

			for (int i = 0; i < items.getLength(); i++) {

				Node item = items.item(i);
				if (item.getNodeType() == Node.ELEMENT_NODE) {
					Element itemE = (Element) item;
					String title = getValueFromNode(itemE, "title");
					String description = getValueFromNode(itemE, "description");
					if (title.equals(latestCrimeTitle)) {
						return crimes;
					}

					crimes.add(new Crime(title, description));
				}
			}

		} catch (SAXException e) {
			LOGGER.error("SAXException at parsing of document in XMLParser. " + e.getMessage());
			throw e;
		} catch (IOException e) {
			LOGGER.error("IOException at parsing of document in XMLParser. " + e.getMessage());
			throw e;
		}

		return crimes;
	}

	private NodeList parsedRSSAsAList() throws SAXException, IOException {
		LOGGER.debug("Skapar dokument f�r att parsa RSS");
		Document document = documentBuilder.parse(rssFeedSource);
		LOGGER.debug("Klar med att skapa dokument med parsad RSS");
		document.getDocumentElement().normalize();
		NodeList items = document.getElementsByTagName("item");

		return items;
	}

	private String getValueFromNode(Element parent, String nodeName) {
		return parent.getElementsByTagName(nodeName).item(0).getFirstChild().getNodeValue();
	}

}
