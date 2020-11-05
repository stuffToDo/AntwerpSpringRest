package com.gobstoppers.antwerp.integration;

import com.gobstoppers.antwerp.model.Portfolio;
import com.gobstoppers.antwerp.repository.PortfolioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PortfolioControllerIntegrationTest {

	private static final String APPLICATION_JSON_HAL = "application/hal+json";
	// Represents a portfolio ID
	private static final UUID ID1 = UUID.fromString("9d48a88c-1bd2-11eb-adc1-0242ac120002");
	// Represents a portfolio ID
	private static final UUID ID2 = UUID.fromString("8d48a88c-1bd2-80eb-edc1-0242ac124532");
	// Portfolio stocks
	private static final Set<String> STOCKS = new HashSet<>(Arrays.asList("AMZN", "GOOG", "TSLA", "AAPL"));
	// Configure the JWT with scopes antwerp_read and antwerp_write
	private static final JwtRequestPostProcessor JWT = jwt().jwt(jwt -> jwt.claim("scope", "openid antwerp_read antwerp_write"));


	@Autowired
	private MockMvc mvc;

	@Autowired
	private PortfolioRepository repo;

	@Test
	void addPortfolio() throws Exception {
		mvc.perform(post("/portfolios/"+ID1.toString())
				.with(JWT)
				.content("{\"stocks\":[\"GOOG\",\"AAPL\",\"TSLA\"]}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(APPLICATION_JSON_HAL))
			.andExpect(jsonPath("uuid").value(ID1.toString()));
	}

	@Test
	void getPortfolio() throws Exception {
		repo.save(new Portfolio(ID1, STOCKS));

		mvc.perform(get("/portfolios/"+ID1.toString())
				.with(JWT)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_HAL))
				.andExpect(jsonPath("uuid").value(ID1.toString()))
				.andExpect(jsonPath("stocks[0]", is("AAPL")));
	}

	@Test
	void getPortfolios() throws Exception {
		repo.save(new Portfolio(ID1, STOCKS));
		repo.save(new Portfolio(ID2, STOCKS));

		mvc.perform(get("/portfolios")
				.with(JWT)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_HAL))
				.andExpect(jsonPath("_embedded.portfolioList").isArray())
				.andExpect(jsonPath("_embedded.portfolioList", hasSize(2)));
	}

	@Test
	void deletePortfolio() throws Exception {
		repo.save(new Portfolio(ID1, STOCKS));

		mvc.perform(delete("/portfolio/"+ID1.toString())
				.with(JWT)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}
