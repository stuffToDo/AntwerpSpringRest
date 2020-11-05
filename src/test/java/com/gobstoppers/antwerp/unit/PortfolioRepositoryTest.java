package com.gobstoppers.antwerp.unit;

import com.gobstoppers.antwerp.model.Portfolio;
import com.gobstoppers.antwerp.repository.PortfolioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PortfolioRepositoryTest {

    // Represents a portfolio ID
    private static final UUID ID = UUID.fromString("9d48a88c-1bd2-11eb-adc1-0242ac120002");
    // Portfolio stocks
    private static final Set<String> stocks = new HashSet<>(Arrays.asList("AMZN", "GOOG", "TSLA", "AAPL"));

    @Autowired
    private PortfolioRepository repo;

    @Test
    public void addPortfolio() {
        Portfolio portfolio = new Portfolio(ID, stocks);
        repo.save(portfolio);
        portfolio = repo.findById(ID).get();
        assertThat(portfolio).isNotNull();
        assertThat(portfolio.getUuid()).isSameAs(ID);
        assertThat(portfolio.getStocks()).isNotEmpty();
        assertThat(portfolio.getStocks()).containsAll(stocks);
    }

    @Test
    public void deletePortfolio() {
        Portfolio portfolio = new Portfolio(ID, stocks);
        repo.save(portfolio);
        repo.deleteById(ID);
        assertThat(repo.count()).isZero();
    }

    @Test
    public void zeroStocks() {
        Portfolio portfolio = new Portfolio(ID, Collections.EMPTY_SET);
        repo.save(portfolio);
        portfolio = repo.findById(ID).get();
        assertThat(portfolio).isNotNull();
        assertThat(portfolio.getUuid()).isSameAs(ID);
        assertThat(portfolio.getStocks()).isEmpty();
    }
}
