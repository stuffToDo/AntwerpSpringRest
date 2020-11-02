package com.gobstoppers.antwerp.rest.repository;

import com.gobstoppers.antwerp.rest.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {
}
