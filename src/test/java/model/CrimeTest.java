package model;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class CrimeTest {

	private static final String TITLE = "2015-09-13 15:53, Skadeg�relse, Botkyrka";
	private static final String DESC = "Sedelv�gen, Tumba. Barn krossat rutor p� f�rskola.";
	private static final String INCORRECT_DESC = "Sedelv�gen, Tumba Barn krossat rutor p� f�rskola.";
	private Crime crime;

	@Before
	public void setup() {
		crime = new Crime(TITLE, DESC);
	}

	@Test
	public void dateStampFormatParsedIsCorrectDay() throws ParseException {

		DateFormat dF = DateFormat.getDateInstance(DateFormat.SHORT);

		Date date = dF.parse(TITLE);
		assertEquals(date, crime.getDateStamp());
	}

	@Test
	public void locationStringGetsCorrectPartOfDesriptionString() {
		String expectedLocation = "Sedelv�gen, Tumba, Sweden";
		String actualLocation = crime.getLocation();

		assertEquals("Fel parsning", expectedLocation, actualLocation);

	}

	@Test
	public void categoryStringGetsCorrectPartOfTitleString() {
		String expectedCategory = " Skadeg�relse";
		
		String actualCategory = crime.getCategory();
		
		assertEquals(expectedCategory, actualCategory);
		
	}

}
