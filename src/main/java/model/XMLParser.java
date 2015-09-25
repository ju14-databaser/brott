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
 * Class for parsing the Police RSS feed into Crime Objects.
 * 
 * @author Erik, Lina
 *
 */
public class XMLParser {

	private DocumentBuilderFactory documentBuilderFactory;
	private DocumentBuilder documentBuilder;
	private static final Logger LOGGER = LoggerFactory.getLogger(XMLParser.class);
	private String rssFeedSource;


	/**
	 * Constructor where the RSS feed is sent in.
	 * 
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
	 * Method for reading and creating crime-objects from the police rss-feed.
	 * For each parent node "item", it extracts the title & description which is
	 * used to create the crime object.
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
	 *             if the RSS source has incorrect formatting.
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

	/**
	 * Creates and parses the dom element from the police Rss-feed. Creates a
	 * nodelist of all the items in the document.
	 * 
	 * @return The nodelist of all the items.
	 * @throws SAXException
	 *             if the RSS source has incorrect formatting.
	 * @throws IOException
	 *             if there was problems connecting to the rss feed.
	 */
	private NodeList parsedRSSAsAList() throws SAXException, IOException {
		LOGGER.debug("Skapar dokument för att parsa RSS");
		Document document = documentBuilder.parse(rssFeedSource);
		LOGGER.debug("Klar med att skapa dokument med parsad RSS");
		document.getDocumentElement().normalize();
		NodeList items = document.getElementsByTagName("item");

		return items;
	}

	/**
	 * Returns the value of the node child. For e.x Title or description.
	 * 
	 * @param Parent
	 *            The parent node, in this case the tag "item"
	 * @param NodeName
	 *            the tag / node name, in this case title & description
	 * @return The node value.
	 */
	private String getValueFromNode(Element parent, String nodeName) {
		return parent.getElementsByTagName(nodeName).item(0).getFirstChild().getNodeValue();
	}

}
