package model;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.mapsengine.MapsEngine;
import com.google.api.services.mapsengine.MapsEngineRequestInitializer;
import com.google.api.services.mapsengine.model.Feature;
import com.google.api.services.mapsengine.model.FeaturesListResponse;
import com.google.api.services.mapsengine.model.GeoJsonPoint;

@Component
public class CrimeHandler {

	public Crime getCrimeFromPolice() {

		String xml = getXMLCrime();
		XMLParser xmlParser = new XMLParser();

		Crime crime = xmlParser.parseTOCrime(xml);

		return crime;

	}

	public static void main(String[] args) throws IOException {

		String PUBLIC_BROWSER_KEY = "AIzaSyB_HBIFXrhqS66-qtfKJYh-rTafD3jwEFo";
		MapsEngineRequestInitializer apiInitializer = new MapsEngineRequestInitializer(PUBLIC_BROWSER_KEY);
		MapsEngine mapsEngine = new MapsEngine.Builder(new NetHttpTransport(), new GsonFactory(),
				null).setMapsEngineRequestInitializer(apiInitializer).setApplicationName("MyFirstProject")
				.build();
		readFeaturesFromTable(mapsEngine);
	}

	public static void readFeaturesFromTable(MapsEngine me) throws IOException {
		// Query the table for offices in WA that are within 100km of Perth.
		String SAMPLE_TABLE_ID = "12421761926155747447-06672618218968397709";
		FeaturesListResponse featResp = me
				.tables()
				.features()
				.list(SAMPLE_TABLE_ID)
				.setVersion("published")
				.setWhere(
						"State='WA' AND ST_DISTANCE(geometry,ST_POINT(115.8589,-31.9522)) < 100000")
				.execute();

		for (Feature feat : featResp.getFeatures()) {
			System.out.println("Properties: " + feat.getProperties().toString() + "\n\t" + "Name: "
					+ feat.getProperties().get("Fcilty_nam") + "\n\t" + "Geometry Type: "
					+ feat.getGeometry().getType());

			if (feat.getGeometry() instanceof GeoJsonPoint) {
				GeoJsonPoint point = (GeoJsonPoint) feat.getGeometry();
				System.out.println("\t" + "Longitude: " + point.getCoordinates().get(0) + ", "
						+ "Latitude: " + point.getCoordinates().get(1));
			} else {
				System.out.println("Only points are expected in this table!");
				return;
			}
		}
	}

	private String getXMLCrime() {
		return null;
	}

}
