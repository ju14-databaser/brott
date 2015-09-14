package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class CrimeTest {

	private static final String TITLE = "2015-09-13 15:53, Skadegörelse, Botkyrka";
	private static final String DESC = "Sedelvägen, Tumba Barn krossat rutor på förskola.";

	@Test
	public void dateStampFormatIsSameDay() throws ParseException {

		Crime crime = new Crime(TITLE, DESC);
		DateFormat dF = DateFormat.getDateInstance(DateFormat.SHORT);

		Date date = dF.parse(TITLE);
		Assert.assertEquals(date, crime.getDateStamp());
	}

}
