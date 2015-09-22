package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import org.xml.sax.SAXException;

public class XMLParserTest {

	private String crimeTitle1;
	private String crimeDesc1;
	private String crimeTitle2;
	private String crimeDesc2;

	@Before
	public void setup() {
		crimeTitle1 = "2015-09-22 06:59, Stöld, försök, Järfälla";
		crimeDesc1 = "Tomasvägen, Jakobsberg. Ynglingar som bryter på mopeder.";
		crimeTitle2 = "2015-09-22 02:29, Skadegörelse, Stockholm";
		crimeDesc2 = "Tunnelbanestation Högdalen. Klottrare som somnat efter sitt klottrande.";
	}

	@Test
	public void xmlFileWithTwoCrimesParsed_ListWithTwoCorrectCrimeObjectsReturned() {
		String xml = "src\\test\\resources\\crimesTestRSS.xml";

		List<Crime> expectedCrimes = Arrays.asList(new Crime(crimeTitle1, crimeDesc1), new Crime(
				crimeTitle2, crimeDesc2));

		XMLParser xmlParser = new XMLParser(xml);
		List<Crime> parsedCrimes;

		try {
			parsedCrimes = xmlParser.parseAllCrimes();
		} catch (IOException | SAXException e) {
			parsedCrimes = new ArrayList<>();
			System.out.println("Error... " + e);
		}

		assertReflectionEquals(expectedCrimes, parsedCrimes);
	}

	@Test
	public void xmlFileWithTwoCrimes_SecondCrimeTitleSentInAsLatestCrimeTitle_OnlyFirstCrimeParsed() {
		// TODO: test att bara nya crimes parsas

	}

	@Test(expected = SAXException.class)
	public void emptyXMLFileSentInForParsing() throws IOException, SAXException {

		String xml = "src\\test\\resources\\IncorrectCrimesRSS.xml";
		XMLParser xmlParser = new XMLParser(xml);
		xmlParser.parseAllCrimes();

	}

	@Test(expected = IOException.class)
	public void nonExistingXMLFileSentInForParsing() throws IOException, SAXException {

		String xml = "src\\test\\resources\\NotExistingXML.xml";
		XMLParser xmlParser = new XMLParser(xml);
		xmlParser.parseAllCrimes();

	}

}
