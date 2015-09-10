package model;

public class CrimeHandler {
	
	public Crime getCrimeFromPolice(){
		
		 String xml = getXMLCrime();
		 Crime crime = XMLParser.parseTOCrime(xml);
		 return crime;
		
	}

	private String getXMLCrime() {
		return null;
	}
	
}
