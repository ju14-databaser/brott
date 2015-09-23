package controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import model.CrimeHandler;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import service.CrimesDAO;

public class BrottControllerTest {


	@Test
	public void theShowDBStatisticsModelHasTwoAttributes() throws Exception {

		CrimesDAO crimesDAO = new CrimesDAO("TestDB");
		CrimeHandler crimeHandler = new CrimeHandler(crimesDAO);
		BrottController controller = new BrottController(crimeHandler);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		MvcResult andReturn = mockMvc.perform(get("/statistik")).andExpect(status().isOk())
				.andExpect(forwardedUrl("dbInfo")).andReturn();
		Assert.assertTrue(andReturn.getModelAndView().getModel().containsKey("noRows"));
		Assert.assertTrue(andReturn.getModelAndView().getModel().containsKey("noEmpty"));
	}
}
