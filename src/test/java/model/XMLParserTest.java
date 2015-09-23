package model;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class XMLParserTest {

	private String crimeTitle1;
	private String crimeDesc1;
	private String crimeTitle2;
	private String crimeDesc2;
	private String correctRssFeed;
	private Crime crime1;
	private Crime crime2;

	@Before
	public void setup() {
		crimeTitle1 = "2015-09-22 06:59, St�ld, f�rs�k, J�rf�lla";
		crimeDesc1 = "Tomasv�gen, Jakobsberg. Ynglingar som bryter p� mopeder.";
		crimeTitle2 = "2015-09-22 02:29, Skadeg�relse, Stockholm";
		crimeDesc2 = "Tunnelbanestation H�gdalen. Klottrare som somnat efter sitt klottrande.";
		correctRssFeed = "src\\test\\resources\\crimesTestRSS.xml";
		crime1 = new Crime(crimeTitle1, crimeDesc1);
		crime2 = new Crime(crimeTitle2, crimeDesc2);
	}

	@Test
	public void xmlFileWithTwoCrimesParsed_ListWithTwoCorrectCrimeObjectsReturned()
			throws Exception {

		List<Crime> expectedCrimes = Arrays.asList(crime1, crime2);

		XMLParser xmlParser = new XMLParser(correctRssFeed);
		List<Crime> parsedCrimes = xmlParser.parseAllCrimes();

		assertReflectionEquals(expectedCrimes, parsedCrimes);
	}

	@Test
	public void xmlFileWithTwoCrimes_SecondCrimeTitleSentInAsLatestCrimeTitle_OnlyFirstCrimeParsed()
			throws Exception {

		XMLParser xmlParser = new XMLParser(correctRssFeed);

		List<Crime> actualNewCrimes = xmlParser.parseNewCrimes(crimeTitle2);
		
		assertReflectionEquals(crime1, actualNewCrimes.get(0));
		Assert.assertEquals(1, actualNewCrimes.size());
		
	}

	@Test(expected = SAXException.class)
	public void emptyXMLFileSentInForParsing() throws Exception {

		String xml = "src\\test\\resources\\IncorrectCrimesRSS.xml";
		XMLParser xmlParser = new XMLParser(xml);
		xmlParser.parseAllCrimes();

	}

	@Test(expected = IOException.class)
	public void nonExistingXMLFileSentInForParsing() throws Exception {

		String xml = "src\\test\\resources\\NotExistingXML.xml";
		XMLParser xmlParser = new XMLParser(xml);
		xmlParser.parseAllCrimes();

	}

}
