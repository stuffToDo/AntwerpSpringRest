package com.gobstoppers.antwerp;

import com.gobstoppers.antwerp.model.Portfolio;
import com.gobstoppers.antwerp.repository.PortfolioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PortfolioRestControllerIntegrationTest {

	private static final String APPLICATION_JSON_HAL = "application/hal+json";

	@Autowired
	private MockMvc mvc;

	@Autowired
	private PortfolioRepository repo;

	@Test
	void addPortfolio() throws Exception {
		mvc.perform(post("/portfolios/5c89dea1-1c07-11eb-ade1-0242ac120002")
				.with(jwt().jwt(jwt -> jwt.claim("scope", "openid antwerp_read antwerp_write")))
				.content("{\"stocks\":[\"GOOG\",\"AAPL\",\"TSLA\"]}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(APPLICATION_JSON_HAL))
			.andExpect(jsonPath("uuid").value("5c89dea1-1c07-11eb-ade1-0242ac120002"));
	}

	@Test
	void getPortfolio() throws Exception {
		repo.save(new Portfolio(UUID.fromString("9d48a88c-1bd2-11eb-adc1-0242ac120002"), new HashSet<>(Arrays.asList("AMZN", "SPOT", "PYPL"))));

		mvc.perform(get("/portfolios/9d48a88c-1bd2-11eb-adc1-0242ac120002")
				.with(jwt().jwt(jwt -> jwt.claim("scope", "openid antwerp_read antwerp_write")))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_HAL))
				.andExpect(jsonPath("uuid").value("9d48a88c-1bd2-11eb-adc1-0242ac120002"))
				.andExpect(jsonPath("stocks[0]", is("AMZN")));
	}

	@Test
	void getPortfolios() throws Exception {
		repo.save(new Portfolio(UUID.fromString("9d48a88c-1bd2-11eb-adc1-0242ac120002"), new HashSet<>(Arrays.asList("AMZN", "SPOT", "PYPL"))));
		repo.save(new Portfolio(UUID.fromString("8d48a88c-1bd2-80eb-edc1-0242ac124532"), new HashSet<>(Arrays.asList("GOOG", "AAPL", "TSLA"))));

		mvc.perform(get("/portfolios")
				.with(jwt().jwt(jwt -> jwt.claim("scope", "openid antwerp_read antwerp_write")))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_HAL))
				.andExpect(jsonPath("_embedded.portfolioList").isArray())
				.andExpect(jsonPath("_embedded.portfolioList", hasSize(2)));
	}
}
