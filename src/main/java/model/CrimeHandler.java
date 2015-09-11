package model;

import org.springframework.stereotype.Component;

@Component
public class CrimeHandler {

	public Crime getCrimeFromPolice() {

		String xml = getXMLCrime();
		XMLParser xmlParser = new XMLParser();

		Crime crime = xmlParser.parseTOCrime(xml);
		
		return crime;

	}

	private String getXMLCrime() {
		return null;
	}

}
