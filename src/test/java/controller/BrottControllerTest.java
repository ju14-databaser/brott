package controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import model.CrimeFacade;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import service.CrimesDAO;

public class BrottControllerTest {

	/**
	 * Creates a MockMVC using an in-memory database to check that correct
	 * attributes are added to the model.
	 * 
	 * @author Lina
	 * @throws Exception
	 */
	@Test
	public void theShowDBStatisticsModelHasTwoAttributes() throws Exception {

		CrimesDAO crimesDAO = new CrimesDAO("TestDB");
		CrimeFacade crimeHandler = new CrimeFacade(crimesDAO);
		CrimesController controller = new CrimesController(crimeHandler);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		MvcResult andReturn = mockMvc.perform(get("/admin")).andExpect(status().isOk())
				.andExpect(forwardedUrl("dbInfo")).andReturn();
		Assert.assertTrue(andReturn.getModelAndView().getModel().containsKey("noRows"));
		Assert.assertTrue(andReturn.getModelAndView().getModel().containsKey("noEmpty"));
	}
}
