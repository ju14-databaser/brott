package model;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Lina
 *
 */
public class LocationTest {

	@Test
	public void newEmptyLocationIsEmpty(){
		Location location = new Location();
		
		Assert.assertTrue(location.isEmpty());
	}

	@Test
	public void filledLocationIsNotEmpty(){
		Location location = new Location();
		location.setLat("423");
		location.setLng("2532");
		Assert.assertFalse(location.isEmpty());
	}
}
